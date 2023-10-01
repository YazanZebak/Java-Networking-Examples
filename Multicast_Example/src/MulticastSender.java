import java.net.*;
import java.util.Scanner;

public class MulticastSender {

    public static void main(String[] args) throws Exception {
        try {


            MulticastSocket ms = new MulticastSocket(4000);

            ms.setTimeToLive(0);

            InetAddress ia = InetAddress.getByName("224.2.2.2");

            int port = 4000;

            while (true) {
                System.out.println("Send: ");

                Scanner myObj = new Scanner(System.in);
                String str = myObj.nextLine();


                byte[] data = str.getBytes();
                DatagramPacket dp = new DatagramPacket(data, data.length, ia, port);
                ms.send(dp);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
