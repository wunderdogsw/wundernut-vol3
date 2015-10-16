case class Piece(val name: String, val sides: List[Int]) {
  def rotate(count: Int) = {
    val shift = sides take count
    Piece(name, (sides diff shift) ++ shift)
  }
}

class Puzzle {
  def solve(allPieces: List[Piece]) = this.placePieceRecursively(List(), allPieces, List())

  private def placePieceRecursively(usedPieces: List[Piece], unusedPieces: List[Piece],
      knownSolutions: List[List[Piece]]): List[List[Piece]] = {

    if (unusedPieces isEmpty) {
      return knownSolutions :+ usedPieces // No pieces to add, we found a solution
    }

    var solutions = knownSolutions
    for (currentPiece <- unusedPieces; i <- 0 to 2) {
      val nextGuess = usedPieces :+ (currentPiece rotate i)

      if (isValid(nextGuess)) {
        solutions = placePieceRecursively(nextGuess, unusedPieces diff Seq(currentPiece), solutions)
      }
    }
    return solutions
  }

  private def isValid(pieces: List[Piece]) = {
    def connectionAllowed(index1: Int, side1: Int, index2: Int, side2: Int): Boolean = {
      pieces.length <= Math.max(index1, index2) ||
      pieces(index1).sides(side1) == -pieces(index2).sides(side2)
    }

    connectionAllowed(0, 2, 1, 1) &&
    connectionAllowed(2, 1, 1, 0) &&
    connectionAllowed(3, 0, 1, 2) &&
    connectionAllowed(2, 2, 4, 1) &&
    connectionAllowed(3, 2, 5, 1) &&
    connectionAllowed(6, 1, 4, 0) &&
    connectionAllowed(7, 0, 4, 2) &&
    connectionAllowed(7, 1, 5, 0) &&
    connectionAllowed(5, 2, 8, 0)
  }
}

object Puzzle {
  val TL = 1
  val TH = -TL
  val GL = 2
  val GH = -GL
  val YL = 3
  val YH = -YL
  val puzzle = new Puzzle()

  def main(args: Array[String]): Unit = {

    val pieces = List(
      Piece("P1", List(GH, GL, YH)),
      Piece("P2", List(YH, GL, TL)),
      Piece("P3", List(GH, YH, GL)),
      Piece("P4", List(YL, GL, YH)),
      Piece("P5", List(TL, YL, YH)),
      Piece("P6", List(TL, GL, TH)),
      Piece("P7", List(GL, TH, GH)),
      Piece("P8", List(TL, TH, YH)),
      Piece("P9", List(GL, YL, YH))
    )

    puzzle solve pieces foreach printSolution
  }

  private def printSolution(list: List[Piece]) = println ("[" + reorganize(list).map(x => x.name).mkString(", ") + "]")

  /* Reorganizes the internal order used by the solver into a more human readable form */
  private def reorganize(in: List[Piece]) = List(
                  in(0),
           in(2), in(1), in(3), 
    in(6), in(4), in(7), in(5), in(8))
}
