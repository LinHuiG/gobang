
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        CheckerBoard checkerboard=new CheckerBoard();
        Scanner cin=new Scanner(System.in);
        System.out.println("1:人机对战\n2:玩家对战");
        int xz=cin.nextInt();
        new MyFrame(checkerboard,xz);

    }
}
