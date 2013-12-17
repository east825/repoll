package repoll.server.rmi;

import repoll.core.rmi.RmiServiceFacade;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
public class RmiServiceFacadeImpl extends UnicastRemoteObject implements RmiServiceFacade {
    private static RmiServiceFacade INSTANCE = null;

    // dumb singleton
    public static RmiServiceFacade getInstance() throws RemoteException {
        if (INSTANCE == null) {
            INSTANCE = new RmiServiceFacadeImpl();
        }
        return INSTANCE;
    }

    // TODO: make private again
    public RmiServiceFacadeImpl() throws RemoteException {
        // do nothing
    }

    @NotNull
    @Override
    public List<String> getListOfStrings(@NotNull String firstItem) throws RemoteException {
        List<String> result = new LinkedList<>(Arrays.asList("bar", "baz"));
        result.add(0, firstItem);
        return result;
    }
}
