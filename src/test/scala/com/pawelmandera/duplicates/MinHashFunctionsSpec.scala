package com.pawelmandera.duplicates

import org.specs2.mutable._

class MinHashFunctionsSpec extends Specification {
  val mhf = new MinHashFunctions {}

  "The MinHashFunctions" should {
    "return random hash functions" in {
      val hashFunc = mhf.hashFunctions(2, 1)
      hashFunc(0) must_!= hashFunc(1)
    }

    "calculate minhash" in {
      val pseudoHashFunc = (i: Long) => i - 100
      val s = (107L to 200).toSet
      mhf.minhash(pseudoHashFunc, s) must_== 7
    }

    "similarity function return simalarity value" in {
      val l1 = ((1L to 5).toList ::: (21L to 25).toList).toVector
      val l2 = ((1L to 5).toList ::: (22L to 26).toList).toVector
      mhf.similarity(l1, l2) must_== 0.5
    }

    "calculate sketch for a sequence of words" in {
      val text = "this is some text".split(" ").toList
      val hashFunc = mhf.hashFunctions(3, 1)
      val s = mhf.sketch(text, hashFunc)
      s.length must_== 3
    }
  }
}
