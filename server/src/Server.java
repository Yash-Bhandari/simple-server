import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {
    ServerSocket serverSocket;
    ArrayList<Socket> clients;
    ArrayList

    public Server(int numThreads, int portNumber) {
        clients = new ArrayList<Socket>();
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Started a server with " + numThreads + " threads on port " + portNumber);
            System.out.println(Inet4Address.getLocalHost());
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
                System.out.println("connected to " + socket.getInetAddress());
                clients.add(socket);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (!socket.isClosed()) {
                    try {
                        String input = reader.readLine();
                        for (Socket s : clients)
                            sendMessage(s, input);
                    } catch (SocketException e) {
                        socket.close();
                        clients.remove(socket);
                    }
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    private void sendMessage(Socket socket, String message) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(message);
        writer.flush();
        System.out.println("Serviced a request from " + socket.getRemoteSocketAddress());
    }

    public static void main(String[] args) {
        new Server(3, 5432);
    }

}
