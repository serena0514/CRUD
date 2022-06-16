
import java.net.*;
import java.io.*;

public class KeyDataClientTCP {

    private static int timeout = 300000;

    public static void main(String[] args) throws IOException {
        String hostname = "localhost";
        int port = 10000;
        if (args.length != 2) {
            System.out.println("Use default setting...");
        } else {
            hostname = args[0];
            port = Integer.parseInt(args[1]);
        }
        // open connection at the port
        Socket s1 = new Socket(hostname, port);
        s1.setSoTimeout(timeout);
        BufferedReader reader = new BufferedReader(new InputStreamReader(s1.getInputStream()));
        PrintWriter writer = new PrintWriter(s1.getOutputStream(), true);
        // reader.readLine();

        Console console = System.console();
        String text;
        do {

            String time = reader.readLine();
            System.out.println("[Server] " + time + "   " + System.currentTimeMillis());
            text = console.readLine("Enter text: ");
            writer.println(text);

        } while (!text.equals("bye"));

        s1.close();
        System.out.println(reader.readLine());
        // PrintWriter writer = new PrintWriter(s1.getOutputStream());

    }
}
