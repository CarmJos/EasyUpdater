package cc.carm.app.easyupdater.function;

@FunctionalInterface
public interface ThrowableFunction<T, R> {

    R apply(T t) throws Exception;

}
