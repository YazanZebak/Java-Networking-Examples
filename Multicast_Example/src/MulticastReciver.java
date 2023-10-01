
import java.net.*;

public class MulticastReciver {

    public static void main(String[] args) throws Exception {

        MulticastSocket ms = new MulticastSocket(4000);

        InetAddress ia = InetAddress.getByName("224.2.2.2");

        ms.joinGroup(ia);

        byte[] buffer = new byte[8192];

        while(true) {
            System.out.println("Receiver wait .... ");
            try{
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

                ms.receive(dp);

                String s = new String(dp.getData());

                System.out.println("Received: " + s);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
