package Lab4.tcp;


import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("127.0.0.1", 7000);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in)
        ) {
            // Nhan danh sach dich vu
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals("END_MENU")) break;
                System.out.println(line);
            }

            System.out.print("Chon dich vu (1-4): ");
            String choice = sc.nextLine();
            out.println(choice);

            System.out.println("Nhap cac dong chuoi.");
            System.out.println("Nhap . de ket thuc nhap:");

            while (true) {
                String data = sc.nextLine();
                out.println(data);

                if (data.equals(".")) break;
            }

            // Nhan ket qua tu Server
            System.out.println("=== KET QUA ===");
            while ((line = in.readLine()) != null) {
                if (line.equals("END_RESULT")) break;
                System.out.println(line);
            }

            // Gui lenh dong ket noi
            out.println("EXIT.");

            line = in.readLine();
            if (line != null) System.out.println(line);

        } catch (IOException e) {
            System.out.println("Khong ket noi duoc Server!");
            System.out.println(e.getMessage());
        }
    }
}

