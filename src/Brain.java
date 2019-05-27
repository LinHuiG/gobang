import java.awt.*;
import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Brain {
    public Board bd;
    private int INFINITY = 1000000;
    private int movex, movey;
    private int level;// 深度
    private int node;// 每层结点


    public Brain() {
        this.bd = new Board();
        this.level = 3;
        this.node = 15;
    }

    public int[] findTreeBestStep() {
        alpha_beta(0, bd, -INFINITY, INFINITY);
        int[] result = {movex, movey};
        return result;
    }

    public int alpha_beta(int depth, Board board, int alpha, int beta) {
        if (depth == level || board.isGameOver() != 0) {
            Chess[] sorted = board.getSorted();
            Chess move = board.getData()[sorted[0].x][sorted[0].y];
            return move.getSum();// 局面估分
        }
        Board temp = new Board(board);
        Chess[] sorted = temp.getSorted();
        int score;
        for (int i = 0; i < node; i++) {
            int x = sorted[i].x;
            int y = sorted[i].y;
            if (!temp.putChess(x, y))
                continue;
            if (sorted[i].getOffense() >= Board.Level.ALIVE_4.score) {
                score = INFINITY + 1;
            } else if (sorted[i].getDefence() >= Board.Level.ALIVE_4.score) {
                score = INFINITY;
            } else {
                score = alpha_beta(depth + 1, temp, alpha, beta);
            }
            temp = new Board(board);

            if (depth % 2 == 0) {
                if (score > alpha) {
                    alpha = score;
                    if (depth == 0) {
                        movex = x;
                        movey = y;
                    }
                }
                if (alpha >= beta) {
                    score = alpha;
                    return score;
                }
            } else {// MIN
                if (score < beta) {
                    beta = score;
                }
                if (alpha >= beta) {
                    score = beta;
                    return score;
                }
            }
        }
        return depth % 2 == 0 ? alpha : beta;
    }

    public static class Chess implements Comparable<Chess> {
        public static final int BLACK = 1;
        public static final int WHITE = 2;
        public static final int BORDER = -1;
        public static final int EMPTY = 0;
        protected int x;
        protected int y;
        protected int offense;//攻击分
        protected int defence;//防守分
        protected int sum;//综合分
        protected int side;//落子
        private StringBuilder detail;//该点的落子估值

        public Chess(int x, int y) {
            this.x = x;
            this.y = y;
            detail = new StringBuilder();
        }

        public int getOffense() {
            return offense;
        }

        public int getDefence() {
            return defence;
        }

        public int getSum() {
            return sum;
        }

        public int getSide() {
            return side;
        }

        public void setSide(int side) {
            this.side = side;
        }

        public StringBuilder append(String more) {
            return this.detail.append(more);
        }

        public void reset() {
            clearDetail();
            offense = defence = sum = 0;
            side = EMPTY;
        }

        public void clearDetail() {
            detail = new StringBuilder();
        }

        public String toString() {
            return String.format(x + "," + y + "-(" + (char) (64 + x) + ","
                    + (16 - y) + ") " + offense + "," + defence + "," + sum);
        }

        public boolean isEmpty() {
            return side == EMPTY ? true : false;
        }

        @Override
        public int compareTo(Chess o) {
            if (o == null)
                return 0;
            int val1 = sum;
            int val2 = o.getSum();
            if (val1 == val2)
                return 0;
            else if (val1 < val2)
                return 1;
            else
                return -1;
        }

    }

    public static class Board {
        // 棋型信息
        public static enum Level {
            CON_5("长连", 0, new String[]{"11111", "22222"}, 100000), ALIVE_4(
                    "活四", 1, new String[]{"011110", "022220"}, 10000), GO_4(
                    "冲四", 2, new String[]{"011112|0101110|0110110",
                    "022221|0202220|0220220"}, 500), DEAD_4("死四", 3,
                    new String[]{"211112", "122221"}, -5), ALIVE_3("活三", 4,
                    new String[]{"01110|010110", "02220|020220"}, 200), SLEEP_3(
                    "眠三", 5, new String[]{
                    "001112|010112|011012|10011|10101|2011102",
                    "002221|020221|022021|20022|20202|1022201"}, 50), DEAD_3(
                    "死三", 6, new String[]{"21112", "12221"}, -5), ALIVE_2("活二",
                    7, new String[]{"00110|01010|010010", "00220|02020|020020"},
                    5), SLEEP_2("眠二", 8, new String[]{
                    "000112|001012|010012|10001|2010102|2011002",
                    "000221|002021|020021|20002|1020201|1022001"}, 3), DEAD_2(
                    "死二", 9, new String[]{"2112", "1221"}, -5), NULL("null", 10,
                    new String[]{"", ""}, 0);
            private String name;
            private int index;
            private String[] regex;// 正则表达式
            int score;// 分值

            Level(String name, int index, String[] regex, int score) {
                this.name = name;
                this.index = index;
                this.regex = regex;
                this.score = score;
            }

            @Override
            public String toString() {
                return this.name;
            }
        }

        private static enum Direction {
            HENG, SHU, PIE, NA
        }

        private static int[][] position = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                {0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 6, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 6, 7, 7, 7, 7, 7, 6, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 7, 6, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 7, 6, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 6, 7, 7, 7, 7, 7, 6, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 6, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0},
                {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        public static final int BOARD_SIZE = 19;// 棋盘格数
        public static final int BT = BOARD_SIZE + 2;
        public static final int CENTER = BOARD_SIZE / 2 + 1;// 中心点
        private int minx, maxx, miny, maxy; // 当前棋局下所有棋子的最小x，最大x，最小y，最大y，用于缩小搜索落子点的范围
        private int currentPlayer = 0;// 当前玩家
        private Stack<Point> history;// 落子历史记录
        private Chess[][] data;// 1-19
        private Chess[] sorted;// 根据各点的落子估值从大到小排序的数组

        public Board() {
            data = new Chess[BT][BT];
            for (int i = 0; i < BT; i++)
                for (int j = 0; j < BT; j++) {
                    data[i][j] = new Chess(i, j);
                    if (i == 0 || i == BT - 1 || j == 0 || j == BT - 1)
                        data[i][j].setSide(Chess.BORDER);// 边界
                }
            history = new Stack<Point>();
            start();
        }

        public Board(Board b) {// 深度拷贝
            Chess[][] b_data = b.getData();
            Chess[] b_sorted = b.getSorted();
            data = new Chess[BT][BT];
            for (int i = 0; i < BT; i++)
                for (int j = 0; j < BT; j++) {
                    data[i][j] = new Chess(i, j);
                    Chess c = b_data[i][j];
                    data[i][j].sum = c.sum;
                    data[i][j].defence = c.defence;
                    data[i][j].offense = c.offense;
                    data[i][j].side = c.side;
                }
            sorted = new Chess[b_sorted.length];
            for (int i = 0; i < sorted.length; i++) {
                Chess c = b_sorted[i];
                sorted[i] = new Chess(c.x, c.y);
                sorted[i].sum = c.sum;
                sorted[i].defence = c.defence;
                sorted[i].offense = c.offense;
                sorted[i].side = c.side;
            }
            currentPlayer = b.getPlayer();
            minx = b.minx;
            maxx = b.maxx;
            miny = b.miny;
            maxy = b.maxy;
            history = new Stack<Point>();
        }

        public void start() {
            currentPlayer = Chess.BLACK;// 默认黑子先行
            putChess(9, 9);// 默认第一步落在中心
            minx = maxx = miny = maxy = CENTER;
        }

        public Point undo() {// 悔棋
            if (!history.isEmpty()) {
                Point p1 = history.pop();
                Point p2 = history.pop();
                data[p1.x][p1.y].setSide(Chess.EMPTY);
                data[p2.x][p2.y].setSide(Chess.EMPTY);
                return history.peek();
            }
            return null;
        }

        public Chess[][] getData() {
            return data;
        }

        public Chess[] getSorted() {
            return sorted;
        }

        public int getPlayer() {
            return currentPlayer;
        }

        public boolean putChess(int x, int y) {
            if (data[x][y].isEmpty()) {
                // 棋盘搜索范围限制
                minx = Math.min(minx, x);
                maxx = Math.max(maxx, x);
                miny = Math.min(miny, y);
                maxy = Math.max(maxy, y);
                data[x][y].setSide(currentPlayer);
                history.push(new Point(x, y));
                trogglePlayer();
                sorted = getSortedChess(currentPlayer);// 重要
                return true;
            }
            return false;
        }

        private void trogglePlayer() {
            currentPlayer = 3 - currentPlayer;
        }

        private int check(int x, int y, int dx, int dy, int chess) {
            int sum = 0;
            for (int i = 0; i < 4; ++i) {
                x += dx;
                y += dy;
                if (x < 1 || x > BOARD_SIZE || y < 1 || y > BOARD_SIZE) {
                    break;
                }
                if (data[x][y].getSide() == chess) {
                    sum++;
                } else {
                    break;
                }
            }
            return sum;
        }

        public int isGameOver() {
            if (!history.isEmpty()) {
                int chess = (history.size() % 2 == 1) ? Chess.BLACK : Chess.WHITE;
                Point lastStep = history.peek();
                int x = (int) lastStep.getX();
                int y = (int) lastStep.getY();
                if (check(x, y, 1, 0, chess) + check(x, y, -1, 0, chess) >= 4) {
                    return chess;
                }
                if (check(x, y, 0, 1, chess) + check(x, y, 0, -1, chess) >= 4) {
                    return chess;
                }
                if (check(x, y, 1, 1, chess) + check(x, y, -1, -1, chess) >= 4) {
                    return chess;
                }
                if (check(x, y, 1, -1, chess) + check(x, y, -1, 1, chess) >= 4) {
                    return chess;
                }
            }
            for (int i = 0; i < BOARD_SIZE; ++i) {
                for (int j = 0; j < BOARD_SIZE; ++j)
                    if (data[i][j].isEmpty()) {
                        return 0;
                    }
            }
            return 3;
        }

        public Chess[] getSortedChess(int player) {
            // 限制范围
            int px = Math.max(minx - 5, 1);
            int py = Math.max(miny - 5, 1);
            int qx = Math.min(maxx + 5, Board.BT - 1);
            int qy = Math.min(maxy + 5, Board.BT - 1);
            Chess[] temp = new Chess[(qx - px + 1) * (qy - py + 1)];
            int count = 0;
            for (int x = px; x <= qx; x++) {
                for (int y = py; y <= qy; y++) {
                    temp[count] = new Chess(x, y);
                    if (data[x][y].isEmpty()) {
                        data[x][y].clearDetail();
                        data[x][y].append("================================\n");
                        int o = getScore(x, y, player) + 1;// 攻击分，优先
                        data[x][y].append("\n");
                        int d = getScore(x, y, 3 - player);// 防守分
                        data[x][y].append("\n");
                        String cs = "【" + (char) (64 + x) + (16 - y) + "】 ";
                        data[x][y].append(cs).append(" 攻击:" + o).append(" 防守:" + d)
                                .append("\n\n");
                        data[x][y].offense = temp[count].offense = o;
                        data[x][y].defence = temp[count].defence = d;
                        data[x][y].sum = temp[count].sum = o + d;// 综合分
                    }
                    count++;
                }
            }
            Arrays.sort(temp);
            return temp;
        }

        public int getScore(int x, int y, int chess) {
            data[x][y].append("-");
            Level l1 = getLevel(x, y, Direction.HENG, chess);
            data[x][y].append("|");
            Level l2 = getLevel(x, y, Direction.SHU, chess);
            data[x][y].append("/");
            Level l3 = getLevel(x, y, Direction.PIE, chess);
            data[x][y].append("\\");
            Level l4 = getLevel(x, y, Direction.NA, chess);
            return level2Score(l1, l2, l3, l4) + position[x - 1][y - 1];
        }

        public Level getLevel(int x, int y, Direction direction, int chess) {
            String seq, left = "", right = "";
            if (direction == Direction.HENG) {
                left = getHalfSeq(x, y, -1, 0, chess);
                right = getHalfSeq(x, y, 1, 0, chess);
            } else if (direction == Direction.SHU) {
                left = getHalfSeq(x, y, 0, -1, chess);
                right = getHalfSeq(x, y, 0, 1, chess);
            } else if (direction == Direction.PIE) {
                left = getHalfSeq(x, y, -1, 1, chess);
                right = getHalfSeq(x, y, 1, -1, chess);
            } else if (direction == Direction.NA) {
                left = getHalfSeq(x, y, -1, -1, chess);
                right = getHalfSeq(x, y, 1, 1, chess);
            }
            seq = left + chess + right;
            String rseq = new StringBuilder(seq).reverse().toString();
            data[x][y].append("\t" + seq + "\t");
            for (Level level : Level.values()) {
                Pattern pat = Pattern.compile(level.regex[chess - 1]);
                Matcher mat = pat.matcher(seq);
                boolean rs1 = mat.find();
                mat = pat.matcher(rseq);
                boolean rs2 = mat.find();
                if (rs1 || rs2) {
                    data[x][y].append(level.name).append("\n");
                    return level;
                }
            }
            return Level.NULL;
        }

        private String getHalfSeq(int x, int y, int dx, int dy, int chess) {
            String sum = "";
            boolean isR = false;
            if (dx < 0 || (dx == 0 && dy == -1))
                isR = true;
            for (int i = 0; i < 5; ++i) {
                x += dx;
                y += dy;
                if (x < 1 || x > BOARD_SIZE || y < 1 || y > BOARD_SIZE) {
                    break;
                }
                if (isR) {
                    sum = data[x][y].getSide() + sum;
                } else
                    sum = sum + data[x][y].getSide();
            }
            return sum;
        }

        public int level2Score(Level l1, Level l2, Level l3, Level l4) {
            int size = Level.values().length;
            int[] levelCount = new int[size];
            for (int i = 0; i < size; i++) {
                levelCount[i] = 0;
            }
            levelCount[l1.index]++;
            levelCount[l2.index]++;
            levelCount[l3.index]++;
            levelCount[l4.index]++;
            int score = 0;
            if (levelCount[Level.GO_4.index] >= 2
                    || levelCount[Level.GO_4.index] >= 1
                    && levelCount[Level.ALIVE_3.index] >= 1)// 双活4，冲4活三
                score = 10000;
            else if (levelCount[Level.ALIVE_3.index] >= 2)// 双活3
                score = 5000;
            else if (levelCount[Level.SLEEP_3.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// 活3眠3
                score = 1000;
            else if (levelCount[Level.ALIVE_2.index] >= 2)// 双活2
                score = 100;
            else if (levelCount[Level.SLEEP_2.index] >= 1
                    && levelCount[Level.ALIVE_2.index] >= 1)// 活2眠2
                score = 10;
            score = Math.max(
                    score,
                    Math.max(Math.max(l1.score, l2.score),
                            Math.max(l3.score, l4.score)));
            return score;
        }
    }
}
