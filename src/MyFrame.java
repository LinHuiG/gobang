
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
    public static int rj;
    public static final int BOARD_SIZE = 19;// 棋盘格数
    public static final int qppy_x=60;// 棋盘偏移
    public static final int qppy_y=50;
    private Ai ai;
    private int cx=-1, cy=-1;
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    // 取得屏幕的高度
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    // 背景图片
    // 保存棋子的坐标
    int x = 0;
    int y = 0;
    int color;
    // 保存之前下过的全部棋子的坐标
    // 其中数据内容 0： 表示这个点并没有棋子， 1： 表示这个点是黑子， 2：表示这个点是白子
    // 标识当前应该黑棋还是白棋下下一步
    boolean isBlack = true;
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
    public MyFrame(CheckerBoard checkerBoard,int rj) {
        // 设置标题
        this.rj=rj;
        color=1;
        ai=new Ai();
        this.checkerBoard=checkerBoard;
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
        if(rj==1&&color==1)
        {

            putdown(9,9);

        }

    }

    public void paint(Graphics g) {
        // 双缓冲技术防止屏幕闪烁
        BufferedImage bi = new BufferedImage(500, 500,
                BufferedImage.TYPE_INT_RGB);
        Graphics g2 = bi.createGraphics();

        g2.setColor(Color.GRAY);
        g2.fillRect(0+qppy_x-10, 50+qppy_y, 400, 400);
        g2.setColor(Color.YELLOW);

        g2.fillRect(0+qppy_x, 60+qppy_y, 380, 380);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("宋体", Font.BOLD, 30));
        g2.drawString("游戏信息：" + message, qppy_x+40, qppy_y+20);
        // 输出时间信息
        g2.setFont(new Font("宋体", 0, 14));
        g2.setColor(Color.BLACK);
        // 绘制棋盘
        for (int i = 0; i < 19; i++) {
            g2.drawLine(10+qppy_x, 70 + 20 * i+qppy_y, 370+qppy_x, 70+qppy_y + 20 * i);
            g2.drawLine(10+qppy_x + 20 * i, 70+qppy_y, 10+qppy_x + 20 * i, 430+qppy_y);
        }

        // 标注点位
        g2.fillOval(68+qppy_x, 128+qppy_y, 4, 4);
        g2.fillOval(308+qppy_x, 128+qppy_y, 4, 4);
        g2.fillOval(308+qppy_x, 368+qppy_y, 4, 4);
        g2.fillOval(68+qppy_x, 368+qppy_y, 4, 4);
        g2.fillOval(308+qppy_x, 248+qppy_y, 4, 4);
        g2.fillOval(188+qppy_x, 128+qppy_y, 4, 4);
        g2.fillOval(68+qppy_x, 248+qppy_y, 4, 4);
        g2.fillOval(188+qppy_x, 368+qppy_y, 4, 4);
        g2.fillOval(188+qppy_x, 248+qppy_y, 4, 4);


        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (checkerBoard.getchessman(i,j) == 1) {
                    // 黑子
                    int tempX = i * 20 + 10;
                    int tempY = j * 20 + 70;

                    g2.setColor(Color.BLACK);
                    g2.fillOval(tempX - 7+qppy_x, tempY - 7+qppy_y, 14, 14);
                }
                if (checkerBoard.getchessman(i,j) == 0) {
                    // 白子
                    int tempX = i * 20 + 10;
                    int tempY = j * 20 + 70;
                    g2.setColor(Color.WHITE);
                    g2.fillOval(tempX - 7+qppy_x, tempY - 7+qppy_y, 14, 14);
                }
            }
        }
        if(cx!=-1&&cy!=-1)
        {
            drawCell(g2,cx,cy);
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
            int pd=checkerBoard.move(x,y,color);
            color++;
            color%=2;
            if(pd>=0)
            {
                System.out.println((pd==1?"黑子":"白子")+"赢了");
                JOptionPane.showMessageDialog(this, "游戏结束,"
                        + (pd==1?"黑":"白") + "方获胜！");
                checkerBoard.reset();
                color=1;
                ai=new Ai();
                if(rj==1&&color==1)
                {

                    putdown(9,9);

                }
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
        if (canPlay == true) {

            x = e.getX()-qppy_x;
            y = e.getY()-qppy_y;
            x = (x ) / 20;
            y = (y - 60) / 20;
            if(x<0||y<0||x>=19||y>=19)return;
            putdown(x,y);
            repa();
            if(rj==1&&color==1)
            {
                long st= System.currentTimeMillis();
                while(System.currentTimeMillis()-st<100);
                int []ans=ai.getXY(checkerBoard.getMap(),checkerBoard);
                x=ans[0];
                y=ans[1];
                putdown(x,y);

            }
        }

    }

    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
        public void mouseMoved(MouseEvent e) {
            int x = e.getX()-qppy_x;
            int y = e.getY()-qppy_y;
            x = (x ) / 20;
            y = (y - 60) / 20;
            if(x<0||y<0||x>=19||y>=19)
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
    private void drawCell(Graphics g2d, int x, int y) {// c 是style

        int length = CELL_WIDTH / 4;
        int xx = (x ) * CELL_WIDTH+10;
        int yy = (y ) * CELL_WIDTH + 70;
        int x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = x4 = xx - CELL_WIDTH / 2+qppy_x;
        x2 = x3 = xx + CELL_WIDTH / 2+qppy_x;
        y1 = y2 = yy - CELL_WIDTH / 2+qppy_y;
        y3 = y4 = yy + CELL_WIDTH / 2+qppy_y;
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