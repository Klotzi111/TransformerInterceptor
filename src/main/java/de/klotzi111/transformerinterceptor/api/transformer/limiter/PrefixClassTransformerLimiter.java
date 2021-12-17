package de.klotzi111.transformerinterceptor.api.transformer.limiter;

import java.util.List;

import de.klotzi111.transformerinterceptor.api.event.ClassTransformer;
import de.klotzi111.transformerinterceptor.api.event.ClassTransformerResult;

/**
 * This limiter only passes the call on when the transformed class's name starts with one of the prefixes in {@code allowedPrefixes}
 *
 */
public class PrefixClassTransformerLimiter extends AbstractClassTransformerLimiter {
	public final List<String> allowedPrefixes;

	public PrefixClassTransformerLimiter(ClassTransformer delegate, List<String> allowedPrefixes) {
		super(delegate);
		this.allowedPrefixes = allowedPrefixes;
	}

	@Override
	public ClassTransformerResult transform(String className, ClassTransformerResult lastClassTransformerResult) {
		for (String allowedPrefix : allowedPrefixes) {
			if (className.startsWith(allowedPrefix)) {
				return delegate.transform(className, lastClassTransformerResult);
			}
		}
		return lastClassTransformerResult;
	}

}
