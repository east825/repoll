package repoll.core.rmi;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
public interface  RmiServiceFacade extends Remote {
    String SERVICE_URL = "//localhost/RmiServiceFacade";

    @NotNull
    List<String> getListOfStrings(@NotNull String firstItem) throws RemoteException;
}
