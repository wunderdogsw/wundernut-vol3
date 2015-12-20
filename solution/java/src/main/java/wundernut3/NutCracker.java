package wundernut3;

import java.util.*;

import wundernut3.Board.NeighborLink;

import static wundernut3.BoardPosition.*;

public class NutCracker {
    private static final List<Solution> ALREADY_VISITED = new ArrayList<>();
    private final Board board = new Board();
    private Set<PartialSolution> partialSolutionsTried = new HashSet<PartialSolution>();

    private List<Solution> findSolutionsWithStartPiece(BoardPosition position, Triangle start) {
        partialSolutionsTried.clear();

        PartialSolution partialSolution = new PartialSolution();
        partialSolution.put(position, start);

        List<Triangle> freePieces = new ArrayList<>(Pieces.ALL_PIECES);
        freePieces.remove(Pieces.getNormalizedMatch(start));
        return findSolutionsWithPartialSolution(partialSolution, freePieces, Arrays.asList(new BoardPosition[]{position}), false);
    }

    private List<Solution> findSolutionsWithPartialSolution(PartialSolution partialSolution, final List<Triangle> freePieces, List<BoardPosition> lastFilledPositions, boolean backingUp) {
        // if backing up in search space, the partial solution exists already but we must keep searching from another position, so we'll skip the duplicate check
        if (!backingUp && partialSolutionsTried.contains(partialSolution)) {
            return ALREADY_VISITED;
        }

        partialSolutionsTried.add(partialSolution);

        if (partialSolution.size() == 9) {
            return Arrays.asList(convertPartialSolutionToSolution(partialSolution));
        }


        List<Solution> solutions = new ArrayList<>();
        List<NeighborLink> neighbors = board.getNeighbors(lastFilledPositions.get(0));
        boolean neighborsAlreadyFilled = true;
        for (NeighborLink link : neighbors) {
            if (isNeighborSlotEmpty(partialSolution, link)) {
                neighborsAlreadyFilled = false;
                for (Triangle triangle : freePieces) {
                    if (matchesWithAllNeighbors(triangle, link.neighbor, partialSolution)) {
                        solutions.addAll(findSolutionsWithNewPartialSolution(triangle, partialSolution, freePieces, link.neighbor, lastFilledPositions));
                    }
                    Triangle rotatedOnce = triangle.rotate();
                    if (matchesWithAllNeighbors(rotatedOnce, link.neighbor, partialSolution)) {
                        solutions.addAll(findSolutionsWithNewPartialSolution(rotatedOnce, partialSolution, freePieces, link.neighbor, lastFilledPositions));
                    }
                    Triangle rotatedTwice = rotatedOnce.rotate();
                    if (matchesWithAllNeighbors(rotatedTwice, link.neighbor, partialSolution)) {
                        solutions.addAll(findSolutionsWithNewPartialSolution(rotatedTwice, partialSolution, freePieces, link.neighbor, lastFilledPositions));
                    }
                }
            }
        }
        if (neighborsAlreadyFilled) {
            // there's no mismatch error, we've just reached a dead end and need to back up a step
            return findSolutionsWithPartialSolution(partialSolution, freePieces, lastFilledPositions.subList(1, lastFilledPositions.size()), true);
        }

        return solutions;
    }

    private boolean matchesWithAllNeighbors(Triangle triangle, BoardPosition position, PartialSolution partialSolution) {
        for (NeighborLink link : board.getNeighbors(position)) {
            if (!isNeighborSlotEmpty(partialSolution, link)) {
                if (!triangle.getEdge(link.direction).matches(partialSolution.get(link.neighbor).getEdge(link.direction.getOpposite()))) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<Solution> findSolutionsWithNewPartialSolution(Triangle triangle, PartialSolution partialSolution, List<Triangle> freePieces, BoardPosition lastFilledPosition, List<BoardPosition> previousFilledPositions) {
        PartialSolution newPartialSolution = new PartialSolution(partialSolution);
        newPartialSolution.put(lastFilledPosition, triangle);

        List<Triangle> newFreePieces = new ArrayList<>(freePieces);
        newFreePieces.remove(Pieces.getNormalizedMatch(triangle));

        List<BoardPosition> filledPositions = new ArrayList<>(previousFilledPositions);
        filledPositions.add(0, lastFilledPosition);
        return findSolutionsWithPartialSolution(newPartialSolution, newFreePieces, filledPositions, false);
    }

    private boolean isNeighborSlotEmpty(PartialSolution partialSolution, NeighborLink link) {
        return partialSolution.get(link.neighbor) == null;
    }

    private Solution convertPartialSolutionToSolution(Map<BoardPosition, Triangle> partialSolution) {
        return new Solution(
                partialSolution.get(Pos1),
                partialSolution.get(Pos2),
                partialSolution.get(Pos3),
                partialSolution.get(Pos4),
                partialSolution.get(Pos5),
                partialSolution.get(Pos6),
                partialSolution.get(Pos7),
                partialSolution.get(Pos8),
                partialSolution.get(Pos9));
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        NutCracker cracker = new NutCracker();
        List<Solution> solutions = new ArrayList<>();
        // see README for the reason why only these start pieces are used
        solutions.addAll(cracker.findSolutionsWithStartPiece(Pos2, Pieces.P7));
        solutions.addAll(cracker.findSolutionsWithStartPiece(Pos3, Pieces.P7));
        solutions.addAll(cracker.findSolutionsWithStartPiece(Pos3, Pieces.P7.rotate()));
        solutions.addAll(cracker.findSolutionsWithStartPiece(Pos3, Pieces.P7.rotate().rotate()));

        Set<Solution> allSolutions = new HashSet<>();
        for (Solution solution : solutions) {
            allSolutions.add(solution);
            allSolutions.addAll(solution.getRotatedVersions());
        }

        long stopTime = System.currentTimeMillis();

        System.out.println(String.format("Found the following %d solutions in %d ms.", allSolutions.size(), (stopTime - startTime)));
        for (Solution solution : allSolutions) {
            System.out.println(solution);
        }
    }
}
