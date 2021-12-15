package de.klotzi111.transformerinterceptor.api.raw;

import java.lang.reflect.Field;

import de.klotzi111.transformerinterceptor.api.raw.tranformer.FabricLauncherProxyFactory;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;

public class RawTransformerInjector {

	private static final Field FabricLauncher_launcher;

	static {
		Field local_FabricLauncher_launcher = null;
		for (Field field : FabricLauncherBase.class.getDeclaredFields()) {
			if (FabricLauncher.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				local_FabricLauncher_launcher = field;
				break;
			}
		}

		if (local_FabricLauncher_launcher == null) {
			throw new RuntimeException("Failed to find \"launcher\" field in class \"" + FabricLauncherBase.class.getSimpleName() + "\"");
		}
		FabricLauncher_launcher = local_FabricLauncher_launcher;
	}

	public static void injectFabricLauncherProxy(FabricLauncherProxyFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException("factory is null");
		}

		// make sure RawTransformerInterceptor is loaded before we set the class transformer
		// ClassUtil.ensureClassLoaded(RawTransformerInterceptor.class);
		try {
			FabricLauncher oldLauncher = (FabricLauncher) FabricLauncher_launcher.get(null);

			// The constructor of FabricLauncherBase will set the launcher field to the newly created launcher object
			// .. but we need to set it to null before otherwise we get an exception
			FabricLauncher_launcher.set(null, null);
			// create the proxy
			try {
				factory.createProxy(oldLauncher);
			} catch (Throwable e) {
				throw new RuntimeException("Failed to create launcherProxy via factory", e);
			}

		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Failed to access fields via reflection", e);
		}
	}

}
