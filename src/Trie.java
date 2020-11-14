/**
 * https://likailee.site
 * CopyRight (c) 2020
 */

import java.util.ArrayList;

/**
 * This class implements a Trie.
 *
 * @author Likai Lee
 */
public class Trie {

    // prevent instantiation
    private Trie() {
    }

    /**
     * Builds a trie by inserting all words in the input array, one at a time,
     * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
     * The words in the input array are all lower case.
     *
     * @param allWords Input array of words (lowercase) to be inserted.
     * @return Root of trie with all words inserted from the input array
     */
    public static TrieNode buildTrie(String[] allWords) {
        /** COMPLETE THIS METHOD **/
        // check if allWords has words
        if (allWords.length == 0) {
            return null;
        }
        // initialize root node
        TrieNode root = new TrieNode(null, null, null);
        // generate first child of root
        Indexes firstChildSubstr = new Indexes(0, (short) 0, (short) (allWords[0].length() - 1));
        TrieNode firstChild = new TrieNode(firstChildSubstr, null, null);
        root.firstChild = firstChild;
        // insert the rest of the words to the Trie
        for (int i = 1; i < allWords.length; i++) {
            String word = allWords[i];
            short wordEnd = (short) (word.length() - 1);
            // search each word's position from the first node
            addWordToTrie(firstChild, word, (short) 0, wordEnd, i, allWords);
        }
        return root;
    }

    /**
     * add a new TrieNode to the Trie
     *
     * @param curNode   Node that in current traversal
     * @param word      Word needed to be inserted
     * @param wordStart Start position of the word needed to be inserted
     * @param wordEnd   End position of the current word
     * @param wordIdx   Index of current word in allWords
     * @param allWords  Input array of words
     */
    private static void addWordToTrie(TrieNode curNode, String word, short wordStart, short wordEnd, int wordIdx, String[] allWords) {
        if (curNode == null) {
            return;
        }
        // restore the string that current node represents
        Indexes curNodeSubstr = curNode.substr;
        String curNodeStr = allWords[curNodeSubstr.wordIndex].substring(curNodeSubstr.startIndex, curNodeSubstr.endIndex + 1);
        // get the LCP of the word to be inserted and the curWord
        short prefixLen = getCommonPrefixLength(word, curNodeStr);
        // if they don't have common prefix
        // find next common prefix in curNode's siblings or insert new a node
        if (prefixLen == 0) {
            // curNode does not have sibling,
            // means current word can not find a prefix, insert a new node to Trie
            if (curNode.sibling == null) {
                Indexes siblingSubstr = new Indexes(wordIdx, wordStart, wordEnd);
                curNode.sibling = new TrieNode(siblingSubstr, null, null);
            } else {
                // traverse the siblings, find the possible prefix
                addWordToTrie(curNode.sibling, word, wordStart, wordEnd, wordIdx, allWords);
            }
            return;
        }
        // prefix match
        // prefix is a substring of current node, need to split current node
        // e.g. node = `be`, prefix = `bid`, we need to spilt `be`
        short prefixEndPos = (short) (curNodeSubstr.startIndex + prefixLen - 1);
        boolean needSplit = prefixEndPos < curNodeSubstr.endIndex;
        if (needSplit) {
            curNode.substr = new Indexes(curNodeSubstr.wordIndex, curNodeSubstr.startIndex, prefixEndPos);
            TrieNode child = curNode.firstChild;
            curNode.firstChild = null;
            // add split node
            Indexes splitIdx = new Indexes(curNodeSubstr.wordIndex, (short) (prefixEndPos + 1), curNodeSubstr.endIndex);
            curNode.firstChild = new TrieNode(splitIdx, child, null);
            // traverse new node's children
            addWordToTrie(curNode, word, wordStart, wordEnd, wordIdx, allWords);
        } else {
            // remove the matched part, and start the new prefix matching
            String trimWord = word.substring(prefixLen);
            // calculate the next prefix start position
            short newWordStartPos = (short) (wordStart + prefixLen);
            addWordToTrie(curNode.firstChild, trimWord, newWordStartPos, wordEnd, wordIdx, allWords);
        }
    }

