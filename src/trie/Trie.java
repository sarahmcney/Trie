package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sarah McNey
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM first to last.
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {

		TrieNode root = new TrieNode(null, null, null);
		if(allWords.length == 0) { return root; }
		
		Indexes firstIndex = new Indexes(0, (short) 0, (short) (allWords[0].length()-1));
		TrieNode first = new TrieNode(firstIndex, null, null);
		root.firstChild = first;
		
		TrieNode ptr = root.firstChild, prev = null;
		
		//loop through remaining words
		for(int i = 1; i < allWords.length; i++) {
			String word = allWords[i];
			String prefix = "";
			while(ptr != null) {
				int allWordsIndex = ptr.substr.wordIndex;
				int start = ptr.substr.startIndex;
				int end = ptr.substr.endIndex;
				
				String ptrPrefix = allWords[allWordsIndex].substring(start, end+1);

				if(word.indexOf(prefix + ptrPrefix) == 0) {
					System.out.println(word);
					prefix += ptrPrefix; //lengthen the prefix
					prev = ptr;
					ptr = ptr.firstChild;
					continue;
				}

				if(word.charAt(start) != ptrPrefix.charAt(0)) { //no similarity between words
					prev = ptr;
					ptr = ptr.sibling;
					continue;
				}
				int count = 0; //keep track of similar characters between the prefix before and
				//the new word to insert
				for(int j = 0; j < ptrPrefix.length(); j++) {
					if(word.charAt(start + j) != ptrPrefix.charAt(j)) {
						break; //break as soon as they don't match
					}
					++count;
				}
				
				Indexes newIndexes = new Indexes(allWordsIndex, (short) (start + count), (short) end);
				TrieNode tmp = new TrieNode(newIndexes, ptr.firstChild, null);
				ptr.firstChild = tmp;
				ptr.substr.endIndex = (short) ((start + count) - 1);
				prefix += allWords[allWordsIndex].substring(start, (start+count));
				
				prev = ptr;
				ptr = ptr.firstChild;
			}

			Indexes ind = new Indexes(i, (short) (prefix.length()), (short) (word.length()-1));
			TrieNode finalNode = new TrieNode(ind, null, null);
			prev.sibling = finalNode;
			
			ptr = root.firstChild;
			prev = null;
		}
		return root;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes doesn't matter. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {

		ArrayList<TrieNode> matches = new ArrayList<TrieNode>();
		if(root.firstChild == null) { return null; }

		TrieNode ptr = root.firstChild, prev = root;
		while(ptr != null) {

			int index = ptr.substr.wordIndex;
			int start = ptr.substr.startIndex;
			int end = ptr.substr.endIndex;
			//i'm using 0 so i can keep track of the whole prefix each time
			String ptrPrefix = allWords[index].substring(0, end+1);

			if(ptrPrefix.indexOf(prefix) == 0 || prefix.indexOf(ptrPrefix) == 0) { //exactly contained in internal node
				if(ptr.firstChild == null) {
					//must be leaf node
					if(prefix.length() <= ptrPrefix.length()) {
						matches.add(ptr);
					}
					ptr = ptr.sibling;
				}
				else {
					prev = ptr;
					ptr = ptr.firstChild;
				}
			}
			else {
				ptr = ptr.sibling;
			}
			if(ptr == null) {
				prev = prev.sibling;
				ptr = prev;
			}
			
		}
		if(matches.size() == 0) { return null; } 
		return matches;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
