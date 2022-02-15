
package jump61;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Stack;
import java.util.function.Consumer;

import static jump61.Side.BLUE;
import static jump61.Side.RED;
import static jump61.Side.WHITE;
import static jump61.Square.INITIAL;

/** Represents the state of a Jump61 game.  Squares are indexed either by
 *  row and column (between 1 and size()), or by square number, numbering
 *  squares by rows, with squares in row 1 numbered from 0 to size()-1, in
 *  row 2 numbered from size() to 2*size() - 1, etc. (i.e., row-major order).
 *
 *  A Board may be given a notifier---a Consumer<Board> whose
 *  .accept method is called whenever the Board's contents are changed.
 *
 *  @author Lyna Jiang
 */
class Board {

    /** An uninitialized Board.  Only for use by subtypes. */
    protected Board() {
        _notifier = NOP;
    }

    /** An N x N board in initial configuration. */
    Board(int N) {
        this();
        _dim = N;
        _moves = 0;
        _board = new ArrayList<>();
        for (int i = 0; i < Math.pow(_dim, 2); i++) {
            _board.add(INITIAL);
        }
        gameStates = new Stack<>();


    }

    /** A board whose initial contents are copied from BOARD0, but whose
     *  undo history is clear, and whose notifier does nothing. */
    Board(Board board0) {
        this(board0.size());
        for (int i = 0; i < Math.pow(board0.size(), 2); i++) {
            _board.set(i, board0.get(i));
        }
        this._moves = board0._moves;
        this.gameStates = new Stack<>();
        _readonlyBoard = new ConstantBoard(this);
    }

    /** Returns a readonly version of this board. */
    Board readonlyBoard() {
        return _readonlyBoard;
    }

    /** (Re)initialize me to a cleared board with N squares on a side. Clears
     *  the undo history and sets the number of moves to 0. */
    void clear(int N) {
        gameStates = new Stack<>();
        _moves = 0;
        _dim = N;
        _board = new ArrayList<>();
        for (int i = 0; i < Math.pow(_dim, 2); i++) {
            _board.add(INITIAL);
        }

        announce();
    }

    /** Copy the contents of BOARD into me.
     * Clear the history and number of moves. */
    void copy(Board board) {
        internalCopy(board);
        gameStates = new Stack<>();
        _moves = 0;

    }

    /** Copy the contents of BOARD into me, without modifying my undo
     *  history. Assumes BOARD and I have the same size. */
    private void internalCopy(Board board) {
        assert size() == board.size();
        this._dim = board._dim;
        this._moves = board._moves;
        for (int i = 0; i < Math.pow(board.size(), 2); i++) {
            this._board.set(i, board.get(i));
        }

    }

    /** Return the number of rows and of columns of THIS. */
    int size() {
        return _dim;
    }

    /** Returns the contents of the square at row R, column C
     *  1 <= R, C <= size (). */
    Square get(int r, int c) {
        return get(sqNum(r, c));
    }

    /** Returns the contents of square #N, numbering squares by rows, with
     *  squares in row 1 number 0 - size()-1, in row 2 numbered
     *  size() - 2*size() - 1, etc. */
    Square get(int n) {
        if (exists(n)) {
            return _board.get(n);
        }
        return null;
    }

    /** Returns the total number of spots on the board. */
    int numPieces() {
        int totalSpots = 0;
        for (int i = 0; i < Math.pow(size(), 2); i++) {
            totalSpots += get(i).getSpots();
        }

        return totalSpots;

    }
    /** Returns the Side of the player who would be next to move.  If the
     *  game is won, this will return the loser (assuming legal position). */
    Side whoseMove() {
        return ((numPieces() + size()) & 1) == 0 ? RED : BLUE;
    }

    /** Return true iff row R and column C denotes a valid square. */
    final boolean exists(int r, int c) {
        return 1 <= r && r <= size() && 1 <= c && c <= size();
    }

    /** Return true iff S is a valid square number. */
    final boolean exists(int s) {
        int N = size();
        return 0 <= s && s < N * N;
    }

    /** Return the row number for square #N. */
    final int row(int n) {
        return n / size() + 1;
    }

