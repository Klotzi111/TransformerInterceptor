package de.klotzi111.transformerinterceptor.api.transformer.limiter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixin;

import de.klotzi111.transformerinterceptor.api.event.ClassTransformer;
import de.klotzi111.transformerinterceptor.api.event.ClassTransformerResult;
import de.klotzi111.transformerinterceptor.api.util.MixinClassDetector;

/**
 * This limiter only passes the call on when the transformed class is a {@link Mixin} class
 *
 */
public class MixinClassTransformerLimiter extends AbstractClassTransformerLimiter {

	public MixinClassTransformerLimiter(ClassTransformer delegate) {
		super(delegate);
	}

	@Override
	public ClassTransformerResult transform(String className, ClassTransformerResult lastClassTransformerResult) {
		if (lastClassTransformerResult.classBytes == null) {
			return lastClassTransformerResult;
		}

		ClassReader reader = new ClassReader(lastClassTransformerResult.classBytes);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);

		if (!MixinClassDetector.isMixinClass(node)) {
			return lastClassTransformerResult;
		}

		return delegate.transform(className, lastClassTransformerResult);
	}

}
