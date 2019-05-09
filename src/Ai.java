

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class Node {
    public int p; // 落子方（玩家or AI）
    public int x; // 落子坐标x
    public int y; // 落子坐标y

    public Node() {

    }

    public Node(int p, int x, int y) {
        this.p = p;
        this.x = x;
        this.y = y;
    }

    public Node(Node node) {
        this.p = node.p == 1 ? 2 :1;
        this.x = node.x;
        this.y = node.y;
    }

}
public class Ai {
    public final   int AI = 1;
    public final   int PLAYER = 2;
    public final   int BLANK = 0;
    private final   int WIN = 0;
    private final   int HUO4 = 1;
    private final   int CHONG4 = 2;
    private final   int HUO3 = 3;
    private final   int MIAN3 = 4;
    private final   int HUO2 = 5;
    private final   int MIAN2 = 6;
    private final   int OL1 = 7;
    private final   int NONE = 8;
    private final   String[] WIN_AI = {"11111"};
    private final   String[] WIN_PLAYER = {"22222"};
    private final   String[] HUO4_AI = {"011110"};
    private final   String[] HUO4_PLAYER = {"022220"};
    private final   String[] CHONG4_AI = {"011112", "211110", "10111", "11011", "11101"};
    private final   String[] CHONG4_PLAYER = {"022221", "122220", "20222", "22022", "22202"};
    private final   String[] HUO3_AI = {"001110", "011100", "010110", "011010"};
    private final   String[] HUO3_PLAYER = {"002220", "022200", "020220", "022020"};
    private final   String[] MIAN3_AI = {"001112", "010112", "011012", "011102", "211100", "211010", "210110", "201110", "00111", "10011", "10101", "10110", "01011", "10011", "11001", "11010", "01101", "10101", "11001", "11100",};
    private final   String[] MIAN3_PLAYER = {"002221", "020221", "022021", "022201", "122200", "122020", "120220", "102220", "00222", "20022", "20202", "20220", "02022", "20022", "22002", "22020", "02202", "20202", "22002", "22200",};
    private final   String[] HUO2_AI = {"000110", "001010", "001100", "001100", "010100", "011000", "000110", "010010", "010100", "001010", "010010", "011000",};
    private final   String[] HUO2_PLAYER = {"000220", "002020", "002200", "002200", "020200", "022000", "000220", "020020", "020200", "002020", "020020", "022000",};
    private final   String[] MIAN2_AI = {"000112", "001012", "010012", "10001", "2010102", "2011002", "211000", "210100", "210010", "2001102"};
    private final   String[] MIAN2_PLAYER = {"000221", "002021", "020021", "20002", "1020201", "1022001", "122000", "120200", "120020", "1002201"};
    private final   String[] OL1_AI = {"1"};
    private final   String[] OL1_PLAYER = {"2"};
    private final   String[] NONE_ = {""};
    // 棋盘宽度
    private final   int BOARD_SIZE = 19;
    // 棋盘
    // 棋盘
    private   int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    private   int[][] scoreMatrix ; // 搜索过程记录落子评分
    private   final int INFINITY = 1000000000;
    public   int [] getXY(int [][]map,CheckerBoard checkerBoard)
    {

        initBoard(map);
        Node root = new Node(AI, 0, 0);
        int N = 3; // 考虑落子的深度，即模拟几步落子
        int score = alphaBeta(root, -INFINITY, INFINITY, N); // 计算下一步落子的评分
        List<Node> nodes = new ArrayList<>(); // 下一步落子方案列表
        for (int i = 0; i < BOARD_SIZE; i++) { // 寻找下一步可能的落子点
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(checkerBoard.isWin(i,j))
                {

                    int[] ans=new int[2];
                    ans[0]=i;
                    ans[1]=j;
                    return ans;
                }
                if (scoreMatrix[i][j] == score) {
                    nodes.add(new Node(AI, i, j));
                }
            }
        }
        Node node = getBestStep(nodes);
        board[node.x][node.y] = 999;
        int[] ans=new int[2];
        ans[0]=node.x;
        ans[1]=node.y;
        return ans;
    }
    public   int alphaBeta(Node node, int alpha, int beta, int depth) {
        if (checkSituation(node.p, getString(node.x, node.y), WIN)) { // 形成连5
            if (node.p == AI) {
                return INFINITY;
            } else {
                return -INFINITY;
            }
        }
        if (depth == 0) {
            return computeScore();
        }
        if (node.p == AI) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (isValid(i, j)) {
                        board[i][j] = node.p; // 模拟落子
                        Node child = new Node(node); // 生成下一步
                        child.x = i;
                        child.y = j;
                        int val = alphaBeta(child, alpha, beta, depth - 1);
                        board[i][j] = BLANK;
                        if (val > alpha) { // 落子有利
                            alpha = val;
                            scoreMatrix[i][j] = alpha;
                        }
                        if (alpha >= beta) {
                            break;
                        }
                    }
                }
                if (alpha >= beta) {
                    break;
                }
            }
            return alpha;
        } else {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (isValid(i, j)) {
                        board[i][j] = node.p;
                        Node child = new Node(node);
                        child.x = i;
                        child.y = j;
                        int val = alphaBeta(child, alpha, beta, depth - 1);
                        board[i][j] = BLANK;
                        if (val < beta) {
                            beta = val;
                            scoreMatrix[i][j] = beta;
                        }
                        if (alpha >= beta) {
                            break;
                        }
                    }
                }
                if (alpha >= beta) {
                    break;
                }
            }
            return beta;
        }
    }

    /**
     * 从所有可能的落子策略中选取最好的一个
     *
     * @param nodes
     * @return
     */
    public   Node getBestStep(List<Node> nodes) {
        Node node = nodes.get(0);
        board[node.x][node.y] = node.p;
        int v = computeScore();
        board[node.x][node.y] = BLANK;
        int index = 0;
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            board[n.x][n.y] = n.p;
            int v1 = computeScore();
            if (v1 > v) {
                index = i;
            }
            board[n.x][n.y] = BLANK;
        }
        node = nodes.get(index);
        return node;
    }

    /**
     * 扫描棋盘计算当前局势得分
     *
     * @return
     */
    public   int computeScore() {
        int score = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != BLANK) {
                    List<String> list = getString(i, j);
                    if (checkSituation(AI, list, WIN)) {
                        score += 100000000;
                    }
                    if (checkSituation(PLAYER, list, WIN)) {
                        score += -100000000;
                    }
                    if (checkSituation(AI, list, HUO4)) {
                        score += 10000000;
                    }
                    if (checkSituation(PLAYER, list, HUO4)) {
                        score += -10000000;
                    }
                    if (checkSituation(AI, list, CHONG4)) {
                        score += 1000000;
                    }
                    if (checkSituation(PLAYER, list, CHONG4)) {
                        score += -1000000;
                    }
                    if (checkSituation(AI, list, HUO3)) {
                        score += 100000;
                    }
                    if (checkSituation(PLAYER, list, HUO3)) {
                        score += -100000;
                    }
                    if (checkSituation(AI, list, MIAN3)) {
                        score += 10000;
                    }
                    if (checkSituation(PLAYER, list, MIAN3)) {
                        score += -10000;
                    }
                    if (checkSituation(AI, list, HUO2)) {
                        score += 1000;
                    }
                    if (checkSituation(PLAYER, list, HUO2)) {
                        score += -1000;
                    }
                    if (checkSituation(AI, list, MIAN2)) {
                        score += 100;
                    }
                    if (checkSituation(PLAYER, list, MIAN2)) {
                        score += -100;
                    }
                    if (checkSituation(AI, list, OL1)) {
                        score += 10;
                    }
                    if (checkSituation(PLAYER, list, OL1)) {
                        score += -10;
                    }
                    if (checkSituation(AI, list, NONE)) {
                        score += 1;
                    }
                    if (checkSituation(PLAYER, list, NONE)) {
                        score += -1;
                    }
                }
            }
        }
        return score;
    }

    /**
     * 判断落子策略是否能够形成相应的棋型
     *
     * @param p
     * @param list
     * @param type
     * @return
     */
    public   boolean checkSituation(int p, List<String> list, int type) {
        switch (type) {
            case WIN:
                if (p == AI) { // 找AI在x,y处落子是否能连五
                    if (checkString(list, WIN_AI)) {
                        return true;
                    }
                } else if (p == PLAYER) { // 找玩家在x,y处落子是否能连五
                    if (checkString(list, WIN_PLAYER)) {
                        return true;
                    }
                }
                return false;
            case HUO4:
                if (p == AI) { // 找AI在x,y处落子是否能活4
                    if (checkString(list, HUO4_AI)) {
                        return true;
                    }
                } else if (p == PLAYER) { // 找玩家在x,y处落子是否能活4
                    if (checkString(list, HUO4_PLAYER)) {
                        return true;
                    }
                }
                return false;
            case CHONG4:
                if (p == AI) { // 找AI在x,y处落子是否能冲4
                    if (checkString(list, CHONG4_AI)) {
                        return true;
                    }
                } else if (p == PLAYER) { // 找玩家在x,y处落子是否能冲4
                    if (checkString(list, CHONG4_PLAYER)) {
                        return true;
                    }
                }
                return false;
            case HUO3:
                if (p == AI) { // 找AI在x,y处落子是否能活3
                    if (checkString(list, HUO3_AI)) {
                        return true;
                    }
                } else if (p == PLAYER) { // 找玩家在x,y处落子是否能活3
                    if (checkString(list, HUO3_PLAYER)) {
                        return true;
                    }
                }
                return false;
            case MIAN3:
                if (p == AI) { // 找AI在x,y处落子是否能眠3
                    if (checkString(list, MIAN3_AI)) {
                        return true;
                    }
                } else if (p == PLAYER) { // 找玩家在x,y处落子是否能眠3
                    if (checkString(list, MIAN3_PLAYER)) {
                        return true;
                    }
                }
                return false;
            case HUO2:
                if (p == AI) { // 找AI在x,y处落子是否能活2
                    if (checkString(list, HUO2_AI)) {
                        return true;
                    }
                } else if (p == PLAYER) { // 找玩家在x,y处落子是否能活2
                    if (checkString(list, HUO2_PLAYER)) {
                        return true;
                    }
                }
                return false;
            case MIAN2:
                if (p == AI) { // 找AI在x,y处落子是否能眠2
                    if (checkString(list, MIAN2_AI)) {
                        return true;
                    }
                } else if (p == PLAYER) { // 找玩家在x,y处落子是否能眠2
                    if (checkString(list, MIAN2_PLAYER)) {
                        return true;
                    }
                }
                return false;
            case OL1:
                if (p == AI) {
                    if (checkString(list, OL1_AI)) {
                        return true;
                    }
                } else if (p == PLAYER) {
                    if (checkString(list, OL1_PLAYER)) {
                        return true;
                    }
                }
                return false;
            case NONE:
                if (p == AI) {
                    if (checkString(list, NONE_)) {
                        return true;
                    }
                } else if (p == PLAYER) {
                    if (checkString(list, NONE_)) {
                        return true;
                    }
                }
                return false;
        }
        return true;
    }

    /**
     * 判断列表元素是否包含特定的字符串
     *
     * @param list
     * @param situation
     * @return
     */
    public   boolean checkString(List<String> list, String[] situation) {
        for (String str : list) {
            for (int i = 0; i < situation.length; i++) {
                if (str.contains(situation[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 取(x, y)坐标点横竖撇捺(─ │ / \)四个方向的字符串
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return
     */
    public   List<String> getString(int x, int y) {
        List<String> strings = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0, j = y; i < BOARD_SIZE; i++) { // 方向:│
            sb.append(board[i][j]);
        }
        strings.add(sb.toString());
        sb.delete(0, sb.length());
        for (int i = x, j = 0; j < BOARD_SIZE; j++) { // 方向:─
            sb.append(board[i][j]);
        }
        strings.add(sb.toString());
        sb.delete(0, sb.length());
        // 方向:/
        if (x + y < BOARD_SIZE) { // 棋盘上三角
            for (int i = 0, j = x + y; i < BOARD_SIZE && j >= 0; i++, j--) {
                sb.append(board[i][j]);
            }
        } else { // 棋盘下三角
            for (int i = x + y - 7, j = BOARD_SIZE - 1; i < BOARD_SIZE && j >= 0; i++, j--) {
                sb.append(board[i][j]);
            }
        }
        strings.add(sb.toString());
        sb.delete(0, sb.length());
        // 方向:\
        if (x <= y) {
            for (int i = 0, j = y - x; i < BOARD_SIZE && j < BOARD_SIZE; i++, j++) {  // 棋盘上三角
                sb.append(board[i][j]);
            }
        } else {
            for (int i = x - y, j = 0; i < BOARD_SIZE && j < BOARD_SIZE; i++, j++) {  // 棋盘下三角
                sb.append(board[i][j]);
            }
        }
        strings.add(sb.toString());
        sb.delete(0, sb.length());
        return strings;
    }


    public   void initBoard( int map[][]) {
        for(int i=0;i<BOARD_SIZE;i++)
        {
            for(int j=0;j<BOARD_SIZE;j++)
            {
                if(map[i][j]==0)board[i][j]=PLAYER;
                if(map[i][j]==1)board[i][j]=AI;
            }
        }
        scoreMatrix = new int[BOARD_SIZE][BOARD_SIZE];

    }

    /**
     * 用于将落子点控制在当前棋局已落子位置周围
     *
     * @param x
     * @param y
     * @return
     */
    public   boolean isValid(int x, int y) {
        if (board[x][y] != BLANK) {
            return false;
        }
        if (x == 0) { // 上边界
            if (y == 0) { // 左上角
                if (board[x + 1][y] == BLANK && board[x][y + 1] == BLANK && board[x + 1][y + 1] == BLANK) {
                    return false;
                }
            } else if (y == BOARD_SIZE - 1) { // 右上角
                if (board[x + 1][y] == BLANK && board[x][y - 1] == BLANK && board[x + 1][y - 1] == BLANK) {
                    return false;
                }
            } else {
                if (board[x][y + 1] == BLANK && board[x][y - 1] == BLANK && board[x + 1][y + 1] == BLANK && board[x + 1][y - 1] == BLANK && board[x + 1][y] == BLANK) {
                    return false;
                }
            }
        } else if (y == 0) {  // 左边界
            if (x == 0) { // 左上角
                if (board[x + 1][y] == BLANK && board[x][y + 1] == BLANK && board[x + 1][y + 1] == BLANK) {
                    return false;
                }
            } else if (x == BOARD_SIZE - 1) { // 左下角
                if (board[x - 1][y] == BLANK && board[x][y + 1] == BLANK && board[x - 1][y + 1] == BLANK) {
                    return false;
                }
            } else {
                if (board[x + 1][y] == BLANK && board[x - 1][y] == BLANK && board[x + 1][y + 1] == BLANK && board[x - 1][y + 1] == BLANK && board[x][y + 1] == BLANK) {
                    return false;
                }
            }
        } else if (x == BOARD_SIZE - 1) {  // 下边界
            if (y == 0) { // 左下角
                if (board[x - 1][y] == BLANK && board[x][y + 1] == BLANK && board[x - 1][y + 1] == BLANK) {
                    return false;
                }
            } else if (y == BOARD_SIZE - 1) { // 右下角
                if (board[x][y - 1] == BLANK && board[x - 1][y] == BLANK && board[x - 1][y - 1] == BLANK) {
                    return false;
                }
            } else {
                if (board[x][y + 1] == BLANK && board[x][y - 1] == BLANK && board[x - 1][y + 1] == BLANK && board[x - 1][y - 1] == BLANK && board[x - 1][y] == BLANK) {
                    return false;
                }
            }
        } else if (y == BOARD_SIZE - 1) {  // 右边界
            if (x == 0) { // 右上角
                if (board[x][y - 1] == BLANK && board[x + 1][y] == BLANK && board[x + 1][y - 1] == BLANK) {
                    return false;
                }
            } else if (x == BOARD_SIZE - 1) { // 右下角
                if (board[x - 1][y] == BLANK && board[x][y - 1] == BLANK && board[x - 1][y - 1] == BLANK) {
                    return false;
                }
            } else {
                if (board[x - 1][y] == BLANK && board[x + 1][y] == BLANK && board[x][y - 1] == BLANK && board[x - 1][y - 1] == BLANK && board[x + 1][y - 1] == BLANK) {
                    return false;
                }
            }
        } else { // 非边界
            if (board[x - 1][y - 1] == BLANK && board[x - 1][y] == BLANK && board[x - 1][y + 1] == BLANK && board[x][y - 1] == BLANK && board[x][y + 1] == BLANK
                    && board[x + 1][y - 1] == BLANK && board[x + 1][y] == BLANK && board[x + 1][y + 1] == BLANK) {
                return false;
            }
        }
        return true;
    }

    public   void show() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.printf("%10d", board[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * 显示落子位置评分
     */
    public   void showScore() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int m = 0; m < BOARD_SIZE; m++) {
                    for (int n = 0; n < BOARD_SIZE; n++) {
                        if (board[m][n] != 0) {
                            scoreMatrix[m][n] = board[m][n];
                        }
                    }
                }
            }
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.printf("%10d", scoreMatrix[i][j]);
            }
            System.out.println();
        }
    }
}