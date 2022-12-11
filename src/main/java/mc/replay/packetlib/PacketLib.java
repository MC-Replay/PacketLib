package mc.replay.packetlib;

import io.netty.buffer.Unpooled;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.PacketListener;
import mc.replay.packetlib.network.PacketRegistry;
import mc.replay.packetlib.network.netty.PacketLibInjector;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.PacketIdentifierLoader;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class PacketLib {

    private static PacketLib instance;

    @ApiStatus.Internal
    public static PacketLib getInstance() {
        return instance;
    }

    private final PacketRegistry packetRegistry;
    private final PacketListener packetListener;
    private final PacketIdentifierLoader packetIdentifierLoader;
    private final PacketLibInjector injector;

    public PacketLib() {
        instance = this;

        this.packetRegistry = new PacketRegistry(this);
        this.packetListener = new PacketListener();
        this.packetIdentifierLoader = new PacketIdentifierLoader();
        this.injector = new PacketLibInjector(this);
    }

    public void inject(@NotNull Player player, boolean listenForClientbound) {
        this.injector.inject(player, listenForClientbound);
    }

    public void sendPacket(@NotNull Player player, @NotNull ClientboundPacket packet) {
        try {
            int packetId = packet.identifier().getIdentifier();

            ByteBuffer buffer = ByteBuffer.allocateDirect(2_097_152);
            PacketBuffer packetBuffer = new PacketBuffer(buffer);

            packet.write(packetBuffer);

            Object clientboundPacket = Reflections.getClientboundPacket(Unpooled.copiedBuffer(buffer), packetId);
            if (clientboundPacket == null) return;

            Reflections.sendPacket(player, clientboundPacket);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public @NotNull PacketRegistry getPacketRegistry() {
        return this.packetRegistry;
    }

    public @NotNull PacketListener getPacketListener() {
        return this.packetListener;
    }

    public @NotNull PacketIdentifierLoader getPacketIdentifierLoader() {
        return this.packetIdentifierLoader;
    }

    public @NotNull PacketLibInjector getInjector() {
        return this.injector;
    }
}