    /**
     * Get the longest common prefix length of two strings
     *
     * @param str1 The first string
     * @param str2 The second string
     * @return Length of the longest common prefix
     */
    private static short getCommonPrefixLength(String str1, String str2) {
        char[] word1 = str1.toCharArray();
        char[] word2 = str2.toCharArray();
        int len1 = word1.length, len2 = word2.length;
        short i = 0, j = 0;
        short len = 0;
        while (i < len1 && j < len2) {
            if (word1[i] != word2[j]) {
                return len;
            }
            len++;
            i++;
            j++;
        }
        return len;
    }

    /**
     * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the
     * trie whose words start with this prefix.
     * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
     * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell";
     * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell",
     * and for prefix "bell", completion would be the leaf node that holds "bell".
     * (The last example shows that an input prefix can be an entire word.)
     * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
     * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
     *
     * @param root     Root of Trie that stores all words to search on for completion lists
     * @param allWords Array of words that have been inserted into the trie
     * @param prefix   Prefix to be completed with words in trie
     * @return List of all leaf nodes in trie that hold words that start with the prefix,
     * order of leaf nodes does not matter.
     * If there is no word in the tree that has this prefix, null is returned.
     */
    public static ArrayList<TrieNode> completionList(TrieNode root,
                                                     String[] allWords, String prefix) {
        /** COMPLETE THIS METHOD **/
        if (root == null) {
            return null;
        }
        ArrayList<TrieNode> matches = new ArrayList<>();
        searchByPrefix(prefix, root.firstChild, allWords, matches);
        return matches.isEmpty() ? null : matches;
    }

    private static void searchByPrefix(String prefix, TrieNode root, String[] allWords, ArrayList<TrieNode> matches) {
        if (root == null) {
            return;
        }
        // prefix == "", the children and siblings of root can be returned
        if (prefix.length() == 0) {
            // add the leaf node
            if (root.firstChild == null) {
                matches.add(root);
            }
            searchByPrefix(prefix, root.firstChild, allWords, matches);
            searchByPrefix(prefix, root.sibling, allWords, matches);
            return;
        }
        // find the LCP length of current node and prefix
        String nodePrefix = allWords[root.substr.wordIndex].substring(root.substr.startIndex, root.substr.endIndex + 1);
        int cpLen = getCommonPrefixLength(prefix, nodePrefix);
        if (cpLen == 0) {
            // prefix cannot match current node, find next prefix in siblings
            searchByPrefix(prefix, root.sibling, allWords, matches);
        }
        // prefix exact match
        else {
            // remove the matched part of prefix, and search the new prefix
            String newPrefix = prefix.substring(cpLen);
            // leaf node, reach the end
            if (newPrefix.length() == 0 && root.firstChild == null) {
                matches.add(root);
            }
            // prefix match current node, find deeper
            searchByPrefix(newPrefix, root.firstChild, allWords, matches);
        }
    }

    public static void print(TrieNode root, String[] allWords) {
        System.out.println("\nTRIE\n");
        print(root, 1, allWords);
    }

    private static void print(TrieNode root, int indent, String[] words) {
        if (root == null) {
            return;
        }
        for (int i = 0; i < indent - 1; i++) {
            System.out.print("    ");
        }

        if (root.substr != null) {
            String pre = words[root.substr.wordIndex]
                    .substring(0, root.substr.endIndex + 1);
            System.out.println("      " + pre);
        }

        for (int i = 0; i < indent - 1; i++) {
            System.out.print("    ");
        }
        System.out.print(" ---");
        if (root.substr == null) {
            System.out.println("root");
        } else {
            System.out.println(root.substr);
        }

        for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
            for (int i = 0; i < indent - 1; i++) {
                System.out.print("    ");
            }
            System.out.println("     |");
            print(ptr, indent + 1, words);
        }
    }
}
