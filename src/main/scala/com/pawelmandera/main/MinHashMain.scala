package com.pawelmandera.main

import java.io.{ File, PrintWriter }

import com.pawelmandera.io.{ TextFile, Listings }
import com.pawelmandera.hash.{ LongHash, ElementHashes }
import com.pawelmandera.text.Text
import com.pawelmandera.duplicates.{ MinHashDetection, SuperShingleCandidates, SharedMemberCandidates }


object MinHashMain {
  /** Configuration object for scopt parser
    *
    * Default parameters for minhash based on:
    * Henzinger, M. (2006). Finding near-duplicate web pages: a large-scale evaluation of algorithms.
    * In Proceedings of the 29th annual international ACM SIGIR conference on Research and
    * development in information retrieval (pp. 284–291). ACM.
    * Retrieved from http://dl.acm.org/citation.cfm?id=1148222
    */
  case class Config(
    inFiles: Vector[File] = Vector(),
    outFile: File = new File("out"),
    verbose: Boolean = false,
    threshold: Double = 0.2,
    superShingles: Option[Int] = None,
    seed: Int = scala.util.Random.nextInt(),
    ngramSize: Int = 8,
    nHashFunc: Int = 84)

  /** build scopt commandline parser */
  val parser = new scopt.OptionParser[Config]("duometer") {
    head("duometer",  "0.1.0")
    opt[File]('i', "input") required() maxOccurs(2) action {
        (x, c) => c.copy(inFiles = c.inFiles :+ x) 
    } valueName("<file|dir>") text(
      "File listing documents or a directory to look for duplicates " +
      "(if set twice, look for duplicates across two lists/directories)")
    opt[File]('o', "output") required() action {
      (x, c) => c.copy(outFile = x)
    } valueName("<file>") text("Output file")
    opt[Int]('n', "ngram-size") action {
      (x, c) => c.copy(ngramSize = x) 
    } valueName("<size>") text(
      "N-gram size for shingling, default: 8")
    opt[Int]('f', "hash-func") action {
      (x, c) => c.copy(nHashFunc = x)
    } valueName("<number>") text(
      "Number of hashing functions in minhash, default: 84")
    opt[Int]('r', "random-seed") action {
      (x, c) => c.copy(seed = x)
    } valueName("<seed>") text("Random seed")
    opt[Int]('s', "super-shingles") action {
      (x, c) => c.copy(superShingles = Some(x))
    } valueName("<size>") text(
      "Compare pairs based on common super-shingles of a given size")
    opt[Double]('t', "threshold") action {
      (x, c) => c.copy(threshold = x)
    } valueName("<value>") text(
      "Similarity threshold for a pair to be listed in the output, default: 0.2")
    opt[Unit]("verbose") action {
      (_, c) => c.copy(verbose = true) } text(
        "Print extra information during processing")
    help("help") text("Print this usage text")
    version("version") text("Print version")
  }

  case class NgramTextFile(n: Int, tf: TextFile)

  def getFiles(source: File, ngramSize: Int): Set[NgramTextFile] = {
    val paths = Listings.listPath(source).getOrElse {
      throw new Exception(s"Cannot get files from $source.")
    }

    paths.toSet map { e: String =>
      NgramTextFile(ngramSize, TextFile(e)) }
  }

  def main(args: Array[String]) {
    parser.parse(args, Config()) match {
      case None => println("Could not parse arguments.")
      case Some(config) => {
        val elemsA = getFiles(config.inFiles(0), config.ngramSize)
        val elemsB = if (config.inFiles.length == 2) { getFiles(config.inFiles(1), config.ngramSize) }
                     else { elemsA }

        val elemToId = (elemsA ++ elemsB).toList.zipWithIndex.toMap
        val idToElem = elemToId map { _.swap }

        val elemsAIds = (elemsA map { elemToId(_) }).toSet
        val elemsBIds = (elemsB map { elemToId(_) }).toSet

        implicit object IndexedElementHashes extends ElementHashes[Int] {
          def tokenizer: Text.SentenceTokenizer = Text.defaultTokenizeSentences

          def hashes(id: Int): Set[Long] = {
            val x = idToElem(id)
            if (config.verbose) {
              println(x.tf.path)
            }
            val ngramsSet = x.tf.ngrams(x.n)(tokenizer)
            (ngramsSet map { ngram => LongHash.ngramHash(ngram) }).toSet
          }
        }

        val minHash = config.superShingles match {
          case None => new MinHashDetection with SharedMemberCandidates
          case Some(x) => new MinHashDetection with SuperShingleCandidates { val size = x }
        }

        val simPairs: Iterator[(Int, Int, Double)] =
          minHash.detect(elemsAIds, elemsBIds, config.nHashFunc, config.seed)

        val duplicates = simPairs filter { _._3 >= config.threshold }

        def outFormat(e: (Int, Int, Double)): String =
          idToElem(e._1).tf.path + "\t" + idToElem(e._2).tf.path + "\t" + e._3

        val writer = new PrintWriter(config.outFile)

        try {
          duplicates foreach { e => writer.println(outFormat(e)) }
        } finally {
          writer.close()
        }
      }
    }
  }
}
