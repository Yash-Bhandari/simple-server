import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {
    private String storedMessage = "You are the first visitor. Leave a message!";
    ServerSocket serverSocket;
    ArrayList<Socket> clients;

    public Server(int numThreads, int portNumber) {
        clients = new ArrayList<Socket>();
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Started a server with " + numThreads + " threads on port " + portNumber);
        } catch (IOException e) {
            System.out.println("Couldn't start a server");
        }

        for (int i = 0; i < numThreads; i++) {
            new Thread(this).start();
        }
    }


    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                clients.add(socket);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while(!socket.isClosed()) {

                    String input = reader.readLine();
                    if (input != null) {
                        setStoredMessage(input);
                        for (Socket s : clients)
                            serviceRequest(s);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void serviceRequest(Socket socket) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(storedMessage);
        writer.flush();
        System.out.println("Serviced a request from " + socket.getRemoteSocketAddress());
    }

    private void setStoredMessage(String newMessage) {
        System.out.println("Changing the stored message to : " + newMessage);
        storedMessage = newMessage;
    }

    public static void main(String[] args) {
        new Server(3, 5000);
    }

}
