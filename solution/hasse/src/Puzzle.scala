import scala.collection.mutable.Set
import scala.annotation.tailrec
import java.time._

case class Piece(val name: String, val sides: List[Int]) {
  lazy val rotations = List(this, this rotate 1, this rotate 2)

  private def rotate(count: Int) = {
    val shift = sides take count
    Piece(name, (sides diff shift) ++ shift)
  }
}

class Puzzle {

  def solve(allPieces: List[Piece]) = {
    val solutions: Set[List[Piece]] = Set.empty;
    this.placePieceRecursively(List(), allPieces, solutions)
    solutions
  }

  private def placePieceRecursively(currentState: List[Piece], unusedPieces: List[Piece],
                                    solutions: Set[List[Piece]]) {

    for (currentPiece <- unusedPieces;
        remainingPieces = unusedPieces diff Seq(currentPiece);
        rotation <- currentPiece.rotations;
        nextGuess = currentState :+ rotation;
        if nextGuess isValid) {

      remainingPieces match {
        case Nil => solutions += nextGuess
        case _ => placePieceRecursively(nextGuess, remainingPieces, solutions)
      }

    }
  }

  implicit class TriangleOrientationValidator(p: List[Piece]) {
    /* The triangle is built in the following order:
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
     * △: left=0, right=1, bottom=2, ▽: left=0, top=1, right=2 */
    def ⬇⬆(top: Int, bottom: Int) = empty(top, bottom) || p(top).sides(2) == -p(bottom).sides(1)
    def △▽(left: Int, right: Int) = empty(left, right) || p(left).sides(1) == -p(right).sides(0)
    def ▽△(left: Int, right: Int) = empty(left, right) || p(left).sides(2) == -p(right).sides(0)
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

  def main(args: Array[String]): Unit = {

    val (solutions, duration) = Timer.profile {
      val puzzle = new Puzzle()
      puzzle solve pieces
    }

    solutions foreach printSolution
    println(s"Completed in ${duration toMillis} ms")
  }

  private def printSolution(list: List[Piece]) =
    println("[" + reorganize(list).map(x => x.name).mkString(", ") + "]")

  /* Reorganizes the internal order used by the solver into a more human readable form */
  private def reorganize(in: List[Piece]) = List(
                  in(0),
           in(2), in(1), in(3),
    in(6), in(4), in(7), in(5), in(8))
}

// Helper object to run code while timing the execution time
object Timer {
  /** @return tuple with the result from the given block of code, and execution time in milliseconds */
  def profile[T](codeBlock: => T): (T, Duration) = {
    val start = Instant.now
    val result = codeBlock

    (result, Duration.between(start, Instant.now))
  }
}
