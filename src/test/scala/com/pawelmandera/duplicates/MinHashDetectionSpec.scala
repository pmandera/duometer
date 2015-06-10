package com.pawelmandera.duplicates

import scala.util.Try

import org.specs2.mutable._

import com.pawelmandera.hash.ElementHashes

class MinHashDetectionSpec extends Specification {
  val mhd = new MinHashDetection with SharedMemberCandidates {}

  def jaccard[A](s1: Set[A], s2: Set[A]) = (s1 & s2).size.toDouble/(s1 ++ s2).size

  implicit object SymbolSetElementHashes extends ElementHashes[Set[Symbol]] {
    def hashes(x: Set[Symbol]) = Try {
      val xs = x.toSeq map { _.toString }
      val hs = xs map { _.hashCode.toLong }
      hs.toSet
    }
  }

  "The MinHashDetection" should {
    "calculate an approximation of jaccard coefficient" in {
      val s1 = Set('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i)
      val s2 = Set('a, 'b, 'c, 'u, 'v, 'w, 'x, 'y, 'z)
      val elemsA = Set(s1, s2)
      val duplicates = mhd.detect(elemsA, elemsA, 500, 1).toList
      duplicates.length must_== 1
      duplicates(0)._3 must be ~(jaccard(s1, s2) +/- 0.05)
    }

    "find pairs of duplicates" in {
      val s1 = Set('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i)
      val s2 = Set('a, 'b, 'c, 'u, 'v, 'w, 'x, 'y, 'z)
      val s3 = Set('t, 'y, 'h, 'u, 'v, 'w, 'x, 'y, 'z)
      val elemsA = Set(s1, s2, s3)
      val duplicates = mhd.detect(elemsA, elemsA, 500, 1).toList filter { _._3 > 0.1 }
      duplicates.length must_== 2
    }
  }
}
