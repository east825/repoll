package repoll.beans;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.entities.DomainObject;

import javax.persistence.*;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
public abstract class BaseEJB<T extends DomainObject> {
    @PersistenceContext
    protected EntityManager manager;

    public abstract Class<T> getEntityClass();

    public void persist(@NotNull T object) {
        manager.persist(object);
    }

    public void refresh(@NotNull T object) {
        manager.merge(object);
    }

    @Nullable
    public T findById(long id) {
        return manager.find(getEntityClass(), id);
    }

    @NotNull
    protected List<T> runNamedQueryReturnMultiple(@NotNull String queryName, String... params) {
        return prepareQuery(queryName, params).getResultList();
    }

    @Nullable
    protected T runNamedQueryReturnSingle(@NotNull String queryName, String... params) {
        try {
            return prepareQuery(queryName, params).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private TypedQuery<T> prepareQuery(String queryName, String[] params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Illegal number of query parameters: " + params.length);
        }
        TypedQuery<T> query = manager.createNamedQuery(queryName, getEntityClass());
        for (int i = 0; i < params.length; i += 2) {
            query.setParameter(params[i], params[i + 1]);
        }
        return query;
    }

}
