package com.pawelmandera.duplicates

trait SuperShingleCandidates extends MinHashCandidates {
  /** How many shingles a super-shingle should contain */
  def size: Int

  /** Calculates supershingle hashes from a Sketch. */
  def superShingle(xs: Sketch, size: Int = 2): Seq[Int] =
    xs.sorted.sliding(size).toSeq map { _.hashCode }

  /** Returns a map with super-shingles as keys and a sets of elements
    * that contain this shingle as values.
    */
  def groupBySuperShingle[A](xs: Map[A, Sketch], size: Int): Map[Int, Set[A]] = {
    def addElems(keys: Iterable[Int], elem: A, acc: Map[Int, Set[A]]): Map[Int, Set[A]] =
      keys.foldLeft(acc) { (res, e) =>
        val newVal = res.getOrElse(e, Set.empty[A]) + elem
        res + (e -> newVal)
      }

    xs.foldLeft(Map.empty[Int, Set[A]]) { (acc, e) =>
      val (key, sketch) = e
      addElems(superShingle(sketch, size), key, acc)
    }
  }

  /** Returns candidates based on super-shingles of a given size. */
  def candidates[A](xs: Map[A, Sketch], ys: Map[A, Sketch]): Iterator[Set[A]] = {
    val xsSuperGroup = groupBySuperShingle(xs, size)
    val ysSuperGroup = if (xs == ys) xsSuperGroup else groupBySuperShingle(ys, size)

    val elems = for {
      (superShingle, xsElems) <- xsSuperGroup.iterator
      ysElem <- ysSuperGroup.getOrElse(superShingle, Iterable.empty)
      xsElem <- xsElems
      if (ysElem != xsElem)
    } yield Set(xsElem, ysElem)

    distinctIter(elems)
  }
}
