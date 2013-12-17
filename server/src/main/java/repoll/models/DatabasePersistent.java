package repoll.models;

import repoll.server.mappers.MapperException;

public interface DatabasePersistent {
    void insert() throws MapperException;

    void update() throws MapperException;

    void delete() throws MapperException;
}
