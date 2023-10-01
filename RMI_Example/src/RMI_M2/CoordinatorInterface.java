package RMI_M2;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface CoordinatorInterface extends Remote {
    void registerUser(UserInterface user) throws RemoteException;

    void searchFile(UserInterface from, String fileName) throws IOException;

    void unshareFile(UserInterface from, String fileName) throws IOException;

    void shareFile(UserInterface from, String fileName) throws IOException;
}
