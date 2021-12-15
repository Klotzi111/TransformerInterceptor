package de.klotzi111.transformerinterceptor.impl.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import de.klotzi111.transformerinterceptor.api.entrypoint.PreMixinLoadEntrypoint;
import de.klotzi111.transformerinterceptor.impl.event.TransformationEventInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class TransformerInterceptorMixinConfig implements IMixinConfigPlugin {

	private static final String PRE_MIXIN_LOAD_ENTRYPOINT_NAME = "ti:preMixinLoad";

	@Override
	public void onLoad(String mixinPackage) {
		TransformationEventInitializer.initEvents();

		// trigger the preMixinLoad entry point for other mods
		FabricLoader.getInstance().getEntrypoints(PRE_MIXIN_LOAD_ENTRYPOINT_NAME, PreMixinLoadEntrypoint.class).forEach(PreMixinLoadEntrypoint::preMixinLoad);
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

}
