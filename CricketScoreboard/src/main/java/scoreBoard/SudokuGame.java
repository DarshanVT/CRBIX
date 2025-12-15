package scoreBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SudokuGame extends JFrame {

    private JTextField[][] cells = new JTextField[9][9];

    private final int[][][] SUDOKU_TEMPLATES = {

        {
            {5,3,0,0,7,0,0,0,0},
            {6,0,0,1,9,5,0,0,0},
            {0,9,8,0,0,0,0,6,0},
            {8,0,0,0,6,0,0,0,3},
            {4,0,0,8,0,3,0,0,1},
            {7,0,0,0,2,0,0,0,6},
            {0,6,0,0,0,0,2,8,0},
            {0,0,0,4,1,9,0,0,5},
            {0,0,0,0,8,0,0,7,9}
        },
        {
            {0,0,0,2,6,0,7,0,1},
            {6,8,0,0,7,0,0,9,0},
            {1,9,0,0,0,4,5,0,0},
            {8,2,0,1,0,0,0,4,0},
            {0,0,4,6,0,2,9,0,0},
            {0,5,0,0,0,3,0,2,8},
            {0,0,9,3,0,0,0,7,4},
            {0,4,0,0,5,0,0,3,6},
            {7,0,3,0,1,8,0,0,0}
        },
        {
            {0,2,0,6,0,8,0,0,0},
            {5,8,0,0,0,9,7,0,0},
            {0,0,0,0,4,0,0,0,0},
            {3,7,0,0,0,0,5,0,0},
            {6,0,0,0,0,0,0,0,4},
            {0,0,8,0,0,0,0,1,3},
            {0,0,0,0,2,0,0,0,0},
            {0,0,9,8,0,0,0,3,6},
            {0,0,0,3,0,6,0,9,0}
        },
        {
            {0,0,0,0,0,0,2,0,0},
            {0,8,0,0,0,7,0,9,0},
            {6,0,2,0,0,0,5,0,0},
            {0,7,0,0,6,0,0,0,0},
            {0,0,0,9,0,1,0,0,0},
            {0,0,0,0,2,0,0,4,0},
            {0,0,5,0,0,0,6,0,3},
            {0,9,0,4,0,0,0,7,0},
            {0,0,6,0,0,0,0,0,0}
        },
        {
            {1,0,0,4,8,9,0,0,6},
            {7,3,0,0,0,0,0,4,0},
            {0,0,0,0,0,1,2,9,5},
            {0,0,7,1,2,0,6,0,0},
            {5,0,0,7,0,3,0,0,8},
            {0,0,6,0,9,5,7,0,0},
            {9,1,4,6,0,0,0,0,0},
            {0,2,0,0,0,0,0,3,7},
            {8,0,0,5,1,2,0,0,4}
        }
    };

    private int[][] puzzle;
    private int[][] solution;

    private JLabel timerLabel;
    private Timer timer;
    private int seconds = 0;

    public SudokuGame() {
        setTitle("Sudoku Game - Java Swing");
        setSize(550, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        generateRandomSudoku();

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Sudoku Game", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));

        timerLabel = new JLabel("Time: 0 sec", JLabel.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(2,1));
        topPanel.add(title);
        topPanel.add(timerLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(9,9));
        Font cellFont = new Font("Arial", Font.BOLD, 18);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {

                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(cellFont);

                int top = (r % 3 == 0) ? 3 : 1;
                int left = (c % 3 == 0) ? 3 : 1;
                cell.setBorder(BorderFactory.createMatteBorder(top, left, 1, 1, Color.BLACK));

                if (puzzle[r][c] != 0) {
                    cell.setText(String.valueOf(puzzle[r][c]));
                    cell.setEditable(false);
                    cell.setBackground(Color.LIGHT_GRAY);
                }

                cell.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (cell.getText().length() >= 1 ||
                                e.getKeyChar() < '1' || e.getKeyChar() > '9') {
                            e.consume();
                        }
                    }
                });

                cells[r][c] = cell;
                gridPanel.add(cell);
            }
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton checkBtn = new JButton("Check Result");
        JButton resetBtn = new JButton("Reset Game");
        JButton hintBtn = new JButton("Hint");

        checkBtn.addActionListener(e -> checkSolution());
        resetBtn.addActionListener(e -> resetGame());
        hintBtn.addActionListener(e -> giveHint());

        buttonPanel.add(checkBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(hintBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        startTimer();
    }

    private void generateRandomSudoku() {
        Random rand = new Random();
        puzzle = SUDOKU_TEMPLATES[rand.nextInt(SUDOKU_TEMPLATES.length)];
        solution = solve(copyGrid(puzzle));
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            seconds++;
            timerLabel.setText("Time: " + seconds + " sec");
        });
        timer.start();
    }

    private void giveHint() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (cells[r][c].getText().isEmpty()) {
                    cells[r][c].setText(String.valueOf(solution[r][c]));
                    return;
                }
            }
        }
    }

    private void checkSolution() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (!cells[r][c].getText().equals(String.valueOf(solution[r][c]))) {
                    JOptionPane.showMessageDialog(this, "Wrong Solution!");
                    return;
                }
            }
        }
        timer.stop();
        JOptionPane.showMessageDialog(this, "Solved in " + seconds + " seconds ");
    }

    private void resetGame() {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (puzzle[r][c] == 0)
                    cells[r][c].setText("");
    }

    private int[][] solve(int[][] board) {
        solveSudoku(board);
        return board;
    }

    private boolean solveSudoku(int[][] board) {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (board[r][c] == 0) {
                    for (int n = 1; n <= 9; n++) {
                        if (isSafe(board, r, c, n)) {
                            board[r][c] = n;
                            if (solveSudoku(board)) return true;
                            board[r][c] = 0;
                        }
                    }
                    return false;
                }
        return true;
    }

    private boolean isSafe(int[][] board, int r, int c, int n) {
        for (int i = 0; i < 9; i++)
            if (board[r][i] == n || board[i][c] == n)
                return false;

        int sr = (r / 3) * 3, sc = (c / 3) * 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[sr+i][sc+j] == n)
                    return false;

        return true;
    }

    private int[][] copyGrid(int[][] src) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            copy[i] = src[i].clone();
        return copy;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuGame().setVisible(true));
    }
}
