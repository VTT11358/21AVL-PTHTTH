import java.util.Scanner;

public class EvenOdd {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(">> Kiem tra so chan le <<");
        System.out.print("Vui long nhap so can kiem tra: ");
        int number = scanner.nextInt();

        if (number % 2 == 0) {
            System.out.println("So " + number + " la so chan.");
        } else {
            System.out.println("So " + number + " la so le.");
        }

        scanner.close();
    }
}
