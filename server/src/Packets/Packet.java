package Packets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {
    public static final String MESSAGE = "message";
    public static final String USERLIST = "userlist";
    public static final String USERJOIN = "userjoin";
    private String type;

    protected Packet(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void send(OutputStream os) throws IOException {
        new ObjectOutputStream(os).writeObject(this);
    }

    public static void main(String[] args) {
        ArrayList b = new ArrayList();
        b.remove(-1 );
    }
}
