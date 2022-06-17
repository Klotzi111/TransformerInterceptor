package de.klotzi111.transformerinterceptor.api.raw.tranformer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.jar.Manifest;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;

public abstract class AbstractFabricLauncherProxy extends FabricLauncherBase {

	public final FabricLauncher delegate;

	// TODO: make this a proxy with javaassist so we do not need to override all the methods
	public AbstractFabricLauncherProxy(FabricLauncher delegate) {
		this.delegate = delegate;
	}

	@Override
	public String getTargetNamespace() {
		return delegate.getTargetNamespace();
	}

	@Override
	public List<Path> getClassPath() {
		return delegate.getClassPath();
	}

	@Override
	public void addToClassPath(Path path, String... allowedPrefixes) {
		delegate.addToClassPath(path, allowedPrefixes);
	}

	@Override
	public void setAllowedPrefixes(Path path, String... prefixes) {
		delegate.setAllowedPrefixes(path, prefixes);
	}

	@Override
	public void setValidParentClassPath(Collection<Path> paths) {
		delegate.setValidParentClassPath(paths);
	}

	@Override
	public EnvType getEnvironmentType() {
		return delegate.getEnvironmentType();
	}

	@Override
	public boolean isClassLoaded(String name) {
		return delegate.isClassLoaded(name);
	}

	@Override
	public Class<?> loadIntoTarget(String name) throws ClassNotFoundException {
		return delegate.loadIntoTarget(name);
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		return delegate.getResourceAsStream(name);
	}

	@Override
	public ClassLoader getTargetClassLoader() {
		return delegate.getTargetClassLoader();
	}

	@Override
	public byte[] getClassByteArray(String name, boolean runTransformers) throws IOException {
		return delegate.getClassByteArray(name, runTransformers);
	}

	@Override
	public Manifest getManifest(Path originPath) {
		return delegate.getManifest(originPath);
	}

	@Override
	public boolean isDevelopment() {
		return delegate.isDevelopment();
	}

	@Override
	public String getEntrypoint() {
		return delegate.getEntrypoint();
	}
}
