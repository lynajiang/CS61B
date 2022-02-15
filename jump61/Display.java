
package jump61;

import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;

import java.awt.*;
import java.util.concurrent.ArrayBlockingQueue;

import static jump61.Side.*;

/** The GUI controller for jump61.  To require minimal change to textual
 *  interface, we adopt the strategy of converting GUI input (mouse clicks)
 *  into textual commands that are sent to the Game object through a
 *  a Writer.  The Game object need never know where its input is coming from.
 *  A Display is an Observer of Games and Boards so that it is notified when
 *  either changes.
 *  @author Lyna Jiang
 */
class Display extends TopLevel implements View, CommandSource, Reporter {

    /** A new window with given TITLE displaying GAME, and using COMMANDWRITER
     *  to send commands to the current game. */
    Display(String title) {
        super(title, true);

        addMenuButton("Game->Quit", this::quit);
        addMenuButton("Game->New Game", this::newGame);
        addMenuButton("Auto->Auto", this::auto);
        addMenuButton("Size->2 x 2", this::two);
        addMenuButton("Size->3 x 3", this::three);
        addMenuButton("Size->4 x 4", this::four);
        addMenuButton("Size->5 x 5", this::five);
        addMenuButton("Size->6 x 6", this::six);
        addMenuButton("Size->7 x 7", this::seven);
        addMenuButton("Size->8 x 8", this::eight);
        addMenuButton("Size->9 x 9", this::nine);
        addMenuButton("Size->10 x 10", this::ten);
        addMenuButton("Color->Red", this::red);
        addMenuButton("Color->Blue", this::blue);

        // FIXME: More needed

        _boardWidget = new BoardWidget(_commandQueue);
        add(_boardWidget, new LayoutSpec("y", 1, "width", 2));
        display(true);
    }

    /** Response to "Quit" button click. */
    void quit(String dummy) {
        System.exit(0);
    }

    /** Response to "New Game" button click. */
    void newGame(String dummy) {
        _commandQueue.offer("new");
    }
    // FIXME

    void auto(String dummy) {
        _commandQueue.offer("auto red");
        _commandQueue.offer("auto blue");
    }
    void red(String dummy) {
        _commandQueue.offer("manual red");
        _commandQueue.offer("auto blue");
    }

    void blue(String dummy) {
        _commandQueue.offer("manual blue");
        _commandQueue.offer("auto red");
    }
    void two(String dummy) {
        _commandQueue.offer("size 2");
        newGame(dummy);
    }

    void three(String dummy) {
        _commandQueue.offer("size 3");
        newGame(dummy);
    }
    void four(String dummy) {
        _commandQueue.offer("size 4");
        newGame(dummy);
    }
    void five(String dummy) {
        _commandQueue.offer("size 5");
        newGame(dummy);
    }
    void six(String dummy) {
        _commandQueue.offer("size 6");
        newGame(dummy);
    }
    void seven(String dummy) {
        _commandQueue.offer("size 7");
        newGame(dummy);
    }
    void eight(String dummy) {
        _commandQueue.offer("size 8");
        newGame(dummy);
    }
    void nine(String dummy) {
        _commandQueue.offer("size 9");
        newGame(dummy);
    }
    void ten(String dummy) {
        _commandQueue.offer("size 10");
        newGame(dummy);
    }
    @Override
    public void update(Board board) {
        board = new Board(board);
        _boardWidget.update(board);
        pack();
        _boardWidget.repaint();
    }

    @Override
    public String getCommand(String ignored) {
        try {
            return _commandQueue.take();
        } catch (InterruptedException excp) {
            throw new Error("unexpected interrupt");
        }
    }

    @Override
    public void announceWin(Side side) {
        showMessage(String.format("%s wins!", side.toCapitalizedString()),
                    "Game Over", "information");
    }

    @Override
    public void announceMove(int row, int col) {
    }

    @Override
    public void msg(String format, Object... args) {
        showMessage(String.format(format, args), "", "information");
    }

    @Override
    public void err(String format, Object... args) {
        showMessage(String.format(format, args), "Error", "error");
    }

    /** Time interval in msec to wait after a board update. */
    static final long BOARD_UPDATE_INTERVAL = 50;

    /** The widget that displays the actual playing board. */
    private BoardWidget _boardWidget;
    /** Queue for commands going to the controlling Game. */
    private final ArrayBlockingQueue<String> _commandQueue =
        new ArrayBlockingQueue<>(5);
}
