import java.util.Collections;
import java.util.Vector;

public class GetMove {
    static int size=19;
    static int[]move=new int[2];
    static int maxdep=4,node=3;
    static int INF=1000000000;
    static public Vector<CheckerBoard> getMoveList(CheckerBoard board) {
        size = board.BOARD_SIZE;
        GetScore.setsize(size);
        Vector<CheckerBoard> vector = new Vector<CheckerBoard>();
        int[][] map = board.getMap();
        for (int i = 0; i < size; i++) {
            //System.out.println();
            for (int j = 0; j < size; j++) {
                //System.out.print(map[i][j]+" ");
                if (map[i][j] == 0) {
                    CheckerBoard newboard = new CheckerBoard(board);
                    newboard.setPrx(i);
                    newboard.setPry(j);
                    newboard.setScore(GetScore.getScorce(newboard.getMap(), i, j));
                    vector.add(newboard);
                }
            }
        }
        //System.out.println(vector.size());
        Collections.sort(vector, CheckerBoard.cmpfx);
        return vector;
    }
    static public int[] getMoveSimple(CheckerBoard board){
        Vector<CheckerBoard> vector=getMoveList(board);
        CheckerBoard b=vector.elementAt(0);
        int [] move =new int[2];
        move[0]=b.getPrx();
        move[1]=b.getPry();
        return move;
    }
    static public int dfs(CheckerBoard board,int a,int b,int dep){
        Vector<CheckerBoard>vector=getMoveList(board);
        if(dep==maxdep){
            CheckerBoard finalborder=vector.elementAt(0);
            return finalborder.getScore();
        }
        int ans=0;
        if(dep%2==0)ans=-INF;
        else ans=INF;
        for (int i=0;i<node;i++){
            CheckerBoard newboard=vector.get(i);
            int score=0;
            if(newboard.isWin(newboard.getPrx(),newboard.getPry())){
                if(dep%2==0)score=INF;
                else score=-INF;
            }
            else {
                newboard.move(newboard.getPrx(),newboard.getPry());
                score=dfs(newboard,a,b,dep+1);
            }
            if (dep==0&&ans<score){
                move[0]=newboard.getPrx();
                move[1]=newboard.getPry();
            }
            if (dep%2==0){
                ans=Math.max(ans,score);
                if (a<score)a=score;
                if (a>=b)return a;
            }
            else {
                ans = Math.min(ans, score);
                b = Math.min(b, score);
                if (a >= b) return b;
            }
        }
        return ans;
    }
    static public int[] getMoveBydfs(CheckerBoard board){
        dfs(board,-INF,INF,0);
        return move;
    }
}
