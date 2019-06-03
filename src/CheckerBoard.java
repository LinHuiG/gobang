import java.io.*;
import java.util.Comparator;
import java.util.Stack;

class chess implements Serializable{
    int x, y,minx,miny,maxx,maxy;
    int color;

    public chess(int x, int y, int color,int maxx,int maxy,int minx,int miny) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.maxx=maxx;
        this.maxy=maxy;
        this.minx=minx;
        this.miny=miny;
    }
}
public class CheckerBoard  implements Serializable , Comparable<CheckerBoard>,Cloneable{
    private int[][] map;//-1是无人 0是白，1是黑，黑先手
    private int count;
    public int BOARD_SIZE;// 棋盘格数
    private int prx=-1;
    private int pry=-1;
    private int score;
    public int isAi=0;
    public int minx =19, miny =19, maxx =0, maxy =0;
    private Stack<chess> historys = new Stack<chess>();

    String path="wzq.mo";


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

    public CheckerBoard(String test)
    {
        File aFile=new File(path);
        FileInputStream fileInputStream= null;
        try {
            fileInputStream = new FileInputStream(aFile);
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
            CheckerBoard checkerBoard=(CheckerBoard) objectInputStream.readObject();
            this.count=checkerBoard.count;
            this.BOARD_SIZE =checkerBoard. BOARD_SIZE;
            this.map=new int[this.BOARD_SIZE][this.BOARD_SIZE];
            this.prx=checkerBoard.prx;
            this.pry=checkerBoard.pry;
            this.score=checkerBoard.score;
            this.isAi=checkerBoard.isAi;
            this.historys = checkerBoard.historys;
            for (int i=0;i<this.BOARD_SIZE;i++)
            {
                for(int j=0;j<this.BOARD_SIZE;j++)
                {
                    this.map[i][j]=checkerBoard.getchessman(i,j);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            count=1;
            this.BOARD_SIZE = 19;
            prx=pry=-1;
            map=new int[BOARD_SIZE][BOARD_SIZE];
            for (int i=0;i<BOARD_SIZE;i++)
            {
                for(int j=0;j<BOARD_SIZE;j++)
                {
                    map[i][j]=-1;
                }
            }
        }

    }
    public void save()
    {
        FileOutputStream fileOutputStream=null;
        try {
            File initFile=new File(path);
            fileOutputStream = new FileOutputStream(initFile);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            objectOutputStream.close();
            System.out.println("保存成功");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("保存失败，FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("保存失败，IOException");
        }finally {
            if(fileOutputStream!=null)
            {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public CheckerBoard(CheckerBoard checkerBoard)
    {
        this.count=checkerBoard.count;
        this.BOARD_SIZE =checkerBoard. BOARD_SIZE;
        this.map=new int[this.BOARD_SIZE][this.BOARD_SIZE];
        for (int i=0;i<this.BOARD_SIZE;i++)
        {
            for(int j=0;j<this.BOARD_SIZE;j++)
            {
                this.map[i][j]=checkerBoard.getchessman(i,j);
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

    public int getScore() {
        return score;
    }

    public void setScore(int sorce) {
        this.score = sorce;
    }

    int getCount()
    {
        return count;
    }
    public void reset()
    {
        minx =19;
        miny =19;
        maxx =0;
        maxy =0;
        count=1;
        for (int i=0;i<BOARD_SIZE;i++)
        {
            for(int j=0;j<BOARD_SIZE;j++)
            {
                map[i][j]=-1;
            }
        }
        prx = -1;
        pry = -1;
        isAi = 0;
        historys = new Stack<chess>();
    }
    public int[][] getMap() {
        int[][] ans=new int[BOARD_SIZE][BOARD_SIZE];
        for (int i=0;i<this.BOARD_SIZE;i++)
        {
            for(int j=0;j<this.BOARD_SIZE;j++)
            {
                ans[i][j]=map[i][j];
                if(ans[i][j]==-1)ans[i][j]=0;
                else if(ans[i][j]==0)ans[i][j]=2;
            }
        }
        return ans;
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

        int color=count%2;
        return isWin(x,y,color);
    }
    boolean isWin(int x,int y,int color)
    {
        if(x>=BOARD_SIZE||y>=BOARD_SIZE||x<0||y<0)return false;
        if(map[x][y]!=-1)
        {
            //return false;
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
        prx=x;
        pry=y;
        minx = minx >x?x: minx;
        maxx = maxx <x?x: maxx;
        miny = miny >y?y: miny;
        maxy = maxy <y?y: maxy;
        historys.push(new chess(x, y, count % 2,maxx,maxy,minx,miny));
        if(isWin(x,y))
        {
            map[x][y]=count%2;
            count++;
            return (count-1)%2;
        }
        map[x][y]=count%2;
        count++;
        if(isTide())return 3;
        return -2;
    }
    void withdraw()
    {
        if(count==1)return;
        if(isAi==1&&count==2)return;
        count--;
        chess temp=historys.pop();
        map[temp.x][temp.y]=-1;
        if(!historys.empty()&&isAi==1)
        {
            count--;
            temp=historys.pop();
            map[temp.x][temp.y]=-1;
        }
        if(historys.empty())
        {
            prx=pry=-1;
            minx =19;
            miny =19;
            maxx =0;
            maxy =0;
        }
        else
        {
            temp=historys.peek();
            prx=temp.x;
            pry=temp.y;
            minx=temp.minx;
            miny=temp.miny;
            maxx=temp.maxx;
            maxy=temp.maxy;
        }
    }
    @Override
    public int compareTo(CheckerBoard o) {
        return Integer.compare(this.getScore(),o.getScore());
    }
    public static Comparator<CheckerBoard> cmpfx = new Comparator<CheckerBoard>() {
        public int compare(CheckerBoard i1, CheckerBoard i2) {
            return i2.getScore()-i1.getScore();
        }
    };
}
