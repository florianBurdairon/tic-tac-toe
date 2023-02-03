package tictactoe;

import tictactoe.grid.Grid2D;
import tictactoe.grid.Grid3D;

public class Main {
    public  static void main(String[] args){
        new Grid2D(3).display();
        System.out.println();
        new Grid2D(4).display();


        new Grid3D(3).display();
        System.out.println();
        new Grid3D(4).display();
        System.out.println();
        new Grid3D(10).display();

    }
}
