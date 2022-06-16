import java.net.*;
import java.util.HashMap;
import java.io.*;

public class UDPServer {
    DatagramSocket aSocket = null;
    private HashMap<String, String> map;
    private int port = 10000;

    public UDPServer(int port) {
        this.port = port;
    }

    public UDPServer(DatagramSocket aSocket) {
        this.aSocket = aSocket;
    }

    public void setMap(HashMap<String, String> map) {
        if (map == null) {
            this.map = new HashMap<>();
        } else {
            this.map = map;
        }
    }

    public void run() {
        try {
            aSocket = new DatagramSocket(port);
            System.out.println("server running on port: " + port + "    " + System.currentTimeMillis());
            byte[] buffer = new byte[1000];
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);

                System.out.println("[Client] :  " + new String(request.getData()) + "   " + System.currentTimeMillis());
                String requestStr = new String(request.getData());
                String replyStr = processClientRequest(requestStr);
                if (replyStr == "Invalid request") {
                    System.out.println("Receiced malfromed request of length " + request.getLength()
                            + " from Client on [Address]" + request.getAddress().getHostAddress() + " on [Port]"
                            + request.getPort());
                }

                DatagramPacket reply = new DatagramPacket(replyStr.getBytes(), replyStr.getBytes().length,
                        request.getAddress(),
                        request.getPort());
                aSocket.send(reply);
            }
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
                return "Invalid request";
            }
        }

    }

    public void parseExecution(String line) {

    }

    public static void main(String args[]) {
        int new_port = 10000;
        // takes the port number
        if (args.length == 1) {

            new_port = Integer.parseInt(args[0]);

        }
        UDPServer s = new UDPServer(new_port);
        s.setMap(null);
        s.run();
        System.out.println("UDPServer is running..." + "    " + System.currentTimeMillis());

    }

}
