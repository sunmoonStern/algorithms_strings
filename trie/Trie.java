import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Trie {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    List<Map<Character, Integer>> buildTrie(String[] patterns) {
        List<Map<Character, Integer>> trie = new ArrayList<Map<Character, Integer>>();
        int maxNode = 1;
        int n = patterns.length;
        int m = 0;
        for (int i = 0; i < n; i++) {
            m += patterns[i].length();
        }
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[m];
        for (int j = 0; j < m; j++) {
            adj[j] = new ArrayList<Integer>();
            trie.add(new HashMap<Character, Integer>());
        }
        for (int i = 0; i < n; i++) {
            int node = 0;
            for (int j = 0; j < patterns[i].length(); j++) {
                Map<Character, Integer> labelIndexMap = trie.get(node);
                char ch = patterns[i].charAt(j);
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

    static public void main(String[] args) throws IOException {
        new Trie().run();
    }

    public void print(List<Map<Character, Integer>> trie) {
        for (int i = 0; i < trie.size(); ++i) {
            Map<Character, Integer> node = trie.get(i);
            for (Map.Entry<Character, Integer> entry : node.entrySet()) {
                System.out.println(i + "->" + entry.getValue() + ":" + entry.getKey());
            }
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        int patternsCount = scanner.nextInt();
        String[] patterns = new String[patternsCount];
        for (int i = 0; i < patternsCount; ++i) {
            patterns[i] = scanner.next();
        }
        List<Map<Character, Integer>> trie = buildTrie(patterns);
        print(trie);
    }
}
