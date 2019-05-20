public class CheckerBoard {
    private int[][] map;//-1是无人 0是白，1是黑，黑先手
    private int count;
    CheckerBoard()
    {
        count=1;
        map=new int[19][19];
        for (int i=0;i<19;i++)
        {
            for(int j=0;j<19;j++)
            {
                map[i][j]=-1;
            }
        }
    }
    int getCount()
    {
        return count;
    }
    public void reset()
    {
        count=1;
        for (int i=0;i<19;i++)
        {
            for(int j=0;j<19;j++)
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
            if(x<0||y<0||x>=19||y>=19)break;
        }
        x=xx;
        y=yy;
        while (map[x][y]==color)
        {
            ans++;
            x++;
            y++;
            if(x<0||y<0||x>=19||y>=19)break;
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
            if(x<0||y<0||x>=19||y>=19)break;
        }
        x=xx;
        y=yy;
        while (map[x][y]==color)
        {
            ans++;
            x--;
            y++;
            if(x<0||y<0||x>=19||y>=19)break;
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
            if(y<0||y>=19)break;
        }
        y=yy;
        while (map[x][y]==color)
        {
            ans++;
            y++;
            if(y<0||y>=19)break;
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
            if(x<0||x>=19)break;
        }
        x=xx;
        while (map[x][y]==color)
        {
            ans++;
            x++;
            if(x<0||x>=19)break;
        }
        ans--;
        return ans;
    }
    boolean isWin(int x,int y)
    {
        if(x>=19||y>=19||x<0||y<0)return false;
        int color=count%2;
        count++;

        if(map[x][y]!=-1)
        {
            count--;
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
    int move(int x,int y,int color)
    {
        if(x>=19||y>=19||x<0||y<0)return 0;
        count++;

        if(map[x][y]!=-1)
        {
            count--;
            return -1;
        }
        map[x][y]=color;
        if(Continuum1(x,y,color)>=5)return color;
        if(Continuum2(x,y,color)>=5)return color;
        if(Continuum3(x,y,color)>=5)return color;
        if(Continuum4(x,y,color)>=5)return color;
        return -2;
    }
    void pf()
    {
        for (int i=0;i<19;i++)
        {
            for(int j=0;j<19;j++)
            {
                System.out.print(map[i][j]+" ");
            }
            System.out.println(" ");
        }
    }



}
