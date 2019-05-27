import java.util.Comparator;

public class CheckerBoard implements Comparable<CheckerBoard>{
    private int[][] map;//-1是无人 0是白，1是黑，黑先手
    private int count;
    private int BOARD_SIZE;// 棋盘格数
    private int prx;
    private int pry;
    private int sorce;

    public CheckerBoard(int BOARD_SIZE)
    {
        count=1;
        this.BOARD_SIZE = BOARD_SIZE;
        map=new int[BOARD_SIZE][BOARD_SIZE];
        for (int i=0;i<BOARD_SIZE;i++)
        {
            for(int j=0;j<BOARD_SIZE;j++)
            {
                map[i][j]=-1;
            }
        }
    }
    public CheckerBoard(CheckerBoard checkerBoard)
    {
        this.count=checkerBoard.count;
        this.BOARD_SIZE =checkerBoard. BOARD_SIZE;
        map=new int[this.BOARD_SIZE][this.BOARD_SIZE];
        for (int i=0;i<this.BOARD_SIZE;i++)
        {
            for(int j=0;j<this.BOARD_SIZE;j++)
            {
                map[i][j]=checkerBoard.getchessman(i,j);
            }
        }
    }
    public int getPrx()
    {
        return prx;
    }
    public int getPry()
    {
        return pry;
    }

    public void setPrx(int prx) {
        this.prx = prx;
    }

    public void setPry(int pry) {
        this.pry = pry;
    }

    public int getSorce() {
        return sorce;
    }

    public void setSorce(int sorce) {
        this.sorce = sorce;
    }

    int getCount()
    {
        return count;
    }
    public void reset()
    {
        count=1;
        for (int i=0;i<BOARD_SIZE;i++)
        {
            for(int j=0;j<BOARD_SIZE;j++)
            {
                map[i][j]=-1;
            }
        }
    }
    public int[][] getMap() {
        return map;
    }

    int getchessman(int x, int y)
    {
        return map[x][y];
    }
    int Continuum1(int x,int y,int color)//右斜
    {
        int xx=x,yy=y;
        int ans=0;
        while (map[x][y]==color)
        {
            ans++;
            x--;
            y--;
            if(x<0||y<0||x>=BOARD_SIZE||y>=BOARD_SIZE)break;
        }
        x=xx;
        y=yy;
        while (map[x][y]==color)
        {
            ans++;
            x++;
            y++;
            if(x<0||y<0||x>=BOARD_SIZE||y>=BOARD_SIZE)break;
        }
        ans--;
        return ans;
    }
    int Continuum2(int x,int y,int color)//右斜
    {
        int xx=x,yy=y;
        int ans=0;
        while (map[x][y]==color)
        {
            ans++;
            x++;
            y--;
            if(x<0||y<0||x>=BOARD_SIZE||y>=BOARD_SIZE)break;
        }
        x=xx;
        y=yy;
        while (map[x][y]==color)
        {
            ans++;
            x--;
            y++;
            if(x<0||y<0||x>=BOARD_SIZE||y>=BOARD_SIZE)break;
        }
        ans--;
        return ans;
    }
    int Continuum3(int x,int y,int color)//横向
    {
        int yy=y;
        int ans=0;
        while (map[x][y]==color)
        {
            ans++;
            y--;
            if(y<0||y>=BOARD_SIZE)break;
        }
        y=yy;
        while (map[x][y]==color)
        {
            ans++;
            y++;
            if(y<0||y>=BOARD_SIZE)break;
        }
        ans--;
        return ans;
    }
    int Continuum4(int x,int y,int color)//纵向
    {
        int xx=x;
        int ans=0;
        while (map[x][y]==color)
        {
            ans++;
            x--;
            if(x<0||x>=BOARD_SIZE)break;
        }
        x=xx;
        while (map[x][y]==color)
        {
            ans++;
            x++;
            if(x<0||x>=BOARD_SIZE)break;
        }
        ans--;
        return ans;
    }
    boolean isWin(int x,int y)
    {
        if(x>=BOARD_SIZE||y>=BOARD_SIZE||x<0||y<0)return false;
        int color=count%2;
        if(map[x][y]!=-1)
        {
            return false;
        }
        map[x][y]=color;
        if(Continuum1(x,y,color)>=5)
        {
            map[x][y]=-1;
            return true;
        }
        if(Continuum2(x,y,color)>=5)
        {
            map[x][y]=-1;
            return true;
        }
        if(Continuum3(x,y,color)>=5)
        {
            map[x][y]=-1;
            return true;
        }
        if(Continuum4(x,y,color)>=5)
        {
            map[x][y]=-1;
            return true;
        }
            map[x][y]=-1;
            return false;

    }
    boolean isTide()
    {
        for (int i=0;i<BOARD_SIZE;i++)
        {
            for(int j=0;j<BOARD_SIZE;j++)
            {
                if(map[i][j]==-1)return false;
            }
        }
        return true;
    }
    int move(int x,int y)
    {
        if(x>=BOARD_SIZE||y>=BOARD_SIZE||x<0||y<0)return -1;

        if(map[x][y]!=-1)
        {
            return -1;
        }
        if(isWin(x,y))
        {
            return count%2;
        }
        map[x][y]=count%2;
        MyFrame.brain.bd.putChess(x,y);
        count++;
        if(isTide())return 3;
        return -2;
    }
    void pf()
    {
        for (int i=0;i<BOARD_SIZE;i++)
        {
            for(int j=0;j<BOARD_SIZE;j++)
            {
                System.out.print(map[i][j]+" ");
            }
            System.out.println(" ");
        }
    }
    @Override
    public int compareTo(CheckerBoard o) {
        return Integer.compare(this.getSorce(),o.getSorce());
    }
    public static Comparator<CheckerBoard> cmpfx = new Comparator<CheckerBoard>() {
        public int compare(CheckerBoard i1, CheckerBoard i2) {
            return i2.getSorce()-i1.getSorce();
        }
    };
}
