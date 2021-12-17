package de.klotzi111.transformerinterceptor.api.util;

import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixin;

public class MixinClassDetector {
	private static final String DESCRIPTOR_Mixin = Type.getDescriptor(Mixin.class);

	public static boolean isMixinClass(ClassNode classNode) {
		List<AnnotationNode> annotations = classNode.invisibleAnnotations;
		if (annotations == null) {
			return false;
		}
		for (AnnotationNode annotation : annotations) {
			if (annotation.desc.equals(DESCRIPTOR_Mixin)) {
				return true;
			}
		}
		return false;
	}

	// TODO: add method to check whether it is an @Accessor (Mixin) or not

	// TODO: add method to get the target class of a @Mixin class

}
