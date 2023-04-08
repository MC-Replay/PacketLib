package mc.replay.packetlib.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    private static final String NM_PACKAGE = "net.minecraft";
    public static final String OBC_PACKAGE = "org.bukkit.craftbukkit";
    public static final String NMS_PACKAGE = NM_PACKAGE + ".server";

    private static final boolean NMS_REPACKAGED = getClassOptional(NM_PACKAGE + ".network.protocol.Packet").isPresent();

    public static Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    public static Optional<Class<?>> getClassOptional(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public static String nmsClassName(String post1_17package, String className) {
        if (NMS_REPACKAGED) {
            String classPackage = post1_17package == null || post1_17package.isEmpty() ? NM_PACKAGE : NM_PACKAGE + '.' + post1_17package;
            return classPackage + '.' + className;
        }
        return NMS_PACKAGE + '.' + ProtocolVersion.getServerVersionString() + '.' + className;
    }

    public static String obcClassName(String className) {
        return OBC_PACKAGE + '.' + ProtocolVersion.getServerVersionString() + '.' + className;
    }

    public static Class<?> nmsClass(String post1_17package, String className) throws ClassNotFoundException {
        return getClass(nmsClassName(post1_17package, className));
    }

    public static Class<?> obcClass(String className) throws ClassNotFoundException {
        return getClass(obcClassName(className));
    }

    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Field findFieldEquals(Class<?> clazz, Class<?> type) throws NoSuchFieldException {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == type) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new NoSuchFieldException("No field of type '" + type.getName() + "' found in '" + clazz.getName() + "'");
    }

    public static Field findFieldAssignable(Class<?> clazz, Class<?> type) throws NoSuchFieldException {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(type)) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new NoSuchFieldException("No field of type '" + type.getName() + "' found in '" + clazz.getName() + "'");
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    public static Method getMethod(Class<?> clazz, Class<?> returnType, Class<?>... parameterTypes) throws NoSuchMethodException {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getReturnType() != returnType || !Arrays.equals(method.getParameterTypes(), parameterTypes))
                continue;

            method.setAccessible(true);
            return method;
        }
        throw new NoSuchMethodException("No method with return type '" + returnType.getName() + "' and parameters '" + Arrays.toString(parameterTypes) + "' found in '" + clazz.getName() + "'");
    }
}