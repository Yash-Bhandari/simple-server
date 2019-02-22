package Packets;

public class JoinPacket extends Packet{
    private String name;

    public JoinPacket(String name) {
        super(Packet.USERJOIN);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
