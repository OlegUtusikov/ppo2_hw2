import entities.User;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new DB());
        server.start();
    }
}
