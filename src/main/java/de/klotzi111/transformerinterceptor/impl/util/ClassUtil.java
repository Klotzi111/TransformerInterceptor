package de.klotzi111.transformerinterceptor.impl.util;

import java.util.List;

public class ClassUtil {

	public static void ensureClassLoaded(Class<?> clazz) {
		// getting the class as argument should already be enough to load the class.
		// But to be sure we get the ClassLoader of the class and load it again
		// It might be possible that the class object was created without initializing the class so we do that now
		try {
			clazz.getClassLoader().loadClass(clazz.getName());
		} catch (ClassNotFoundException e1) {
		}
	}

	public static Class<?> tryLoadClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to load class: " + className, e);
		}
	}

	public static boolean startsWithPrefix(String className, List<String> prefixes) {
		for (String prefix : prefixes) {
			if (className.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	public static String fromClassInternal(String className) {
		return className.replace('/', '.');
	}

	public static String toClassInternal(String className) {
		return className.replace('.', '/');
	}

}
