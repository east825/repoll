package repoll.models;

import repoll.server.mappers.AbstractMapper;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;

import java.io.Serializable;

public abstract class DomainObject implements DatabasePersistent, Serializable {
    public final static long UNSAVED_OBJECT_ID = -1;

    private long id = UNSAVED_OBJECT_ID;

    public final boolean isSaved() {
        return id != UNSAVED_OBJECT_ID;
    }

    public long getId() {
        return id;
    }

    /*
     * Setter for primary key should be called only once - from corresponding mapper
     */
    public void setId(long id) {
        if (isSaved()) {
            throw new AssertionError("setId() called twice for object " + this);
        }
        this.id = id;
    }

    @Override
    public void update() throws MapperException {
        @SuppressWarnings("unchecked")
        AbstractMapper<DomainObject> mapper = (AbstractMapper<DomainObject>) Mappers.getForClass(getClass());
        mapper.update(this);
    }

    @Override
    public void insert() throws MapperException {
        @SuppressWarnings("unchecked")
        AbstractMapper<DomainObject> mapper = (AbstractMapper<DomainObject>) Mappers.getForClass(getClass());
        mapper.insert(this);
    }

    @Override
    public void delete() throws MapperException {
        @SuppressWarnings("unchecked")
        AbstractMapper<DomainObject> mapper = (AbstractMapper<DomainObject>) Mappers.getForClass(getClass());
        try {
            mapper.delete(this);
        } finally {
            id = UNSAVED_OBJECT_ID;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DomainObject)) return false;
        return isSaved() && id == ((DomainObject) obj).getId();
    }
}
