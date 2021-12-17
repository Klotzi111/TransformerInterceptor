package de.klotzi111.transformerinterceptor.api.transformer.limiter;

import de.klotzi111.transformerinterceptor.api.event.ClassTransformer;

/**
 * This class proxies an other {@link ClassTransformer} ({@code delegate})
 * so that the ({@code delegate}) only gets called when the implementation of this class passes the call on to the delegate
 *
 */
public abstract class AbstractClassTransformerLimiter implements ClassTransformer {
	public final ClassTransformer delegate;

	public AbstractClassTransformerLimiter(ClassTransformer delegate) {
		this.delegate = delegate;
	}

}
