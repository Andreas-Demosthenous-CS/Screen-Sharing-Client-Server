package remotedesktop;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ServerWorker implements Runnable {

    //final byte[] jpgSOI = {(byte) 65496};
    final String jpgSOI = "FFD8FF";
    final String pngSOI = "89504E470D0A1A0A0000000D494844";
    final String gifSOI = "474946383961";
    private ChunkList chunkBuffer;
    private Socket socket = null;
    private DatagramSocket ds;
    private ScreenFrame fr = null;
    private JLabel screen = null;
    ProcessImageChunkThread prc = null;

    TCPTransmitter transmitter;
    TCPReceiver receiver;

    ServerWorker(ScreenFrame fr, Socket clientSocket, int portUDP) {
        this.fr = fr;
        this.socket = clientSocket;
        screen = fr.getScreen();
        chunkBuffer = new ChunkList();

        try {

            //Setting UDP socket
            ds = new DatagramSocket(portUDP);

            //setting TCP transmitter/receiver
            transmitter = new TCPTransmitter(new PrintWriter(socket.getOutputStream(), true));
            receiver = new TCPReceiver(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            receiver.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {

        try {

            new addChunkThread(ds).start();

            prc = new ProcessImageChunkThread();
            prc.start();

        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public byte[] removePart(byte[] bytes, int start, int end) {
        byte[] firstPart = Arrays.copyOfRange(bytes, 0, start);
        byte[] secondPart = Arrays.copyOfRange(bytes, end, bytes.length);
        byte[] result = new byte[firstPart.length + secondPart.length];
        System.arraycopy(firstPart, 0, result, 0, firstPart.length);
        System.arraycopy(secondPart, 0, result, firstPart.length, secondPart.length);
        return result;
    }

    public byte[] extractPart(byte[] bytes, int start, int end) {
        return Arrays.copyOfRange(bytes, start, end);
    }

    public byte[] addOnStart(byte[] data, byte[] added) {
        byte[] result = new byte[added.length + data.length];
        System.arraycopy(added, 0, result, 0, added.length);
        System.arraycopy(data, 0, result, added.length, data.length);
        return result;
    }

    private BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    private boolean byteEquals(byte[] b1, byte[] b2) {
        if (b1.length != b2.length) {
            return false;
        }
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                return false;
            }
        }
        return true;
    }

    public void printBytes(byte[] data) {
        int cnt = 0;
        for (byte b : data) {
            String st = String.format("%02X", b);
            System.out.print(st);
            if (cnt % 20 == 0) {
                System.out.println("");
            }

            cnt++;
        }
    }

    public String bytesToString(byte[] data) {
        String hex = "";
        String st;
        for (byte b : data) {
            st = String.format("%02X", b);
            hex += st;
        }
        return hex;
    }

    public static int byteArrayToInt(byte[] encodedValue) {

        int value = 0;
        int cnt = 0;

        while (cnt < encodedValue.length) {

            value |= (encodedValue[cnt] & 0xFF) << (Byte.SIZE * (encodedValue.length - 1 - cnt));
            cnt++;

        }

        return value;

    }

    public byte[] addOnEnd(byte[] data, byte[] added) {
        byte[] result = new byte[added.length + data.length];
        System.arraycopy(data, 0, result, 0, data.length);
        System.arraycopy(added, 0, result, data.length, added.length);
        return result;
    }

    private ArrayList<byte[]> removeHeaders(ArrayList<byte[]> chunks) {
        for (int i = 0; i < chunks.size(); i++) {
            chunks.set(i, removePart(chunks.get(i), chunks.get(i).length - 1, chunks.get(i).length));
        }
        return chunks;
    }

    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    private class TCPReceiver extends Thread {

        private BufferedReader br;
        private String data = "NULL";

        TCPReceiver(BufferedReader br) {
            this.br = br;
        }

        public void run() {

            while (true) {
                try {
                    data = br.readLine();
                } catch (IOException ex) {
                    // System.out.println("Error receiving data");
                }
            }

        }

        public void setData(String data) {
            this.data = data;
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
            pr.println(msg);

        }

    }

    private class ProcessImageChunkThread extends Thread {

        private boolean firstImage = true;

        ProcessImageChunkThread() {

        }

        public void run() {
            ChunkList image = new ChunkList();

            while (true) {
                //System.out.println("Size of buffer: "+ chunkBuffer.size());
                if (!chunkBuffer.isEmpty() && chunkBuffer.getFirst().isStartOfImage()) {

                    image.add(chunkBuffer.removeFirst());
                    while (chunkBuffer.isEmpty()) {
                        try {
                            //transmitter.sendString("RDY");
                            Thread.sleep(0, 1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    while (!chunkBuffer.isEmpty() && !chunkBuffer.getFirst().isStartOfImage()) {
                        image.add(chunkBuffer.removeFirst());
                        while (chunkBuffer.isEmpty()) {
                            //transmitter.sendString("RDY");
                            try {
                                Thread.sleep(0, 1);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    BufferedImage finalImage = image.convert();
                    if (finalImage != null) {
                        screen.setIcon(new ImageIcon(ImageProcessLib.resizeImage(finalImage, 1280, 720)));

                    } else {
                        System.out.println("nuller");
                    }
                    image = new ChunkList();
                } else if (chunkBuffer.isEmpty()) {
                    //transmitter.sendString("RDY");
                }
                if (image.isEmpty()) {
                    image = new ChunkList();

                }

                try {
                    Thread.sleep(0, 1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private class ImageChunk {

        byte[] chunk;
        byte[] soi;
        private boolean isStartOfImage;

        ImageChunk(byte[] imageBytes) {
            chunk = imageBytes.clone();
            //PNG
            soi = extractPart(chunk, 0, 15);            
            isStartOfImage = pngSOI.equals(bytesToString(soi));

            //JPG
            //soi = extractPart(chunk, 0, 3);
            //isStartOfImage = jpgSOI.equals(bytesToString(soi));
            //soi = extractPart(chunk, 0, 3);
            //isStartOfImage = (byteArrayToInt(soi) == 65496);
            //System.out.println("soi: " + ImageProcessLib.byteArrayToInt(soi));

            //GIF
            soi = extractPart(chunk, 0, 6);  
            //System.out.println("SOI: "+bytesToString(soi));
            isStartOfImage = gifSOI.equals(bytesToString(soi));
        }

        public byte[] getChunk() {
            return chunk;
        }

        public byte[] getSOI() {
            return soi;
        }

        public boolean isStartOfImage() {
            return isStartOfImage;
        }

    }

    private class ChunkList extends LinkedList<ImageChunk> {

        ChunkList() {
        }

        public byte[] mergeChunks() {
            byte[] mergedChunks = new byte[0];
            for (int i = 0; i < size(); i++) {
                mergedChunks = addOnEnd(mergedChunks, get(i).getChunk());
            }
            return mergedChunks;
        }

        public BufferedImage convert() {
            return createImageFromBytes(mergeChunks());
        }

    }

    private class addChunkThread extends Thread {

        DatagramSocket socket;

        public addChunkThread(DatagramSocket socket) {

            this.socket = socket;
        }

        public void run() {
            byte buffer[] = null;
            DatagramPacket packet;
            while (true) {
                buffer = new byte[ImageProcessLib.maxUDPSize];
                packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.setReceiveBufferSize(ImageProcessLib.maxUDPSize);
                
                socket.setSendBufferSize(ImageProcessLib.maxUDPSize);
                socket.setReuseAddress(true);
                
                } catch (SocketException ex) {
                    Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
                transmitter.sendString("RDY");
                try {
                    System.out.println("Receiving");

                    socket.receive(packet);

                    System.out.println("Received");
                } catch (IOException ex) {
                    Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (packet.getLength() < ImageProcessLib.maxUDPSize) {
                    buffer = removePart(buffer, packet.getLength(), buffer.length);
                }
                chunkBuffer.addLast(new ImageChunk(buffer));
            }
        }

    }

}
