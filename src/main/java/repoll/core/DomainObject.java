package repoll.core;

import repoll.mappers.AbstractMapper;
import repoll.mappers.MapperException;

public abstract class DomainObject {
    public final static long UNSAVED_OBJECT_ID = -1;

    private long id = UNSAVED_OBJECT_ID;

    public abstract <T extends DomainObject> AbstractMapper<T> mapper();
//    protected abstract AbstractMapper<? extends DomainObject> mapper();


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void update() throws MapperException {
        mapper().update(this);
    }

    public void insert() throws MapperException {
        mapper().insert(this);
    }

    public void delete() throws MapperException {
        try {
            mapper().delete(this);
        } finally {
            id = UNSAVED_OBJECT_ID;
        }
    }

}
