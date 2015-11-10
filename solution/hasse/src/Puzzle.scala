import scala.collection.mutable.Set
import java.time._

case class Piece(val name: String, val sides: List[Int]) {
  lazy val rotations = List(this, this rotate 1, this rotate 2)

  private def rotate(count: Int) = {
    val shift = sides take count
    Piece(name, (sides diff shift) ++ shift)
  }
}

/**
 * Represents the triangle that consists of smaller pieces in the game. The triangle
 * may or may not be complete, so a triangle may contain between 0 and 9 pieces.
 * The triangle may only be built incrementally by inserting one piece at a time,
 * in the following insertion order:
 *   △      0
 *  △▽△    213
 * △▽△▽△  64758
 */
case class Triangle(private val pieces: List[Piece] = List()) {
  /** @return a new Triangle with the given piece placed in the next empty slot */
  def :+ (p: Piece) = Triangle(pieces :+ p)

  def isValid: Boolean = {
    /* Sides: △ { left=0, right=1, bottom=2 }, ▽ { left=0, top=1, right=2 } */
    def ⬇⬆(top: Int, bottom: Int) = piecesMatch(top, bottom, 2, 1)
    def △▽(left: Int, right: Int) = piecesMatch(left, right, 1, 0)
    def ▽△(left: Int, right: Int) = piecesMatch(left, right, 2, 0)

    def piecesMatch(pieceA: Int, pieceB: Int, sideA: Int, sideB: Int) =
      pieces.size <= Math.max(pieceA, pieceB) || pieces(pieceA).sides(sideA) == -pieces(pieceB).sides(sideB)

    return ⬇⬆(0, 1) &&
      △▽(2, 1) && ▽△(1, 3) &&
      ⬇⬆(2, 4) && ⬇⬆(3, 5) &&
      △▽(6, 4) && ▽△(4, 7) && △▽(7, 5) && ▽△(5, 8)
  }

  override def toString = "[" + sorted.map(_.name).mkString(", ") + "]"

  /* Reorganizes the internal order used by the solver into a more human readable form */
  def sorted = List(      pieces(0),
               pieces(2), pieces(1), pieces(3),
    pieces(6), pieces(4), pieces(7), pieces(5), pieces(8))
}

object Solver {
  def solve(allPieces: List[Piece]) = placePieceRecursively(Triangle(), allPieces, Set empty)

  private def placePieceRecursively(current: Triangle, unusedPieces: List[Piece],
                                    solutions: Set[Triangle]): Set[Triangle] = {
    for (currentPiece <- unusedPieces;
        remainingPieces = unusedPieces diff Seq(currentPiece);
        rotation <- currentPiece.rotations;
        nextGuess = current :+ rotation;
        if nextGuess isValid) {

      remainingPieces match {
        case Nil => solutions += nextGuess
        case _   => placePieceRecursively(nextGuess, remainingPieces, solutions)
      }
    }
    solutions
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
      Solver solve pieces
    }

    solutions foreach println
    println(s"Completed in ${duration toMillis} ms")
  }
}

object Timer {
  /** @return the result from the given block of code and the Duration that it took to execute */
  def profile[T](codeBlock: => T): (T, Duration) = {
    val start = Instant now
    val result = codeBlock

    (result, Duration between(start, Instant now))
  }
}
