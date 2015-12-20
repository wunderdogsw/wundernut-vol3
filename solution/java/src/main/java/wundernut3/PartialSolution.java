package wundernut3;

import java.util.HashMap;

import static wundernut3.BoardPosition.*;

public class PartialSolution extends HashMap<BoardPosition, Triangle> {
    public PartialSolution() {
        super();
    }

    public PartialSolution(PartialSolution partialSolutionToClone) {
        super(partialSolutionToClone);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof PartialSolution)) {
            return false;
        }

        PartialSolution otherPartial = (PartialSolution) other;
        if (!otherPartial.keySet().equals(this.keySet())) {
            return false;
        }
        for (BoardPosition pos : otherPartial.keySet()) {
            Triangle otherTriangle = otherPartial.get(pos);
            Triangle thisTriangle = this.get(pos);
            if (!otherTriangle.getName().equals(thisTriangle.getName())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(nullSafeValue(Pos1));
        sb.append(nullSafeValue(Pos2));
        sb.append(nullSafeValue(Pos3));
        sb.append(nullSafeValue(Pos4));
        sb.append(nullSafeValue(Pos5));
        sb.append(nullSafeValue(Pos6));
        sb.append(nullSafeValue(Pos7));
        sb.append(nullSafeValue(Pos8));
        sb.append(nullSafeValue(Pos9));
        return sb.toString().hashCode();
    }

    private String nullSafeValue(BoardPosition pos) {
        return get(pos) == null ? "--" : get(pos).getName();
    }
}
