package Lab4.tcp;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(7000);
        System.out.println("Server dang chay tai port 7000...");

        while (true) {
            Socket socket = server.accept();
            System.out.println("Client da ket noi!");

            new Thread(() -> handleClient(socket)).start();
        }
    }

    static void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true)
        ) {
            out.println("=== CAC DICH VU TCP ===");
            out.println("1. Dao nguoc toan bo chuoi");
            out.println("2. Dao nguoc tung tu, giu vi tri tu");
            out.println("3. Dem tong so tu");
            out.println("4. Dem so tu tung dong");
            out.println("END_MENU");

            String choice = in.readLine();

            if (choice == null) return;

            List<String> lines = new ArrayList<>();
            String line;

            // Nhan cac dong den khi gap dau cham
            while ((line = in.readLine()) != null) {
                if (line.equals(".")) break;
                if (line.equals("EXIT.")) return;
                lines.add(line);
            }

            if (line == null) return;

            switch (choice) {
                case "1":
                    for (String s : lines) {
                        out.println(reverseAll(s));
                    }
                    break;

                case "2":
                    for (String s : lines) {
                        out.println(reverseEachWord(s));
                    }
                    break;

                case "3":
                    int total = 0;
                    for (String s : lines) {
                        total += countWords(s);
                    }
                    out.println("Tong so tu: " + total);
                    break;

                case "4":
                    for (int i = 0; i < lines.size(); i++) {
                        out.println("Dong " + (i + 1) + ": "
                            + countWords(lines.get(i)) + " tu");
                    }
                    break;

                default:
                    out.println("Dich vu khong hop le!");
            }

            out.println("END_RESULT");

            // Cho Client gui EXIT. roi moi dong ket noi
            String command = in.readLine();
            if ("EXIT.".equals(command)) {
                out.println("Da dong ket noi!");
            }

        } catch (IOException e) {
            System.out.println("Client da ngat ket noi.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // Bo qua loi dong socket
            }
        }
    }

    // Dich vu 1: dao nguoc ca chuoi, viet hoa dau moi tu
    static String reverseAll(String s) {
        String reversed = new StringBuilder(s).reverse().toString();
        return capitalizeWords(reversed);
    }

    // Dich vu 2: dao nguoc ky tu trong tung tu
    static String reverseEachWord(String s) {
        String[] words = s.trim().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (result.length() > 0) result.append(" ");
            result.append(
                new StringBuilder(word).reverse().toString()
            );
        }

        return capitalizeWords(result.toString());
    }

    static String capitalizeWords(String s) {
        StringBuilder result = new StringBuilder();
        boolean newWord = true;

        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                result.append(c);
                newWord = true;
            } else {
                result.append(newWord
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c));
                newWord = false;
            }
        }

        return result.toString();
    }

    static int countWords(String s) {
        s = s.trim();
        if (s.isEmpty()) return 0;
        return s.split("\\s+").length;
    }
}
