package de.klotzi111.transformerinterceptor.api.basic.transformer;

import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

@FunctionalInterface
public interface MixinTransformerProxyFactory {
	public AbstractMixinTransformerProxy createProxy(IMixinTransformer oldMixinTransformer);
}
