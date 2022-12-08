package mc.replay.packetlib.utils;

import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

@ApiStatus.Internal
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

    public static boolean isRepackaged() {
        return NMS_REPACKAGED;
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

    public static Optional<Class<?>> nmsOptionalClass(String post1_17package, String className) {
        return getClassOptional(nmsClassName(post1_17package, className));
    }

    public static Class<?> obcClass(String className) throws ClassNotFoundException {
        return getClass(obcClassName(className));
    }

    public static Optional<Class<?>> obcOptionalClass(String className) {
        return getClassOptional(obcClassName(className));
    }
}