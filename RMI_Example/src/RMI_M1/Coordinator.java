package RMI_M1;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class Coordinator extends UnicastRemoteObject implements CoordinatorInterface {
    private static List<UserInterface> users = new ArrayList<>();
    static Registry registry;

    public Coordinator() throws RemoteException {
        super();
    }

    @Override
    public void registerUser(UserInterface user) throws RemoteException {
        for (UserInterface currentUser : users) {
            if (currentUser.getUsername().equals(user.getUsername())) {
                return;
            }
        }
        users.add(user);
        System.out.println("User registered: " + user.getUsername());
        System.out.println("IP Address: " + user.getIPAddress() + ":" + user.getPort());
    }

    @Override
    public void searchFile(UserInterface from, String fileName) throws IOException, RemoteException {
        for (UserInterface user : users) {
            final String result = user.findSharedFile(from.getUsername(), fileName);
            if (result != null) {
                from.downloadFile(user, result, fileName);
                return;
            }
        }
        System.out.println("File " + fileName + " Not Found");
    }

    public static void main(String[] args) {
        try {
            Coordinator coordinator = new Coordinator();
            registry = LocateRegistry.createRegistry(6667);
            registry.bind("Coordinator", coordinator);
            System.out.println("Coordinator started.");
        }
        catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}