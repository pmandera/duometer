# Duometer - near-duplicate detection tool

Duometer allows to efficiently identify near-duplicate pairs of documents in
large collections of texts. It is written in Scala and implements a MinHash
algorithm.

For example, to extract text from all files in `~/text-files` and identify
those that have similar content, run:

```bash
./duometer -i ~/text-files -o text-files.duplicates
```

For more information about how to use duometer see [this tutorial](http://www.pawelmandera.com/duometer/tutorial.html).

# Features

- Efficiently finds pairs of documents that contain similar text.
- Automatically extracts text from files in a huge number of different formats (including HTML, PDF, Microsoft Office document formats and the OpenDocument format)
- Works well with very large collections of documents.
- Makes use of multiple CPU cores.
- The default settings should work well in most cases but you can
  customize the duplicate detection for your purposes (
  run `./duometer --help` for a full set of options).

# Installation

## All platforms

The only prerequisite is a Java runtime.

1. Download the current version of the tool [here](http://www.pawelmandera.com/download/duometer-0.1.3.zip).
2. Extract the archive and go to `./bin`.
3. Run `./duometer`  (on Linux and Mac) or `duometer.bat` (on Windows).

## Debian (Ubuntu)

Download [a package](http://www.pawelmandera.com/download/duometer_0.1.3_all.deb)
and run:

```bash
sudo dpkg -i duometer_0.1.3_all.deb
```

`duometer` is now installed and should be available as a shell command.

# Building 

Duometer uses [sbt-native-packager](http://www.scala-sbt.org/sbt-native-packager/).
You can build the tool from source by running the `dist` command in sbt
to create a `.zip` archive that can be run on any machine with Java installed.

Debian binary package can be created by executing `debian:packageBin`.

# MinHash algorithm

For background information about the algoritm see a [relevant chapter](http://nlp.stanford.edu/IR-book/html/htmledition/near-duplicates-and-shingling-1.html)
in Manning and Schütze (2008) or read the original [Broder (1997)](http://gatekeeper.dec.com/ftp/pub/dec/SRC/publications/broder/positano-final-wpnums.pdf) paper.

# Supported file formats

Duometer uses Apache Tika to extract text from a huge number of different
file types. For the full list of supported formats see
[here](https://tika.apache.org/1.8/formats.html).

# Memory and large collections of file

When using duometer to detect duplicates in a large collection of documents,
you should make sure that enough memory is available to the JVM in which duometer. 
If you do not know what your default settings are or do not want to change them,
you can easily override them by adding `-J-Xmx` argument when calling duometer.
For example, if you want to make 10GB of memory available to duometer, you should run:

```bash
./duometer -J-Xmx10G -i ~/text-files -o text-files.duplicates
```


# Contribute 

- Issue Tracker: https://github.com/pmandera/duometer/issues
- Source Code: https://github.com/pmandera/duometer

# Authors

The tool was developed at Center for Reading Research, Ghent University by [Paweł Mandera](http://crr.ugent.be/pawel-mandera).

# License

The project is licensed under the Apache License 2.0.

# References

Broder, A. Z. (1997). On the resemblance and containment of documents. In
Compression and Complexity of Sequences 1997. Proceedings (pp. 21–29). IEEE.

Manning, C. D., Raghavan, P., & Schütze, H. (2008). Introduction to information
retrieval. New York: Cambridge University Press.
