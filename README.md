# Duometer - near-duplicate detection tool

Duometer allows to efficiently identify near-duplicate pairs of documents in
large collections of texts. It is written in Scala and implements a MinHash
algorithm.

For example, to look in `~/text-files` for files that contain similar text, run:

```bash
./duometer -i ~/text-files -o text-files.duplicates
```

# Features

- Efficiently finds pairs of documents that contain similar text.
- Works well with very large collections of documents.
- Makes use of multiple CPU cores.
- The default settings should work well in most cases but you can
  customize the duplicate detection for your purposes (
  run `./duometer --help` for a full set of options).

# Installation

The only prerequisite is Java.

1. Download the current version of the tool [here](http://www.pawelmandera.com/download/duometer-0.1.0.zip).
2. Extract the archive and go to `./bin`.
3. Run `./duometer`  (on Linux and Mac) or `duometer.bat` (on Windows).

# Builiding 

You can build the tool from source by running the `dist` command in sbt.

# MinHash algorithm

For background information about the algoritm see a [relevant chapter](http://nlp.stanford.edu/IR-book/html/htmledition/near-duplicates-and-shingling-1.html)
in Manning and Schütze (2008) or read the original [Broder (1997)](http://gatekeeper.dec.com/ftp/pub/dec/SRC/publications/broder/positano-final-wpnums.pdf) paper.

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
