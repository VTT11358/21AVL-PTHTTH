
package Lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BenchmarkClient {

    private static final String HOST = "localhost";

    private static final int TCP_PORT = 5010;
    private static final int UDP_PORT = 5011;

    private static final int MESSAGE_COUNT = 1000;
    private static final int REPEAT = 5;
    private static final int MESSAGE_SIZE = 64;

    // Timeout cho mỗi lần chờ phản hồi UDP.
    private static final int UDP_TIMEOUT_MS = 200;

    // Timeout đọc TCP để tránh chờ vô hạn.
    private static final int TCP_TIMEOUT_MS = 2000;

    private static class Result {
        long elapsedMs;
        int received;
        int correct;

        Result(long elapsedMs, int received, int correct) {
            this.elapsedMs = elapsedMs;
            this.received = received;
            this.correct = correct;
        }
    }

    public static void main(String[] args) throws IOException {

        InetAddress address = InetAddress.getByName(HOST);

        System.out.println("===== TCP vs UDP BENCHMARK =====");
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("Java: " + System.getProperty("java.version"));
        System.out.println("Host: " + HOST);
        System.out.println("Messages per run: " + MESSAGE_COUNT);
        System.out.println("Payload size: " + MESSAGE_SIZE + " bytes");
        System.out.println("Number of runs: " + REPEAT);
        System.out.println("UDP timeout: " + UDP_TIMEOUT_MS + " ms");
        System.out.println();

        long tcpTotal = 0;
        long udpTotal = 0;

        int tcpReceivedTotal = 0;
        int udpReceivedTotal = 0;

        int tcpCorrectTotal = 0;
        int udpCorrectTotal = 0;

        System.out.printf(
                "%-8s | %-12s | %-12s | %-12s | %-12s%n",
                "Run", "TCP (ms)", "UDP (ms)",
                "TCP replies", "UDP replies"
        );

        System.out.println(
                "----------------------------------------------------------------"
        );

        for (int run = 1; run <= REPEAT; run++) {

            Result tcp = testTcp(address);
            Result udp = testUdp(address);

            tcpTotal += tcp.elapsedMs;
            udpTotal += udp.elapsedMs;

            tcpReceivedTotal += tcp.received;
            udpReceivedTotal += udp.received;

            tcpCorrectTotal += tcp.correct;
            udpCorrectTotal += udp.correct;

            System.out.printf(
                    "%-8d | %-12d | %-12d | %-12d | %-12d%n",
                    run,
                    tcp.elapsedMs,
                    udp.elapsedMs,
                    tcp.received,
                    udp.received
            );
        }

        System.out.println();
        System.out.println("===== TONG KET =====");

        System.out.printf(
                "TCP average: %.2f ms%n",
                tcpTotal / (double) REPEAT
        );

        System.out.printf(
                "UDP average: %.2f ms%n",
                udpTotal / (double) REPEAT
        );

        System.out.printf(
                "TCP replies: %d/%d%n",
                tcpReceivedTotal,
                MESSAGE_COUNT * REPEAT
        );

        System.out.printf(
                "UDP replies: %d/%d%n",
                udpReceivedTotal,
                MESSAGE_COUNT * REPEAT
        );

        System.out.printf(
                "TCP correct replies: %d/%d%n",
                tcpCorrectTotal,
                MESSAGE_COUNT * REPEAT
        );

        System.out.printf(
                "UDP correct replies: %d/%d%n",
                udpCorrectTotal,
                MESSAGE_COUNT * REPEAT
        );

        System.out.println();
        System.out.println(
                "Thoi gian duoc do bang System.nanoTime()."
        );
        System.out.println(
                "Moi lan do gom 1000 request-response tuan tu."
        );
    }

    private static Result testTcp(InetAddress address)
            throws IOException {

        int received = 0;
        int correct = 0;

        long start = System.nanoTime();

        try (
            Socket socket = new Socket(address, TCP_PORT);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                    socket.getOutputStream(),
                    StandardCharsets.UTF_8
                ),
                true
            )
        ) {
            socket.setSoTimeout(TCP_TIMEOUT_MS);

            for (int i = 0; i < MESSAGE_COUNT; i++) {

                String message = createMessage(i);

                out.println(message);

                String response = in.readLine();

                if (response == null) {
                    break;
                }

                received++;

                if (message.equals(response)) {
                    correct++;
                }
            }
        }

        long elapsed = System.nanoTime() - start;

        return new Result(
                elapsed / 1_000_000,
                received,
                correct
        );
    }

    private static Result testUdp(InetAddress address)
            throws IOException {

        int received = 0;
        int correct = 0;

        long start = System.nanoTime();

        try (DatagramSocket socket = new DatagramSocket()) {

            socket.setSoTimeout(UDP_TIMEOUT_MS);

            for (int i = 0; i < MESSAGE_COUNT; i++) {

                byte[] data = createMessage(i)
                        .getBytes(StandardCharsets.US_ASCII);

                DatagramPacket request = new DatagramPacket(
                        data,
                        data.length,
                        address,
                        UDP_PORT
                );

                socket.send(request);

                byte[] buffer = new byte[1024];

                DatagramPacket response =
                        new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(response);
                    received++;

                    byte[] receivedData = Arrays.copyOfRange(
                            response.getData(),
                            response.getOffset(),
                            response.getOffset()
                                    + response.getLength()
                    );

                    if (Arrays.equals(data, receivedData)) {
                        correct++;
                    }

                } catch (SocketTimeoutException e) {
                    // Không nhận phản hồi trong thời gian quy định.
                    // Tiếp tục thông điệp tiếp theo.
                }
            }
        }

        long elapsed = System.nanoTime() - start;

        return new Result(
                elapsed / 1_000_000,
                received,
                correct
        );
    }

    // Tạo thông điệp ASCII đúng 64 byte.
    private static String createMessage(int sequence) {

        String prefix = String.format("SEQ-%04d-", sequence);

        StringBuilder message = new StringBuilder(prefix);

        while (message.length() < MESSAGE_SIZE) {
            message.append('X');
        }

        return message.toString();
    }
}