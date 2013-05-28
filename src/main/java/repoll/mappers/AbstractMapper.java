package repoll.mappers;

import repoll.core.ConnectionProvider;
import repoll.core.DomainObject;
import repoll.core.User;

import java.sql.*;
import java.util.Map;

public abstract class AbstractMapper<T extends DomainObject> {

    protected final Connection connection;

    AbstractMapper () {
        connection = ConnectionProvider.connection();
    }

    protected abstract Map<Long, T> getLoadedMap();

    protected abstract PreparedStatement getLoadByIdStatement(long id) throws SQLException;

    protected abstract PreparedStatement getUpdateStatement(T domainObject) throws SQLException;

    protected abstract PreparedStatement getInsertStatement(T domainObject) throws SQLException;

    protected abstract PreparedStatement getDeleteStatement(T domainObject) throws SQLException;

    protected abstract T loadObject(ResultSet resultSet) throws SQLException;

    private long generateId() {
        // TODO: unique primary key generation
        return 42L;
    }

    public T loadById(long id) throws MapperException {
        Map<Long, T> loadedMap = getLoadedMap();
        T object = loadedMap.get(id);
        if (object != null) {
            return object;
        }
        try (PreparedStatement statement = getLoadByIdStatement(id)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null)
                return null;
            resultSet.next();
            object = loadObject(resultSet);
            getLoadedMap().put(id, object);
            return object;
        } catch (SQLException e) {
            throw new MapperException("Error searching object with id = " + id, e);
        }
    }

    public final void update(T domainObject) throws MapperException {
        Map<Long, ? extends DomainObject> loadedMap = getLoadedMap();
        if (domainObject.getId() == DomainObject.UNSAVED_OBJECT_ID) {
            throw new IllegalStateException("Object " + domainObject + " was not saved in database before its update");
        }
        try (PreparedStatement statement = getUpdateStatement(domainObject)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new MapperException("Error while updating object " + domainObject, e);
        }
    }

    public final void delete(T domainObject) throws MapperException {
        if (domainObject.getId() == DomainObject.UNSAVED_OBJECT_ID) {
            throw new IllegalStateException("Object " + domainObject + " was not saved in database before its update");
        }
        try (PreparedStatement statement = getDeleteStatement(domainObject)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new MapperException("Error while deleting object " + domainObject, e);
        } finally {
            Map<Long, T> map = getLoadedMap();
            map.remove(domainObject.getId());
        }
    }

    public final long insert(T domainObject) throws MapperException {
        if (domainObject.getId() != DomainObject.UNSAVED_OBJECT_ID) {
            throw new IllegalStateException("Object " + domainObject + " probably already was saved in database");
        }
        try (PreparedStatement statement = getInsertStatement(domainObject)) {
            statement.executeUpdate();
            long newId = generateId(statement);
            domainObject.setId(newId);
            System.out.println(newId);
            return newId;
        } catch (SQLException e) {
            throw new MapperException("Error while inserting object " + domainObject, e);
        }
    }

    private long generateId(PreparedStatement statement) throws MapperException {
        try {
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            }
            throw new AssertionError("No generated keys available");
        } catch (SQLException e) {
            throw new MapperException("Error while generating primary key", e);
        }
    }

    public static void main(String[] args) throws Exception {
//        User me = User.builder("east826", "foobarbaz").withAdditionalInfo("Some uninteresting stuff").build();
//        System.out.println(me);
//        me.insert();
        System.out.println(UserMapper.getInstance().loadById(30));
    }
}
