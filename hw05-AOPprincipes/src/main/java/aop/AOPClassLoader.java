package aop;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.io.IOException;
import java.util.*;

import static org.objectweb.asm.Opcodes.ASM9;

public class AOPClassLoader extends ClassLoader {
    protected Map<String, SomeMethodAttributes> loggedMethods = new HashMap<>();

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> resultClass = null;
        if (!name.startsWith("java.")) {
            try {
                ClassReader cr = new ClassReader(name);
                ClassWriter cw = new ClassWriter(0);
                ClassWriter cw1 = new ClassWriter(0);
                byte[] b2;
                AOPLoggedMethodsNamesClassVisitor aopLoggedMethodsNamesClassVisitor = new AOPLoggedMethodsNamesClassVisitor(ASM9, cw, loggedMethods);
                cr.accept(aopLoggedMethodsNamesClassVisitor, 0);
                b2 = cw.toByteArray();
                ClassReader c2 = new ClassReader(b2);
                AOPLoggedMethodsCodeClassVisitor aopLoggedMethodsCodeClassVisitor = new AOPLoggedMethodsCodeClassVisitor(ASM9, cw1, loggedMethods);
                c2.accept(aopLoggedMethodsCodeClassVisitor, 0);
                b2 = cw1.toByteArray();
                resultClass = defineClass(name, b2, 0, b2.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            resultClass = super.loadClass(name, resolve);
        }
        return resultClass;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }
}