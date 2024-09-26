package swapstones.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import puzzle.TwoPhaseMoveState;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PuzzleStateTest {

    private PuzzleState puzzleState;

    @BeforeEach
    public void setUp() {
        puzzleState = new PuzzleState();
    }

    @Test
    public void testInitialState() {
        int redCount = 0;
        int blackCount = 0;
        for (int i = 0; i < PuzzleState.BOARD_SIZE; i++) {
            Stone stone = puzzleState.getStone(i);
            if (stone == Stone.TAIL) {
                redCount++;
            } else if (stone == Stone.HEAD) {
                blackCount++;
            }
        }
        assertEquals(3, redCount, "There should be exactly 3 TAIL stones.");
        assertEquals(3, blackCount, "There should be exactly 3 HEAD stones.");
    }

    @Test
    public void testIsLegalToMoveFrom() {
        assertTrue(puzzleState.isLegalToMoveFrom(0), "Should be legal to move from position 0.");
        assertFalse(puzzleState.isLegalToMoveFrom(15), "Should not be legal to move from position 15.");
        assertFalse(puzzleState.isLegalToMoveFrom(-1), "Should not be legal to move from a negative position.");
    }

    @Test
    public void testIsLegalToMoveTo() {
        assertFalse(puzzleState.isLegalToMoveTo(0), "Should not be legal to move to position 0.");
        assertFalse(puzzleState.isLegalToMoveTo(15), "Should not be legal to move to position 15.");
        assertTrue(puzzleState.isLegalToMoveTo(10), "Should be legal to move to an empty position.");
    }

    @Test
    public void testIsLegalMove() {
        assertTrue(puzzleState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(0, 10)), "Should be a legal move.");
        assertFalse(puzzleState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(10, 0)), "Should not be a legal move.");
    }

    @Test
    public void testMakeMove() {
        TwoPhaseMoveState.TwoPhaseMove<Integer> move = new TwoPhaseMoveState.TwoPhaseMove<>(0, 10);
        puzzleState.makeMove(move);

        assertEquals(Stone.EMPTY, puzzleState.getStone(0), "Position 0 should be empty after move.");
        assertEquals(Stone.EMPTY, puzzleState.getStone(1), "Position 1 should be empty after move.");
        assertEquals(Stone.TAIL, puzzleState.getStone(10), "Position 10 should have TAIL after move.");
        assertEquals(Stone.HEAD, puzzleState.getStone(11), "Position 11 should have TAIL after move.");
    }

    @Test
    public void testIsSolved() {
        TwoPhaseMoveState.TwoPhaseMove<Integer> move = new TwoPhaseMoveState.TwoPhaseMove<>(3, 6);
        TwoPhaseMoveState.TwoPhaseMove<Integer> move1 = new TwoPhaseMoveState.TwoPhaseMove<>(5, 3);

        TwoPhaseMoveState.TwoPhaseMove<Integer> move2 = new TwoPhaseMoveState.TwoPhaseMove<>(1, 5);
        TwoPhaseMoveState.TwoPhaseMove<Integer> move3 = new TwoPhaseMoveState.TwoPhaseMove<>(6, 1);

        puzzleState.makeMove(move);
        puzzleState.makeMove(move1);
        puzzleState.makeMove(move2);
        assertFalse(puzzleState.isSolved(), "Should not be solved.");
        puzzleState.makeMove(move3);
        assertTrue(puzzleState.isSolved(), "Puzzle should be solved.");
    }


    @Test
    public void testGetLegalMoves() {
        Set<TwoPhaseMoveState.TwoPhaseMove<Integer>> legalMoves = puzzleState.getLegalMoves();
        assertNotNull(legalMoves, "Legal moves should not be null.");
        assertFalse(legalMoves.isEmpty(), "There should be some legal moves initially.");
    }

    @Test
    public void testEqualsAndHashCode() {
        PuzzleState puzzleState2 = new PuzzleState();
        assertEquals(puzzleState, puzzleState2, "Two initial puzzle states should be equal.");
        assertEquals(puzzleState.hashCode(), puzzleState2.hashCode(), "Hash codes should match.");
    }

    @Test
    public void testClone() {
        PuzzleState clonedState = (PuzzleState) puzzleState.clone();
        assertEquals(puzzleState, clonedState, "Cloned puzzle state should be equal to the original.");
        assertNotSame(puzzleState, clonedState, "Cloned puzzle state should not be the same instance as the original.");
    }
}
