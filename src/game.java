import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class game {
    public static void main(String[] args) {
        System.out.println("game beginning");
        Battle.init();
    }
}

//the main class witch man-machine battle take place here
class Battle {
    //get board info from Board class
    private static Board board;
    private static int userColor;
    private static int robotColor;
    private static Frame frame = new Frame("draw");
    //input chessboard
    private static MyBoard playArea;
    private static Robot robot;
    private static int playerWins = 0;
    private static int robotWins = 0;

    public static void init() {


        playArea = new MyBoard();
        board = playArea.getChess();
        userColor = board.WHITE;
        robotColor = board.BLACK;
        String value = (String) JOptionPane.showInputDialog(null, "Please select game difficulty", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"esay", "medium", "hard"}, "esay");

        if (null == value) {
            System.exit(-1);
        }
        robot = Robot.getRobot(value);

        board.move(board.N / 2 + 1, board.N / 2 + 1, board.BLACK);

        playArea.setPreferredSize(new Dimension(720, 720));

        playArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int i = (x - 60) / 40 + 1;
                int j = (y - 60) / 40 + 1;
                if (board.isEmpty(i, j)) {

                    board.move(i, j, userColor);
                    int rel = board.isEnd(i, j, userColor);
                    if (rel != 0) {
                        System.out.println("player wins!");
                        JOptionPane.showMessageDialog(null, "you win");
                        playerWins++;
                        frame.removeAll();
                        init();
                        return;
                    }
                    playArea.repaint();
                    int rob[] = robot.getNext(robotColor);
                    board.move(rob[0], rob[1], robotColor);
                    rel = board.isEnd(rob[0], rob[1], robotColor);
                    if (rel != 0) {
                        System.out.println("robot wins");
                        JOptionPane.showMessageDialog(null, "robot wins");
                        robotWins++;
                        frame.removeAll();
                        init();
                        return;
                    }
                    playArea.repaint();
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
        drawBackground();


    }

    //draw the Background
    private static void drawBackground() {
        Label label = new Label();
        label.setText("you wins:" + playerWins + " " + "robot wins:" + robotWins);
        frame.add(label, BorderLayout.EAST);
        frame.add(playArea);
        frame.setSize(900, 800);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(-1);
            }
        });
        Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();
        frame.setLocation((int) (screenWidth - frame.getWidth()) / 2, (int) (screenHeight - frame.getHeight()) / 2);
    }
}

//this class describes how to draw a chessboard
class MyBoard extends Canvas {
    //input board info
    private Board board = Board.getInstance();
    private final int N = board.N + 2;
    //the distance of each horizontal or vertical line
    private final int S = 40;
    private final int X = S;
    private final int Y = S;
    private final int length = (N - 1) * S;

    MyBoard() {
        board.init();
    }

    public Board getChess() {
        return board;
    }

    public void drawPiece(int color, int x, int y, Graphics g) {
        if (color == board.BLACK) {
            g.setColor(new Color(0, 0, 0));
            g.fillArc(X + x * S - 19, Y + y * S - 19, 38, 38, 0, 360);
        } else if (color == board.WHITE) {
            g.setColor(new Color(255, 255, 255));
            g.fillArc(X + x * S - 19, Y + y * S - 19, 38, 38, 0, 360);
        }
    }

    public void paint(Graphics g) {

        g.setColor(new Color(0, 0, 0));
        g.fillRect(X - 8, Y - 8, X + (N - 2) * S + 15, Y + (N - 2) * S + 15);
        g.setColor(new Color(139, 255, 71));
        g.fillRect(X - 4, Y - 4, X + (N - 2) * S + 7, Y + (N - 2) * S + 7);

        g.setColor(new Color(0, 0, 0));

        g.fillArc(X + 8 * S - 6, Y + 8 * S - 6, 12, 12, 0, 360);
        g.fillArc(X + 4 * S - 6, Y + 4 * S - 6, 12, 12, 0, 360);
        g.fillArc(X + 4 * S - 6, Y + 12 * S - 6, 12, 12, 0, 360);
        g.fillArc(X + 12 * S - 6, Y + 4 * S - 6, 12, 12, 0, 360);
        g.fillArc(X + 12 * S - 6, Y + 12 * S - 6, 12, 12, 0, 360);

        for (int i = 0; i < N; i++) {

            g.drawLine(X + i * S, Y, X + i * S, Y + length);
            g.drawLine(X, Y + i * S, X + length, Y + i * S);
        }

        for (int i = 1; i <= board.N; i++) {
            for (int j = 1; j <= board.N; j++) {
                drawPiece(board.board[i][j], i, j, g);
            }
        }
    }
}

