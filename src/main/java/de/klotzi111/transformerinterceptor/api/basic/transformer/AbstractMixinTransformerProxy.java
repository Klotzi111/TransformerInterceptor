package de.klotzi111.transformerinterceptor.api.basic.transformer;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.IExtensionRegistry;
import org.spongepowered.asm.transformers.TreeTransformer;

public abstract class AbstractMixinTransformerProxy extends TreeTransformer implements IMixinTransformer {

	public final IMixinTransformer delegate;
	public final boolean isTreeTransformer;

	public AbstractMixinTransformerProxy(IMixinTransformer delegate) {
		this.delegate = delegate;
		isTreeTransformer = (delegate instanceof TreeTransformer);
	}

	@Override
	public void audit(MixinEnvironment environment) {
		delegate.audit(environment);
	}

	@Override
	public List<String> reload(String mixinClass, ClassNode classNode) {
		return delegate.reload(mixinClass, classNode);
	}

	@Override
	public boolean computeFramesForClass(MixinEnvironment environment, String name, ClassNode classNode) {
		return delegate.computeFramesForClass(environment, name, classNode);
	}

	@Override
	public byte[] transformClassBytes(String name, String transformedName, byte[] basicClass) {
		return delegate.transformClassBytes(name, transformedName, basicClass);
	}

	@Override
	public byte[] transformClass(MixinEnvironment environment, String name, byte[] classBytes) {
		return delegate.transformClass(environment, name, classBytes);
	}

	@Override
	public boolean transformClass(MixinEnvironment environment, String name, ClassNode classNode) {
		return delegate.transformClass(environment, name, classNode);
	}

	@Override
	public byte[] generateClass(MixinEnvironment environment, String name) {
		return delegate.generateClass(environment, name);
	}

	@Override
	public boolean generateClass(MixinEnvironment environment, String name, ClassNode classNode) {
		return delegate.generateClass(environment, name, classNode);
	}

	@Override
	public IExtensionRegistry getExtensions() {
		return delegate.getExtensions();
	}

	@Override
	public String getName() {
		if (isTreeTransformer) {
			return ((TreeTransformer) delegate).getName();
		}
		return this.getClass().getName();
	}

	@Override
	public boolean isDelegationExcluded() {
		if (isTreeTransformer) {
			return ((TreeTransformer) delegate).isDelegationExcluded();
		}
		return true;
	}

}
