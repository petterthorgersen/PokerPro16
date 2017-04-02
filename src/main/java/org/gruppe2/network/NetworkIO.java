package org.gruppe2.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Arrays;


/**
 * Reader and writer for net protocol
 */
public class NetworkIO {
    public boolean isPing() {
        return ping;
    }

    public void setPing(boolean ping) throws IOException {
        this.ping = ping;

        if (ping)
            sendPing();
    }

    public enum Format {STRING, OBJECT}

    private static final Charset charset = Charset.forName("UTF-8");
    private static final long timeoutDelay = 60000; // 1 minute
    private static final long pingDelay = 10000; // 10 seconds

    private static class Ping implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3120573551247595944L;
    }

    private static class Pong implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1140716799816113057L;
    }

    private static final String pingStr = "PING";
    private static final String pongStr = "PONG";

    private static final Ping pingObj = new Ping();
    private static final Pong pongObj = new Pong();

    private final SocketChannel channel;

    private final ByteArrayOutputStream inputBuffer = new ByteArrayOutputStream();
    private final ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();

    private final ByteBuffer readByteBuffer = ByteBuffer.allocate(1024);
    private final ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024);

    private Format inputFormat = Format.STRING;
    private Format outputFormat = Format.STRING;

    private long timeout = -1;
    private boolean ping = false;
    private boolean sendPingState = true;

    public NetworkIO(SocketChannel channel) throws IOException {
        this.channel = channel;
        this.readByteBuffer.flip();
    }

    public void sendMessage(String message) throws IOException {
        if (outputFormat != Format.STRING)
            throw new RuntimeException("Wrong output format");

        outputBuffer.write(message.getBytes());

//        System.out.printf("Sent Message: [%s]\n", message.replace("\r\n", ""));
    }

    /**
     * Attempts to read a string that ends with "\r\n" and returns a split array of arguments
     *
     * @return Array of arguments or null if unsuccessful
     * @throws IOException
     */
    public String[] readMessage() throws IOException {
        if (inputFormat != Format.STRING)
            return null;

        fillBuffer();

        if (inputBuffer.size() <= 0)
            return null;

        byte[] bytes = inputBuffer.toByteArray();

        int crlf = -1;

        for (int i = 0; i < bytes.length - 1; i++) {
            if (bytes[i] == (byte) '\r' && bytes[i + 1] == (byte) '\n') {
                crlf = i;
                break;
            }
        }

        if (crlf < 0)
            return null;

        String message = new String(bytes, 0, crlf, charset);
        inputBuffer.reset();
        inputBuffer.write(bytes, crlf + 2, bytes.length - (crlf + 2));

        

        if (message.equals(pingStr)) {
            sendPong();
        } else if (message.equals(pongStr)) {
            timeout = System.currentTimeMillis() + pingDelay;
            sendPingState = true;
        }

        return splitMessage(message);
    }

    public void sendObject(Object object) throws IOException {
        if (outputFormat != Format.OBJECT)
            throw new RuntimeException("Wrong output format");

       

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(object);

        byte[] bytes = byteStream.toByteArray();

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.clear();
        lengthBuffer.putInt(bytes.length);
        lengthBuffer.flip();

        outputBuffer.write(lengthBuffer.array());
        outputBuffer.write(bytes);

//        System.out.printf("Sent Object: [%s] (size: %d)\n", object.getClass(), bytes.length + 4);
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        if (inputFormat != Format.OBJECT)
            throw new RuntimeException("Wrong input format");

        fillBuffer();

        if (inputBuffer.size() <= 4)
            return null;

        byte[] bytes = inputBuffer.toByteArray();

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.clear();
        lengthBuffer.put(bytes, 0, 4);
        lengthBuffer.flip();

        int length = lengthBuffer.getInt();

        if (length + 4 > bytes.length) // Not enough data
            return null;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes, 4, length);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        inputBuffer.reset();
        inputBuffer.write(bytes, length + 4, bytes.length - (length + 4));

        Object object = objectInputStream.readObject();

//        System.out.printf("Received Object: [%s]\n", object.getClass());

        if (object instanceof Ping) {
            sendPong();
        } else if (object instanceof Pong) {
            timeout = System.currentTimeMillis() + pingDelay;
            sendPingState = true;
        }

        return object;
    }

    private void sendPing() throws IOException {
        if (outputFormat == Format.STRING) {
            sendMessage(pingStr + "\r\n");
        } else {
            sendObject(pingObj);
        }

        timeout = System.currentTimeMillis() + timeoutDelay;
        sendPingState = false;
    }

    private void sendPong() throws IOException {
        if (outputFormat == Format.STRING) {
            sendMessage(pongStr + "\r\n");
        } else {
            sendObject(pongObj);
        }

        timeout = System.currentTimeMillis() + timeoutDelay;
    }

    /**
     * Attempts to write as much as possible to the SocketChannel
     * object from the internal write buffer.
     *
     * @return number of bytes remaining
     * @throws IOException
     */
    public int flush() throws IOException {
        if (timeout < 0) {
            if (ping) {
                sendPing();
            }

            timeout = System.currentTimeMillis() + timeoutDelay;
        } else if (timeout < System.currentTimeMillis()) {
            if (ping && sendPingState) {
                sendPing();
            } else {
                throw new EOFException();
            }
        }

        while (outputBuffer.size() > 0) {

            byte[] bytes = outputBuffer.toByteArray();

            writeByteBuffer.clear();
            writeByteBuffer.flip();

            writeByteBuffer.clear();
            writeByteBuffer.put(bytes, 0, Math.min(bytes.length, 1024));
            writeByteBuffer.flip();

            int written = channel.write(writeByteBuffer);

            if (written > 0) {
                outputBuffer.reset();
                outputBuffer.write(bytes, written, bytes.length - written);
            } else {
                break;
            }
        }

        return outputBuffer.size();
    }

    public Format getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(Format inputFormat) {
        this.inputFormat = inputFormat;
    }

    public Format getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(Format outputFormat) {
        this.outputFormat = outputFormat;
    }

    private int fillBuffer() throws IOException {
        flush();

        int read;
        readByteBuffer.clear();

        if ((read = channel.read(readByteBuffer)) <= 0)
            return read;

        inputBuffer.write(readByteBuffer.array(), 0, readByteBuffer.position());

        return read;
    }

    private String[] splitMessage(String msg) {
        int indexOfColon = msg.indexOf(":");

        if (indexOfColon < 0) {
            return msg.split(";");
        }

        String[] args = msg.substring(0, indexOfColon).split(";");
        String rest = msg.substring(indexOfColon + 1);

        String[] output = Arrays.copyOf(args, args.length + 1);

        output[args.length] = rest;

        return output;
    }
}