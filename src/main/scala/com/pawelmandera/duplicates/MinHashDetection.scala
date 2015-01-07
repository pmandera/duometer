package com.pawelmandera.duplicates

import com.pawelmandera.hash.ElementHashes

/**
  * Implementation of the minhash algorithm.
  *
  * For more information see:
  * http://nlp.stanford.edu/IR-book/html/htmledition/near-duplicates-and-shingling-1.html
  */
trait MinHashDetection extends MinHashFunctions {
  self: MinHashCandidates =>

  /** Detects duplicates across two lists of elements.
    *
    * @param xs first set of elements of an ElementHashes type class
    * @param ys second element set
    * @param hfn number of hash functions to be used
    * @param seed random seed
    * @return iterator with pairs of elements which have non-zero minhash similarity
    */
  def detect[A: ElementHashes](xs: Set[A], ys: Set[A], hfn: Int, seed: Int):
    Iterator[(A, A, Double)] = {
    val hf = hashFunctions(hfn, seed)

    val xsSketches: Map[A, Sketch] = sketchesMap(xs, hf)
    val ysSketches: Map[A, Sketch] = if (xs == ys) xsSketches else sketchesMap(ys, hf)

    candidates(xsSketches, ysSketches).map { _.toList } map {
      case List(x, y, _*) =>
        (x, y, similarity(xsSketches(x), ysSketches(y)))
    }
  }
}
