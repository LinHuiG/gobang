
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyFrame extends JFrame implements MouseListener, Runnable {

    // 取得屏幕的宽度
    private final int CELL_WIDTH = 20;
    public static int ISAIPLAYER=-1;
    public static final int BOARD_SIZE = 19;// 棋盘格数
    public static final int OFFSET_X =60;// 棋盘偏移
    public static final int OFFSET_Y =50;
    public static int SELECT = -1;
    private int cx=-1, cy=-1;
    boolean csms = false;
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    // 取得屏幕的高度
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    // 背景图片
    // 保存棋子的坐标
    int x = 0;
    int y = 0;
    int  color;
    // 标识当前游戏是否可以继续
    boolean canPlay = true;
    // 保存显示的提示信息
    String message = "黑方先行";
    // 保存最多拥有多少时间(秒)
    int maxTime = 0;
    // 做倒计时的线程类
    Thread t = new Thread(this);
    // 保存黑方与白方的剩余时间
    // 保存双方剩余时间的显示信息
    CheckerBoard checkerBoard;
    public MyFrame() {
        // 设置标题
        color=1;
        this.checkerBoard=new CheckerBoard(BOARD_SIZE);
        this.setTitle("五子棋");
        // 设置窗体大小
        this.setSize(500, 550);
        // 设置窗体出现位置
        this.setLocation((width - 500) / 2, (height - 500) / 2);
        // 将窗体设置为大小不可改变
        this.setResizable(false);
        // 将窗体的关闭方式设置为默认关闭后程序结束
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 为窗体加入监听器
        this.addMouseListener(this);
        this.setBackground(Color.BLACK);
        // 将窗体显示出来
        this.addMouseMotionListener(mouseMotionListener);
        this.setVisible(true);
        t.start();


        // 刷新屏幕,防止开始游戏时出现无法显示的情况.
        this.repaint();
        if(ISAIPLAYER ==1&&color==1)
        {
            putdown(9,9);
        }

    }

    public void paint(Graphics g) {

        // 双缓冲技术防止屏幕闪烁

        BufferedImage bi = new BufferedImage(500, 550,
                BufferedImage.TYPE_INT_RGB);
        Graphics g2 = bi.createGraphics();
        if (SELECT == 0)
        {
            g2.setColor(Color.GRAY);
            g2.fillRect(0+ OFFSET_X -10, 50+ OFFSET_Y, 400, 400);
            g2.setColor(Color.YELLOW);

            g2.fillRect(0+ OFFSET_X, 60+ OFFSET_Y, 380, 380);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("宋体", Font.BOLD, 30));
            g2.drawString("游戏信息：" + message, OFFSET_X +40, OFFSET_Y +20);
            // 输出时间信息
            g2.setFont(new Font("宋体", Font.BOLD, 20));
            g2.drawString("悔棋", OFFSET_X +100, OFFSET_Y +470);
            g2.drawString("重新开始", OFFSET_X +220, OFFSET_Y +470);
            g2.setFont(new Font("宋体", 0, 14));
            g2.setColor(Color.BLACK);
            // 绘制棋盘
            for (int i = 0; i < BOARD_SIZE; i++) {
                g2.drawLine(10+ OFFSET_X, 70 + 20 * i+ OFFSET_Y, 370+ OFFSET_X, 70+ OFFSET_Y + 20 * i);
                g2.drawLine(10+ OFFSET_X + 20 * i, 70+ OFFSET_Y, 10+ OFFSET_X + 20 * i, 430+ OFFSET_Y);
            }

            // 标注点位
            for (int xx = 0; xx < 3; xx++) {
                for (int yy = 0; yy < 3; yy++) {
                    g2.fillOval(68 + OFFSET_X + xx * 120, 128 + OFFSET_Y + yy * 120, 4, 4);
                }
            }


            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (checkerBoard.getchessman(i,j) == 1) {
                        // 黑子
                        int tempX = i * 20 + 10;
                        int tempY = j * 20 + 70;

                        g2.setColor(Color.BLACK);
                        g2.fillOval(tempX - 7+ OFFSET_X, tempY - 7+ OFFSET_Y, 14, 14);
                    }
                    if (checkerBoard.getchessman(i,j) == 0) {
                        // 白子
                        int tempX = i * 20 + 10;
                        int tempY = j * 20 + 70;
                        g2.setColor(Color.WHITE);
                        g2.fillOval(tempX - 7+ OFFSET_X, tempY - 7+ OFFSET_Y, 14, 14);
                    }
                }
            }
            if(cx!=-1&&cy!=-1)
            {
                drawCell(g2,cx,cy);
            }
            if(checkerBoard.getPrx()!=-1&&checkerBoard.getPry()!=-1)
            {
                drawCell(g2,checkerBoard.getPrx(),checkerBoard.getPry());
            }
        }
        else
        {

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("宋体", Font.BOLD, 30));
            g2.drawString("请选择游戏模式", OFFSET_X +40, OFFSET_Y +20);
            if(SELECT==1) g2.setColor(Color.MAGENTA);
            g2.setFont(new Font("宋体", Font.BOLD, 30));
            g2.drawString("1.人机模式", OFFSET_X +60, OFFSET_Y +120);
            if(SELECT==2) g2.setColor(Color.MAGENTA);
            else g2.setColor(Color.WHITE);
            g2.setFont(new Font("宋体", Font.BOLD, 30));
            g2.drawString("2.玩家对战", OFFSET_X +60, OFFSET_Y +160);

            if (SELECT == 3) g2.setColor(Color.MAGENTA);
            else g2.setColor(Color.WHITE);
            g2.setFont(new Font("宋体", Font.BOLD, 30));
            g2.drawString("3.继续游戏", OFFSET_X +60, OFFSET_Y +200);

        }

        g.drawImage(bi, 0, 0, this);
    }
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }
    public void putdown(int x,int y)
    {
            if(checkerBoard.getchessman(x,y)!=-1)return;
            int pd=checkerBoard.move(x,y);
            repa();
            color++;
            color%=2;
        checkerBoard.save();
            if(pd>=0)
            {
                if(pd==3)
                {
                    System.out.println("平局");
                    JOptionPane.showMessageDialog(this, "游戏结束,平局！");
                }
                else
                {
                    System.out.println((pd==1?"黑子":"白子")+"赢了");
                    JOptionPane.showMessageDialog(this, "游戏结束," + (pd==1?"黑":"白") + "方获胜！");
                }
                checkerBoard.withdraw();
                checkerBoard.save();
                checkerBoard.reset();
                color=1;
                SELECT = -1;
                ISAIPLAYER=-1;
            }
            else if(color==0)
            {
                message = "轮到白方";
            }
            else if(color==1)
            {
                message = "轮到黑方";
            }

        // 刷新屏幕,防止开始游戏时出现无法显示的情况.
        this.repaint();


    }
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        /*
         * System.out.println("X:"+e.getX()); System.out.println("Y:"+e.getY());
         */
        if (SELECT != 0)
        {
            if (SELECT == -1) return;
            if (SELECT == 3)
            {

                checkerBoard=new CheckerBoard("cs");
                ISAIPLAYER=1;//ai
                checkerBoard.isAi=1;
                SELECT = 0;
                csms = true;
                color = checkerBoard.getCount() % 2;
                if (ISAIPLAYER == 1 && color == 1) {
                        int[] ans = GetMove.getMoveBydfs(checkerBoard);
                        x = ans[0];
                        y = ans[1];
                        putdown(x, y);

                }

                return;
            }
            ISAIPLAYER = SELECT % 2;
            checkerBoard.isAi=ISAIPLAYER;
            SELECT = 0;
            if(ISAIPLAYER ==1&&color==1)
            {
                putdown(9,9);
            }
            return;
        }
        if (canPlay == true) {
            x = e.getX()- OFFSET_X;
            y = e.getY()- OFFSET_Y;
            System.out.println(x+" "+y);
            if(y>450&&y<470)
            {
                if(x>100&&x<140)
                {
                    checkerBoard.withdraw();
                    return;
                }
                if(x>220&&x<300)
                {
                    checkerBoard.reset();

                    color=1;
                    SELECT = -1;
                    ISAIPLAYER=-1;
                    return;
                }
            }
            x = (x ) / 20;
            y = (y - 60) / 20;
            if(x<0||y<0||x>=BOARD_SIZE||y>=BOARD_SIZE)return;
            putdown(x,y);
            repa();
            if(ISAIPLAYER ==1&&color==1)
            {
                int []ans=GetMove.getMoveBydfs(checkerBoard);
                x=ans[0];
                y=ans[1];
                System.out.println(x+" "+y);
                putdown(x,y);

            }
        }

    }

    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
        public void mouseMoved(MouseEvent e) {
            if (SELECT != 0)
            {
                int x = e.getX();
                int y = e.getY();
                int xz;
                if(x>OFFSET_X +60&&y>OFFSET_Y +80&&x<OFFSET_X +260&&y< OFFSET_Y +120)xz=1;
                else if(x>OFFSET_X +60&&y>OFFSET_Y +120&&x<OFFSET_X +260&&y< OFFSET_Y +160)xz=2;
                else if (x > OFFSET_X + 60 && y > OFFSET_Y + 160 && x < OFFSET_X + 260 && y < OFFSET_Y + 200) xz = 3;
                else xz = -1;
                if(xz!=SELECT)
                {
                    SELECT=xz;
                    repa();
                }
                return;
            }

            int x = e.getX()- OFFSET_X;
            int y = e.getY()- OFFSET_Y;
            x = (x ) / 20;
            y = (y - 60) / 20;
            if(x<0||y<0||x>=BOARD_SIZE||y>=BOARD_SIZE)
            {
                cx=-1;
                cy=-1;
            }
            else if(cx!=x||cy!=y)
            {
                cx=x;
                cy=y;
                repa();
            }

        }
    };
    private void repa()
    {

        this.repaint();
    }
    private void drawCell(Graphics g2d, int x, int y) {

        int length = CELL_WIDTH / 4;
        int xx = (x ) * CELL_WIDTH+10;
        int yy = (y ) * CELL_WIDTH + 70;
        int x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = x4 = xx - CELL_WIDTH / 2+ OFFSET_X;
        x2 = x3 = xx + CELL_WIDTH / 2+ OFFSET_X;
        y1 = y2 = yy - CELL_WIDTH / 2+ OFFSET_Y;
        y3 = y4 = yy + CELL_WIDTH / 2+ OFFSET_Y;
        g2d.setColor(Color.RED);
        g2d.drawLine(x1, y1, x1 + length, y1);
        g2d.drawLine(x1, y1, x1, y1 + length);
        g2d.drawLine(x2, y2, x2 - length, y2);
        g2d.drawLine(x2, y2, x2, y2 + length);
        g2d.drawLine(x3, y3, x3 - length, y3);
        g2d.drawLine(x3, y3, x3, y3 - length);
        g2d.drawLine(x4, y4, x4 + length, y4);
        g2d.drawLine(x4, y4, x4, y4 - length);
    }

    // 判断棋子连接的数量
    public void run() {
        // TODO Auto-generated method stub

            while (true) {

                this.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

    }

}