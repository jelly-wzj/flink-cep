package com.jelly.flink.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author jelly * @date 2018-07-10 20:38
 * @ClassName: ObjectUtils
 * @Description: TODO
 */
public final class ObjectUtils {

    /**
     * @param o
     * @return
     */
    @Deprecated
    public static boolean hasNullValue(Object o) throws IllegalAccessException {
        final Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.get(o) == null || StringUtils.isEmpty(field.get(o).toString())) return true;
        }
        return false;
    }

    /**
     * @param o
     * @param excludeFields
     * @return
     * @throws IllegalAccessException
     */
    public static boolean hasNullValue(Object o, String... excludeFields) throws IllegalAccessException {
        if (o != null) {
            Class clazz = o.getClass();
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                final Field[] declaredFields = clazz.getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    if (!ArrayUtils.contains(excludeFields, field.getName())) {
                        if (field.get(o) == null || StringUtils.isEmpty(field.get(o).toString())) return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T removeInvalidValue(T t, List<String> removes) throws IllegalAccessException {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getType().getName().equals("java.lang.String")) {
                Object vObject = f.get(t);
                if (vObject != null) {
                    String v = vObject.toString();
                    for (String remove : removes) v = org.apache.commons.lang.StringUtils.remove(v, remove);
                    if (StringUtils.isEmpty(v)) f.set(t, null);
                    else f.set(t, v);
                }
            }
        }
        return t;
    }

    /**
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        if (obj instanceof String)
            return StringUtils.isEmpty((String) obj);
        else
            return Objects.isNull(obj);
    }


    /**
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj instanceof Map) {
            return null == obj || ((Map) obj).isEmpty();
        } else if (obj instanceof List) {
            return null == obj || ((List) obj).isEmpty();
        } else if (obj instanceof Set) {
            return null == obj || ((Set) obj).isEmpty();
        } else
            return isNull(obj);
    }

    /**
     * @param obj
     * @param fieldNames
     * @return
     * @throws IllegalAccessException
     */
    public static List<Object> getValue(Object obj, String... fieldNames) {
        List<Object> resList = new ArrayList<>(fieldNames.length);
        for (String name : fieldNames) {
            try {
                Field field = obj.getClass().getDeclaredField(name);
                field.setAccessible(true);
                resList.add(field.get(obj));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return resList;
    }

    /**
     * @param obj
     * @param values
     * @throws IllegalAccessException
     */
    public static void setValue(final Object obj, Map<String, Object> values) {
        values.forEach((fieldName, value) -> {
            try {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(obj, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * deepcopy
     *
     * @param obj
     * @return
     */
    public static Object deepClone(Object obj) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            return (oi.readObject());
        } catch (Exception e) {
            return null;
        }
    }


    public static <T> T typeCovert(Object obj, Class<T> clazz) {
        if (null == obj) return null;

        String objType = obj.getClass().getSimpleName();
        String clazzType = clazz.getSimpleName();

        if (objType.equals(clazzType)) return (T) obj;

        String val = String.valueOf(obj);

        switch (clazzType) {
            case "Long":
                return (T) Long.valueOf(val);
            case "Integer":
                return (T) Integer.valueOf(val);
            case "Float":
                return (T) Float.valueOf(val);
            case "Double":
                return (T) Double.valueOf(val);
            case "Boolean":
                return (T) Boolean.valueOf(val);
            case "Short":
                return (T) Short.valueOf(val);
            default:
                break;
        }
        return (T) val;
    }

    public static <T> T getTypeValue(Object object) {
        String objType = object.getClass().getSimpleName();
        String val = String.valueOf(object);
        switch (objType) {
            case "Long":
                return (T) Long.valueOf(val);
            case "Integer":
                return (T) Integer.valueOf(val);
            case "Float":
                return (T) Float.valueOf(val);
            case "Double":
                return (T) Double.valueOf(val);
            case "Boolean":
                return (T) Boolean.valueOf(val);
            case "Short":
                return (T) Short.valueOf(val);
            case "byte":
                return (T) Byte.valueOf(val);
            case "Character":
                return (T) Character.valueOf((val.charAt(0)));
            default:
                break;
        }

        return (T) object;
    }

    /**
     * 对象序列化到文件
     *
     * @param object
     * @param filePath
     * @throws IOException
     */
    public static void serializeToFile(Object object, String filePath) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.writeObject(object);
        }
    }

    /**
     * 从文件中反序列化成对象
     *
     * @param filePath
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> T deserializeFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return (T) objectInputStream.readObject();
        }
    }
}