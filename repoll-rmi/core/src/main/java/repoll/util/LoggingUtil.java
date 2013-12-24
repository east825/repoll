package repoll.util;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author Mikhail Golubev
 */
public class LoggingUtil {
    /**
     * Service class
     */
    private LoggingUtil() {
        // empty
    }

    public static void configure() {
//        BasicConfigurator.configure();
        PropertyConfigurator.configure("logging.properties");
    }
}
