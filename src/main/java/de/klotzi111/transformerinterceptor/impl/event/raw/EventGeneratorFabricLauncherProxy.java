package de.klotzi111.transformerinterceptor.impl.event.raw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.klotzi111.transformerinterceptor.api.event.ClassTransformer;
import de.klotzi111.transformerinterceptor.api.event.ClassTransformerResult;
import de.klotzi111.transformerinterceptor.api.raw.RawTransformerInterceptor;
import de.klotzi111.transformerinterceptor.api.raw.tranformer.AbstractFabricLauncherProxy;
import de.klotzi111.transformerinterceptor.api.util.PriorityMapHelper;
import de.klotzi111.transformerinterceptor.api.util.PriorityMapHelper.PrioritySearchDirection;
import de.klotzi111.transformerinterceptor.impl.util.ClassUtil;
import net.fabricmc.loader.impl.launch.FabricLauncher;

public class EventGeneratorFabricLauncherProxy extends AbstractFabricLauncherProxy implements ClassTransformer {

	// this is required otherwise we are stuck in an infinite resursive loop to load the own classes or classes used in the process to process the transformers
	protected final List<String> DISALLOWED_PACKAGE_NAMES = new ArrayList<>(2);

	public EventGeneratorFabricLauncherProxy(FabricLauncher delegate) {
		super(delegate);

		DISALLOWED_PACKAGE_NAMES.addAll(Arrays.asList(
			"de.klotzi111.transformerinterceptor", // this library
			"it.unimi.dsi.fastutil" // fastutil is used by the event apply methods
		));

		registerDefaultEventHandler();
	}

	@Override
	public ClassTransformerResult transform(String className, ClassTransformerResult lastClassTransformerResult) {
		if (lastClassTransformerResult.classBytes == null || lastClassTransformerResult.runTransformers) {
			try {
				className = ClassUtil.toClassInternal(className);
				lastClassTransformerResult.classBytes = super.getClassByteArray(className, lastClassTransformerResult.runTransformers);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lastClassTransformerResult;
	}

	protected void registerDefaultEventHandler() {
		PriorityMapHelper.putAndMoveOthers(RawTransformerInterceptor.CLASS_TRANSFORMERS, 0, this::transform, PrioritySearchDirection.LOWER);
	}

	@Override
	public byte[] getClassByteArray(String name, boolean runTransformers) throws IOException {
		// because the class names here are with Slashes
		name = ClassUtil.fromClassInternal(name);
		if (ClassUtil.startsWithPrefix(name, DISALLOWED_PACKAGE_NAMES)) {
			return super.getClassByteArray(name, runTransformers);
		}

		return RawTransformerInterceptor.getClassByteArray(name, runTransformers);
	}

}
