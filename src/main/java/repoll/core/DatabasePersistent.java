package repoll.core;

import repoll.mappers.MapperException;

public interface DatabasePersistent {
    void insert() throws MapperException;

    void update() throws MapperException;

    void delete() throws MapperException;
}
