package RMI_M1;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class User extends UnicastRemoteObject implements UserInterface {
    public String username;
    public String port;
    public String ipAddress;
    final static List<String> sharedFiles = new ArrayList<>();

    public User(String username, String ipAddress, String port) throws RemoteException {
        super(Integer.parseInt(port));
        this.username = username;
        this.port = port;
        this.ipAddress = ipAddress;
    }
    @Override
    public String getIPAddress() throws RemoteException {
        return ipAddress;
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    @Override
    public String getPort() throws RemoteException {
        return port;
    }

    @Override
    public String findSharedFile(String from, String fileName) throws RemoteException {
        if (!this.username.equalsIgnoreCase(from)) {
            File dir = new File(this.username);
            for (File child : Objects.requireNonNull(dir.listFiles())) {
                if (child.getName().equalsIgnoreCase(fileName)) {
                    return getIPAddress() + ":" + getPort();
                }
            }
        }
        System.out.println("File " + fileName + " Not Found");
        return null;
    }

    @Override
    public void downloadFile(UserInterface user, String path, String fileName) throws IOException, RemoteException {
        System.out.println("Fetched from path " + path);
        System.out.println("Do you want to download it? y/n");
        final Scanner scanner = new Scanner(System.in);
        final String input = scanner.next();
        if (input.equalsIgnoreCase("y")) {
            File file = new File(user.getUsername() + File.separator + fileName);
            FileInputStream sourcePath = new FileInputStream(file);

            File outputFile = new File(username + File.separator + fileName);
            Path destinationPath = outputFile.toPath();

            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("The File Has Been Downloaded");
        }
    }


    public static void main(String[] args) throws IOException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(6667);

        CoordinatorInterface coordinator = (CoordinatorInterface) registry.lookup("Coordinator");

        final Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a username");
        final String username = scanner.nextLine();


        System.out.println("Please enter a port to work on");
        final String port = scanner.nextLine();

        Registry userRegistry = LocateRegistry.createRegistry(Integer.parseInt(port));

        coordinator.registerUser(new User(username, InetAddress.getLocalHost().getHostAddress(), port));

        do {
            String[] options = {"1-Find file", "2-Exit"};

            for (String option : options) {
                System.out.println(option);
            }

            final String choice = scanner.next();

            if ("1".equals(choice)) {
                System.out.println("Enter the name of the file you want to search for");
                final String searchName = scanner.next();
                coordinator.searchFile(new User(username, InetAddress.getLocalHost().getHostAddress(), port), searchName);
            } else {
                System.exit(0);
            }

        } while (true);
    }

}