package mc.replay.packetlib;

public record PacketLibSettings(boolean global, boolean listenServerbound, boolean listenMinecraftClientbound,
                                boolean listenPacketLibClientbound) {
}