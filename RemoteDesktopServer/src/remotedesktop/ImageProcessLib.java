package remotedesktop;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageProcessLib {

    public static final int maxUDPSize = 65507;
    public static final byte[] jpgSOI = {(byte) 65496};

    public static BufferedImage resizeImage(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE)
                ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

    public static byte[] compressJPGImage(BufferedImage bufferedImage, float quality) throws IOException {

        Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter imageWriter = iterator.next();
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(quality);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        imageWriter.setOutput(ios);

        imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

        return baos.toByteArray();

    }

    public static void printBytes(byte[] data) {
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

    public static BufferedImage createImageFromBytes(byte[] imageData) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        return ImageIO.read(bais);
    }

    public static boolean byteEquals(byte[] b1, byte[] b2) {
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

    public static byte[] extractPart(byte[] bytes, int start, int end) {
        return Arrays.copyOfRange(bytes, start, end);
    }

    public static byte[] removePart(byte[] bytes, int start, int end) {
        byte[] firstPart = Arrays.copyOfRange(bytes, 0, start);
        byte[] secondPart = Arrays.copyOfRange(bytes, end, bytes.length);
        byte[] result = new byte[firstPart.length + secondPart.length];
        System.arraycopy(firstPart, 0, result, 0, firstPart.length);
        System.arraycopy(secondPart, 0, result, firstPart.length, secondPart.length);
        return result;
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

    public static byte[] addOnEnd(byte[] data, byte[] added) {
        byte[] result = new byte[added.length + data.length];
        System.arraycopy(data, 0, result, 0, data.length);
        System.arraycopy(added, 0, result, data.length, added.length);
        return result;
    }

    public static String bytesToString(byte[] data) {
        String hex = "";
        String st;
        for (byte b : data) {
            st = String.format("%02X", b);
            hex += st;
        }
        return hex;
    }

    public static byte[] addOnStart(byte[] data, byte[] added) {
        byte[] result = new byte[added.length + data.length];
        System.arraycopy(added, 0, result, 0, added.length);
        System.arraycopy(data, 0, result, added.length, data.length);
        return result;
    }
}
