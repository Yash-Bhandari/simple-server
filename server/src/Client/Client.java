package Client;

import Packets.JoinPacket;
import Packets.MessagePacket;
import Packets.Packet;
import Packets.UserListPacket;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    ClientDisplay cd;
    Socket connection;
    ObjectOutputStream packetWriter;
    String name;
    JoinedUsersDisplay users;

    public Client() {
        cd = new ClientDisplay(this);
        users = cd.users;

        try {
            connection = new Socket("96.52.76.131", 5432);
            packetWriter = new ObjectOutputStream(connection.getOutputStream());
            cd.displayMessage("Server", "Connected");
            new Thread(new ServerReader()).start();
            String name = cd.getName();
            new JoinPacket(name).send(packetWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Client();
    }

    public void sendText(String message) {
        try {
            new MessagePacket(name, message).send(packetWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handlePacket(Packet p) {
        if (p instanceof UserListPacket)
            handleUserListPacket((UserListPacket) p);
        if (p instanceof MessagePacket)
            handleMessagePacket((MessagePacket) p);

    }

    private void handleUserListPacket(UserListPacket p) {
        users.updateUserList(p.getNewUserList());
    }

    public void handleMessagePacket(MessagePacket mp) {
        cd.displayMessage(mp.getSenderName(), mp.getMessage());
    }

    class ServerReader implements Runnable {
        @Override
        public void run() {
            try {
                ObjectInputStream packetReader = new ObjectInputStream(connection.getInputStream());
                while (true) {
                    handlePacket((Packet) packetReader.readObject());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

        }
    }
}
