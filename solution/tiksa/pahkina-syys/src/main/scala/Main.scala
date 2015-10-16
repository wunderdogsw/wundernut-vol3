object Main {

  case class RotatedPart(edges: Array[String], angle: Int)
  case class Neighbor(slotIndex: Int, angle: Int)
  case class Slot(rotatedPart: Option[RotatedPart], neighbors: Seq[Neighbor])

  val parts: Seq[Array[String]] = Seq(
    Array("YH", "GH", "GL"),
    Array("TL", "YH", "GL"),
    Array("GH", "YH", "GL"),
    Array("GL", "YH", "YL"),
    Array("YL", "YH", "TL"),
    Array("TH", "TL", "GL"),
    Array("GH", "GL", "TH"),
    Array("TL", "TH", "YH"),
    Array("YH", "GL", "YL")
  )

  val rotatedParts: Seq[RotatedPart] = parts flatMap ((edges: Array[String]) => (0 to 2) map (new RotatedPart(edges, _)))

  def emptySlot(neighbors: Seq[Neighbor]): Slot = Slot(None, neighbors)

  val slots: Seq[Slot] = Seq(
    emptySlot(Seq()),
    emptySlot(Seq(Neighbor(0, 0))),
    emptySlot(Seq(Neighbor(0, 2))),
    emptySlot(Seq(Neighbor(0, 1))),
    emptySlot(Seq(Neighbor(2, 0))),
    emptySlot(Seq(Neighbor(4, 1))),
    emptySlot(Seq(Neighbor(5, 2), Neighbor(3, 0))),
    emptySlot(Seq(Neighbor(4, 2))),
    emptySlot(Seq(Neighbor(6, 1)))
  )

  def isValid(slots: Seq[Slot], allSlots: Seq[Slot]): Boolean = {
    if (slots.isEmpty)
      true
    else {
      val slot = slots.head
      slot.neighbors forall((n: Neighbor) => matches(slot.rotatedPart.get, allSlots(n.slotIndex).rotatedPart.get, n.angle))
    }
  }

  def toSolutionOrder(slots: Seq[Slot]): Seq[Slot] = Seq(slots(1), slots(2), slots(0), slots(3), slots(7), slots(4), slots(5), slots(6), slots(8))

  def slotToSolutionFormat(slot: Slot): String = "P" + (parts.indexOf(slot.rotatedPart.get.edges) + 1)

  def stringsToTriangle(s: Seq[String]): String = "      " + s(0) + "\n   " + s(1) + " " + s(2) + " " + s(3) + "\n" + s(4) + " " + s(5) + " " + s(6) + " " + s(7) + " " + s(8)

  def matches(r1: RotatedPart, r2: RotatedPart, angle: Int): Boolean = {
    val edge1 = r1.edges((angle + (3 - r1.angle)) % 3)
    val edge2 = r2.edges((angle + (3 - r2.angle)) % 3)

    edge1.head == edge2.head && edge1.last != edge2.last
  }

  def slotsFull(slots: Seq[Slot]): Boolean = slots.forall(_.rotatedPart.isDefined)

  def filterNonEmptySlots(slots: Seq[Slot]): Seq[Slot] = slots filter (_.rotatedPart.isDefined)

  def checkIfUnique(solutions: Seq[Seq[Slot]], candidate: Seq[Slot]): Boolean = {
    if (solutions isEmpty)
      true
    else
      solutions forall{(s: Seq[Slot]) =>
        val edgeLists = s map (_.rotatedPart.get.edges)
        val candStr = candidate map (_.rotatedPart.get.edges) map (_ mkString ",")
        def compareSymmetric(indices: Int*): Boolean = candStr equals (indices map (edgeLists(_)) map (_ mkString ","))
        !(compareSymmetric(4, 7, 5, 2, 6, 3, 0, 8, 1) ||
          compareSymmetric(6, 8, 3, 5, 0, 2, 4, 1, 7))
      }
  }

  def solve(slots: Seq[Slot], available: Seq[RotatedPart], solutions: Seq[Seq[Slot]] = Seq()): Seq[Seq[Slot]] = {
    if (!isValid(filterNonEmptySlots(slots) reverse, slots)) {
      Nil
    } else {
      if (slotsFull(slots)) {
        solutions :+ slots
      } else {
        val currentSlot = slots find ((s: Slot) => s.rotatedPart.isEmpty) get

        solutions ++ available.flatMap{(r: RotatedPart) =>
          val newSlots: Seq[Slot] = slots map ((s: Slot) =>
            if (s.equals(currentSlot)) {
                new Slot(Some(r), s.neighbors)
              } else {
                s
              })
          solve(newSlots, available filter (!_.edges.sameElements(r.edges)))
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    type Solution = Seq[Slot]
    val solutions = solve(slots, rotatedParts, Seq[Solution]())

    val uniques = solutions.foldLeft[Seq[Solution]](Seq[Solution]()){(memo: Seq[Solution], current: Solution) =>
      if (checkIfUnique(memo, current))
        memo :+ current
      else
        memo
    }

    for (s <- uniques)
      println(stringsToTriangle(toSolutionOrder(s) map slotToSolutionFormat) + "\n")
  }
}