// This file contains definitions for an OPTIONAL part of your project.  If you
// choose not to do the optional point, you can delete this file from your
// project.

// This file contains a SUGGESTION for the structure of your program.  You
// may change any of it, or add additional files to this directory (package),
// as long as you conform to the project specification.

// Comments that start with "//" are intended to be removed from your
// solutions.
package jump61;

import ucb.gui2.Pad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import java.util.concurrent.ArrayBlockingQueue;

import static jump61.Side.*;

/** A GUI component that displays a Jump61 board, and converts mouse clicks
 *  on that board to commands that are sent to the current Game.
 *  @author Lyna Jiang
 */
class BoardWidget extends Pad {

    /** Length of the side of one square in pixels. */
    private static final int SQUARE_SIZE = 50;
    /** Width and height of a spot. */
    private static final int SPOT_DIM = 8;
    /** Minimum separation of center of a spot from a side of a square. */
    private static final int SPOT_MARGIN = 10;
    /** Width of the bars separating squares in pixels. */
    private static final int SEPARATOR_SIZE = 3;
    /** Width of square plus one separator. */
    private static final int SQUARE_SEP = SQUARE_SIZE + SEPARATOR_SIZE;

    /** Colors of various parts of the displayed board. */
    private static final Color
        NEUTRAL = Color.WHITE,
        SEPARATOR_COLOR = Color.BLACK,
        SPOT_COLOR = Color.BLACK,
        RED_TINT = new Color(255, 200, 200),
        BLUE_TINT = new Color(200, 200, 255);

    /** A new BoardWidget that monitors and displays a game Board, and
     *  converts mouse clicks to commands to COMMANDQUEUE. */
    BoardWidget(ArrayBlockingQueue<String> commandQueue) {
        _commandQueue = commandQueue;
        _side = 6 * SQUARE_SEP + SEPARATOR_SIZE;
        setMouseHandler("click", this::doClick);
    }

    /* .update and .paintComponent are synchronized because they are called
     *  by three different threads (the main thread, the thread that
     *  responds to events, and the display thread).  We don't want the
     *  saved copy of our Board to change while it is being displayed. */

    /** Update my display to show BOARD.  Here, we save a copy of
     *  BOARD (so that we can deal with changes to it only when we are ready
     *  for them), and recompute the size of the displayed board. */
    synchronized void update(Board board) {
        if (board.equals(_board)) {
            return;
        }
        if (_board != null && _board.size() != board.size()) {
            invalidate();
        }
        _board = new Board(board);
        _side = _board.size() * SQUARE_SEP + SEPARATOR_SIZE;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(_side, _side);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(_side, _side);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(_side, _side);
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        if (_board == null) {
            return;
        }

        g.setColor(NEUTRAL);
        g.fillRect(0, 0, _side, _side);
        g.setColor(SEPARATOR_COLOR);
        for (int k = 0; k <= _side; k += SQUARE_SEP) {
            g.fillRect(0, k, _side, SEPARATOR_SIZE);
            g.fillRect(k, 0, SEPARATOR_SIZE, _side);
        }

        for (int i = 1; i <= _board.size(); i++) {
            for (int j = 1; j <= _board.size(); j++) {
                displaySpots(g, i, j);
            }
        }


//        g.drawLine(50, 50, 50, 50);
//        g.setColor(SEPARATOR_COLOR);
//
//
//        for (int i = 1; i <= _board.size(); i++) {
//            for (int j = 1; j <= _board.size(); j++) {
//
//                g.drawRect((i - 1) * length, (j - 1) * length, SQUARE_SIZE, SQUARE_SIZE);
//
//                displaySpots(g, i, j);
//            }
//
//        }
//
    }

    /** Color and display the spots on the square at row R and column C
     *  on G.  (Used by paintComponent). */
    private void displaySpots(Graphics2D g, int r, int c) {
        // FIXME
        if (_board.get(r, c) == null) {
            return;
        }

        int x = SEPARATOR_SIZE + (r - 1)  * SQUARE_SEP;
        int y = SEPARATOR_SIZE + (c - 1)  * SQUARE_SEP;
        int n = (c - 1) + (r - 1) * _board.size();

        if (_board.exists(r, c)) {
            Side color = _board.get(r, c).getSide();
            int numSpots = _board.get(r, c).getSpots();
            Color colorOfSquare;
            if (color.equals(RED)) {
                colorOfSquare = RED_TINT;
            } else if (color.equals(BLUE)) {
                colorOfSquare = BLUE_TINT;
            } else {
                colorOfSquare = NEUTRAL;
            }

            g.setColor(colorOfSquare);
            g.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);

            switch (numSpots) {
                case 2:
                    spot(g, x + 12, y + 12 * 2);
                    spot(g, x + 12 * 3, y + 12 * 2);
                    break;
                case 3:
                    spot(g, x + 12, y + 12);
                    spot(g, x + 12 * 2, y + 12 * 2);
                    spot(g, x + 12 * 3, y + 12 * 3);
                    break;
                case 4:
                    spot(g, x + 12, y + 12);
                    spot(g, x + 12, y + 12 * 3);
                    spot(g, x + 12 * 3, y + 12);
                    spot(g, x + 12 * 3, y + 12 * 3);
                    break;
                case 5:
                    spot(g, x + 12, y + 12);
                    spot(g, x + 12, y + 12 * 3);
                    spot(g, x + 12 * 2, y + 12 * 2);
                    spot(g, x + 12 * 3, y + 12);
                    spot(g, x + 12 * 3, y + 12 * 3);
                    break;
                default:
                    spot(g, x + 12 * 2, y + 12 * 2);
                    break;
            }

        
    


        
            
        }



    }


    /** Draw one spot centered at position (X, Y) on G. */
    private void spot(Graphics2D g, int x, int y) {
        g.setColor(SPOT_COLOR);
        g.fillOval(x - SPOT_DIM / 2, y - SPOT_DIM / 2, SPOT_DIM, SPOT_DIM);
    }

    /** Respond to the mouse click depicted by EVENT. */
    public void doClick(String dummy, MouseEvent event) {
        // x and y coordinates relative to the upper-left corner of the
        // upper-left square (increasing y is down).
        int x = event.getX() - SEPARATOR_SIZE,
            y = event.getY() - SEPARATOR_SIZE;
        // FIXME:
        // REPLACE THE FOLLOWING WITH COMPUTATION OF r AND c FROM x and y,
        // AND SEND THE APPROPRIATE COMMAND TO OUR GAME, IF THE EVENT
        // OCCURS AT A VALID POSITION.  OTHERWISE, DOES NOTHING.
        int r = ((x - SEPARATOR_SIZE) / SQUARE_SEP) + 1;
        int c = ((y - SEPARATOR_SIZE) / SQUARE_SEP) + 1;
        _commandQueue.offer(String.format("%d %d", r, c));


    }

    /** The Board I am displaying. */
    private Board _board;
    /** Dimension in pixels of one side of the board. */
    private int _side;
    /** Destination for commands derived from mouse clicks. */
    private ArrayBlockingQueue<String> _commandQueue;
}
