package repoll;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import repoll.server.service.PollsResource;
import repoll.server.service.rmi.RmiServiceFacadeImpl;
import repoll.server.ui.MainApplication;

import javax.swing.*;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;

/**
 * Server application main entry point
 */
public class Repoll {
    // alternatively could call BasicConfigurator#configure()
    public final static Logger LOG = Logger.getLogger(Repoll.class);

    public static final String RMI_SERVICE_URL = "//localhost/repoll";
    public static final String REST_SERVICE_URL = "http://localhost:8000";

    public static void main(String[] args) {

        String action = args.length >= 1 ? args[0] : null;

        if (action == null || action.equals("gui")) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MainApplication.getInstance().createAndShowGUI();
                }
            });
        }
        try {
            switch (action) {
                case "rest":
                    runRestServer();
                    break;
                case "rmi":
                    runRmiServer();
                    break;
                default:
                    LOG.error("Unknown command: " + args[0]);
                    System.exit(2);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    private static void runRestServer() throws Exception {
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                UriBuilder.fromUri(REST_SERVICE_URL).build(),
                new ResourceConfig(PollsResource.class));
        LOG.info("REST server started");
        waitKeyPressed();
        server.stop();
    }

    private static void runRmiServer() throws Exception {
        try {
            System.setSecurityManager(new RMISecurityManager());
            RmiServiceFacadeImpl facade = new RmiServiceFacadeImpl();
            Naming.rebind(RMI_SERVICE_URL, facade);
            LOG.info("RMI server started");
        } finally {
//            Naming.unbind(RMI_SERVICE_URL);
        }
    }


    private static void waitKeyPressed() throws IOException {
        System.out.println("Press enter to stop");
        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (IOException ignored) {
        }
    }
}
