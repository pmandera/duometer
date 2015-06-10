package com.pawelmandera.io

import scala.util.Try

import com.pawelmandera.text.Text
import com.pawelmandera.io.tika.TikaParser

/*
 * Simple wrapper for working with text files.
 */

trait HasTextLines {
  def lines: Try[Traversable[String]]
}

/*
 *
 */
class TextFile(val path: String) {
  self: HasTextLines =>

  val file = new java.io.File(path)

  /** Return the whole text of a file. */
  def text: Try[String] = lines map { _.mkString("\n") }

  /** Returns all tokens in a file. */
  def tokens(implicit tokenizer: Text.SentenceTokenizer): Try[Seq[String]] =
    lines map { tokenizer(_).toSeq.flatten }

  /** Returns ngrams in a file. */
  def ngrams(n: Int)(implicit tokenizer: Text.SentenceTokenizer): Try[TraversableOnce[Seq[String]]] =
    tokens(tokenizer) map { Text.ngrams(n, _) }
}

/** Plain text files **/

trait PlainTextLines extends HasTextLines {
  self: TextFile =>

  /** Returns lines of a file. */
  def lines: Try[List[String]] = Try {
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
  def lines: Try[List[String]] =
    Try(TikaParser.text(file).split("\n").toList)
}

case class PlainTextFile(p: String) extends TextFile(p) with PlainTextLines

case class TikaFile(p: String) extends TextFile(p) with TikaTextLines
