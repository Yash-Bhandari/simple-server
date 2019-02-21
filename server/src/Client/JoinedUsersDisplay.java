package Client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class JoinedUsersDisplay extends JPanel {
    private JLabel boxLabel;
    private JList<String> users;
    private DefaultListModel userList;


    JoinedUsersDisplay() {
        boxLabel = new JLabel("Online Users");
        userList = new DefaultListModel();
        users = new JList(userList);
        add(boxLabel, BorderLayout.NORTH);
        add(users, BorderLayout.SOUTH);
    }

    void addUser(String name) {
        name = name.trim();
        if (!taken(name)) {
            userList.addElement(name);
            System.out.println("added : " + name);
        }
        else addUser(name + "I");
    }

    private boolean taken(String name) {
        for (int i = 0; i < users.getModel().getSize(); i++)
            if (users.getModel().getElementAt(i).equals(name))
                return true;
        return false;
    }

    private void update() {

    }
}
