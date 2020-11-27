package aop;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class AOPMethodTransformationByLogAnnotation extends ClassVisitor {

    public AOPMethodTransformationByLogAnnotation(int asmVersion) {
        super(asmVersion);
    }

    public AOPMethodTransformationByLogAnnotation(int asmVersion, ClassVisitor classVisitor) {
        super(asmVersion, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = new AOPMethodVisitor(ASM9, super.visitMethod(access, name, descriptor, signature, exceptions), name);
        return methodVisitor;
/*        return new MethodVisitor(ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {

            private boolean isLogPresent;

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                System.out.println("desc is " + descriptor);
                isLogPresent = descriptor.equals("Laop/Log;");
                System.out.println(isLogPresent);
                return super.visitAnnotation(descriptor, visible);
            }

            @Override
            public void visitCode() {
                mv.visitCode();
                if(isLogPresent){
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn("aaa");
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                }
            }
        };*/
    }
}
