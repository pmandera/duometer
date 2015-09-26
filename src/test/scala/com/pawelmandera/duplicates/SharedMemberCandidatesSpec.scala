package com.pawelmandera.duplicates

import scala.util.Try
import org.specs2.mutable._

import com.pawelmandera.hash.ElementHashes

class SharedMemberCandidatesSpec extends Specification {
  val smc = new SharedMemberCandidates {}

  implicit object SymbolSetElementHashes extends ElementHashes[Set[Symbol]] {
    def hashes(x: Set[Symbol]) = Try {
      val xs = x.toSeq map { _.toString }
      val hs = xs map { _.hashCode.toLong }
      hs.toSet
    }
  }

  "The SharedMemberCandidates" should {
    "generate candidate duplicates based on overlap in sketches for one set" in {
      val elemsA = Map(
        'a -> Vector(1L, 2L, 3L),
        'b -> Vector(1L, 2L, 4L))
      val candidates = smc.candidates(elemsA, elemsA).toList
      candidates.length must_== 1
    }

    "generate candidate duplicates based on overlap in sketches for two sets" in {
      val elemsA = Map(
        'a -> Vector(1L, 2L, 3L),
        'b -> Vector(1L, 2L, 4L),
        'c -> Vector(4L, 5L, 6L))

      val elemsB = Map(
        'x -> Vector(2L, 5L, 3L),
        'y -> Vector(1L, 2L, 8L))

      val candidates = smc.candidates(elemsA, elemsB).toList

      candidates must containTheSameElementsAs(
        Seq(Set('a, 'x), Set('a, 'y), Set('b, 'y), Set('c, 'x))
      )
    }
  }
}
