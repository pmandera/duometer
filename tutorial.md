---
title: Duometer
subtitle: Usage tutorial
layout: page
---

This tutorial assumes that duometer is installed and can be called using the `duometer` command.
You can check [this](/) page for more information about how to install duometer.

If you want to follow the examples from this tutorial you can download
[this](/downloads/duometer-tutorial.zip) file. In `texts/`, this archive contains a tiny corpus
including excerpts from [Alice in Wonderland](http://www.gutenberg.org/cache/epub/11/pg11.txt)
by Lewis Caroll. Importantly, some of the excerpts are not entirely unique.
One of them is repeated twice in the exactly same form, the copy of the second one contains only
a part of the original text, and the copy of the third one has some phrases changed.
Duometer will help us to identify these duplicate excerpts!

### Quickstart

If you want to just identify duplicates in the folder without customizing any settings you can 
run:

```bash
duometer -i texts/ -o texts-duplicates.txt
```

After running this command you can examine `text-dupicates.txt`. It should contain
three lines with tab-separated values similar to these:

```
texts/after-duplicate.txt       texts/after.txt 1.0
texts/hedgehog.txt      texts/hedgehog-part.txt 0.5238095238095238
texts/curioser-nearduplicate.txt        texts/curioser.txt      0.7380952380952381
```

The first two columns list a pair of potentially duplicate files, and the third column shows a
measure of similarity between the two files - the higher the value the more similar
the files (where 0.0 is the minimum and 1.0 maximum similarity).

If you examine the pairs, you will see that `after.txt` and `after-duplicate.txt`
have the exact same content, `hedgehog-part.txt` contains most of the text from `hedgehog.txt`, and
`curioser-nearduplicate.txt` contains almost the same text as `curioser.txt` with a few differences:
the word _English_ was substituted with _Persian_, _Christmas_ with _Easter_, and the order with
_stockings and shoes_ was changed.

By default duometer will list only pairs of documents for which similarity value is at least 0.2
but it is for you to decide what is the threshold for considering two files to be duplicates.

### Alternative ways of specifying input

Alternatively, you can look for duplicates across two different directories. For illustration,
you can create a second directory named `texts-2/` and copy `curioser-nearduplicate.txt` 
to that directory giving it a new name, `curioser-nearduplicate-2.txt`.

You can now specify two input directories for duometer:

```bash
duometer -i texts -i texts-2 -o texts-texts-2-duplicates.txt
```

If you now look at `texts-texts-2-duplicates.txt` you will find two lines there: one listing
`curioser-nearduplicate-2.txt` as an exact duplicate of `curioser-nearduplicate.txt` and the second one
as a near-duplicate of `curioser.txt`.

The third way in which you can specify input for duometer is to list files which should be considered
when looking for duplicate pairs. Path to each file should be put on a new line.
For example, you can create a file `list.txt` like this:

```
texts/after.txt
texts/hedgehog.txt
texts/hedgehog-part.txt
texts/curioser.txt
texts-2/curioser-nearduplicate-2.txt
```

and then run:

```bash
duometer -i list.txt -o list-duplicates.txt
```

### Further information

This tutorial does not cover more advanced settings. Please run `duometer --help` for more options.
