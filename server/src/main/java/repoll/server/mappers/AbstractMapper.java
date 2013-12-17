package repoll.server.mappers;

import repoll.models.ConnectionProvider;
import repoll.models.DomainObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapper<T extends DomainObject> {

    protected final Connection connection;
    private final Map<Long, T> loadedMap = new HashMap<>();

    /*
     * All mapper objects are singletons and can't be instantiated directly
     */
    protected AbstractMapper() {
        connection = ConnectionProvider.connection();
    }

    protected abstract PreparedStatement getLoadByIdStatement(long id) throws SQLException;

    protected abstract PreparedStatement getUpdateStatement(T domainObject) throws SQLException;

    protected abstract PreparedStatement getInsertStatement(T domainObject) throws SQLException;

    protected abstract PreparedStatement getDeleteStatement(T domainObject) throws SQLException;

    protected abstract PreparedStatement getSelectByStatement(DomainObject object) throws SQLException;

    protected abstract PreparedStatement getSelectAllStatement() throws SQLException;

    protected abstract T loadObject(ResultSet resultSet) throws SQLException, MapperException;

    public T loadById(long id) throws MapperException {
        Map<Long, T> loadedMap = this.loadedMap;
        T object = loadedMap.get(id);
        if (object != null) {
            return object;
        }
        try (PreparedStatement statement = getLoadByIdStatement(id)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            object = loadObject(resultSet);
            loadedMap.put(id, object);
            return object;
        } catch (SQLException e) {
            throw new MapperException("Error searching object with id = " + id, e);
        }
    }

    public List<T> selectRelated(DomainObject domainObject) throws MapperException {
        try (PreparedStatement statement = getSelectByStatement(domainObject)) {
            if (statement == null) {
                throw new UnsupportedOperationException("Can't select by " + domainObject);
            }
            ResultSet resultSet = statement.executeQuery();
            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(loadById(resultSet.getLong("id")));
            }
            return results;
        } catch (SQLException e) {
            throw new MapperException("Error while selecting by " + domainObject, e);
        }
    }

    public final void update(T domainObject) throws MapperException {
        if (!domainObject.isSaved()) {
            throw new MapperException("Object " + domainObject + " was not saved in database before its update");
        }
        try (PreparedStatement statement = getUpdateStatement(domainObject)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new MapperException("Error while updating object " + domainObject, e);
        }
    }

    public final void delete(T domainObject) throws MapperException {
        if (!domainObject.isSaved()) {
            throw new MapperException("Object " + domainObject + " was not saved in database before its update");
        }
        try (PreparedStatement statement = getDeleteStatement(domainObject)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new MapperException("Error while deleting object " + domainObject, e);
        } finally {
            loadedMap.remove(domainObject.getId());
        }
    }

    public final List<T> all() throws MapperException {
        try (PreparedStatement statement = getSelectAllStatement()) {
            ResultSet resultSet = statement.executeQuery();
            List<T> objects = new ArrayList<>();
            while (resultSet.next()) {
                objects.add(loadById(resultSet.getLong("id")));
            }
            return objects;
        } catch (SQLException e) {
            throw new MapperException("Error while selecting all objects", e);
        }
    }

    public final long insert(T domainObject) throws MapperException {
        if (domainObject.isSaved()) {
            throw new MapperException("Object " + domainObject + " probably already was saved in database");
        }
        try (PreparedStatement statement = getInsertStatement(domainObject)) {
            statement.executeUpdate();
            long newId = generatedId(statement);
            domainObject.setId(newId);
            loadedMap.put(newId, domainObject);
            return newId;
        } catch (SQLException e) {
            throw new MapperException("Error while inserting object " + domainObject, e);
        }
    }

    private long generatedId(PreparedStatement statement) throws MapperException {
        try {
            ResultSet keys = statement.getGeneratedKeys();
            if (keys == null || !keys.next()) {
                throw new AssertionError("No generated keys. Probably missing Statement.RETURN_GENERATED_KEYS.");
            }
            return keys.getLong(1);
        } catch (SQLException e) {
            throw new MapperException("Error while generating primary key", e);
        }
    }
}
