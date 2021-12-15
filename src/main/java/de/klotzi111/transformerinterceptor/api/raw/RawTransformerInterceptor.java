package de.klotzi111.transformerinterceptor.api.raw;

import java.util.Comparator;

import de.klotzi111.transformerinterceptor.api.event.ClassTransformer;
import de.klotzi111.transformerinterceptor.api.event.ClassTransformerResult;
import de.klotzi111.transformerinterceptor.impl.event.TransformationEventInitializer;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;

public class RawTransformerInterceptor {

	// highest priority is on top
	public static final Int2ObjectAVLTreeMap<ClassTransformer> CLASS_TRANSFORMERS = new Int2ObjectAVLTreeMap<>(Comparator.reverseOrder());

	static {
		TransformationEventInitializer.initEvents();
	}

	// event callers
	public static byte[] getClassByteArray(String className, boolean runTransformers) {
		ClassTransformerResult lastClassTransformerResult = new ClassTransformerResult(null, runTransformers);
		for (ClassTransformer transformer : CLASS_TRANSFORMERS.values()) {
			lastClassTransformerResult = transformer.transform(className, lastClassTransformerResult);
		}
		return lastClassTransformerResult.classBytes;
	}

}
