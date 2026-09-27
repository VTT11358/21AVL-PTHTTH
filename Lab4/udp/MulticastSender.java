package Lab4.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class MulticastSender {

    private static final String GROUP_ADDRESS = "239.255.0.1";
    private static final int PORT = 5014;

    public static void main(String[] args) throws IOException {
        InetAddress group = InetAddress.getByName(GROUP_ADDRESS);

        NetworkInterface networkInterface;

        if (args.length >= 1) {
            networkInterface = NetworkInterface.getByName(args[0]);

            if (networkInterface == null) {
                System.err.println("Không tìm thấy interface: " + args[0]);
                return;
            }
        } else {
            networkInterface = findInterface();

            if (networkInterface == null) {
                System.err.println("Không tìm thấy interface phù hợp.");
                System.err.println("Hãy truyền tên interface khi chạy.");
                return;
            }
        }

        if (!networkInterface.isUp()
                || !networkInterface.supportsMulticast()) {
            System.err.println("Interface không hoạt động hoặc không hỗ trợ multicast.");
            return;
        }

        try (MulticastSocket socket = new MulticastSocket()) {
            socket.setNetworkInterface(networkInterface);
            socket.setTimeToLive(1);

            System.out.println("Multicast Sender đang chạy.");
            System.out.println("Group: " + GROUP_ADDRESS);
            System.out.println("Port: " + PORT);
            System.out.println("Interface: " + networkInterface.getName());
            System.out.println("Nhập nội dung cần gửi (QUIT để thoát).");

            BufferedReader console = new BufferedReader(
                    new InputStreamReader(System.in, StandardCharsets.UTF_8)
            );

            String message;

            while ((message = console.readLine()) != null) {
                if (message.equalsIgnoreCase("QUIT")) {
                    break;
                }

                byte[] data = message.getBytes(StandardCharsets.UTF_8);

                DatagramPacket packet = new DatagramPacket(
                        data,
                        data.length,
                        group,
                        PORT
                );

                socket.send(packet);
                System.out.println("Đã gửi: " + message);
            }
        }
    }

    private static NetworkInterface findInterface()
            throws SocketException {

        Enumeration<NetworkInterface> interfaces =
                NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();

            if (ni.isUp()
                    && !ni.isLoopback()
                    && ni.supportsMulticast()) {
                return ni;
            }
        }

        return null;
    }
}