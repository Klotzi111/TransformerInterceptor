package de.klotzi111.transformerinterceptor.api.event;

@FunctionalInterface
public interface ClassTransformer {
	public ClassTransformerResult transform(String className, ClassTransformerResult lastClassTransformerResult);
}
