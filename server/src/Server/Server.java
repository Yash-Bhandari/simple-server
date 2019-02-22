package Server;

import Packets.JoinPacket;
import Packets.Packet;
import Packets.UserListPacket;
import com.sun.corba.se.impl.orbutil.ObjectWriter;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.*;
import java.rmi.Remote;
import java.util.ArrayList;

public class Server implements Runnable, Serializable {
    ServerSocket serverSocket;
    ArrayList<Socket> clients;
    ArrayList<String> clientNames;

    public Server(int numThreads, int portNumber) {
        clients = new ArrayList<Socket>();
        clientNames = new ArrayList<String>();
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
                ObjectInputStream packetReader = new ObjectInputStream(socket.getInputStream());

                while (!socket.isClosed()) {
                    try {
                        handlePacket((Packet) packetReader.readObject());
                    } catch (SocketException e) {
                        closeSocket(socket);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
            clientNames.remove(clients.indexOf(socket));
            clients.remove(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePacket(Packet p) {
        if (p instanceof JoinPacket)
            handleJoinPacket((JoinPacket) p);
    }

    private void handleJoinPacket(JoinPacket jp) {
        clientNames.add(jp.getName());
        sendAll(new UserListPacket(clientNames));
    }

    private void sendAll(Packet p) {
        for (Socket s : clients) {
            try {
                p.send(s.getOutputStream());
            } catch (IOException e) {
                closeSocket(s);
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
