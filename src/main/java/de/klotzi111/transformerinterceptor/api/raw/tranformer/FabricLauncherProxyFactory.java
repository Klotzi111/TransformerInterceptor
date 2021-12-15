package de.klotzi111.transformerinterceptor.api.raw.tranformer;

import net.fabricmc.loader.impl.launch.FabricLauncher;

@FunctionalInterface
public interface FabricLauncherProxyFactory {
	public AbstractFabricLauncherProxy createProxy(FabricLauncher oldFabricLauncher);
}
