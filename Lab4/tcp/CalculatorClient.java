
package Lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalculatorClient {

    public static void main(String[] args) {

        String host =
                args.length > 0 ? args[0] : "localhost";

        int port =
                args.length > 1
                        ? Integer.parseInt(args[1])
                        : 5005;

        try (
            Socket socket = new Socket(host, port);

            BufferedReader console = new BufferedReader(
                new InputStreamReader(
                    System.in,
                    StandardCharsets.UTF_8
                )
            );

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
            System.out.println("Connected to calculator.");
            System.out.println(
                    "Nhap: CALC + 100 200"
            );
            System.out.println(
                    "Toan tu: + - * / | Thoat: QUIT"
            );

            String request;

            while ((request = console.readLine()) != null) {

                out.println(request);

                String response = in.readLine();

                if (response == null) {
                    System.out.println("Server disconnected.");
                    break;
                }

                System.out.println("Server: " + response);

                if (request.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "Connection error: " + e.getMessage()
            );
        } catch (NumberFormatException e) {
            System.err.println("Port khong hop le.");
        }
    }
}