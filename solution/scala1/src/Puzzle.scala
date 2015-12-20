import scala.collection.mutable.Set
import java.time._

case class Piece(val name: String, val sides: List[Int]) {
  lazy val rotations = List(this, this rotate 1, this rotate 2)

  private def rotate(count: Int) = {
    val shift = sides take count
    Piece(name, (sides diff shift) ++ shift)
  }

  override def toString = name
}

/**
 * The "big" triangle that consists of smaller ones. A triangle may or may not be
 * complete, so a triangle object may contain any number of pieces. Triangles are
 * immutable and can only be built incrementally by inserting one piece at a time.
 * Each insertion creates a new triangle. The triangles are built in the  following
 * insertion order:
 *   △      0
 *  △▽△    213
 * △▽△▽△  64758
 */
case class Triangle(private val pieces: List[Piece] = List()) {
  def get(i: Int) = pieces lift i

  /** @return a new Triangle with the given piece placed in the next empty slot */
  def :+ (p: Piece) = Triangle(pieces :+ p)

  def isValid: Boolean = {
    /* Sides: △ { left=0, right=1, bottom=2 }, ▽ { left=0, top=1, right=2 } */
    def ⬇⬆(top: Int, bottom: Int) = piecesMatch(top, bottom, 2, 1)
    def △▽(left: Int, right: Int) = piecesMatch(left, right, 1, 0)
    def ▽△(left: Int, right: Int) = piecesMatch(left, right, 2, 0)

    def piecesMatch(pieceA: Int, pieceB: Int, sideA: Int, sideB: Int) =
      get(pieceA).isEmpty || get(pieceB).isEmpty || pieces(pieceA).sides(sideA) == -pieces(pieceB).sides(sideB)

    return ⬇⬆(0, 1) &&
      △▽(2, 1) && ▽△(1, 3) &&
      ⬇⬆(2, 4) && ⬇⬆(3, 5) &&
      △▽(6, 4) && ▽△(4, 7) && △▽(7, 5) && ▽△(5, 8)
  }

  /** Reorganizes the internally used order of pieces to the one used in the game specification */
  def sorted = List(  get(0),
              get(2), get(1), get(3),
      get(6), get(4), get(7), get(5), get(8)
    ).filter(_.isDefined).map(_.get)

    override def toString = "[" + sorted.mkString(", ") + "]"
}

object Solver {
  def solve(allPieces: List[Piece]) = placePieceRecursively(Triangle(), allPieces, Set empty)

  /**
   * Tries to put each of the unused pieces in each rotation to the next empty slot
   * in the triangle. If the piece fits, a recursive call is made to place the next
   * piece. When all pieces are added and they all fit, the formed triangle is stored
   * in the set of solutions, which is also returned at the end of the algorithm. */
  private def placePieceRecursively(triangle: Triangle, unusedPieces: List[Piece],
                                    solutions: Set[Triangle]): Set[Triangle] = {
    for (piece <- unusedPieces;
        remainingPieces = unusedPieces diff Seq(piece);
        rotatedPiece <- piece.rotations;
        nextGuess = triangle :+ rotatedPiece;
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
