package repoll.ui;

import repoll.core.Poll;
import repoll.mappers.MapperException;

import java.util.ArrayList;
import java.util.List;

public class SearchUtil {
    public static List<Poll> findPolls(String query) {
        List<Poll> results = new ArrayList<>();
        String[] words = query.split("\\s+");
        try {
        forAllPolls:
            for (Poll poll : Poll.getMapper().all()) {
                for (String word : words) {
                    if (!(poll.getTitle().contains(word)) || poll.getDescription().contains(word)) {
                        continue forAllPolls;
                    }
                }
                results.add(poll);
            }
        } catch (MapperException e) {
            System.err.println("MapperException");
            e.printStackTrace();
        }
        return results;
    }
}
