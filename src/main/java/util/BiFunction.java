package util;

@FunctionalInterface
public interface BiFunction <U, T, R>{
    R apply(U u, T t);
}
