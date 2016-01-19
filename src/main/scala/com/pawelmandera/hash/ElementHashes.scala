package com.pawelmandera.hash

import scala.util.Try

/** A type class for things that can be mapped to a Set of Long hashes.*/
trait ElementHashes[-A] {
  def hashes(x: A): Try[Set[Long]]
}

object ElementHashes {
  implicit object ShingleElementHashes extends ElementHashes[List[String]] {
    def hashes(x: List[String]): Try[Set[Long]] =
      Try { (x map { LongHash.stringHash(_) }).toSet }
  }
}
