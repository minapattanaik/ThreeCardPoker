import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

import javafx.application.Platform;

public class ClientConnect extends Thread {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Consumer<Serializable> callback;

    public ClientConnect(Consumer<Serializable> callback) {
        this.callback = callback;
    }

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        socket.setTcpNoDelay(true);
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Serializable data = (Serializable) in.readObject();
                Platform.runLater(() -> callback.accept(data));
            } catch (Exception e) {
                System.out.println("Connection closed");
                break;
            }
        }
    }

    public void send(Serializable data) {
        try {
            out.writeObject(data);
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}