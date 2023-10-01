package RMI_M1;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserInterface extends Remote {

    String getUsername() throws RemoteException;

    String getPort() throws RemoteException;

    String getIPAddress() throws RemoteException;
    String findSharedFile(String username, String fileName) throws RemoteException;
    void downloadFile(UserInterface user, String path, String fileName) throws IOException, RemoteException;
}