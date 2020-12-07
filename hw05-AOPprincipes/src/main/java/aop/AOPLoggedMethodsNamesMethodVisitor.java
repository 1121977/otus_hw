package aop;

import org.objectweb.asm.*;
import java.util.Map;

public class AOPLoggedMethodsNamesMethodVisitor extends MethodVisitor {
    protected boolean isLogPresent;
    protected SomeMethodAttributes someMethodAttributes;
    protected Map<String, SomeMethodAttributes> loggedMethods;

    public AOPLoggedMethodsNamesMethodVisitor(int api, MethodVisitor methodVisitor, Map<String, SomeMethodAttributes> loggedMethods, SomeMethodAttributes someMethodAttributes) {
        super(api, methodVisitor);
        this.loggedMethods = loggedMethods;
        this.someMethodAttributes = someMethodAttributes;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        isLogPresent = Type.getDescriptor(Log.class).equals(descriptor);
        if(isLogPresent){
            loggedMethods.put(someMethodAttributes.getName() + someMethodAttributes.getDescription(), someMethodAttributes);
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitMaxs(maxStack + 3, maxLocals + 4);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        String methodParameters = someMethodAttributes.getDescription().replaceFirst(".*\\(","").replaceFirst("\\).*", "");
        if (isLogPresent & index > 0) {
            someMethodAttributes.getMethodVariablesHashMap().put(name,new TypeIndex(index, parseParameters(methodParameters, index)));
        }
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    private Type parseParameters(String parameters, int index) {
        if (index == 1) {
            if (parameters.startsWith("L")) {
                return Type.getType(parameters.substring(0,parameters.indexOf(';')+1));
            } else {
                return Type.getType(parameters.substring(0, 1));
            }
        } else {
            if (parameters.startsWith("L")){
                return parseParameters(parameters.substring(parameters.indexOf(';')+1),index-1);
            } else {
                return parseParameters(parameters.substring(1),index-1);
            }
        }
    }

    final protected class TypeIndex {
        private int index;
        private Type type;

        TypeIndex(int index, Type type) {
            this.index = index;
            this.type = type;
        }

        protected int getIndex() {
            return index;
        }

        protected Type getType() {
            return type;
        }
    }
}
