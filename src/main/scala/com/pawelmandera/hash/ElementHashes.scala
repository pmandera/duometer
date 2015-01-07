package com.pawelmandera.hash

/** A typeclass for things that can be mapped to a Set of Long hashes.*/
trait ElementHashes[-A] {
  def hashes(x: A): Set[Long]
}

object ElementHashes {
  implicit object ShingleElementHashes extends ElementHashes[List[String]] {
    def hashes(x: List[String]): Set[Long] =
      (x map { LongHash.stringHash(_) }).toSet
  }
}
