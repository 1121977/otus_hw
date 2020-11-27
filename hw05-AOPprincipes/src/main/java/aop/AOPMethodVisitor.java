package aop;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class AOPMethodVisitor extends MethodVisitor {
    protected boolean isLogPresent;
    protected String methodName;

    public AOPMethodVisitor(int api) {
        super(api);
    }

    public AOPMethodVisitor(int api, MethodVisitor methodVisitor, String methodName) {
        super(api, methodVisitor);
        this.methodName = methodName;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        isLogPresent = descriptor.equals("Laop/Log;");
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitCode() {
        mv.visitCode();
        if (isLogPresent) {
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
    }

    @Override  // Refactor before send
    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitMaxs(maxStack + 3, maxLocals);
    }

    public AOPMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        System.out.println("visitTypeAnnotation: typeRef - " + typeRef + "; typePath - " + typePath + "; descriptor - " + descriptor);
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        System.out.println("visitAnnotableParameterCount: parameterCount - " + parameterCount);
        super.visitAnnotableParameterCount(parameterCount, visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        System.out.println("visitParameterAnnotation: parameter - " + parameter + "; descriptor - " + descriptor);
        return super.visitParameterAnnotation(parameter, descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        System.out.println("visitAttribute: attribute - " + attribute);
        super.visitAttribute(attribute);
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        if(isLogPresent){
            System.out.println("visitFrame: type -" + type + "; numLocal - " + numLocal + "; local - " + local + "; numStack - " + numStack + "; stack - " + stack);
        }
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitInsn(int opcode) {
        /*if(isLogPresent){
            System.out.println("visitInsn: opcode - " + opcode);
        }*/
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        /*if(isLogPresent){
            System.out.println("visitIntInsn: opcode - " + opcode + "; operand" + operand);
        }*/
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        /*if (isLogPresent){
            System.out.println("visitVarInsn: opcode - " + opcode + "; var" + var);
        }*/
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        /*if (isLogPresent){
            System.out.println("visitTypeInsn: opcode - " + opcode + "; type" + type);
        }*/
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        /*if(isLogPresent){
            System.out.println("visitFieldInsn: opcode - " + opcode + "; owner" + owner + "; name - " + name + "; descriptor" + descriptor);
        }*/
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }


    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        /*if (isLogPresent){
            System.out.println("visitMethodInsn: opcode" + opcode + "; owner" + owner + "; name - " + name + "; descriptor" + descriptor);
        }*/
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        /*if(isLogPresent){
            System.out.println("visitInvokeDynamicInsn: name - " + name + "; descriptor - " + descriptor + "; bootstrapMethodHandle - " + bootstrapMethodHandle + "; bootstrapMethodArguments - "+  bootstrapMethodArguments);
        }*/
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        /*if(isLogPresent){
            System.out.println("visitJumpInsn: opcode - " + opcode + "; label - " + label);
        }*/
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
        if (isLogPresent){
            System.out.println("visitLabel: label - " + label);
        }
        super.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object value) {
        /*if(isLogPresent) {
            System.out.println("visitLdcInsn: value - " + value);
        }*/
        super.visitLdcInsn(value);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        /*if (isLogPresent){
            System.out.println("visitIincInsn: var - " + var + "; increment" + increment);
        }*/
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        /*if (isLogPresent){
            System.out.println("visitTableSwitchInsn: min -" + min + "; max - " + max + "; dflt - " + dflt + "; labels - " + labels);
        }*/
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        /*if(isLogPresent){
            System.out.println("visitLookupSwitchInsn: dflt - " + dflt + "; keys - " + keys + "; labels - " + labels);
        }*/
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        /*if (isLogPresent){
            System.out.println("visitMultiANewArrayInsn: descriptor - " + descriptor + "; numDimensions" + numDimensions);
        }*/
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        if (isLogPresent){
            System.out.println("visitInsnAnnotation: typeRef - " + typeRef + "; typePath - " + typePath + "; descriptor - " + descriptor);
        }
        return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        if (isLogPresent){
            System.out.println("visitTryCatchBlock: start - " + start + "; end - " + end + "; handler - " + handler + "; type - " + type);
        }
        super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        if (isLogPresent){
            System.out.println("visitTryCatchAnnotation: typeRef - " + typeRef + "; typePath - " + typePath + "; " + "; descriptor - " + descriptor);
        }
        return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        if(isLogPresent){
            System.out.println("visitLocalVariable: name - " + name + "; descriptor - " + descriptor + "; signature - " + signature + "; start - " + start + "; end - " + end + "; index - " + index);
        }
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        if (isLogPresent) {
            System.out.println("visitLocalVariableAnnotation: typeRef - " + typeRef + "; typePath - " + typePath + "; start" + start + "; end - " + end + "; index" + index + "; descriptor - " + descriptor);
        }
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        if (isLogPresent){
            System.out.println("visitLineNumber: line - " + line + "; start - " + start);
        }
        super.visitLineNumber(line, start);
    }
}
