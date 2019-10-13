import java.io.*;
import java.util.*;

class Node
{
	public static final int Letters =  4;
	public static final int NA      = -1;
	public int next [];

	Node ()
	{
		next = new int [Letters];
		Arrays.fill (next, NA);
	}
}

public class TrieMatching implements Runnable {
	int letterToIndex (char letter)
	{
		switch (letter)
		{
			case 'A': return 0;
			case 'C': return 1;
			case 'G': return 2;
			case 'T': return 3;
			default: assert (false); return Node.NA;
		}
	}

	List<Map<Character, Integer>> buildTrie(List<String> patterns) {
		List<Map<Character, Integer>> trie = new ArrayList<Map<Character, Integer>>();
		int maxNode = 1;
		int n = patterns.size();
		int m = 0;
		for (int i = 0; i < n; i++) {
			m += patterns.get(i).length();
		}
		ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[m];
		for (int j = 0; j < m; j++) {
			adj[j] = new ArrayList<Integer>();
			trie.add(new HashMap<Character, Integer>());
		}
		trie.add(new HashMap<Character, Integer>()); // check for leaf
		for (int i = 0; i < n; i++) {
			int node = 0;
			int len = patterns.get(i).length();
			for (int j = 0; j < len; j++) {
				Map<Character, Integer> labelIndexMap = trie.get(node);
				char ch = patterns.get(i).charAt(j);
				int dest = -1;
				for (int ad: adj[node]) {
					if (labelIndexMap.containsKey(ch)) {
						if (labelIndexMap.get(ch) == ad) {
							dest = ad;
							break;
						}
					}
				}
				if (dest != -1) {
					node = dest;
				} else {
					adj[node].add(maxNode);
					Map<Character, Integer> localMap = trie.get(node);
					localMap.put(ch, maxNode);
					trie.set(node, localMap);
					node = maxNode;
					maxNode += 1;
				}
			}
		}
		return trie;
	}

	List <Integer> solve (String text, int n, List <String> patterns) {
		List <Integer> result = new ArrayList <Integer> ();
		List<Map<Character, Integer>> trie = buildTrie(patterns);
		for (int i = 0; i < text.length(); i++) {
			int res = getPrefixTrieMatch(i, text, trie);
			if (res != -1) {
				result.add(res);
			}
		}
		return result;
	}

	int getPrefixTrieMatch (int startIndex, String text, List<Map<Character, Integer>> trie) {
		int index = startIndex;
		char symbol = text.charAt(index);
		int node = 0;
		while (true) {
			if (isLeaf(node, trie)) {
				return startIndex;
			} else if (trie.get(node).keySet().contains(symbol)) {
				node = trie.get(node).get(symbol);
				if (isLeaf(node, trie)) {
					return startIndex;
				}
				index += 1;
				if (index >= text.length()) {
					return -1;
				}
				symbol = text.charAt(index);
			} else {
				return -1;
			}
		}
	}

	boolean isLeaf(int node, List<Map<Character, Integer>> trie) {
		return trie.get(node).size() == 0;
	}

	public void run () {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
			String text = in.readLine ();
		 	int n = Integer.parseInt (in.readLine ());
		 	List <String> patterns = new ArrayList <String> ();
			for (int i = 0; i < n; i++) {
				patterns.add (in.readLine ());
			}

			List <Integer> ans = solve (text, n, patterns);

			for (int j = 0; j < ans.size (); j++) {
				System.out.print ("" + ans.get (j));
				System.out.print (j + 1 < ans.size () ? " " : "\n");
			}
		}
		catch (Throwable e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}

	public static void main (String [] args) {
		new Thread (new TrieMatching ()).start ();
	}
}
