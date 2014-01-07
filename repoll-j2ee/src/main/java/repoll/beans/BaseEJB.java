package repoll.beans;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.entities.DomainObject;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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

    public T merge(@NotNull T object) {
        return manager.merge(object);
    }

    public boolean contains(@NotNull T object) {
        return manager.contains(object);
    }

    @Nullable
    public T findById(long id) {
        return manager.find(getEntityClass(), id);
    }

    @NotNull
    protected List<T> runNamedQueryReturnMultiple(@NotNull String queryName, Object... params) {
        return prepareQuery(queryName, params).getResultList();
    }

    @Nullable
    protected T runNamedQueryReturnSingle(@NotNull String queryName, Object... params) {
        try {
            return prepareQuery(queryName, params).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private TypedQuery<T> prepareQuery(String queryName, Object[] params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Illegal number of query parameters: " + params.length);
        }
        TypedQuery<T> query = manager.createNamedQuery(queryName, getEntityClass());
        for (int i = 0; i < params.length; i += 2) {
            query.setParameter((String)params[i], params[i + 1]);
        }
        return query;
    }

}
