package com.pawelmandera.text

import scala.util.matching.Regex

/** Methods for working with texts. */
object Text {
  type Tokenizer = String => TraversableOnce[String]
  type SentenceTokenizer = TraversableOnce[String] => TraversableOnce[TraversableOnce[String]]

  /** Returns a stopword set.
    *
    * Reads a file in which stopwords are listed in separate lines and
    * returns a filtering function.
    */
  def stoplistFromFile(f: java.io.File): Set[String] = scala.io.Source.fromFile(f).getLines.toSet

  /**
    * Text lines to frequencies.
    */
  def tokensToFreqs(tokenTransform: String => Option[String])(tokens: TraversableOnce[String]): Map[String, Int] = {
    val trTokens = for {
      token <- tokens
      trToken <- tokenTransform(token)
    } yield trToken
    freqs(trTokens)
  }

  /**
    * Count frequency of occurence.
    */
  def freqs[A](tokens: TraversableOnce[A]): Map[A, Int] = {
    val freqsHM = new scala.collection.mutable.HashMap[A, Int] {
      override def default(key: A): Int = 0
    }

    tokens foreach { token =>
      freqsHM(token) = freqsHM(token) + 1
    }

    freqsHM.toMap
  }

  /**
    * Calculate spectrum from frequency distribution.
    */
  def spectrum(data: Iterable[(Any, Int)]): Map[Int, Int] = freqs((data map { _._2 }).toSeq)

  /**
    * Regex tokenizer.
    * Takes a regex as an input and returns a function which finds all matches.
    */
  def tokenize(exp: Regex)(str: String): TraversableOnce[String] = exp findAllIn str

  /** Tokenize each of the strings. */
  def tokenizeSentences(exp: Regex)(sentences: TraversableOnce[String]): TraversableOnce[TraversableOnce[String]] = {
    val reTokenizer = tokenize(exp)(_)
    for { sentence <- sentences } yield reTokenizer(sentence)
  }

  /** Default tokenizer regex that matches all alphabetical strings. */
  val defaultTokenRegex = """[\p{L}\p{M}]+""".r

  /** Sentence tokenizer based on default regex. */
  def defaultTokenize: Tokenizer = tokenize(defaultTokenRegex)(_)

  /** Sentence tokenizer based on default regex. */
  def defaultTokenizeSentences: SentenceTokenizer = tokenizeSentences(defaultTokenRegex)(_)

  /** Surrounds tokens with prefix and suffix '#' */
  def surround(n: Int, s: TraversableOnce[String]): Vector[String] = {
    val surroundingVec = Vector.fill(n - 1)("#")
    surroundingVec ++: s.toVector ++: surroundingVec
  }

  /** Returns ngrams from tokens */
  def ngrams(n: Int, s: TraversableOnce[String], padding: Boolean = true): Iterator[Seq[String]] = {
    val tokens = if (padding) surround(n, s) else s.toVector
    tokens.sliding(n)
  }
}
