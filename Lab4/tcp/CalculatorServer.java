
package Lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalculatorServer {

    private static final int PORT = 5005;

    public static void main(String[] args) {

        try (ServerSocket server =
                     new ServerSocket(PORT)) {

            System.out.println(
                    "Calculator Server running on port "
                            + PORT
            );

            while (true) {
                try (Socket socket = server.accept()) {
                    handleClient(socket);
                } catch (IOException e) {
                    System.err.println(
                            "Client error: " + e.getMessage()
                    );
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "Server error: " + e.getMessage()
            );
        }
    }

    private static void handleClient(Socket socket)
            throws IOException {

        try (
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
            String request;

            while ((request = in.readLine()) != null) {

                String response = calculate(request);
                out.println(response);

                if (request.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }
        }
    }

    private static String calculate(String request) {

        String trimmed = request.trim();

        if (trimmed.equalsIgnoreCase("QUIT")) {
            return "OK BYE";
        }

        String[] parts = trimmed.split("\\s+");

        if (parts.length != 4
                || !parts[0].equalsIgnoreCase("CALC")) {
            return "ERR INVALID_FORMAT";
        }

        String operator = parts[1];
        double a;
        double b;

        try {
            a = Double.parseDouble(parts[2]);
            b = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            return "ERR INVALID_NUMBER";
        }

        // Không chấp nhận NaN hoặc Infinity.
        if (!Double.isFinite(a) || !Double.isFinite(b)) {
            return "ERR INVALID_NUMBER";
        }

        double result;

        switch (operator) {

            case "+":
                result = a + b;
                break;

            case "-":
                result = a - b;
                break;

            case "*":
                result = a * b;
                break;

            case "/":
                if (b == 0) {
                    return "ERR DIVIDE_BY_ZERO";
                }
                result = a / b;
                break;

            default:
                return "ERR UNSUPPORTED_OPERATOR";
        }

        if (!Double.isFinite(result)) {
            return "ERR RESULT_OUT_OF_RANGE";
        }

        return "OK " + result;
    }
}