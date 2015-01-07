package com.pawelmandera.duplicates

trait MinHashCandidates {
  /** Returns an iterator over Sets of possible duplicate pairs. */
  def candidates[A](xs: Map[A, Sketch], ys: Map[A, Sketch]): Iterator[Set[A]]

  /** Returns an iterator including only unique elements. */
  def distinctIter[A](iter: Iterator[A]): Iterator[A] = {
    val produced = scala.collection.mutable.Set[Int]()
    for {
      e <- iter
      eHash = e.hashCode
      if !produced(eHash)
      _ = produced += eHash
    } yield e
  }
}
