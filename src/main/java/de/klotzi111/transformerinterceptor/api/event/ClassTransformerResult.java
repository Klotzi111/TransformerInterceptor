package de.klotzi111.transformerinterceptor.api.event;

import org.jetbrains.annotations.Nullable;

public class ClassTransformerResult {

	public @Nullable byte[] classBytes;
	public boolean runTransformers;

	public ClassTransformerResult(byte[] classBytes, boolean runTransformers) {
		this.classBytes = classBytes;
		this.runTransformers = runTransformers;
	}

}
