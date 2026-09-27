
package Lab4.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpEchoServer {

    private static final int PORT = 5011;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(PORT)) {

            System.out.println(
                    "UDP Echo Server dang chay tai port " + PORT
            );

            while (true) {

                byte[] buffer = new byte[BUFFER_SIZE];

                DatagramPacket request =
                        new DatagramPacket(buffer, buffer.length);

                socket.receive(request);

                // Sao chép đúng phần dữ liệu đã nhận.
                byte[] responseData = new byte[request.getLength()];

                System.arraycopy(
                        request.getData(),
                        request.getOffset(),
                        responseData,
                        0,
                        request.getLength()
                );

                DatagramPacket response = new DatagramPacket(
                        responseData,
                        responseData.length,
                        request.getAddress(),
                        request.getPort()
                );

                socket.send(response);
            }

        } catch (SocketException e) {
            System.err.println(
                    "UDP Socket error: " + e.getMessage()
            );
        } catch (IOException e) {
            System.err.println(
                    "UDP Server error: " + e.getMessage()
            );
        }
    }
}