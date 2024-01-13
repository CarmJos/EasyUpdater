package cc.carm.app.easyupdater.function;

@FunctionalInterface
public interface ThrowableTask<T> {
    void execute(T task) throws Exception;
}