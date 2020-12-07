package aop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;
import static org.objectweb.asm.Opcodes.*;

public class AOPLoggedMethodsNamesClassVisitor extends ClassVisitor {

    protected Map<String, SomeMethodAttributes> loggedMethods;

    public AOPLoggedMethodsNamesClassVisitor(int asmVersion, ClassVisitor classVisitor, Map<String, SomeMethodAttributes> loggedMethods) {
        super(asmVersion, classVisitor);
        this.loggedMethods = loggedMethods;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor aopLoggedMethodsNamesMethodVisitor = new AOPLoggedMethodsNamesMethodVisitor(ASM9, super.visitMethod(access, name, descriptor, signature, exceptions), loggedMethods, new SomeMethodAttributes(descriptor, access, name, signature, exceptions, new MethodVariablesHashMap()));
        return aopLoggedMethodsNamesMethodVisitor;
    }

}
