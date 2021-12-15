package de.klotzi111.transformerinterceptor.api.basic;

import java.util.Comparator;

import de.klotzi111.transformerinterceptor.api.event.ClassTransformer;
import de.klotzi111.transformerinterceptor.api.event.ClassTransformerResult;
import de.klotzi111.transformerinterceptor.impl.event.TransformationEventInitializer;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;

public class BasicTransformerInterceptor {

	// highest priority is on top
	public static final Int2ObjectAVLTreeMap<ClassTransformer> CLASS_TRANSFORMERS = new Int2ObjectAVLTreeMap<>(Comparator.reverseOrder());

	static {
		TransformationEventInitializer.initEvents();
	}

	// event callers
	public static byte[] transformClass(String className, /* unused */ String transformedClassName, byte[] rawClassBytes) {
		// they should always be the same for farbic's Knot launcher
		assert className.equals(transformedClassName);

		ClassTransformerResult lastClassTransformerResult = new ClassTransformerResult(rawClassBytes, true);
		for (ClassTransformer transformer : CLASS_TRANSFORMERS.values()) {
			lastClassTransformerResult = transformer.transform(className, lastClassTransformerResult);
		}
		return lastClassTransformerResult.classBytes;
	}

}
