import java.util.Collections;
import java.util.Vector;

public class GetMove {
    static int size=19;
    static public int[] getMoveSimple(CheckerBoard board){
        size=board.BOARD_SIZE;
        GetScore.setsize(size);
        Vector<CheckerBoard> vector = new Vector<CheckerBoard>();
        int [][] map=board.getMap();
        for (int i=0;i<size;i++){
            //System.out.println();
            for (int j=0;j<size;j++){
                //System.out.print(map[i][j]+" ");
                if(map[i][j]==0){
                    CheckerBoard newboard=new CheckerBoard(board);
                    newboard.setPrx(i);
                    newboard.setPry(j);
                    newboard.setSorce(GetScore.getScorce(newboard.getMap(),i,j));
                    vector.add(newboard);
                }
            }
        }
        System.out.println(vector.size());
        Collections.sort(vector,CheckerBoard.cmpfx);
        CheckerBoard b=vector.elementAt(0);
        int [] move =new int[2];
        move[0]=b.getPrx();
        move[1]=b.getPry();
        return move;
    }
}
