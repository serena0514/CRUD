import java.io.*;
import java.net.*;
import java.util.*;

public class KeyDataServerTCP {

    protected ServerSocket svsk;
    protected Socket socket;
    private int port = 10000;
    private HashMap<String, String> map;
    protected boolean isStopped = false;

    public void setSocket(Socket s) {
        this.socket = s;

    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }

    public void openServerSocket(int port) {
        this.port = port;
        try {
            svsk = new ServerSocket(port);
            // svsk.setSoTimeout(10000);
            System.out.println("KeyDataserverTCP started on port " + port + "    " + System.currentTimeMillis());
        } catch (IOException e) {
            throw new RuntimeException("Cannot oepn port " + port + "    " + System.currentTimeMillis());
        }
    }

    public void run() throws SocketTimeoutException {
        if (svsk == null) {
            throw new RuntimeException(" no server socket available yet");
        }

        System.out.println("Waiting for client");
        while (!isStopped) {

            // connect to client
            try {
                socket = svsk.accept();
                Inet4Address cadd = (Inet4Address) socket.getInetAddress();
                System.out.println("Connected to client [address: " + cadd.getHostAddress() + " port: " + port + "]"
                        + "    " + System.currentTimeMillis());
                map = new HashMap<>();
                System.out.println("Process client request..." + "    " + System.currentTimeMillis());

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                // inital conversation with client
                String textFromClient;
                writer.println("Hi");

                do {
                    textFromClient = reader.readLine();
                    System.out.println("[client] " + textFromClient + "    " + System.currentTimeMillis());
                    String response = processClientRequest(textFromClient);
                    System.out.println("[server]" + response + "    " + System.currentTimeMillis());
                    writer.println("response: " + response);
                } while (!textFromClient.equals("bye"));

                // writer.writeUTF("[Server] received: " + " --" +time);
                System.out.println("Client connection lost " + port + "    " + System.currentTimeMillis());
                writer.close();
                reader.close();
                socket.close();

            } catch (Exception e) {
                if (isStopped) {
                    throw new RuntimeException("server socket is stopped" + "    " + System.currentTimeMillis());
                }
                System.out.println(e);
            }

            // read datainputstream from client

        }

    }

    // put key value, get key, delete key value
    private String processClientRequest(String request) {
        System.out.println("process client request...");
        String[] requests = request.split(" ");
        if (requests.length == 1 || requests.length == 0) {
            return "Invalid request";
        } else {
            String method = requests[0];
            String key = requests[1];
            // check if key exists:
            if (method.equals("PUT")) {
                if (requests.length == 2) {
                    return "Invalid requests for PUT";
                } else {
                    // check if key exists
                    map.put(key, requests[2]);
                    return "sucessfully put " + requests[1] + " " + requests[2];
                }
            } else if (method.equals("GET")) {
                if (!map.containsKey(key)) {
                    return "this key does not exist";
                } else {
                    return map.get(key);
                }
            } else if (method.equals("DELETE")) {
                if (!map.containsKey(key)) {
                    return "this key does not exist";
                } else {
                    map.remove(key);
                    return "key " + key + " is sucessfully removed";
                }
            } else {
                return "request invalid";
            }
        }

    }

    public void parseExecution(String line) {

    }

    public void stopServer() throws IOException {
        isStopped = true;
        svsk.close();
    }

    public static void main(String[] args) throws IOException {

        int new_port = 10000;
        // takes the port number
        if (args.length == 1) {
            try {
                new_port = Integer.parseInt(args[0]);

            } catch (Exception e) {

            }
        }
        System.out.println("KeyDataServerTCP is running..." + "    " + System.currentTimeMillis());
        KeyDataServerTCP server = new KeyDataServerTCP();
        server.openServerSocket(new_port);
        server.run();

        // server.stopServer();

    }

}