    /** Return the column number for square #N. */
    final int col(int n) {
        return n % size() + 1;
    }

    /** Return the square number of row R, column C. */
    final int sqNum(int r, int c) {
        return (c - 1) + (r - 1) * size();
    }

    /** Return a string denoting move (ROW, COL)N. */
    String moveString(int row, int col) {
        return String.format("%d %d", row, col);
    }

    /** Return a string denoting move N. */
    String moveString(int n) {
        return String.format("%d %d", row(n), col(n));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
        to square at row R, column C. */
    boolean isLegal(Side player, int r, int c) {
        return isLegal(player, sqNum(r, c));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
     *  to square #N. */
    boolean isLegal(Side player, int n) {
        if (isLegal(player) && exists(n)) {
            if (get(n).getSide().equals(WHITE)
                    || get(n).getSide().equals(player)) {
                return true;
            }
        }
        return false;
    }

    /** Returns true iff PLAYER is allowed to move at this point. */
    boolean isLegal(Side player) {
        return (getWinner() == null);
    }

    /** Returns the winner of the current position, if the game is over,
     *  and otherwise null. */
    final Side getWinner() {
        Side firstSide = get(0).getSide();
        if (firstSide.toString().equals("white")) {
            return null;
        }
        for (int i = 1; i < Math.pow(_dim, 2); i++) {
            if (!(get(i).getSide()).equals(firstSide)) {
                return null;
            }
        }

        return firstSide;
    }

    /** Return the number of squares of given SIDE. */
    int numOfSide(Side side) {
        int totalSquares = 0;
        for (int i = 0; i < Math.pow(_dim, 2); i++) {
            if ((get(i).getSide()).equals(side)) {
                totalSquares += 1;
            }
        }
        return totalSquares;
    }

    /** Add a spot from PLAYER at row R, column C.  Assumes
     *  isLegal(PLAYER, R, C). */
    void addSpot(Side player, int r, int c) {
        addSpot(player, sqNum(r, c));


    }

    /** Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N). */
    void addSpot(Side player, int n) {
        if (isLegal(player, n)) {
            Board newBoard = new Board(this._dim);
            for (int i = 0; i < this._board.size(); i++) {
                newBoard._board.set(i, this._board.get(i));
            }
            gameStates.add(newBoard);
            _moves += 1;
            if (get(n).getSpots() < _MAXSPOTS) {
                internalSet(n, get(n).getSpots() + 1, player);
                jump(n);
            }
        }

    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white). */
    void set(int r, int c, int num, Side player) {
        internalSet(r, c, num, player);
        announce();
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white).  Does not announce
     *  changes. */
    private void internalSet(int r, int c, int num, Side player) {
        internalSet(sqNum(r, c), num, player);
    }

    /** Set the square #N to NUM spots (0 <= NUM), and give it color PLAYER
     *  if NUM > 0 (otherwise, white). Does not announce changes. */
    private void internalSet(int n, int num, Side player) {
        if (num > 0) {
            _board.set(n, Square.square(player, num));
        }

    }


    /** Undo the effects of one move (that is, one addSpot command).  One
     *  can only undo back to the last point at which the undo history
     *  was cleared, or the construction of this Board. */
    void undo() {
        internalCopy(gameStates.pop());

    }

    /** Record the beginning of a move in the undo history. */
    private void markUndo() {


    }

    /** Add DELTASPOTS spots of side PLAYER to row R, column C,
     *  updating counts of numbers of squares of each color. */
    private void simpleAdd(Side player, int r, int c, int deltaSpots) {
        internalSet(r, c, deltaSpots + get(r, c).getSpots(), player);
    }

    /** Add DELTASPOTS spots of color PLAYER to square #N,
     *  updating counts of numbers of squares of each color. */
    private void simpleAdd(Side player, int n, int deltaSpots) {
        internalSet(n, deltaSpots + get(n).getSpots(), player);
    }

    /** Used in jump to keep track of squares needing processing.  Allocated
     *  here to cut down on allocations. */
    private final ArrayDeque<Integer> _workQueue = new ArrayDeque<>();

    /** Do all jumping on this board, assuming that initially, S is the only
     *  square that might be over-full. */
    private void jump(int S) {
        /**
         * Need to check S exists?
         * Check that it over-full numSpots > numNeighbors
         * recursive call through to get all jumps with one initial
         */
        int[][] neighbors = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        if (getWinner() == null && exists(S)) {
            if (get(S).getSpots() > neighbors(S)) {
                simpleAdd(get(S).getSide(), S, -neighbors(S));
                for (int i = 0; i < neighbors.length; i++) {
                    if (getWinner() == null) {
                        if (row(S) + neighbors[i][0] > 0
                                && col(S) + neighbors[i][1] > 0) {
                            if (row(S) + neighbors[i][0] < _dim + 1
                                    && col(S) + neighbors[i][1] < _dim + 1) {
                                int sqN = sqNum(row(S) + neighbors[i][0],
                                        col(S) + neighbors[i][1]);
                                if (get(sqN) != null
                                        && getWinner() == null) {
                                    simpleAdd(get(S).getSide(), sqN, 1);
                                    jump(sqN);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /** Returns my dumped representation. */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        String color;
        out.format("===" + System.lineSeparator());
        for (int i = 0; i <= _board.size() - 1; i++) {
            if (i % _dim == 0) {
                out.format("    ");
            }
            if (String.valueOf(_board.get(i).getSide()).equals("blue")) {
                color = "b";
            } else if (String.valueOf(_board.get(i).getSide()).
                    equals("red")) {
                color = "r";
            } else {
                color = "-";
            }

            out.format("%d", _board.get(i).getSpots());
            out.format(color);
            out.format(" ");
            if ((i + 1) % _dim == 0 && ((i + 1) != (Math.pow(_dim, 2)))) {
                out.format(System.lineSeparator());
            }
        }

        out.format(System.lineSeparator());
        out.format("===");
        if (getWinner() != null) {
            out.format(System.lineSeparator());
            out.format("* ");
            out.format(getWinner().toString().substring(0, 1).toUpperCase());
            out.format(getWinner().toString().substring(1));
            out.format(" ");
            out.format("wins.");
        }
        return out.toString();
    }

    /** Returns an external rendition of me, suitable for human-readable
     *  textual display, with row and column numbers.  This is distinct
     *  from the dumped representation (returned by toString). */
    public String toDisplayString() {
        String[] lines = toString().trim().split("\\R");
        Formatter out = new Formatter();
        for (int i = 1; i + 1 < lines.length; i += 1) {
            out.format("%2d %s%n", i, lines[i].trim());
        }
        out.format("  ");
        for (int i = 1; i <= size(); i += 1) {
            out.format("%3d", i);
        }
        return out.toString();
    }

    /** Returns the number of neighbors of the square at row R, column C. */
    int neighbors(int r, int c) {
        int size = size();
        int n;
        n = 0;
        if (r > 1) {
            n += 1;
        }
        if (c > 1) {
            n += 1;
        }
        if (r < size) {
            n += 1;
        }
        if (c < size) {
            n += 1;
        }
        return n;
    }

    /** Returns the number of neighbors of square #N. */
    int neighbors(int n) {
        return neighbors(row(n), col(n));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Board)) {
            return false;
        } else {
            Board B = (Board) obj;
            return this == obj;
        }
    }

    @Override
    public int hashCode() {
        return numPieces();
    }

    /** Set my notifier to NOTIFY. */
    public void setNotifier(Consumer<Board> notify) {
        _notifier = notify;
        announce();
    }

    /** Take any action that has been set for a change in my state. */
    private void announce() {
        _notifier.accept(this);
    }

    /** A notifier that does nothing. */
    private static final Consumer<Board> NOP = (s) -> { };

    /** A read-only version of this Board. */
    private ConstantBoard _readonlyBoard;

    /** Use _notifier.accept(B) to announce changes to this board. */
    private Consumer<Board> _notifier;

    /** Number of rows/columns on board. */
    private int _dim;

    /** Keeps track of number of moves. */
    private int _moves;

    /** Max number of spots. */
    private final int _MAXSPOTS = 5;

    /** Stored board. */
    private ArrayList<Square> _board;

    /** Save previous game states. */
    private Stack<Board> gameStates;



}
