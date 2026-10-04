import java.util.Scanner;

public class SumAB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Vui long nhap so hang thu nhat: ");
        int a = scanner.nextInt();

        System.out.print("Vui long nhap so hang thu hai: ");
        int b = scanner.nextInt();

        int result = a + b;

        System.out.println("Tinh tong [" + a + " + " + b + " = " + result + "]");

        scanner.close();
    }
}
