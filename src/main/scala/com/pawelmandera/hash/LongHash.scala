package com.pawelmandera.hash

/** Implementations of 64-bit hashes. */
object LongHash {
  val prime: Long = 1125899906842597L

  /** Calculates a 64-bit hash for a string */
  def stringHash(string: String): Long = {
    var h = prime
    for { c <- string } h = 31 * h + c
    h
  }

  /** Calculates a 64-bit hash for an ngram */
  def ngramHash(ngram: Seq[String]): Long = {
    var h = prime
    for { word <- ngram } h = 31 * h + stringHash(word)
    h
  }
}
