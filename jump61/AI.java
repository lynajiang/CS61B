
package jump61;

import java.util.ArrayList;
import java.util.Random;

import static jump61.Side.*;

/** An automated Player.
 *  @author P. N. Hilfinger
 */
class AI extends Player {

    /** A new player of GAME initially COLOR that chooses moves automatically.
     *  SEED provides a random-number seed used for choosing moves.
     */
    AI(Game game, Side color, long seed) {
        super(game, color);
        _random = new Random(seed);
    }

    @Override
    String getMove() {
        Board board = getGame().getBoard();

        assert getSide() == board.whoseMove();
        int choice = searchForMove();
        getGame().reportMove(board.row(choice), board.col(choice));
        return String.format("%d %d", board.row(choice), board.col(choice));
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private int searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert getSide() == work.whoseMove();
        _foundMove = -1;
        int depth = 5;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        if (getSide() == RED) {
            value = minMax(work, depth, true, 1, alpha, beta);
        } else {
            value = minMax(work, depth, true, -1, alpha, beta);
        }
        return _foundMove;
    }

    /**
     *
     * @param board given Board
     * @param sense given sense/player
     * @return ArrayList of validMoves
     */
    private ArrayList<Integer> validMoves(Board board, int sense) {
        ArrayList<Integer> validMoves = new ArrayList<>();
        Side player = (sense > 0) ? RED : BLUE;
        for (int i = 0; i < Math.pow(board.size(), 2); i++) {
            if (board.isLegal(player, i)) {
                validMoves.add(i);
            }
        }
        return validMoves;

    }
    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove,
                       int sense, int alpha, int beta) {
        Side player = (sense > 0) ? RED : BLUE;
        ArrayList<Integer> possibleMoves;
        if (depth == 0 || board.getWinner() != null) {
            return staticEval(board, sense);
        }
        if (sense == 1) {
            int maxEval = Integer.MIN_VALUE;
            possibleMoves = validMoves(board, sense);
            for (int legalMove : possibleMoves) {
                board.addSpot(player, legalMove);
                int response = minMax(board, depth - 1,
                        false, -1, alpha, beta);
                board.undo();
                maxEval = Math.max(maxEval, response);
                alpha = Math.max(alpha, maxEval);
                if (response >= maxEval && saveMove) {
                    _foundMove = legalMove;

                }
                if (alpha >= beta) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            possibleMoves = validMoves(board, sense);
            for (int legalMove: possibleMoves) {
                board.addSpot(player, legalMove);
                int response = minMax(board, depth - 1,
                        false, 1, alpha, beta);
                board.undo();
                minEval = Math.min(minEval, response);
                beta = Math.min(beta, minEval);
                if (response <= minEval && saveMove) {
                    _foundMove = legalMove;


                }

                if (alpha >= beta) {
                    break;
                }
            }
            return minEval;
        }

    }


    /** Return a heuristic estimate of the value of board position B.
     *  Use WINNINGVALUE to indicate a win for Red and -WINNINGVALUE to
     *  indicate a win for Blue. */
    private int staticEval(Board b, int winningValue) {
        Side player = (winningValue > 0) ? RED : BLUE;
        Side opponent = (-winningValue > 0) ? RED : BLUE;
        int playerPieces = 0;
        int opponentPieces = 0;
        if (b.getWinner() == RED) {
            return Integer.MAX_VALUE;
        }
        if (b.getWinner() == BLUE) {
            return Integer.MIN_VALUE;
        }
        for (int i = 0; i < Math.pow(b.size(), 2); i++) {
            if (b.get(i).getSide().equals(player)) {
                playerPieces += b.numOfSide(player);
            }
            if (b.get(i).getSide().equals(opponent)) {
                opponentPieces += b.numOfSide(opponent);
            }
        }
        return (playerPieces - opponentPieces) * winningValue;
    }

    /** A random-number generator used for move selection. */
    private Random _random;

    /** Used to convey moves discovered by minMax. */
    private int _foundMove;
}
