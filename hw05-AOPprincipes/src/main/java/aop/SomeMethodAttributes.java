package aop;

public class SomeMethodAttributes {
    private String description;
    private int access;
    private String name;
    private String signature;
    private String[] exceptions;
    private MethodVariablesHashMap methodVariablesHashMap;

    SomeMethodAttributes(String description, int access, String name, String signature, String[] exceptions,
                         MethodVariablesHashMap methodVariablesHashMap) {
        this.description = description;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.exceptions = exceptions;
        this.methodVariablesHashMap = methodVariablesHashMap;
    }

    public String getDescription() {
        return this.description;
    }

    public int getAccess() {
        return this.access;
    }

    public String getName() {
        return this.name;
    }

    public String getSignature() {
        return this.signature;
    }

    public String[] getExceptions() {
        return this.exceptions;
    }

    public MethodVariablesHashMap getMethodVariablesHashMap(){
        return this.methodVariablesHashMap;
    }
}
