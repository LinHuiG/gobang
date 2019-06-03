import java.util.regex.Pattern;

public class GetScore {
    static int size;
    static int INF=1000000000;
    protected int scorce,attack,denfence;
    public static enum Level {
        CON_5("长连", 0, new String[] { "11111", "22222" }, 100000),
        ALIVE_4("活四", 1, new String[] { "011110|1011101", "022220|2022202" }, 10000),
        GO_4("冲四", 2, new String[] { "011112|10111|11011", "022221|20222|22022" }, 500),
        ALIVE_3("活三", 3, new String[] { "01110|010110", "02220|020220" }, 200),
        SLEEP_3("眠三", 4, new String[] {"001112|010112|011012|10011|10101|2011102", "002221|020221|022021|20022|20202|1022201" }, 50),
        ALIVE_2("活二", 5, new String[] { "00110|01010|010010", "00220|02020|020020" }, 5),
        SLEEP_2("眠二", 6, new String[] {"000112|001012|010012|10001|2010102|2011002", "000221|002021|020021|20002|1020201|1022001" }, 3),
        DEAD_3("死三", 7, new String[] { "21112", "12221" }, -5),
        DEAD_4("死四", 8, new String[] { "211112", "122221" }, -5),
        DEAD_2("死二", 9, new String[] { "2112", "1221" }, -5),
        NULL("null", 10, new String[] { "", "" }, 0);
        private String name;
        private int index;
        private String[] regex;
        int score;

        // 构造方法
        private Level(String name, int index, String[] regex, int score) {
            this.name = name;
            this.index = index;
            this.regex = regex;
            this.score = score;
        }
        @Override
        public String toString() {
            return this.name;
        }
    };
    static int []dx={0,0,-1,1,1,-1,-1,1};
    static int []dy={1,-1,0,0,-1,1,-1,1};
    static public String getSeq(int map[][],int x,int y,int p,int dx,int dy){
        String seq="";
        map[x][y]=p;
        int ni=0;
        while (x-dx>=0&&x-dx<size&&y-dy>=0&&y-dy<size){
            x-=dx;
            y-=dy;
            ni++;
            if(ni>=4)break;
        }
        int shun=0;
        while(x>=0&&x<size&&y>=0&&y<size){
            seq=seq+map[x][y];
            x+=dx;
            y+=dy;
            shun++;
            if(shun-ni>=5)break;
        }
        return seq;
    }
    static void setsize(int s){
        size=s;
    }
    static int getPlayerScorce(int map[][],int x,int y,int p){
        int state[]=new int[20];
        int ans=0;
        for(int i=0;i<size;i++)state[i]=0;
        for (int i=0;i<4;i++){
            String seq=getSeq(map,x,y,p,dx[i*2],dy[i*2]);
            //System.out.println(x+" "+y+" "+seq);
            String seq1=getSeq(map,x,y,p,dx[i*2+1],dy[i*2+1]);
            for (Level level:Level.values()){
                String pl=p+"";
                if(Pattern.matches(".*("+level.regex[Integer.parseInt(pl)-1]+").*",seq)
                ||Pattern.matches(".*("+level.regex[Integer.parseInt(pl)-1]+").*",seq1)){
                    state[level.index]++;
                    ans=Math.max(ans,level.score);
                    //if(x==4&&y==7)System.out.println(i+" "+level.name+"  "+p);
                    break;
                }

            }
        }
        int score=0;
        if (state[Level.GO_4.index] >= 2
                || (state[Level.GO_4.index] >= 1
                && state[Level.ALIVE_3.index] >= 1))// 双冲4，冲4活三
            score = 10000;
        else if (state[Level.ALIVE_3.index] >= 2)// 双活3
            score = 5000;
        else if (state[Level.SLEEP_3.index] >= 1
                && state[Level.ALIVE_3.index] >= 1)// 活3眠3
            score = 1000;
        else if (state[Level.ALIVE_2.index] >= 2)// 双活2
            score = 100;
        else if (state[Level.SLEEP_2.index] >= 1
                && state[Level.ALIVE_2.index] >= 1)// 活2眠2
            score = 10;
        ans=Math.max(ans,score);
        return ans;
    }
    static int getScorce(int map[][],int x,int y){
        int d=Math.max(Math.abs(x-size/2),Math.abs(y-size/2));
        int ans=0;
        int attack=getPlayerScorce(map,x,y,1);
        int defence=getPlayerScorce(map,x,y,2);
        //if(x==4&&y==7)System.out.println(x+"  "+y+"  "+attack+" "+defence+"  "+d);
        ans=defence+attack+19-d;
        return ans;
    }
    static int getfinalScore(CheckerBoard board){
        int attack=0,defence=0,se=0;
        int [][]map=board.getMap();
        for (int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(map[i][j]==0){
                    attack=Math.max(attack,getPlayerScorce(board.getMap(),i,j,1));
                    int t=getPlayerScorce(board.getMap(),i,j,2);
                    if(t>=defence) {
                        se = defence;
                        defence = t;
                    }
                    else if(t>=se)se=t;
                }
            }
        }
        return 2*attack-se;
    }
}
