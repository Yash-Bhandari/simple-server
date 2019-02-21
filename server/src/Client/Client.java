package Client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    JFrame frame;
    String name;
    ArrayList<String> joinedUsers;

    JTextArea display;
    JoinedUsersDisplay users;
    JTextField chatBox;
    Socket connection;

    Client() {
        joinedUsers = new ArrayList<String>();
        frame = new JFrame("Chat");
        display = new JTextArea();
        display.setEditable(false);
        users = new JoinedUsersDisplay();
        users.addUser("Yash");
        users.addUser("Andrew");

        chatBox = new JTextField();
        chatBox.addKeyListener(new ChatBoxListener());


        //frame.getContentPane().add(BorderLayout.NORTH, menu);
        frame.getContentPane().add(BorderLayout.EAST, users);
        frame.getContentPane().add(BorderLayout.SOUTH, chatBox);
        frame.getContentPane().add(BorderLayout.CENTER, display);
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //JOptionPane userNamePrompt = new JOptionPane("enter your name", JOptionPane.showInputDialog(null));
        //JOptionPane.showMessageDialog(frame, "welcome to my chat client");
        name = JOptionPane.showInputDialog("Enter your name");

        try {
            connection = new Socket("96.52.76.131", 5432);
            display.append("Connected");
        } catch (IOException e) {
            e.printStackTrace();
            display.append("You couldn't connect");
        }
        new Thread(new ServerReader()).start();
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Client();
    }

    private void sendText(String s) {
        try {
            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            writer.println(name + ": " + s);
            writer.flush();
            System.out.println("sent out message" + s);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addUser(String name) {
        name = name.trim();
        for (String s : joinedUsers)
            if (s.equals(name)) {
                addUser(name + "I");
                break;
            }
        joinedUsers.add(name);
    }

    private void handleInput(String s) {
        if (s.substring(0, 4).equals("meta")) {
            if (s.substring(5, 7).equals("uj")) // user joined
                joinedUsers.add(s.substring(7));
        }
    }

    class ChatBoxListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                JTextField source = (JTextField) keyEvent.getSource();
                if (!source.getText().equals(""))
                    sendText(source.getText());
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
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                while (true) {
                    display.append("\n" + reader.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
