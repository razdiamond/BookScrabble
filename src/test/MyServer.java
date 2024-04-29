package test;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {
    int port;
    ClientHandler clientHandler;
    private volatile boolean stop;

    public MyServer(int port, ClientHandler clientHandler) {
        this.port = port;
        this.clientHandler = clientHandler;
        stop = false;
    }

    private void runServer() throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);
        while (!stop) {
            try {
                Socket socket = serverSocket.accept();
                try {
                    clientHandler.handleClient(socket.getInputStream(), socket.getOutputStream());
                    socket.getInputStream().close();
                    socket.getOutputStream().close();
                    socket.close();
                } catch (IOException ignored) {}
            } catch (SocketTimeoutException ignored) {}
        }
        serverSocket.close();
    }

    public void start() {
        new Thread(()-> {
            try {
                runServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void close() {
        stop = true;
    }
	
}
