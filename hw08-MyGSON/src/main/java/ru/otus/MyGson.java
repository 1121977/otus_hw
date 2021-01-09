package ru.otus;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyGson {

    static final private Set<Class<?>> supportedNonJsonficationClassesSet = new HashSet<>();

    static {
        supportedNonJsonficationClassesSet.add(Boolean.class);
        supportedNonJsonficationClassesSet.add(Byte.class);
        supportedNonJsonficationClassesSet.add(Character.class);
        supportedNonJsonficationClassesSet.add(Double.class);
        supportedNonJsonficationClassesSet.add(Float.class);
        supportedNonJsonficationClassesSet.add(Integer.class);
        supportedNonJsonficationClassesSet.add(Long.class);
        supportedNonJsonficationClassesSet.add(Short.class);
        supportedNonJsonficationClassesSet.add(String.class);
        supportedNonJsonficationClassesSet.add(Number.class);
    }

    public String toJson(Object obj) {
        switch (defineObjectSort(obj)) {
            case "ArrayOrCollection":
                return myGsonCreateArrayBuilder(obj).build().toString();
            case "PrimitiveOrWrapperOrString":
                return obj.toString();
            case "Custom":
                return myGsonCreateObjectBuilder(obj).build().toString();
            default:
                return "null";
        }
    }

    private void putPrimitiveToJsonArrayBuilder(JsonArrayBuilder jsonArrayBuilder, Object primitive) throws IllegalAccessException {
        switch (primitive.getClass().getName()) {
            case "boolean":
            case "java.lang.Boolean":
                jsonArrayBuilder.add((boolean) primitive);
                break;
            case "byte":
            case "java.lang.Byte":
                jsonArrayBuilder.add((byte) primitive);
                break;
            case "char":
            case "java.lang.Character":
                jsonArrayBuilder.add((char) primitive);
                break;
            case "double":
            case "java.lang.Double":
                jsonArrayBuilder.add((double) primitive);
                break;
            case "float":
            case "java.lang.Float":
                jsonArrayBuilder.add((float) primitive);
                break;
            case "int":
            case "java.lang.Integer":
                jsonArrayBuilder.add((int) primitive);
                break;
            case "long":
            case "java.lang.Long":
                jsonArrayBuilder.add((long) primitive);
                break;
            case "short":
            case "java.lang.Short":
                jsonArrayBuilder.add((short) primitive);
                break;
            case "java.lang.String":
                jsonArrayBuilder.add((String) primitive);
                break;
            default:
        }

    }

    private void putPrimitiveFieldToJson(JsonObjectBuilder jsonObjectBuilder, Object obj, Field field) throws IllegalAccessException {
        switch (field.getType().getName()) {
            case "java.lang.Boolean":
                jsonObjectBuilder.add(field.getName(),(Boolean) field.get(obj));
                break;
            case "boolean":
                jsonObjectBuilder.add(field.getName(), field.getBoolean(obj));
                break;
            case "java.lang.Byte":
                jsonObjectBuilder.add(field.getName(),(Byte)field.get(obj));
                break;
            case "byte":
                jsonObjectBuilder.add(field.getName(), field.getByte(obj));
                break;
            case "java.lang.Character":
                jsonObjectBuilder.add(field.getName(),(Character)field.get(obj));
                break;
            case "char":
                jsonObjectBuilder.add(field.getName(), field.getChar(obj));
                break;
            case "java.lang.Double":
                jsonObjectBuilder.add(field.getName(),(Double)field.get(obj));
                break;
            case "double":
                jsonObjectBuilder.add(field.getName(), field.getDouble(obj));
                break;
            case "java.lang.Float":
                jsonObjectBuilder.add(field.getName(),(Float)field.get(obj));
                break;
            case "float":
                jsonObjectBuilder.add(field.getName(), field.getFloat(obj));
                break;
            case "java.lang.Long":
                jsonObjectBuilder.add(field.getName(),(Long)field.get(obj));
                break;
            case "long":
                jsonObjectBuilder.add(field.getName(), field.getLong(obj));
                break;
            case "java.lang.Integer":
                jsonObjectBuilder.add(field.getName(), (Integer) field.get(obj));
                break;
            case "int":
                jsonObjectBuilder.add(field.getName(), field.getInt(obj));
                break;
            case "java.lang.Short":
                jsonObjectBuilder.add(field.getName(), (Short) field.get(obj));
                break;
            case "short":
                jsonObjectBuilder.add(field.getName(), field.getShort(obj));
                break;
            default:
                jsonObjectBuilder.add(field.getName(), field.get(obj).toString());
        }
    }

    private String defineObjectSort(Object obj) {
        if (obj == null) {
            return "Null";
        }
        if (obj.getClass().isArray() || obj instanceof Collection<?>) {
            return "ArrayOrCollection";
        } else {
            if (obj.getClass().isPrimitive() || supportedNonJsonficationClassesSet.contains(obj.getClass())) {
                return "PrimitiveOrWrapperOrString";
            }
        }
        return "Custom";
    }

    private JsonObjectBuilder myGsonCreateObjectBuilder(Object obj) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                switch (defineObjectSort(field.get(obj))) {
                    case "Custom":
                        jsonObjectBuilder.add(field.getName(), myGsonCreateObjectBuilder(field.get(obj)));
                        break;
                    case "ArrayOrCollection":
                        jsonObjectBuilder.add(field.getName(), myGsonCreateArrayBuilder(field.get(obj)));
                        break;
                    case "PrimitiveOrWrapperOrString":
                        putPrimitiveFieldToJson(jsonObjectBuilder, obj, field);
                        break;
                    case "Null":
                    default:
                }
            } catch (IllegalAccessException illegalAccessException) {
                System.out.println("Accessibility to field " + field.getName() + " doesn't set.");
            }
            field.setAccessible(false);
        }
        return jsonObjectBuilder;
    }

    private JsonArrayBuilder myGsonCreateArrayBuilder(Object obj) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        if (obj instanceof Collection<?>) {
            Iterator<?> iterator = ((Collection<?>) obj).iterator();
            while (iterator.hasNext()) {
                Object object = iterator.next();
                switch (defineObjectSort(object)) {
                    case "Null":
                        break;
                    case "ArrayOrCollection":
                        jsonArrayBuilder.add(myGsonCreateArrayBuilder(object));
                        break;
                    case "PrimitiveOrWrapperOrString":
                        try {
                            putPrimitiveToJsonArrayBuilder(jsonArrayBuilder, object);
                        } catch (IllegalAccessException illegalAccessException) {
                            illegalAccessException.printStackTrace();
                        }
                        break;
                    case "Custom":
                        jsonArrayBuilder.add(myGsonCreateObjectBuilder(object));
                        break;
                    default:
                }
            }
        } else {
            if (obj.getClass().getComponentType().isPrimitive()
                    || supportedNonJsonficationClassesSet.contains(obj.getClass().getComponentType())) {
                putPrimitiveArrayToJsonArrayBuilder(jsonArrayBuilder, obj);
            } else {
                Object[] objectArray = (Object[]) obj;
                for (Object object : objectArray) {
                    jsonArrayBuilder.add(myGsonCreateObjectBuilder(object));
                }
            }

        }
        return jsonArrayBuilder;
    }

    private void putPrimitiveArrayToJsonArrayBuilder(JsonArrayBuilder jsonArrayBuilder, Object primitiveArray) {
        switch (primitiveArray.getClass().getComponentType().getName()) {
            case "boolean":
            case "java.lang.Boolean":
                boolean[] booleans = (boolean[]) primitiveArray;
                for (boolean booleanExemplar : booleans) {
                    jsonArrayBuilder.add(booleanExemplar);
                }
                break;
            case "byte":
            case "java.lang.Byte":
                byte[] bytes = (byte[]) primitiveArray;
                for (byte byteExemplar : bytes) {
                    jsonArrayBuilder.add(byteExemplar);
                }
                break;
            case "char":
            case "java.lang.Character":
                char[] chars = (char[]) primitiveArray;
                for (char charExemplar : chars) {
                    jsonArrayBuilder.add(charExemplar);
                }
                break;
            case "double":
            case "java.lang.Double":
                double[] doubles = (double[]) primitiveArray;
                for (double doubleExemplar : doubles) {
                    jsonArrayBuilder.add(doubleExemplar);
                }
                break;
            case "float":
            case "java.lang.Float":
                float[] floats = (float[]) primitiveArray;
                for (float floatExemplar : floats) {
                    jsonArrayBuilder.add(floatExemplar);
                }
                break;
            case "int":
            case "java.lang.Integer":
                int[] ints = (int[]) primitiveArray;
                for (int intExemplar : ints) {
                    jsonArrayBuilder.add(intExemplar);
                }
                break;
            case "long":
            case "java.lang.Long":
                long[] longs = (long[]) primitiveArray;
                for (long longExemplar : longs) {
                    jsonArrayBuilder.add(longExemplar);
                }
                break;
            case "short":
            case "java.lang.Short":
                short[] shorts = (short[]) primitiveArray;
                for (short shortExemplar : shorts) {
                    jsonArrayBuilder.add(shortExemplar);
                }
                break;
            case "java.lang.String":
                String[] strings = (String[]) primitiveArray;
                for (String stringExemplar : strings) {
                    jsonArrayBuilder.add(stringExemplar);
                }
                break;

            default:
        }

    }
}