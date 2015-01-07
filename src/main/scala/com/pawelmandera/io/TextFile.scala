package com.pawelmandera.io

import com.pawelmandera.text.Text

/*
 * Simple wrapper for working with text files.
 */

trait HasTextLines {
  def lines: Traversable[String]
}

trait FileTextLines extends HasTextLines {
  self: AbstractFile =>

  /** Returns lines of a file. */
  def lines: List[String] = {
    val source = scala.io.Source.fromFile(file)
    val fileLines = source.getLines.toList
    source.close()
    fileLines
  }
}

class AbstractFile(val path: String) {
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

case class TextFile(p: String) extends AbstractFile(p) with FileTextLines
