package com.pawelmandera.duplicates

import org.specs2.mutable._

class MinHashCandidatesSpec extends Specification {
  val mhc = new MinHashCandidates {
    def candidates[A](xs: Map[A, Sketch], ys: Map[A, Sketch]): Iterator[Set[A]] = Iterator[Set[A]]()
  }

  "The MinHashCandidates" should {
    "filter distinct elements from an iterator" in {
      val elems = (0 to 50).toList
      val xs = for { _ <- (1 to 70).toList } yield scala.util.Random.shuffle(elems).head
      mhc.distinctIter(xs.iterator).toList must containTheSameElementsAs(xs.distinct)
    }
  }
}
