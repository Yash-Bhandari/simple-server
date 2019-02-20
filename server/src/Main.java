import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        System.out.println(receive());
        if (args.length > 0)
            send(args[0]);
    }

    private static String receive() {
        try {
            Socket connection = new Socket("172.21.43.81", 5000);
            System.out.println("connected");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String message = reader.readLine();
            connection.close();
            return "The following message was received from the server : " + message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ayye";
    }

    public static void send(String message) {
        try {
            Socket connection = new Socket("172.21.43.81", 5000);
            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
