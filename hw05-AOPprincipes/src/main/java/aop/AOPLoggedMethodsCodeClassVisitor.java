package aop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import java.util.Map;
import static org.objectweb.asm.Opcodes.ASM9;

public class AOPLoggedMethodsCodeClassVisitor extends ClassVisitor {

    protected Map<String, SomeMethodAttributes> loggedMethods;

    public AOPLoggedMethodsCodeClassVisitor(int asmVersion, ClassVisitor classVisitor, Map<String, SomeMethodAttributes> loggedMethods) {
        super(asmVersion, classVisitor);
        this.loggedMethods = loggedMethods;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor superMethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (loggedMethods.get(name + descriptor) != null) {
            return new AOPLoggedMethodsCodeMethodVisitor(ASM9, superMethodVisitor, loggedMethods.get(name + descriptor));
        }
        return superMethodVisitor;
    }
}
