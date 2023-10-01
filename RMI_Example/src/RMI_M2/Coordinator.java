package RMI_M2;


import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Coordinator extends UnicastRemoteObject implements CoordinatorInterface {


    static Registry registry;
    private static Map<String, Set<String>> sharedFiles = new HashMap<>();

    protected Coordinator() throws RemoteException {
        super();
    }

    @Override
    public void registerUser(UserInterface user) throws RemoteException {
        for (String currentUser : sharedFiles.keySet()) {
            if (currentUser.split(",")[0].equals(user.getUsername())) {
                return;
            }
        }
        sharedFiles.put(user.getUsername() + "," + user.getIPAddress(), new HashSet<>());
        System.out.println("User registered: " + user.getUsername());
        System.out.println("IP Address: " + user.getIPAddress());
    }

    @Override
    public void searchFile(UserInterface from, String fileName) throws IOException, RemoteException {
        final List<String> usersHaveFiles = new ArrayList<>();
        sharedFiles.forEach((user, files) -> files.forEach(currentFile -> {
            if (currentFile.equalsIgnoreCase(fileName)) {
                usersHaveFiles.add(user);
            }
        }));
        for (String user : usersHaveFiles) {
            from.downloadFile(user.split(",")[0], user.split(",")[1], fileName);
        }
    }

    @Override
    public void unshareFile(UserInterface from, String fileName) throws IOException {
        sharedFiles.get(from.getUsername() + "," + from.getIPAddress()).remove(fileName);
    }

    @Override
    public void shareFile(UserInterface from, String fileName) throws IOException {
        sharedFiles.get(from.getUsername() + "," + from.getIPAddress()).add(fileName);
    }


    public static void main(String[] args) {
        try {
            registry = LocateRegistry.createRegistry(7712);
            CoordinatorInterface coordinator = new Coordinator();
            registry.bind("Coordinator", coordinator);
            System.out.println("Coordinator started.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }

}
