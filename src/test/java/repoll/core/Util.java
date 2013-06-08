package repoll.core;

import repoll.mappers.MapperException;

public class Util {
    public static Poll newAnonymousPoll(String title) throws MapperException {
        return newAnonymousPoll(title, "");
    }

    public static Poll newAnonymousPoll(String title, String description) throws MapperException {
        Poll poll = new Poll(null, title, description);
        poll.insert();
        return poll;
    }

    public static Commentary newAnonymousCommentary(Poll poll, String message) throws MapperException {
        Commentary commentary = new Commentary(null, poll, message);
        commentary.insert();
        return commentary;
    }
}
