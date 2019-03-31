package Client;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ClientDisplay {
    Client client;
    JFrame frame;

    JTextPane display;
    StyledDocument chatDoc;
    JoinedUsersDisplay users;
    JTextField chatBox;


    public ClientDisplay(Client client) {
        this.client = client;
        frame = new JFrame("Chat");
        display = new JTextPane();
        display.setEditable(false);
        chatDoc = display.getStyledDocument();
        setStyles(chatDoc);
        users = new JoinedUsersDisplay();
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
    }

    public void displayMessage(String sender, String message) {
        try {
            chatDoc.insertString(chatDoc.getLength(), "\n" + sender + " : ", chatDoc.getStyle("name"));
            chatDoc.insertString(chatDoc.getLength(), message, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    //Prompts the user for a name and returns it
    public String getName() {
        String[] options = {"OK"};
        JPanel modal = new JPanel();
        JTextField input = new JTextField(15);
        modal.add(new JLabel("Choose a name"), -1);
        modal.add(input, -1);
        JOptionPane.showOptionDialog(frame, modal, "Choose a name", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        return input.getText();
    }

    private void setStyles(StyledDocument document) {
        document.addStyle("default", null);
        StyleConstants.setFontSize(document.getStyle("default"), 20);

        document.addStyle("server", document.getStyle("default"));
        StyleConstants.setForeground(document.getStyle("server"), Color.BLUE);
        StyleConstants.setItalic(document.getStyle("server"), true);

        document.addStyle("name", document.getStyle("default"));
        StyleConstants.setBold(document.getStyle("name"), true);

    }

    class ChatBoxListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                JTextField source = (JTextField) keyEvent.getSource();
                if (!source.getText().equals(""))
                    client.sendText(source.getText());
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
}
