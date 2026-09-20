package Lab4.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitClient {

    public static void main(String[] args) {

        String host =
                args.length > 0
                        ? args[0]
                        : "localhost";

        int port =
                args.length > 1
                        ? Integer.parseInt(args[1])
                        : 5002;

        try (
                Socket socket =
                        new Socket(host, port);

                BufferedReader console =
                        new BufferedReader(
                                new InputStreamReader(
                                        System.in,
                                        StandardCharsets.UTF_8
                                )
                        );

                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream(),
                                        StandardCharsets.UTF_8
                                )
                        );

                PrintWriter out =
                        new PrintWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream(),
                                        StandardCharsets.UTF_8
                                ),
                                true
                        )
        ) {

            System.out.println(
                    "Connected to Digit Server."
            );

            System.out.println(
                    "Nhập 0-9 hoặc QUIT:"
            );

            String input;

            while ((input =
                    console.readLine()) != null) {

                out.println(input);

                String response =
                        in.readLine();

                System.out.println(
                        "Server: " + response
                );

                if (input.equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Lỗi: " + e.getMessage()
            );
        }
    }
}