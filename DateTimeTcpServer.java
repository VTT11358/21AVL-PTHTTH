package Lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTcpServer {

    private static final int PORT = 5003;

    private static final DateTimeFormatter
            DATE_FORMAT =
            DateTimeFormatter.ofPattern(
                    "dd MM yyyy"
            );

    private static final DateTimeFormatter
            TIME_FORMAT =
            DateTimeFormatter.ofPattern(
                    "HH mm ss"
            );

    private static final DateTimeFormatter
            DATETIME_FORMAT =
            DateTimeFormatter.ofPattern(
                    "dd MM yyyy HH mm ss"
            );

    public static void main(String[] args) {

        try (ServerSocket server =
                     new ServerSocket(PORT)) {

            System.out.println(
                    "DateTime TCP Server port "
                            + PORT
            );

            while (true) {

                try (Socket socket =
                             server.accept()) {

                    handleClient(socket);

                } catch (IOException e) {

                    System.err.println(
                            "Lỗi client: "
                                    + e.getMessage()
                    );
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Không mở được server: "
                            + e.getMessage()
            );
        }
    }

    private static void handleClient(
            Socket socket) throws IOException {

        try (
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

            String request;

            while ((request = in.readLine()) != null) {

                String command =
                        request.trim().toUpperCase();

                LocalDateTime now =
                        LocalDateTime.now();

                switch (command) {

                    case "DATE":
                        out.println(
                                "OK "
                                        + now.format(
                                        DATE_FORMAT
                                )
                        );
                        break;

                    case "TIME":
                        out.println(
                                "OK "
                                        + now.format(
                                        TIME_FORMAT
                                )
                        );
                        break;

                    case "DATETIME":
                        out.println(
                                "OK "
                                        + now.format(
                                        DATETIME_FORMAT
                                )
                        );
                        break;

                    case "QUIT":
                        out.println("OK BYE");
                        return;

                    default:
                        out.println(
                                "ERR UNKNOWN_COMMAND"
                        );
                }
            }
        }
    }
}