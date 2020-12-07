package aop;

import java.util.HashMap;

public class MethodVariablesHashMap extends HashMap<String, AOPLoggedMethodsNamesMethodVisitor.TypeIndex> {
    @Override
    public AOPLoggedMethodsNamesMethodVisitor.TypeIndex put(String name, AOPLoggedMethodsNamesMethodVisitor.TypeIndex typeIndex) {
        for (AOPLoggedMethodsNamesMethodVisitor.TypeIndex existingTypeIndex : this.values()) {
            if (typeIndex.getIndex() == existingTypeIndex.getIndex()) {
                throw new IllegalArgumentException();
            }
        }
        return super.put(name, typeIndex);
    }
}