//this class used to store all chessboard infomation
class Board {
    public final int N = 15;
    public final int EMPTY = 0;
    public final int BLACK = 1;
    public final int WHITE = 2;

    public int[][] board;

    public void init() {
        board = new int[N + 1][N + 1];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                left.put(i + "," + j, new int[]{i, j});
            }
        }
    }

    public Map<String, int[]> left = new HashMap<>();

    private Board() {

    }

    //get all the empty positions as a map ---key:"x,y" value:[x,y]
    public Map<String, int[]> getLeft() {
        return left;
    }

    private static Board chessBoard = new Board();

    public static Board getInstance() {
        return chessBoard;
    }

    public boolean isEmpty(int x, int y) {
        return board[x][y] == EMPTY;
    }

    public void move(int x, int y, int color) {
        board[x][y] = color;
        left.remove(x + "," + y);
    }

    public void unMove(int x, int y) {
        board[x][y] = EMPTY;
        left.put(x + "," + y, new int[]{x, y});
    }


    public int isEnd(int x, int y, int color) {
        int dx[] = {1, 0, 1, 1};
        int dy[] = {0, 1, 1, -1};

        for (int i = 0; i < 4; i++) {
            int sum = 1;

            int tx = x + dx[i];
            int ty = y + dy[i];
            while (tx > 0 && tx <= N && ty > 0 && ty <= N && board[tx][ty] == color) {
                tx += dx[i];
                ty += dy[i];
                ++sum;
            }

            tx = x - dx[i];
            ty = y - dy[i];
            while (tx > 0 && tx <= N && ty > 0 && ty <= N && board[tx][ty] == color) {
                tx -= dx[i];
                ty -= dy[i];
                ++sum;
            }

            if (sum >= 5) {
                return color;
            }
        }
        return 0;
    }
}

//Easy model
class Easy implements Robot {

    private static Board board = Board.getInstance();

    public Easy() {

    }

    @Override
    public int[] getNext(int color) {
        Map<String, int[]> left = board.getLeft();
        Set<String> strings = left.keySet();
        int i = new Random().nextInt(left.size());
        String s = new ArrayList<>(strings).get(i);
        int[] ints = left.get(s);
        left.remove(s);
        return ints;
    }
}

//Medium model
class Medium implements Robot {

    private static Board chessBoard = Board.getInstance();

    public Medium() {

    }

    @Override
    public int[] getNext(int color) {
        int[] result = new int[2];
        int max = 0;
        for (int x = 1; x <= chessBoard.N; x++) {
            for (int y = 1; y <= chessBoard.N; y++) {
                if (!chessBoard.isEmpty(x, y)) {
                    continue;
                }

                int maxR = getMax(x, y, chessBoard.BLACK);
                int maxP = getMax(x, y, chessBoard.WHITE);

                if (maxR > max) {
                    result[0] = x;
                    result[1] = y;
                    max = maxR;
                }
                if (maxP > max && maxP > maxR) {
                    result[0] = x;
                    result[1] = y;
                    max = maxP;
                }

            }
        }
        return result;
    }

    private int getMax(int x, int y, int color) {
        int dx[] = {1, 0, 1, 1};
        int dy[] = {0, 1, 1, -1};

        int max = 0;
        for (int i = 0; i < 4; i++) {
            int temp = getMaxByPosition(x, y, color, dx[i], dy[i]);
            if (temp > max) {
                max = temp;
            }
        }
        return max;
    }

    private int getMaxByPosition(int x, int y, int color, int dx, int dy) {
        int max = 0;
        int tempx = x, tempy = y;
        for (int i = 0; i < 4; i++) {
            if (max >= 4) {
                break;
            }
            x += dx;
            y += dy;

            System.out.println(x);
            System.out.println(y);
            if (x <= 0 || x > 15 || y <= 0 || y > 15) {
                break;
            }

            if (chessBoard.board[x][y] != color) {
                break;
            }

            max++;
        }

        x = tempx;
        y = tempy;
        for (int i = 0; i < 4; i++) {
            if (max >= 4) {
                break;
            }

            x += (-dx);
            y += (-dy);

            if (x <= 0 || x > 15 || y <= 0 || y > 15) {
                break;
            }

            if (chessBoard.board[x][y] != color) {
                break;
            }

            max++;
        }
        return max;
    }
}

//Hard model
class Hard implements Robot {

    private static Board chess = Board.getInstance();

    public Hard() {
    }

    private static int robotColor = chess.BLACK;

