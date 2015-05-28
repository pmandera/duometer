package com.pawelmandera.io.tika

import java.io.{ File, FileInputStream }

import org.apache.tika.metadata.Metadata
import org.apache.tika.sax.BodyContentHandler
import org.apache.tika.parser.AutoDetectParser

/*
 * Simple wrapper for Apache Tika.
 */

object TikaParser {
  def text(file: File): String = {
    val is = new FileInputStream(file)
    val handler = new BodyContentHandler()
    val metadata = new Metadata()
    val parser = new AutoDetectParser()
    parser.parse(is, handler, metadata)
    handler.toString
  }
}
