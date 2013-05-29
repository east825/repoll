package repoll.core;

import repoll.mappers.AbstractMapper;
import repoll.mappers.MapperException;
import repoll.mappers.Mappers;

public abstract class DomainObject implements DatabasePersistent {
    public final static long UNSAVED_OBJECT_ID = -1;

    private long id = UNSAVED_OBJECT_ID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void update() throws MapperException {
        @SuppressWarnings("unchecked")
        AbstractMapper<DomainObject> mapper = (AbstractMapper<DomainObject>)Mappers.getForClass(getClass());
        mapper.update(this);
    }

    @Override
    public void insert() throws MapperException {
        @SuppressWarnings("unchecked")
        AbstractMapper<DomainObject> mapper = (AbstractMapper<DomainObject>)Mappers.getForClass(getClass());
        mapper.insert(this);
    }

    @Override
    public void delete() throws MapperException {
        @SuppressWarnings("unchecked")
        AbstractMapper<DomainObject> mapper = (AbstractMapper<DomainObject>)Mappers.getForClass(getClass());
        try {
            mapper.delete(this);
        } finally {
            id = UNSAVED_OBJECT_ID;
        }
    }

    public final boolean isSaved() {
        return id != UNSAVED_OBJECT_ID;
    }

}
