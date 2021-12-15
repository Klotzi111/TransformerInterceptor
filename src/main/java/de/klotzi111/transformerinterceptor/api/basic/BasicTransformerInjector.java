package de.klotzi111.transformerinterceptor.api.basic;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

import de.klotzi111.transformerinterceptor.api.basic.transformer.AbstractMixinTransformerProxy;
import de.klotzi111.transformerinterceptor.api.basic.transformer.MixinTransformerProxyFactory;
import de.klotzi111.transformerinterceptor.impl.util.ClassUtil;

public class BasicTransformerInjector {

	private static final List<Class<? extends ClassLoader>> KNOT_CLASS_LOADER_CLASSES = new LinkedList<>();

	private static final Class<?> KnotClassDelegate_class;

	private static final Field KnotClassDelegate_mixinTransformer;

	static {
		String knotClassLoaderBasePackage = "net.fabricmc.loader.impl.launch.knot.";
		tryLoadKnotClassLoaderClass(knotClassLoaderBasePackage + "KnotClassLoader");
		tryLoadKnotClassLoaderClass(knotClassLoaderBasePackage + "KnotCompatibilityClassLoader");

		KnotClassDelegate_class = ClassUtil.tryLoadClass(knotClassLoaderBasePackage + "KnotClassDelegate");

		Field local_KnotClassDelegate_mixinTransformer = null;
		for (Field field : KnotClassDelegate_class.getDeclaredFields()) {
			if (IMixinTransformer.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				local_KnotClassDelegate_mixinTransformer = field;
				break;
			}
		}

		if (local_KnotClassDelegate_mixinTransformer == null) {
			throw new RuntimeException("Failed to find \"mixinTransformer\" field in class \"" + KnotClassDelegate_class.getSimpleName() + "\"");
		}
		KnotClassDelegate_mixinTransformer = local_KnotClassDelegate_mixinTransformer;
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends ClassLoader> tryLoadKnotClassLoaderClass(String knotClassLoaderClassName) {
		Class<? extends ClassLoader> knotClassLoaderClass = (Class<? extends ClassLoader>) ClassUtil.tryLoadClass(knotClassLoaderClassName);
		KNOT_CLASS_LOADER_CLASSES.add(knotClassLoaderClass);
		return knotClassLoaderClass;
	}

	private static boolean isKnotClassLoader(ClassLoader classLoader) {
		Class<? extends ClassLoader> actualClassLoaderClass = classLoader.getClass();
		for (Class<? extends ClassLoader> knotClassLoaderClass : KNOT_CLASS_LOADER_CLASSES) {
			if (knotClassLoaderClass.isAssignableFrom(actualClassLoaderClass)) {
				return true;
			}
		}
		return false;
	}

	private static Field getKnotClassDelegateField(ClassLoader knotClassLoader) {
		Class<? extends ClassLoader> actualKnotClassLoaderClass = knotClassLoader.getClass();
		for (Field field : actualKnotClassLoaderClass.getDeclaredFields()) {
			if (KnotClassDelegate_class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				return field;
			}
		}
		return null;
	}

	public static void injectMixinTransformerProxy(MixinTransformerProxyFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException("factory is null");
		}

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if (!isKnotClassLoader(classLoader)) {
			throw new IllegalStateException("Thread.currentThread().getContextClassLoader() is not a Knot classloader");
		}

		Field delegateField = getKnotClassDelegateField(classLoader);
		if (delegateField == null) {
			throw new IllegalStateException("The currently used Knot classloader has no suitable \"delegate\" field");
		}

		// make sure BasicTransformerInterceptor is loaded before we set the class transformer
		ClassUtil.ensureClassLoaded(BasicTransformerInterceptor.class);
		try {
			Object delegate = delegateField.get(classLoader);

			IMixinTransformer oldMixinTransformer = (IMixinTransformer) KnotClassDelegate_mixinTransformer.get(delegate);

			// create the proxy
			AbstractMixinTransformerProxy mixinTransformerProxy = null;
			try {
				mixinTransformerProxy = factory.createProxy(oldMixinTransformer);
			} catch (Throwable e) {
				throw new RuntimeException("Failed to create mixinTransformerProxy via factory", e);
			}

			KnotClassDelegate_mixinTransformer.set(delegate, mixinTransformerProxy);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Failed to access fields via reflection", e);
		}
	}

}
