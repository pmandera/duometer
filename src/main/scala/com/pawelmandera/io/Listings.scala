package com.pawelmandera.io

import java.io.File
import scala.io.Source

object Listings {
  def listPath(f: File): Option[List[String]] = f match {
    case f if f.isDirectory => Some(listDir(f))
    case f if f.exists => Some(listFile(f))
    case _ => None
  }

  /*
   * Get a source from a directory.
   */
  def listDir(f: File): List[String] = {
    val (dirs, files) = f.listFiles partition { _.isDirectory }
    val s1 = files.toList map { _.getPath }
    s1
  }

  /*
   * Get a source from a text file.
   */
  def listFile(f: File): List[String] = {
    val lines = Source.fromFile(f).getLines.toList
    val (exist, nonexist) = lines partition { new File(_).isFile }
    exist
  }
}

