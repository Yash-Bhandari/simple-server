package Packets;

public class MessagePacket extends Packet{
    String senderName;
    String message;

    public MessagePacket(String senderName, String message) {
        super(Packet.MESSAGE);
        this.senderName = senderName;
        this.message = message;
    }

    public String getSenderName(){
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    public String getComposite() {
        return senderName + " : " + message;
    }

}
