package repoll.core;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserTest.class,
        PollTest.class,
        VoteTest.class,
        CommentaryTest.class,
        AnswerTest.class
})
public class AllTestsSuite {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(AllTestsSuite.class);
        System.out.println(
                String.format("Total: %d, failed: %d", result.getRunCount(), result.getFailureCount())
        );
    }
}
