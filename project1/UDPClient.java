import java.net.*;
import java.io.*;

public class UDPClient {
    static int port = 10000;
    static int timeout = 300000;

    public static void main(String[] args) {
        DatagramSocket aSocket = null;
        String message = "hi";
        byte[] hi = message.getBytes();

        Console console = System.console();
        String text;

        byte[] buffer = null;

        try {
            aSocket = new DatagramSocket();
            aSocket.setSoTimeout(timeout);
            InetAddress aHost = InetAddress.getByName("localhost");
            DatagramPacket aPacket = new DatagramPacket(hi, hi.length, aHost, port);
            aSocket.send(aPacket); // saying hi to the server

            buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            System.out.println("Reply: " + new String(reply.getData()));

            //
            do {

                text = console.readLine("Enter request: ");
                DatagramPacket request = new DatagramPacket(text.getBytes(), text.getBytes().length, aHost, port);
                aSocket.send(request);

                buffer = new byte[1000];
                reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);
                System.out.println("Reply: " + new String(reply.getData()) + "   " + System.currentTimeMillis());

            } while (!text.equals("bye"));

        } catch (SocketException e) {
            System.out.println("socket" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO" + e.getMessage());
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }

    }

}
