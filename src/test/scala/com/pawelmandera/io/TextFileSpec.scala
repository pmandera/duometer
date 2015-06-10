package com.pawelmandera.io

import scala.util.Success

import org.specs2.mutable._
import org.specs2.mock._
import org.specs2.specification.Scope


class TextFileSpec extends Specification {
  trait TestData extends Scope {
    val mockedFile = new TextFile("test/path") with HasTextLines {
      val lines = Success(Vector(
        "This is a first line",
        "This is a second line",
        "This is a third line"
      ))
    }
  }

  "A TextFile object " should {
    "return the text" in new TestData {
      val text = mockedFile.text
      text.get must_== mockedFile.lines.get.mkString("\n")
    }
  }
}
