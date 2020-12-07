package aop;

import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;
import java.util.Map;

public class AOPLoggedMethodsCodeMethodVisitor extends MethodVisitor {

    protected SomeMethodAttributes someMethodAttributes;

    public AOPLoggedMethodsCodeMethodVisitor(int api, MethodVisitor methodVisitor, SomeMethodAttributes someMethodAttributes) {
        super(api, methodVisitor);
        this.someMethodAttributes = someMethodAttributes;
    }

    @Override
    public void visitCode() {
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Method: \"" + someMethodAttributes.getName() + "\"");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        for (Map.Entry<String, AOPLoggedMethodsNamesMethodVisitor.TypeIndex> parameter : someMethodAttributes.getMethodVariablesHashMap().entrySet()) {
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("Argument \"" + parameter.getKey() + "\": ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(parameter.getValue().getType().getOpcode(ILOAD), parameter.getValue().getIndex());
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(" + transformToPrintlnArg(parameter.getValue().getType().getDescriptor()) + ")V", false);
        }
    }

    private String transformToPrintlnArg(String arg){
        if(arg.matches("[BSI]{1}")){
            return "I";
        }
        return arg;
    }
}
