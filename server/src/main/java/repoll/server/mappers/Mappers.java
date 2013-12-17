package repoll.server.mappers;

import repoll.models.*;

import java.util.IdentityHashMap;
import java.util.Map;

public class Mappers {
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
}
