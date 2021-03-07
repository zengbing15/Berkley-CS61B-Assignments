package HW0;

public class DrawTriangle {
    public static void main(String[] args) {
        int size = 5;
        int row =0;
        while (row < size ) {
            int col = 0;
            while (col <= row){
                System.out.print("*");
                col = col + 1;
            }
            System.out.println();
            row = row + 1;
        }
    }
}
