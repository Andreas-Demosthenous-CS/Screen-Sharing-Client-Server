package remotedesktopclient;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;

public class RemoteDesktopClient {

    private final int maxUDPSize = ImageProcessLib.maxUDPSize;
    private final InetAddress ip;
    private final int portUDP, portTCP;
    private final TCPTransmitter transmitter;
    private final TCPReceiver receiver;
    private final Socket socketTCP;
    private final DatagramSocket socketUDP;
    private final RDPWorker rdpWorker;

    private int currentX, currentY;

    public RemoteDesktopClient(String ip, int portTCP, int portUDP) throws UnknownHostException, AWTException, IOException {

        this.currentX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        this.currentY = (int) MouseInfo.getPointerInfo().getLocation().getY();
        this.ip = InetAddress.getByName(ip);
        this.portUDP = portUDP;
        this.portTCP = portTCP;

        this.socketTCP = waitConnectionTCP(ip, portTCP);
        this.receiver = new TCPReceiver(new BufferedReader(new InputStreamReader(socketTCP.getInputStream())));
        receiver.start();
        this.transmitter = new TCPTransmitter(new PrintWriter(socketTCP.getOutputStream(), true));

        this.socketUDP = new DatagramSocket();

        this.rdpWorker = new RDPWorker(socketUDP, this.ip, portUDP);
        rdpWorker.start();

    }

    public Socket waitConnectionTCP(String ip, int portTCP) throws IOException {
        return new Socket(ip, portTCP);
    }

    public void sendDataUDP(byte[] data, int dataLength, InetAddress IP, int port, DatagramSocket dSocket) throws IOException, InterruptedException {
        DatagramPacket packet;
        byte[] buffer = null;
        System.out.println("My size: "+dataLength);
        System.out.println("My soi: " + ImageProcessLib.byteArrayToInt(ImageProcessLib.extractPart(data, 0, 2)));
        while (dataLength > 0) {

            buffer = new byte[maxUDPSize];

            if (dataLength >= maxUDPSize) {
                buffer = ImageProcessLib.extractPart(data, 0, maxUDPSize);
                data = ImageProcessLib.removePart(data, 0, maxUDPSize);
            } else {
                buffer = data.clone();
            }
            packet = new DatagramPacket(buffer, buffer.length, IP, port);

            while (!receiver.isReady()) {
                Thread.sleep(1);
            }
            
            receiver.setAvailability(false);
            
            dSocket.send(packet);       
            
            dataLength -= (maxUDPSize);

        }

    }

    public class RDPWorker extends Thread {

        private int imageWidth = 1067;
        private int imageHeight = 600;

        private Robot robot;
        private Rectangle imageSize;
        private DatagramSocket socketUDP;
        private InetAddress ip;
        private int portUDP;

        public RDPWorker(DatagramSocket socketUDP, InetAddress ip, int portUDP) {
            this.socketUDP = socketUDP;
            this.ip = ip;
            this.portUDP = portUDP;
            this.imageSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

            try {
                this.robot = new Robot();
            } catch (AWTException ex) {
                System.out.println("Error capturing screen");
                System.out.println(ex.getMessage());
            }

        }

        public void run() {
            ByteArrayInputStream imageBytes;
            ByteArrayOutputStream imageBytesCompressed;

            while (true) {

                BufferedImage image = null;
                try {
                    imageBytesCompressed = new ByteArrayOutputStream();
                    image = ImageProcessLib.captureScreen(robot, imageSize);

                    image = ImageProcessLib.getScaledInstance(image, imageWidth, imageHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

                    ImageIO.write(image, "gif", imageBytesCompressed);

                    sendDataUDP(imageBytesCompressed.toByteArray(), imageBytesCompressed.toByteArray().length, ip, portUDP, socketUDP);

                } catch (IOException ex) {
                    System.out.println("Error creating image");
                    System.out.println(ex.getMessage());
                } catch (InterruptedException ex) {
                    System.out.println("Error sending image bytes");
                    System.out.println(ex.getMessage());
                }

            }
        }
    }

    private class TCPReceiver extends Thread {

        private BufferedReader br;
        private String data;
        private boolean isReady;
        private Robot robot;

        TCPReceiver(BufferedReader br) throws AWTException {
            this.br = br;
            this.robot = new Robot();
            this.isReady = false;
            this.data = "";
        }

        public void run() {

            while (true) {
                try {
                    data = br.readLine();
                    if (data.equals("RDY")) {
                        isReady = true;
                    }

                } catch (IOException ex) {
                    System.out.println("Error receiving data");
                    System.exit(0);
                    System.out.println(ex.getMessage());
                }
            }

        }

        public boolean isReady() {
            return isReady;
        }

        public void setAvailability(boolean isReady) {
            this.isReady = isReady;
        }

        public String getData() {
            return data;
        }
    }

    private class TCPTransmitter {

        private PrintWriter pr;

        TCPTransmitter(PrintWriter pr) {
            this.pr = pr;
        }

        public void sendString(String msg) {
            pr.write(msg);
        }

    }

    public static void main(String[] args) {
        try {
            new RemoteDesktopClient("31.216.99.191", 7777, 9876);
        } catch (AWTException ex) {
            Logger.getLogger(RemoteDesktopClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Connection lost");
            System.exit(0);
        }
    }
}
