package com.pawelmandera.text

import org.specs2.mutable._
import org.specs2.specification

class TextSpec extends Specification {
  "Text" should {
    "tokenize text with defaultTokenize" in {
      val text = "This is a text123 with fancy words ('ćma', 'wąż')"
      val tokens = Text.defaultTokenize(text).toList
      tokens must_== List(
        "This", "is", "a", "text", "with", "fancy", "words", "ćma", "wąż"
      )
    }

    "tokenize sentences with defaultTokenizeSentences" in {
      val text = List(
        "This is a text123 with fancy words ('ćma', 'wąż')",
        "This is a text123 with fancy words ('ćmaaa', 'wążaa')")
      val tokens = Text.defaultTokenizeSentences(text).toList map { _.toList }
      tokens must_== List(
        List("This", "is", "a", "text", "with", "fancy", "words", "ćma", "wąż"),
        List("This", "is", "a", "text", "with", "fancy", "words", "ćmaaa", "wążaa")
      )
    }

    "calculate frequencies" in {
      val data = List("one", "three", "two", "three", "two", "three")
      Text.freqs(data) must_== Map("one" -> 1, "two" -> 2, "three" -> 3)
    }

    "calculate frequency spectrum" in {
      val freqs = Map("one" -> 1, "two" -> 1, "three" -> 3, "four" -> 4)
      Text.spectrum(freqs) must_== Map(1 -> 2, 3 -> 1, 4 -> 1)
    }

    "surround with hashes if n > 1" in {
      val helloWorld = "hello world"

      val notSurrounded = Text.surround(1, helloWorld.split(" "))
      notSurrounded.mkString(" ") must_== "hello world"

      val surroundedBi = Text.surround(2, helloWorld.split(" "))
      surroundedBi.mkString(" ") must_== "# hello world #"

      val surroundedTri = Text.surround(3, helloWorld.split(" "))
      surroundedTri.mkString(" ") must_== "# # hello world # #"
    }

    "return correct bigrams" in {
      val shortText = "this is example text"
      val ngrams = Text.ngrams(2, shortText.split(" ")).toList
      ngrams must_== List(
        Vector("#", "this"),
        Vector("this", "is"),
        Vector("is", "example"),
        Vector("example", "text"),
        Vector("text", "#"))
    }

    "return correct trigrams" in {
      val shortText = "this is example text"
      val ngrams = Text.ngrams(3, shortText.split(" ")).toList
      ngrams must_== List(
        Vector("#", "#", "this"),
        Vector("#", "this", "is"),
        Vector("this", "is", "example"),
        Vector("is", "example", "text"),
        Vector("example", "text", "#"),
        Vector("text", "#", "#"))
    }


    class Limerick extends specification.Scope {
      val tokens = "There was a young lady named Bright one a one a one bright there".split(" ").toSeq
    }

    "calculate frequencies from lines" in new Limerick {
      val freqs = Text.tokensToFreqs(s => Some(s))(tokens)
      freqs must_== Map(
        "young" -> 1, "a" -> 3, "lady" -> 1, "There" -> 1, "bright" -> 1,
        "named" -> 1, "was" -> 1, "Bright" -> 1, "one" -> 3, "there" -> 1)
    }

    "calculate frequencies from lines with filtering" in new Limerick {
      val filterFunc = (str: String) => if (Set("a", "there")(str)) None else Some(str)
      val freqs = Text.tokensToFreqs(filterFunc)(tokens)
      freqs must_== Map(
        "young" -> 1, "lady" -> 1, "There" -> 1, "bright" -> 1,
        "named" -> 1, "was" -> 1, "Bright" -> 1, "one" -> 3)
    }

    "calculate frequencies from lines with transformation" in new Limerick {
      val filterFunc = (str: String) => Some(str.toLowerCase)
      val freqs = Text.tokensToFreqs(filterFunc)(tokens)
      freqs must_== Map(
        "young" -> 1, "a" -> 3, "lady" -> 1, "there" -> 2,
        "named" -> 1, "was" -> 1, "bright" -> 2, "one" -> 3)
    }
  }
}
