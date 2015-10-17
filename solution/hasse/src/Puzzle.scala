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

      if (nextGuess isValid) {
        solutions = placePieceRecursively(nextGuess, unusedPieces diff Seq(currentPiece), solutions)
      }
    }
    return solutions
  }

  implicit class TriangleOrientationValidator(p: List[Piece]) {
    /* Positions:
     *   △      0
     *  △▽△    213
     * △▽△▽△  64758
     */
    def isValid = 
      ⬇⬆(0, 1) &&
      △▽(2, 1) && ▽△(1, 3) &&
      ⬇⬆(2, 4) && ⬇⬆(3, 5) &&
      △▽(6, 4) && ▽△(4, 7) && △▽(7, 5) && ▽△(5, 8)

    /* Sides in each orientation:
     * △: left=0, right=1, bottom=2
     * ▽: left=0, top=1, right=2
     */
    def ⬇⬆(top: Int, bottom: Int) = empty(top, bottom) || p(top).sides(2) + p(bottom).sides(1) == 0
    def △▽(left: Int, right: Int) = empty(left, right) || p(left).sides(1) + p(right).sides(0) == 0
    def ▽△(left: Int, right: Int) = empty(left, right) || p(left).sides(2) + p(right).sides(0) == 0
    def empty(a: Int, b: Int) = p.size <= Math.max(a, b)
  }
}

object Puzzle {
  val TL = 1
  val TH = -TL
  val GL = 2
  val GH = -GL
  val YL = 3
  val YH = -YL

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

    val puzzle = new Puzzle()
    puzzle solve pieces foreach printSolution
  }

  private def printSolution(list: List[Piece]) =
    println("[" + reorganize(list).map(x => x.name).mkString(", ") + "]")

  /* Reorganizes the internal order used by the solver into a more human readable form */
  private def reorganize(in: List[Piece]) = List(
                  in(0),
           in(2), in(1), in(3), 
    in(6), in(4), in(7), in(5), in(8))
}
