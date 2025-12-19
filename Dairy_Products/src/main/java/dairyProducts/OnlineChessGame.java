package dairyProducts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OnlineChessGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessFrame::new);
    }
}

class ChessFrame extends JFrame {
    ChessFrame() {
        setTitle("Chess Game ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(new ChessPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class ChessPanel extends JPanel implements MouseListener {

    private final int TILE = 80;
    private Piece[][] board = new Piece[8][8];
    private int selR = -1, selC = -1;
    private boolean whiteTurn = true;

    ChessPanel() {
        setPreferredSize(new Dimension(TILE * 8, TILE * 8 + 40));
        addMouseListener(this);
        setupBoard();
    }

    private void setupBoard() {
        PieceType[] back = {
                PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP,
                PieceType.QUEEN, PieceType.KING,
                PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
        };

        for (int i = 0; i < 8; i++) {
            board[6][i] = new Piece(PieceType.PAWN, true);
            board[1][i] = new Piece(PieceType.PAWN, false);
            board[7][i] = new Piece(back[i], true);
            board[0][i] = new Piece(back[i], false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                g.setColor((r + c) % 2 == 0 ? Color.LIGHT_GRAY : Color.GRAY);
                g.fillRect(c * TILE, r * TILE, TILE, TILE);

                if (board[r][c] != null)
                    board[r][c].draw(g, c * TILE, r * TILE, TILE);
            }
        }
    }

    private boolean clearPath(int sr, int sc, int dr, int dc) {
        int rStep = Integer.compare(dr, sr);
        int cStep = Integer.compare(dc, sc);
        sr += rStep;
        sc += cStep;

        while (sr != dr || sc != dc) {
            if (board[sr][sc] != null) return false;
            sr += rStep;
            sc += cStep;
        }
        return true;
    }

    private boolean isLegalMove(int sr, int sc, int dr, int dc, boolean test) {
        Piece p = board[sr][sc];
        if (p == null) return false;
        if (board[dr][dc] != null && board[dr][dc].white == p.white)
            return false;

        int dx = Math.abs(dc - sc);
        int dy = Math.abs(dr - sr);

        boolean ok = false;

        switch (p.type) {
            case ROOK -> ok = (dx == 0 || dy == 0) && clearPath(sr, sc, dr, dc);
            case BISHOP -> ok = dx == dy && clearPath(sr, sc, dr, dc);
            case QUEEN -> ok = (dx == dy || dx == 0 || dy == 0) && clearPath(sr, sc, dr, dc);
            case KNIGHT -> ok = (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
            case KING -> {
                ok = dx <= 1 && dy <= 1;
                if (!p.moved && dy == 0 && dx == 2) ok = canCastle(sr, sc, dc);
            }
            case PAWN -> {
                int dir = p.white ? -1 : 1;
                if (sc == dc && board[dr][dc] == null && dr - sr == dir)
                    ok = true;
                if (dx == 1 && dr - sr == dir &&
                        board[dr][dc] != null &&
                        board[dr][dc].white != p.white)
                    ok = true;
            }
        }

        if (!ok) return false;
        if (test) return true;

        Piece temp = board[dr][dc];
        board[dr][dc] = p;
        board[sr][sc] = null;
        boolean inCheck = isKingInCheck(p.white);
        board[sr][sc] = p;
        board[dr][dc] = temp;

        return !inCheck;
    }

   private boolean isKingInCheck(boolean white) {
        int kr = -1, kc = -1;

        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (board[r][c] != null &&
                        board[r][c].type == PieceType.KING &&
                        board[r][c].white == white) {
                    kr = r;
                    kc = c;
                }

        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (board[r][c] != null &&
                        board[r][c].white != white &&
                        isLegalMove(r, c, kr, kc, true))
                    return true;

        return false;
    }

    private boolean isCheckmate(boolean white) {
        if (!isKingInCheck(white)) return false;

        for (int sr = 0; sr < 8; sr++)
            for (int sc = 0; sc < 8; sc++)
                if (board[sr][sc] != null && board[sr][sc].white == white)
                    for (int dr = 0; dr < 8; dr++)
                        for (int dc = 0; dc < 8; dc++)
                            if (isLegalMove(sr, sc, dr, dc, false))
                                return false;

        return true;
    }

    private boolean canCastle(int r, int kc, int dc) {
        Piece king = board[r][kc];
        int rookCol = dc > kc ? 7 : 0;
        Piece rook = board[r][rookCol];

        if (rook == null || rook.moved) return false;
        if (!clearPath(r, kc, r, rookCol)) return false;
        if (isKingInCheck(king.white)) return false;

        return true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int c = e.getX() / TILE;
        int r = e.getY() / TILE;
        if (r > 7) return;

        if (selR == -1) {
            if (board[r][c] != null && board[r][c].white == whiteTurn) {
                selR = r;
                selC = c;
            }
        } else {
            if (isLegalMove(selR, selC, r, c, false)) {
                Piece p = board[selR][selC];

                // Castling move rook
                if (p.type == PieceType.KING && Math.abs(c - selC) == 2) {
                    int rookFrom = c > selC ? 7 : 0;
                    int rookTo = c > selC ? c - 1 : c + 1;
                    board[r][rookTo] = board[r][rookFrom];
                    board[r][rookFrom] = null;
                }

                board[r][c] = p;
                board[selR][selC] = null;
                p.moved = true;

                // Promotion
                if (p.type == PieceType.PAWN && (r == 0 || r == 7))
                    board[r][c] = new Piece(PieceType.QUEEN, p.white);

                whiteTurn = !whiteTurn;

                if (isCheckmate(whiteTurn))
                    JOptionPane.showMessageDialog(this, "CHECKMATE!");
            }

            selR = selC = -1;
        }
        repaint();
    }

    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}


enum PieceType { KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN }

class Piece {
    PieceType type;
    boolean white;
    boolean moved = false;

    Piece(PieceType t, boolean w) {
        type = t;
        white = w;
    }

    void draw(Graphics g, int x, int y, int s) {
        g.setColor(white ? Color.WHITE : Color.BLACK);
        g.fillOval(x + 6, y + 6, s - 12, s - 12);
        g.setColor(Color.RED);
        g.drawString(type.name().substring(0,1),
                x + s/2 - 4, y + s/2 + 4);
    }
}
