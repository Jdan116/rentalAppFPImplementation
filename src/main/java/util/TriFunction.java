package util;

@FunctionalInterface
public interface TriFunction <U, T, V, R>{
    R apply(U u, T t, V v);
}
