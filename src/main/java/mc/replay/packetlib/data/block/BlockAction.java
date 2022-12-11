package mc.replay.packetlib.data.block;

import org.jetbrains.annotations.NotNull;

public abstract class BlockAction {

    protected byte actionId;
    protected byte actionParam;

    private BlockAction() {
    }

    public byte actionId() {
        return this.actionId;
    }

    public byte actionParam() {
        return this.actionParam;
    }

    public static @NotNull PistonBlockAction piston() {
        return new PistonBlockAction();
    }

    public static @NotNull ChestLikeBlockAction chest() {
        return new ChestLikeBlockAction();
    }

    public static @NotNull ChestLikeBlockAction enderChest() {
        return new ChestLikeBlockAction();
    }

    public static @NotNull BeaconBlockAction beacon() {
        return new BeaconBlockAction();
    }

    public static @NotNull SpawnerBlockAction spawner() {
        return new SpawnerBlockAction();
    }

    public static @NotNull EndGatewayBlockAction endGateway() {
        return new EndGatewayBlockAction();
    }

    public static @NotNull ChestLikeBlockAction shulkerBox() {
        return new ChestLikeBlockAction();
    }

    public static @NotNull BellBlockAction bell() {
        return new BellBlockAction();
    }

    @SuppressWarnings("unchecked")
    private static abstract class DirectionalBlockAction<A extends BlockAction> extends BlockAction {

        public @NotNull A down() {
            this.actionParam = 0;
            return (A) this;
        }

        public @NotNull A up() {
            this.actionParam = 1;
            return (A) this;
        }

        public @NotNull A south() {
            this.actionParam = 2;
            return (A) this;
        }

        public @NotNull A west() {
            this.actionParam = 3;
            return (A) this;
        }

        public @NotNull A north() {
            this.actionParam = 4;
            return (A) this;
        }

        public @NotNull A east() {
            this.actionParam = 5;
            return (A) this;
        }
    }

    public static final class PistonBlockAction extends DirectionalBlockAction<PistonBlockAction> {

        private PistonBlockAction() {
        }

        public @NotNull PistonBlockAction extend() {
            this.actionId = 0;
            return this;
        }

        public @NotNull PistonBlockAction retract() {
            this.actionId = 1;
            return this;
        }
    }

    public static final class ChestLikeBlockAction extends BlockAction {

        private ChestLikeBlockAction() {
            this.actionId = 1;
        }

        public @NotNull BlockAction.ChestLikeBlockAction close() {
            this.actionParam = 0;
            return this;
        }

        public @NotNull BlockAction.ChestLikeBlockAction open(byte viewers) {
            this.actionParam = viewers;
            return this;
        }
    }

    public static final class BeaconBlockAction extends BlockAction {

        private BeaconBlockAction() {
            this.actionId = 1;
            this.actionParam = 0;
        }
    }

    public static final class SpawnerBlockAction extends BlockAction {

        private SpawnerBlockAction() {
            this.actionId = 1;
            this.actionParam = 0;
        }
    }

    public static final class EndGatewayBlockAction extends BlockAction {

        private EndGatewayBlockAction() {
            this.actionId = 1;
            this.actionParam = 0;
        }
    }

    public static final class BellBlockAction extends DirectionalBlockAction<BellBlockAction> {

        private BellBlockAction() {
            this.actionId = 1;
        }
    }
}