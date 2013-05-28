package repoll.core;

public interface DatabasePersistent<T> {
    void save();
    T load();
}
