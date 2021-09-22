package util;
@FunctionalInterface
public interface QuadFunction <T, U, V, Y, R>{
    R apply(T t, U u, V v, Y y);
}
