import java.io.IOException;

public class HelloJava {
    static String tenlop;
    public static void main(String [] args ) throws IOException {
        tenlop ="cntt";
        System.out.println("Hello Java" + tenlop);
        // int ch = System.in.read();
        // System.out.println("Ky tu vua nhap la " + ch + " Co ma ASCII la " + (char)ch);

        System.out.println("Nhập ký tự ");
        int ch = System.in.read();
       
    while (ch == 'Q'){
        return;
    }if (ch == 'K') {
         System.out.println("Bạn đã trúng thưởng ");

        }else{
            System.out.println("May mắn lần sau ");
        }
        
    }
}