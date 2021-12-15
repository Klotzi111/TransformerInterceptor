# TransformerInterceptor

Fabric mod library to register raw class transformation/generation listeners.

## Usage
As of now there is no maven repository to download it from.
Do the following to use this library mod:
 - Download, build and publish this mod to your local maven repository (use the gradle task `publishToMavenLocal` for that)
 - Add the following in your build.gradle:
 
```groovy
repositories {
    mavenLocal()
}

dependencies {
    include(modImplementation("de.klotzi111:TransformerInterceptor:1+"))
}
```

## Transformation types
There are two transformation types (register points):
### Basic (Without Mixin classes)
`ClassTransformer`s registered on `BasicTransformerInterceptor` will be called for classes that are required from execution.
For the following kind of classes the transformer will be called:
Classes of:
 - the game
 - other mods
 - fabric
 - libraries.

BUT NOT classes:
 - who's package starts with `java`

### Raw (With Mixin classes)
`ClassTransformer`s registered on `RawTransformerInterceptor` will be called for classes loaded by the `MixinTransformer` (SpongepoweredMixin).
These transformers will be called:
 - when a mixin class is loaded
 - when a class required for the mixin is loaded

If you register for both `Basic` and `Raw` then you might get called for the same class twice (for each type). But that is good. So you should transform for both types.
Because:

#### Note
Classes loaded for the `Raw` type are sometimes ONLY loaded for the `MixinTransformer` (SpongepoweredMixin) and the class will be loaded again when it is required from normal execution.

## Example
Because classes are loaded almost every time during execution you want to register your class transformer as early as possible.
If you register your class transformer after a class has been loaded your transformer will not get called for it, because the class will not be loaded again.

To register the class transformer very early this library has the `PreMixinLoadEntrypoint`. It does exactly what it says: Gets called before any mixin classes are loaded.
In order for the `PreMixinLoadEntrypoint` to get actually called you need to register it in your `fabric.mod.json` within `"entrypoints"` like so:
```js
	"entrypoints": {
		"ti:preMixinLoad": [
			"YOUR.FULL.PATH.TO.ENTRYPOINT.CLASS"
		]
	}
```

```java
public class TransformationEntryPoint implements PreMixinLoadEntrypoint {

	private static class ExampleClassTransformer implements ClassTransformer {

		@Override
		public ClassTransformerResult transform(String className, ClassTransformerResult lastClassTransformerResult) {
			if(!lastClassTransformerResult.runTransformers) {
				// do not apply transformer when previous transformer wants no more transformations to happen
				// you can ignore that if you need to and transform anyway
				return lastClassTransformerResult;
			}
			if(lastClassTransformerResult.classBytes == null) {
				// if the class was not previously loaded we have nothing to transform
				// you can "create" the class here if you want
				return lastClassTransformerResult;
			}
			
			// here comes the transformation logic
			// IMPORTANT: you should NOT modify values inside the byte[] you get from 'lastClassTransformerResult.classBytes'
			// just make a copy. Or use the asm lib it will create a new byte[]
			
			// + Example
			// only apply transformation for specific class 'TestClass'
			if(!className.equals("YOUR.FULL.PATH.TO.CLASS.TestClass")) {
				return lastClassTransformerResult;
			}
			ClassReader reader = new ClassReader(lastClassTransformerResult.classBytes);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);

			// remove all fields from the class called 'REMOVE_ME'
			boolean changesMade = node.fields.removeIf((field) -> field.name.equals("REMOVE_ME"));
			if (changesMade) {
				// if we removed fields
				ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
				// accept the node to the writer
				node.accept(writer);

				// generate the byte[] and set it in the result
				lastClassTransformerResult.classBytes = writer.toByteArray();
			}
			// if we did not remove fields/made changes, we do not need to create a new byte[] since it will have the exact same values
			// - Example
			
			return lastClassTransformerResult;
		}
	}

	@Override
	public void preMixinLoad() {
		// instantiate the class transformer
		ExampleClassTransformer transformer = new ExampleClassTransformer();
		
		// register the class transformer listener after default actions
		// raw for mixin classes themselves
		PriorityMapHelper.addSafe(RawTransformerInterceptor.CLASS_TRANSFORMERS, /* just some arbitrary priority number */ -1000, transformer);
		// basic still required for non mixins to be persistent
		PriorityMapHelper.addSafe(BasicTransformerInterceptor.CLASS_TRANSFORMERS, /* just some arbitrary priority number */ -1000, transformer);
	}

}
```