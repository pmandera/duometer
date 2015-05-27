package com.pawelmandera.io

import com.pawelmandera.text.Text
import com.pawelmandera.io.tika.TikaParser

/*
 * Simple wrapper for working with text files.
 */

trait HasTextLines {
  def lines: Traversable[String]
}

/*
 *
 */
class TextFile(val path: String) {
  self: HasTextLines =>

  val file = new java.io.File(path)

  /** Return the whole text of a file. */
  def text: String = lines.mkString("\n")

  /** Returns all tokens in a file. */
  def tokens(implicit tokenizer: Text.SentenceTokenizer): Seq[String] =
    tokenizer(lines).toSeq.flatten

  /** Returns ngrams in a file. */
  def ngrams(n: Int)(implicit tokenizer: Text.SentenceTokenizer): TraversableOnce[Seq[String]] =
    Text.ngrams(n, tokens(tokenizer))
}

/** Plain text files **/

trait PlainTextLines extends HasTextLines {
  self: TextFile =>

  /** Returns lines of a file. */
  def lines: List[String] = {
    val source = scala.io.Source.fromFile(file)
    val fileLines = source.getLines.toList
    source.close()
    fileLines
  }
}

/** Use Apache Tika to extract text from files  supported formats. **/

trait TikaTextLines extends HasTextLines {
  self: TextFile =>

  /** Parses a file using Apache Tika and returns lines. */
  def lines: List[String] =
    TikaParser.text(file).split("\n").toList
}

case class PlainTextFile(p: String) extends TextFile(p) with PlainTextLines

case class TikaFile(p: String) extends TextFile(p) with TikaTextLines
