package com.pawelmandera.duplicates

trait SharedMemberCandidates extends MinHashCandidates {
  /** Returns an iterator with duplicate candidates.
    *
    * The iterator contains all pairs whose sketches have
    * at least one member in common.
    *
    * @param xs the first sketches map
    * @param ys the second sketches map
    * @return an iterator with two-element sets of unique pairs
    */
  def candidates[A](xs: Map[A, Sketch], ys: Map[A, Sketch]): Iterator[Set[A]] = {
    def groupAtIndex(xs: Map[A, Sketch], i: Int): Map[Long, Iterable[A]] =
      xs.keys.groupBy { e => xs(e)(i) }

    val sketchSize = xs.head._2.length

    val elems = for {
      i <- (0 until sketchSize).iterator
      xsGrouped = groupAtIndex(xs, i)
      ysGrouped = groupAtIndex(ys, i)
      (xi, xsElems) <- xsGrouped
      ysElem <- ysGrouped.getOrElse(xi, Iterable.empty)
      xsElem <- xsElems
      if (ysElem != xsElem)
    } yield Set(xsElem, ysElem)

    distinctIter(elems)
  }
}
