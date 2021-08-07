# Trie

**This was the second project assigned in Rutgers 01:198:112 (Data Structures).

In this project, I wrote an application to build a tree structure called a Trie for a given dictionary of English words, then used the Trie to generate completion lists for string searches.

A Trie is a general tree, in that each node can have any number of children. It is used to store a dictionary (list) of words that can be searched on, in a manner that allows for efficient generation of completion lists.

The word list is originally stored in an array, and the trie is built off of this array. A node within the Trie stores the longest common prefix among its children. Every leaf node represents a complete word, and every complete word is represented by some leaf node. (In other words, internal nodes do not represent complete words, only proper prefixes.)

Since the nodes in a trie have varying numbers of children, the structure is built using linked lists in which each node has three fields:
- substring (which is a triplet of indexes)
- first child, and
- sibling, which is a pointer to the next sibling.

I implemented the following methods in the Trie class:
- buildTrie: Starting with an empty trie, builds it up by inserting words from an input array, one word at a time. The words in the input array are all lower case, and comprise of letters ONLY.
- completionList: For a given search prefix, scans the trie efficiently, gathers and returns an ArrayList of references to all leaf TrieNodes that hold words that begin with the search prefix (you should NOT create new nodes.)
