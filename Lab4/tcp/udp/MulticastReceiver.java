package Lab4.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class MulticastReceiver {

    private static final String GROUP_ADDRESS = "239.255.0.1";
    private static final int PORT = 5014;

    public static void main(String[] args) throws IOException {
        InetAddress group = InetAddress.getByName(GROUP_ADDRESS);

        if (!group.isMulticastAddress()) {
            System.err.println("Địa chỉ không phải multicast.");
            return;
        }

        NetworkInterface networkInterface;

        if (args.length >= 1) {
            networkInterface = NetworkInterface.getByName(args[0]);

            if (networkInterface == null) {
                System.err.println("Không tìm thấy network interface: " + args[0]);
                return;
            }
        } else {
            networkInterface = findInterface();

            if (networkInterface == null) {
                System.err.println("Không tìm thấy network interface phù hợp.");
                System.err.println("Hãy truyền tên interface khi chạy chương trình.");
                return;
            }
        }

        if (!networkInterface.isUp()
                || !networkInterface.supportsMulticast()) {
            System.err.println("Interface không hoạt động hoặc không hỗ trợ multicast.");
            return;
        }

        InetSocketAddress groupAddress =
                new InetSocketAddress(group, PORT);

        try (MulticastSocket socket = new MulticastSocket(null)) {
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(PORT));

            // Tham gia nhóm multicast trên interface được chỉ định.
            socket.joinGroup(groupAddress, networkInterface);

            System.out.println("Multicast Receiver đang chạy.");
            System.out.println("Group: " + GROUP_ADDRESS);
            System.out.println("Port: " + PORT);
            System.out.println("Interface: " + networkInterface.getName());
            System.out.println("Đang chờ thông báo...");

            byte[] buffer = new byte[2048];

            try {
                while (true) {
                    DatagramPacket packet =
                            new DatagramPacket(buffer, buffer.length);

                    socket.receive(packet);

                    String message = new String(
                            packet.getData(),
                            packet.getOffset(),
                            packet.getLength(),
                            StandardCharsets.UTF_8
                    );

                    System.out.println(
                            "Nhận từ " + packet.getAddress()
                                    + ": " + message
                    );
                }
            } finally {
                // Rời nhóm trước khi socket được đóng.
                socket.leaveGroup(groupAddress, networkInterface);
                System.out.println("Đã rời nhóm multicast.");
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
                && ni.supportsMulticast()
                && ni.getInetAddresses().hasMoreElements()) {

            Enumeration<InetAddress> addresses = ni.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();

                if (address instanceof java.net.Inet4Address) {
                    System.out.println(
                            "Chọn interface: " + ni.getName()
                                    + " - IPv4: " + address.getHostAddress()
                    );
                    return ni;
                }
            }
        }
    }

    return null;
}
}