package mc.replay.packetlib.utils;

import java.util.function.Function;

public record Either<L, R>(boolean isLeft, L left, R right) {

    public static <L, R> Either<L, R> left(L left) {
        return new Either<>(true, left, null);
    }

    public static <L, R> Either<L, R> right(R right) {
        return new Either<>(false, null, right);
    }

    public <T> T map(Function<L, T> leftMapper, Function<R, T> rightMapper) {
        return this.isLeft ? leftMapper.apply(this.left) : rightMapper.apply(this.right);
    }
}