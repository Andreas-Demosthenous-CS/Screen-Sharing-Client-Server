package remotedesktop;

import java.awt.AWTException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private ServerSocket sS;
    private Socket s;
    private ScreenFrame fr;
    private ConnectionListener listener;

    public Server(String ip, int portTCP, int portUDP) throws IOException, AWTException, InterruptedException {
        fr = new ScreenFrame();
        listener = new ConnectionListener(ip, portTCP, portUDP);
        listener.start();

    }

    private class ConnectionListener extends Thread {

        private boolean isConnected;
        private ServerSocket serverSocket;
        private Socket connection;
        private int portUDP, portTCP;
        private String ip;

        public ConnectionListener(String ip, int portTCP, int portUDP) throws IOException {
            this.ip = ip;
            serverSocket = new ServerSocket(portTCP);
            isConnected = false;
            this.portUDP = portUDP;
            this.portTCP = portTCP;
            
        }

        public void run() {
            while (!isConnected) {
                fr.getStatusArea().append(" Server status: UP");
                fr.getStatusArea().append("\n Server ip: " + ip);
                fr.getStatusArea().append("\n Listening ports: " + portTCP + " , " + portUDP);
                fr.getStatusArea().append("\n Waiting for connections...");
                try {
                    System.out.println("Listening");
                    connection = serverSocket.accept();
                    System.out.println("Bound");
                } catch (IOException ex) {
                    System.out.println("Connection error");
                    System.out.println(ex.getMessage());
                }
                fr.getStatusArea().append("\n Connected: " + connection.getInetAddress().getCanonicalHostName());
                ServerWorker sw = new ServerWorker(fr, connection, portUDP);
                sw.run();
                isConnected = true;
            }
        }

        public boolean isConnectd() {
            return isConnected;
        }

        public void disconnect() {
            isConnected = false;
        }

        public void startListener() {
            this.notify();
        }

        public void stopListener() {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
