package Client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

class JoinedUsersDisplay extends JPanel {
    private JLabel boxLabel;
    private JList<String> users;
    private DefaultListModel userList;


    JoinedUsersDisplay() {
        setLayout(new BorderLayout());
        boxLabel = new JLabel("Online Users");
        userList = new DefaultListModel();
        users = new JList(userList);
        add(BorderLayout.NORTH, boxLabel);
        add(BorderLayout.CENTER, users);
    }

    void updateUserList(Collection<String> newUserList){
        userList.clear();
        for(String s : newUserList)
            userList.addElement(s);
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

}
