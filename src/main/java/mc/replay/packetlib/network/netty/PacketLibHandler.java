package mc.replay.packetlib.network.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.utils.PacketUtils;
import org.bukkit.entity.Player;

public final class PacketLibHandler extends ChannelDuplexHandler {

    private final PacketLib packetLib;
    private final Player player;
    private final boolean listenForClientbound;

    public PacketLibHandler(PacketLib packetLib, Player player, boolean listenForClientbound) {
        this.packetLib = packetLib;
        this.player = player;
        this.listenForClientbound = listenForClientbound;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packetObject) throws Exception {
        if (this.player == null || !this.player.isOnline() || !this.player.isValid()) return;

        ServerboundPacket serverboundPacket = PacketUtils.readServerboundPacket(packetObject);
        if (serverboundPacket != null) {
            this.packetLib.getPacketListener().publishServerbound(this.player, serverboundPacket);
        }

        super.channelRead(ctx, packetObject);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object packetObject, ChannelPromise promise) throws Exception {
        if (this.listenForClientbound) {
            ClientboundPacket clientboundPacket = PacketUtils.readClientboundPacket(packetObject);
            if (clientboundPacket != null) {
                this.packetLib.getPacketListener().publishClientbound(clientboundPacket);
            }
        }

        super.write(ctx, packetObject, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}