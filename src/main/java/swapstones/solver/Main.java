package swapstones.solver;

import puzzle.TwoPhaseMoveState;
import puzzle.solver.BreadthFirstSearch;
import swapstones.model.PuzzleState;

public class Main {

    public static void main(String[] args) {
        var bfs = new BreadthFirstSearch<TwoPhaseMoveState.TwoPhaseMove<Integer>>();
        bfs.solveAndPrintSolution(new PuzzleState());
    }
}
