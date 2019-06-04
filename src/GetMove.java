import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;

class Info implements Comparable<Info>{
    public int x,y,score,at,de;
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int compareTo(Info B) {
        return Integer.compare(B.score,score);
    }
}
public class GetMove {
    static int size=19;
    static int[]move=new int[2];
    static int maxdep=4,node=5;
    static int maxat,maxde;
    static int INF=1000000000;

    static public Vector<Info> getMoveList(CheckerBoard board) {
        size = board.BOARD_SIZE;
        maxde=0;maxat=0;
        GetScore.setsize(size);
        int minx=board.minx,miny=board.miny;
        int maxx=board.maxx,maxy=board.maxy;
        //System.out.println(minx+" "+maxx+" "+miny+" "+maxy);
        minx=Math.max(0,minx-2);miny=Math.max(0,miny-2);
        maxx=Math.min(maxx+2,size-1);maxy=Math.min(maxy+2,size-1);
        Vector<Info> vector = new Vector<>();
        //System.out.println(minx+" "+maxx+" "+miny+" "+maxy);
        int[][] map = board.getMap();
        for (int i = minx; i <maxx; i++) {
            //System.out.println();
            for (int j = miny; j < maxy; j++) {
                //System.out.print(map[i][j]+" ");
                if (map[i][j] == 0) {
                    /*CheckerBoard newboard = new CheckerBoard(board);
                    newboard.setPrx(i);
                    newboard.setPry(j);*/
                    Info info=GetScore.getScorce(board.getMap(), i, j);
                    maxat=Math.max(maxat,info.at);
                    maxde=Math.max(maxde,info.de);
                    vector.add(info);
                }
            }
        }
        //System.out.println(vector.size());
        Collections.sort(vector);
        return vector;
    }
    static public int dfs(CheckerBoard board,int a,int b,int dep){
        //Vector<Info>vector=getMoveList(new CheckerBoard(board));
        if(dep==maxdep){
            /*Info finalmove=vector.elementAt(0);
            if (maxdep==0){
                move[0]=finalmove.getX();
                move[1]=finalmove.getY();
            }*/
            return GetScore.getfinalScore(board);
            //return finalborder.getScore();
        }
        Vector<Info>vector=getMoveList(new CheckerBoard(board));
        int ans=0;
        if(dep%2==0)ans=-INF;
        else ans=INF;
        if (dep%2==0&&maxat>=10000&&maxde<100000)return INF;
        else if(dep%2==1&&maxde>=10000&&maxat<100000)return -INF;
        Boolean update=false;
        for (int i=0;i<Math.min(node,vector.size());i++){
            Info newmove=vector.get(i);
            if(dep<=1){
                System.out.println("try "+newmove.getX()+"    "+newmove.getY()+"  "+newmove.getScore());
            }
            int score=0;
            CheckerBoard newboard=new CheckerBoard(board);
            if(newboard.isWin(newmove.getX(),newmove.getY())){
                if(dep%2==0)score=INF;
                else score=-INF;
            }
            else {
                newboard.move(newmove.getX(),newmove.getY());
                score=dfs(newboard,a,b,dep+1);
            }
            if (dep==0)System.out.println(score);
            if (dep==0&&(ans<score||update==false)){
                move[0]=newmove.getX();
                move[1]=newmove.getY();
                update=true;
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
        System.out.println(move[0]+" "+move[1]);
        return move;
    }
}
