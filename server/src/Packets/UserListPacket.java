package Packets;

import java.util.Collection;

public class UserListPacket extends Packet {
    Collection<String> userList;

    public UserListPacket(Collection<String> userList) {
        super(Packet.USERLIST);
        this.userList = userList;
    }

    public Collection<String> getNewUserList() {
        return userList;
    }
}
