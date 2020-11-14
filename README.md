- [Trie](#trie)
  * [Trie Structure](#trie-structure)
  * [Data Structure](#data-structure)
  * [Prefix Search](#prefix-search)
  * [Testing](#testing)

# Trie
## Trie Structure
A Trie is a general tree, in that each node can have any number of children. It is used to store a dictionary (list) of words that can be searched on,
in a manner that allows for efficient generation of completion lists.

The word list is originally stored in an array, and the trie is built off of this array. Here are some examples of word lists and the tries built to store the words, followed by an explanation of the trie structure and its relationship to its source word list.

![](https://raw.githubusercontent.com/LikaiLee/likailee.github.io/img/20201114134152.png)
![](https://raw.githubusercontent.com/LikaiLee/likailee.github.io/img/20201114133812.png)

## Data Structure
Since the nodes in a trie have varying numbers of children, the structure is built using linked lists in which each node has three fields:

* **substring** (which is a triplet of indexes)
* **first child**, and
* **sibling**, which is a pointer to the next sibling.

## Prefix Search
Once the trie is set up for a list of words, you can compute word completions efficiently.

For instance, in the trie of Example above (cat, muscle, ...), suppose you wanted to find all words that started with "po" (prefix). The search would start at the root, and touch the nodes `[0,0,2],(1,0,2),(2,0,1),(2,2,2),(3,2,3),[2,3,6],[6,3,5],[3,4,7],[4,4,5]`. The nodes marked in red are the ones that hold words that begin with the given prefix.

Note that NOT ALL nodes in the tree are examined. In particular, after examining (1,0,2), the entire subtree rooted at that node is skipped. This makes the search efficient. (Searching all nodes in the tree would obviously be very inefficient, you might as well have searched the word array in that case, why bother building a trie!)

## Testing

You can test your program using the supplied `TrieApp` driver. It first asks for the name of an input file of words, with which it builds a trie by calling the `Trie.buildTree` method. After the trie is built, it asks for search prefixes for which it computes completion lists, calling the `Trie.completionList` method.

Several sample word files are given with the project, directly under the project folder. (`words0.txt`, `words1.txt`, `words2.txt`, `words3.txt`, `words4.txt`). The first line of a word file is the number of the words, and the subsequent lines are the words, one per line.

There's a convenient `print` method implemented in the `Trie` class that is used by `TrieApp` to output a tree for verification and debugging ONLY.

Here are a couple of examples of running TrieApp:

The first run is for `words3.txt`:

```
Enter words file name => words3.txt

TRIE

 ---root
     |
          doo
     ---(0,0,2)
         |
              door
         ---(0,3,3)
         |
              doom
         ---(3,3,3)
     |
          por
     ---(1,0,2)
         |
              pore
         ---(1,3,3)
         |
              port
         ---(2,3,3)

completion list for (enter prefix, or 'quit'): do
door,doom

completion list for: quit
```
The second run is for `words4.txt`:

```
Enter words file name => words4.txt

TRIE

 ---root
     |
          cat
     ---(0,0,2)
     |
          mus
     ---(1,0,2)
         |
              muscle
         ---(1,3,5)
         |
              musk
         ---(5,3,3)
     |
          po
     ---(2,0,1)
         |
              pot
         ---(2,2,2)
             |
                  pottery
             ---(2,3,6)
             |
                  potato
             ---(6,3,5)
         |
              poss
         ---(3,2,3)
             |
                  possible
             ---(3,4,7)
             |
                  possum
             ---(4,4,5)

completion list for (enter prefix, or 'quit'): pos
possible,possum

completion list for: mu
muscle,musk

completion list for: pot
pottery,potato

completion list for: quit
```
