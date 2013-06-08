package repoll;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;

import java.io.IOException;

public class Repoll {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("No command specified");
            System.exit(1);
        } else if (args[0].equals("service")) {
            runServer();
        } else {
            System.err.println("Unknown command: " + args[0]);
            System.exit(2);
        }
    }

    private static void runServer() throws IOException {
        System.out.println("Starting server...");
        HttpServer server = GrizzlyServerFactory.createHttpServer(
                "http://localhost:8000", new PackagesResourceConfig("repoll.service"));
        System.out.println("Press enter to stop server");
        System.in.read();
        server.stop();
    }
}
