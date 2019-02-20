import java.util.Calendar;
import java.util.Date;

public class Message {
    private String text;
    private Date timeStamp;
    private String sender;
    private String senderIP;

    public Message (String text, String sender) {
        this.text = text;
        this.sender = sender;
        this.timeStamp = Calendar.getInstance().getTime();
    }

    @Override
    public String toString() {
        return(timeStamp + text);
    }

    public static void main(String[] args) {
        System.out.println(Calendar.getInstance().getTime());
    }

}