    public int cutBranchFind(int depth, int min, int max, int color, int prex, int prey) {

        if (depth >= depth || 0 != chess.isEnd(prex, prey, color % 2 + 1)) {

            int score = estimate(robotColor) - estimate(robotColor % 2 + 1);

            if (depth % 2 != 0) {
                return score;
            }
            score = -score;

            return score;
        }

        //iterate every position than get the highest action score
        for (int x = 1; x <= chess.N; x++) {
            for (int y = 1; y <= chess.N; y++) {

                if (!chess.isEmpty(x, y)) {
                    continue;
                }

                chess.move(x, y, color);
                int val = -cutBranchFind(depth + 1, -max, -min, color % 2 + 1, x, y);

                chess.unMove(x, y);

                if (val >= max) {
                    return max;
                }

                if (val > min) {
                    min = val;
                }
            }
        }
        return min;
    }

    public int[] getNext(int color) {
        int rel[] = new int[2];
        int ans = Integer.MIN_VALUE;

        Random random = new Random();

        for (int x = 1; x <= chess.N; x++) {
            for (int y = 1; y <= chess.N; y++) {

                if (!chess.isEmpty(x, y)) {
                    continue;
                }

                chess.move(x, y, color);

                int val = -cutBranchFind(0, Integer.MIN_VALUE, Integer.MAX_VALUE, color % 2 + 1, x, y);

                int ra = random.nextInt(100);
                if (val > ans || val == ans && ra >= 50) {
                    ans = val;
                    rel[0] = x;
                    rel[1] = y;
                }
                chess.unMove(x, y);
            }
        }
        return rel;
    }


    public int estimate(int color) {

        int dx[] = {1, 0, 1, 1};
        int dy[] = {0, 1, 1, -1};
        int ans = 0;

        for (int x = 1; x <= chess.N; x++) {
            for (int y = 1; y <= chess.N; y++) {
                if (chess.board[x][y] != color) {
                    continue;
                }

                int num[][] = new int[2][100];

                for (int i = 0; i < 4; i++) {
                    int sum = 1;
                    int flag1 = 0, flag2 = 0;

                    int tx = x + dx[i];
                    int ty = y + dy[i];
                    while (tx > 0 && tx <= chess.N && ty > 0 && ty <= chess.N && chess.board[tx][ty] == color) {
                        tx += dx[i];
                        ty += dy[i];
                        ++sum;
                    }

                    if (tx > 0 && tx <= chess.N && ty > 0 && ty <= chess.N && chess.board[tx][ty] == chess.EMPTY) {
                        flag1 = 1;
                    }

                    tx = x - dx[i];
                    ty = y - dy[i];
                    while (tx > 0 && tx <= chess.N && ty > 0 && ty <= chess.N && chess.board[tx][ty] == color) {
                        tx -= dx[i];
                        ty -= dy[i];
                        ++sum;
                    }

                    if (tx > 0 && tx <= chess.N && ty > 0 && ty <= chess.N && chess.board[tx][ty] == chess.EMPTY) {
                        flag2 = 1;
                    }

                    if (flag1 + flag2 > 0) {
                        ++num[flag1 + flag2 - 1][sum];
                    }
                }

                if (num[0][5] + num[1][5] > 0) {
                    ans = Math.max(ans, 100000);
                } else if (num[1][4] > 0 || num[0][4] > 1 || (num[0][4] > 0 && num[1][3] > 0)) {
                    ans = Math.max(ans, 10000);
                } else if (num[1][3] > 1) {
                    ans = Math.max(ans, 5000);
                } else if (num[1][3] > 0 && num[0][3] > 0) {
                    ans = Math.max(ans, 1000);
                } else if (num[0][4] > 0) {
                    ans = Math.max(ans, 500);
                } else if (num[1][3] > 0) {
                    ans = Math.max(ans, 200);
                } else if (num[1][2] > 1) {
                    ans = Math.max(ans, 100);
                } else if (num[0][3] > 0) {
                    ans = Math.max(ans, 50);
                } else if (num[1][2] > 1) {
                    ans = Math.max(ans, 10);
                } else if (num[1][2] > 0) {
                    ans = Math.max(ans, 5);
                } else if (num[0][2] > 0) {
                    ans = Math.max(ans, 1);
                }

            }
        }

        return ans;
    }
}

//the interface is used to choose model according to the user's choice
interface Robot {

    //all three diffculty implement the interface and overwrite the method,
    //return different strategy depend on difficult
    int[] getNext(int color);

    static Robot getRobot(String difficult) {
        if ("esay".equals(difficult)) {
            return new Easy();
        } else if ("medium".equals(difficult)) {
            return new Medium();
        } else if ("hard".equals(difficult)) {
            return new Hard();
        }
        return null;
    }
}