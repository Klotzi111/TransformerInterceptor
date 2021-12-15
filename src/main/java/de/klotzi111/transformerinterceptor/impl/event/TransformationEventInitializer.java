package de.klotzi111.transformerinterceptor.impl.event;

import de.klotzi111.transformerinterceptor.api.basic.BasicTransformerInjector;
import de.klotzi111.transformerinterceptor.api.basic.transformer.MixinTransformerProxyFactory;
import de.klotzi111.transformerinterceptor.api.raw.RawTransformerInjector;
import de.klotzi111.transformerinterceptor.api.raw.tranformer.FabricLauncherProxyFactory;
import de.klotzi111.transformerinterceptor.impl.event.basic.EventGeneratorMixinTransformerProxy;
import de.klotzi111.transformerinterceptor.impl.event.raw.EventGeneratorFabricLauncherProxy;

public class TransformationEventInitializer {

	/**
	 * Can be changed. But you should know what you are doing
	 */
	public static MixinTransformerProxyFactory DEFAULT_EVENT_GENERATOR_MIXIN_TRANSFORMER_PROXY_FACTORY = (oldMixinTransformer) -> {
		if (oldMixinTransformer instanceof EventGeneratorMixinTransformerProxy) {
			throw new RuntimeException("MixinTransformerProxyFactory called to create EventGeneratorMixinTransformerProxy but the delegate is already of that type. This would cause duplicate events");
		}
		return new EventGeneratorMixinTransformerProxy(oldMixinTransformer);
	};

	/**
	 * Can be changed. But you should know what you are doing
	 */
	public static FabricLauncherProxyFactory DEFAULT_EVENT_GENERATOR_FABRIC_LAUNCHER_PROXY_FACTORY = (oldLauncher) -> {
		if (oldLauncher instanceof EventGeneratorFabricLauncherProxy) {
			throw new RuntimeException("FabricLauncherProxyFactory called to create EventGeneratorFabricLauncherProxy but the delegate is already of that type. This would cause duplicate events");
		}
		return new EventGeneratorFabricLauncherProxy(oldLauncher);
	};

	private static boolean IN_INIT_PHASE = false;
	private static boolean EVENTS_INITED_BASIC = false;
	private static boolean EVENTS_INITED_RAW = false;

	public static void initEvents() {
		// to not init recursively
		if (IN_INIT_PHASE) {
			return;
		}
		IN_INIT_PHASE = true;
		initEventsBasic();
		initEventsRaw();
		IN_INIT_PHASE = false;
	}

	private static void initEventsBasic() {
		if (EVENTS_INITED_BASIC) {
			return;
		}
		BasicTransformerInjector.injectMixinTransformerProxy(DEFAULT_EVENT_GENERATOR_MIXIN_TRANSFORMER_PROXY_FACTORY);
		EVENTS_INITED_BASIC = true;
	}

	private static void initEventsRaw() {
		if (EVENTS_INITED_RAW) {
			return;
		}
		RawTransformerInjector.injectFabricLauncherProxy(DEFAULT_EVENT_GENERATOR_FABRIC_LAUNCHER_PROXY_FACTORY);
		EVENTS_INITED_RAW = true;
	}

}
