package de.klotzi111.transformerinterceptor.impl.event.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

import de.klotzi111.transformerinterceptor.api.basic.BasicTransformerInterceptor;
import de.klotzi111.transformerinterceptor.api.basic.transformer.AbstractMixinTransformerProxy;
import de.klotzi111.transformerinterceptor.api.event.ClassTransformer;
import de.klotzi111.transformerinterceptor.api.event.ClassTransformerResult;
import de.klotzi111.transformerinterceptor.api.util.PriorityMapHelper;
import de.klotzi111.transformerinterceptor.api.util.PriorityMapHelper.PrioritySearchDirection;
import de.klotzi111.transformerinterceptor.impl.util.ClassUtil;

public class EventGeneratorMixinTransformerProxy extends AbstractMixinTransformerProxy implements ClassTransformer {

	// this is required otherwise we are stuck in an infinite resursive loop to load the own classes or classes used in the process to process the transformers
	protected final List<String> DISALLOWED_PACKAGE_NAMES = new ArrayList<>(2);

	public EventGeneratorMixinTransformerProxy(IMixinTransformer delegate) {
		super(delegate);

		DISALLOWED_PACKAGE_NAMES.addAll(Arrays.asList(
			"de.klotzi111.transformerinterceptor", // this library
			"it.unimi.dsi.fastutil" // fastutil is used by the event apply methods
		));

		registerDefaultEventHandler();
	}

	@Override
	public ClassTransformerResult transform(String className, ClassTransformerResult lastClassTransformerResult) {
		if (!lastClassTransformerResult.runTransformers) {
			return lastClassTransformerResult;
		}

		lastClassTransformerResult.classBytes = super.transformClassBytes(className, className, lastClassTransformerResult.classBytes);
		return lastClassTransformerResult;
	}

	protected void registerDefaultEventHandler() {
		PriorityMapHelper.putAndMoveOthers(BasicTransformerInterceptor.CLASS_TRANSFORMERS, 0, this::transform, PrioritySearchDirection.LOWER);
	}

	@Override
	public byte[] transformClassBytes(String name, String transformedName, byte[] basicClass) {
		if (ClassUtil.startsWithPrefix(name, DISALLOWED_PACKAGE_NAMES)) {
			return super.transformClassBytes(name, transformedName, basicClass);
		}

		return BasicTransformerInterceptor.transformClass(name, transformedName, basicClass);
	}

}
