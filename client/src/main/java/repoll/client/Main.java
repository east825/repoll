package repoll.client;

import org.apache.log4j.Logger;
import repoll.core.rmi.RmiServiceFacade;
import repoll.models.User;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

/**
 * @author Mikhail Golubev
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        System.setSecurityManager(new RMISecurityManager());
        RmiServiceFacade serviceFacade = (RmiServiceFacade) Naming.lookup(RmiServiceFacade.SERVICE_URL);
        User user = serviceFacade.findUser("east825", "7&^%*&^b");
        System.out.println(serviceFacade.getUserPolls(user));
    }
}
