package RMI_M2;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserInterface extends Remote {
    String getUsername() throws RemoteException;

    String getPort() throws RemoteException;

    String getIPAddress() throws RemoteException;

    void downloadFile(String username, String path, String fileName) throws IOException;
}
