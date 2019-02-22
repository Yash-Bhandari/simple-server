package Server;

import Packets.JoinPacket;
import Packets.MessagePacket;
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
    ArrayList<ObjectOutputStream> clients;
    ArrayList<String> clientNames;

    public Server(int numThreads, int portNumber) {
        clients = new ArrayList<ObjectOutputStream>();
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
                ObjectInputStream packetReader = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream packetWriter = new ObjectOutputStream(socket.getOutputStream());
                clients.add(packetWriter);

                while (!socket.isClosed()) {
                    try {
                        handlePacket((Packet) packetReader.readObject());
                    } catch (SocketException e) {
                        closeSocket(packetWriter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void closeSocket(ObjectOutputStream oos) {
        System.out.println("closing");
        int index = clients.indexOf(oos);
        if (index >= 0)
            clientNames.remove(index);
        clients.remove(oos);
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendAll(new UserListPacket(clientNames));
    }

    private void handlePacket(Packet p) {
        if (p instanceof JoinPacket)
            handleJoinPacket((JoinPacket) p);
        else if (p instanceof MessagePacket)
            handleMessagePacket((MessagePacket) p);
    }

    private void handleJoinPacket(JoinPacket jp) {
        clientNames.add(jp.getName());
        sendAll(new UserListPacket(clientNames));
    }

    private void handleMessagePacket(MessagePacket mp) {
        sendAll(new UserListPacket(clientNames));
        sendAll(mp);
    }


    private void sendAll(Packet p) {
        for (ObjectOutputStream oos : clients) {
            try {
                p.send(oos);
                oos.reset();
            } catch (IOException e) {
                closeSocket(oos);
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
        new Server(20, 5432);
    }

}
