package com.pawelmandera.duplicates

import scala.util.Try

import com.pawelmandera.hash.ElementHashes

trait MinHashFunctions {
  /** Calculate sketch for an element. */
  def sketch[A: ElementHashes](x: A, hf: HashFunctions): Try[Sketch] = {
    val initHashes = implicitly[ElementHashes[A]].hashes(x)
    initHashes map { h => hf map { minhash(_, h) } }
  }

  /**
    * Calculate sketches for a set of elements.
    *
    * @param xs set of elements of an ElementHashes type class
    * @param hf hash functions
    * @return a map with elements as keys and sketch as values
    */
  def sketchesMap[A: ElementHashes](xs: Set[A], hf: HashFunctions): Map[A, Sketch] = {
    val sketchesMapTry = (xs.par map { e => (e, sketch(e, hf)) }).seq
    (sketchesMapTry filter { _._2.isSuccess } map { e => (e._1, e._2.get) }).toMap
  }

  /** Generate random hash functions
    *
    * @param n number of hash functions
    * @param seed a random seed
    * @return a vector of hash functions
    */
  def hashFunctions(n: Int, seed: Int): HashFunctions = {
    val randomGen = new scala.util.Random(seed)

    def randHashF: Long => Long = {
      val a: Long = randomGen.nextLong()
      val b: Long = randomGen.nextLong()
      (x: Long) => (a * x + b) % 768614336404564651L
    }

    Vector.fill(n)(randHashF)
  }

  /** Calculates a minhash given a hash function and a set of numbers. */
  def minhash(hashFunc: Long => Long, s: Set[Long]): Long = (s map { hashFunc(_) }).min

  /** Calculates a similarity based on minhashes. */
  def similarity(s1: Sketch, s2: Sketch): Double =
    (s1 zip s2 count { case (e1, e2) => e1 == e2 }).toDouble/s1.length
}
