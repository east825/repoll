package repoll;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import repoll.service.PollsResource;
import repoll.ui.MainApplication;

import javax.swing.*;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Repoll {
    public final static Logger LOG = createRootLogger();

    private static Logger createRootLogger() {
        Logger logger = Logger.getLogger("repoll");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
        return logger;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MainApplication.getInstance().createAndShowGUI();
                }
            });
        } else if (args[0].equals("service")) {
            runServer();
        } else {
            System.err.println("Unknown command: " + args[0]);
            System.exit(2);
        }
    }

    private static void runServer() throws IOException {
        System.out.println("Starting server...");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                UriBuilder.fromUri("http://localhost:8000").build(),
                new ResourceConfig(PollsResource.class));
        System.out.println("Press enter to stop server");
        System.in.read();
        server.stop();
    }
}
