import javax.swing.*;
import javax.xml.soap.Text;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

public class Client {
    JFrame frame;
    Button b1, b2, b3;
    boolean[] buttonStatus = new boolean[3];
    String name;

    TextArea display;
    TextField chatBox;
    JToolBar menu;
    Socket connection;

    Client() {
        frame = new JFrame("Chat");
        /*b1 = new Button("Press me!");
        b2 = new Button("Me too!");
        b3 = new Button("And me!");
        b1.addActionListener(new ButtonListener());
        b2.addActionListener(new ButtonListener());
        b3.addActionListener(new ButtonListener());
        menu = new JToolBar();*/
        display = new TextArea("", 5, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        display.setEditable(false);
        /*menu.add(BorderLayout.WEST, b1);
        menu.add(BorderLayout.CENTER, b2);
        menu.add(BorderLayout.EAST, b3);
        menu.setFloatable(false);*/
        chatBox = new TextField();
        chatBox.addKeyListener(new ChatBoxListener());


        //frame.getContentPane().add(BorderLayout.NORTH, menu);
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

    private void pressedButton(Button b) {
        if (b.equals(b1))
            buttonStatus[0] = true;
        if (b.equals(b2))
            buttonStatus[1] = true;
        if (b.equals(b3))
            buttonStatus[2] = true;
        for (boolean bool : buttonStatus)
            if (!bool) return;
        display.setText("all the buttons have been pressed");
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

    class ChatBoxListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                TextField source = (TextField) keyEvent.getSource();
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
