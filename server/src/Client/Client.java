package Client;

import Packets.JoinPacket;
import Packets.Packet;
import Packets.UserListPacket;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    JFrame frame;
    String name;

    JTextArea display;
    JoinedUsersDisplay users;
    JTextField chatBox;
    Socket connection;

    Client() {
        frame = new JFrame("Chat");
        display = new JTextArea();
        display.setEditable(false);
        users = new JoinedUsersDisplay();

        chatBox = new JTextField();
        chatBox.addKeyListener(new ChatBoxListener());


        JScrollPane chatDisplay = new JScrollPane(display);
        chatDisplay.setBorder(BorderFactory.createEmptyBorder());
        //frame.getContentPane().add(BorderLayout.NORTH, menu);
        frame.getContentPane().add(BorderLayout.EAST, users);
        frame.getContentPane().add(BorderLayout.SOUTH, chatBox);
        frame.getContentPane().add(BorderLayout.CENTER, chatDisplay);
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //JOptionPane userNamePrompt = new JOptionPane("enter your name", JOptionPane.showInputDialog(null));
        //JOptionPane.showMessageDialog(frame, "welcome to my chat client");

        try {
            connection = new Socket("96.52.76.131", 5432);
            display.append("Connected");
            new Thread(new ServerReader()).start();
            name = getName();
            new JoinPacket(name).send(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            display.append("You couldn't connect");
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


    private void sendText(boolean isChatMessage, String message) {
        try {
            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            if (isChatMessage) writer.println(name + ": " + message);
            else writer.println("meta-" + message);
            writer.flush();
            System.out.println("sent out message : " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getName() {
        String[] options = {"OK"};
        JPanel modal = new JPanel();
        JTextField input = new JTextField(15);
        modal.add(new JLabel("Choose a name"), -1);
        modal.add(input, -1);
        JOptionPane.showOptionDialog(frame, modal, "Choose a name", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        return input.getText();
    }

    private void handlePacket(Packet p) {
        if (p instanceof UserListPacket)
            handleUserListPacket((UserListPacket) p);
    }

    private void handleUserListPacket(UserListPacket p) {
        users.updateUserList(p.getNewUserList());
    }

    class ChatBoxListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                JTextField source = (JTextField) keyEvent.getSource();
                if (!source.getText().equals(""))
                    sendText(true, source.getText());
                source.setText("");
            }
        }

        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }

    class ServerReader implements Runnable {
        @Override
        public void run() {
            try {
                ObjectInputStream objectReader = new ObjectInputStream(connection.getInputStream());
                while (true) {
                    handlePacket((Packet)objectReader.readObject());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
