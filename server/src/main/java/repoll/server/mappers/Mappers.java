package repoll.server.mappers;

import org.jetbrains.annotations.NotNull;
import repoll.models.*;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class Mappers {
    /**
     * Service class
     */
    private Mappers() {
        // empty
    }

    private static final Map<Class<? extends DomainObject>, AbstractMapper<? extends DomainObject>> registry =
            new IdentityHashMap<>();

    static {
        registry.put(User.class, UserMapper.getInstance());
        registry.put(Poll.class, PollMapper.getInstance());
        registry.put(Answer.class, AnswerMapper.getInstance());
        registry.put(Commentary.class, CommentaryMapper.getInstance());
        registry.put(Vote.class, VoteMapper.getInstance());
    }

    public static <T extends DomainObject> AbstractMapper<T> getForClass(Class<T> cls) {
        @SuppressWarnings("unchecked")
        AbstractMapper<T> mapper = (AbstractMapper<T>) registry.get(cls);
        return mapper;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T extends DomainObject> T insert(@NotNull T object) throws MapperException {
        ((AbstractMapper<T>) getForClass(object.getClass())).insert(object);
        return object;
    }

    @SuppressWarnings("unchecked")
    public static <T extends DomainObject> void update(@NotNull T object) throws MapperException {
        ((AbstractMapper<T>) getForClass(object.getClass())).update(object);
    }

    @SuppressWarnings("unchecked")
    public static <T extends DomainObject> void delete(@NotNull T object) throws MapperException {
        ((AbstractMapper<T>) getForClass(object.getClass())).delete(object);
    }

    @NotNull
    public static <T extends DomainObject> T loadById(@NotNull Class<T> cls, long id) throws MapperException {
        return getForClass(cls).loadById(id);
    }

    @NotNull
    public static <T extends DomainObject> List<T> selectRelated(@NotNull Class<T> cls, DomainObject relatedTo) throws MapperException{
        return getForClass(cls).selectRelated(relatedTo);
    }
}
