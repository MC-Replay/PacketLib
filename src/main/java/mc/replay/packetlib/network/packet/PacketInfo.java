package mc.replay.packetlib.network.packet;

import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketInfo {

    @NotNull ProtocolVersion since() default ProtocolVersion.MINECRAFT_1_16_5;

    @NotNull ProtocolVersion until() default ProtocolVersion.MINECRAFT_1_20_1;
}