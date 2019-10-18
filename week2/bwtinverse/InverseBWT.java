import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;

public class InverseBWT {
    Map<Integer, SimpleEntry<Character, Integer>> indexToOccurence = new HashMap<>();
    Map<SimpleEntry<Character, Integer>, Integer> occurenceToIndex = new HashMap<>();

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

    int convertLastToFirst(int i, String firstColumn, String lastColumn) {
        char letter = lastColumn.charAt(i);
        return 0;
    }

    String inverseBWT(String bwt) {
        StringBuilder result = new StringBuilder();
        char[] chars = bwt.toCharArray();
        Arrays.sort(chars);
        String firstColumn = new String(chars);
        int n = chars.length;
        // find all occurences of A, C, G, T, $
        createMap(bwt, getAllIndex(bwt, 'A'), getAllIndex(bwt, 'C'),
            getAllIndex(bwt, 'G'), getAllIndex(bwt, 'T'));
        createInvMap(firstColumn, getAllIndex(firstColumn, 'A'),
            getAllIndex(firstColumn, 'C'),
            getAllIndex(firstColumn, 'G'), getAllIndex(firstColumn, 'T'));
        int index = 0;
        for (int i = 0; i < n; i++) {
            if (i == n - 1) {
                result.append(bwt.charAt(index));
            } else {
                result.insert(0, bwt.charAt(index));
            }
            SimpleEntry<Character, Integer> tmp = indexToOccurence.get(index);
            index = occurenceToIndex.get(tmp);
        }
        return result.toString();
    }

    List<Integer> getAllIndex(String str, char target) {
        List<Integer> res = new ArrayList<Integer>();
        int index = str.indexOf(target);
        while (index >= 0) {
            res.add(index);
            index = str.indexOf(target, index + 1);
        }
        return res;
    }

    void createMap(String str, List<Integer> aOccurence, List<Integer> cOccurence,
                   List<Integer> gOccurence, List<Integer> tOccurence) {
        for (int i = 0; i < str.length(); i++) {
            if (aOccurence.contains(i)) {
                int occurence = aOccurence.indexOf(i) + 1;
                indexToOccurence.put(i, new SimpleEntry<Character, Integer>('A', occurence));
            } else if (cOccurence.contains(i)) {
                int occurence = cOccurence.indexOf(i) + 1;
                indexToOccurence.put(i, new SimpleEntry<Character, Integer>('C', occurence));
            } else if (gOccurence.contains(i)) {
                int occurence = gOccurence.indexOf(i) + 1;
                indexToOccurence.put(i, new SimpleEntry<Character, Integer>('G', occurence));
            } else if (tOccurence.contains(i)) {
                int occurence = tOccurence.indexOf(i) + 1;
                indexToOccurence.put(i, new SimpleEntry<Character, Integer>('T', occurence));
            } else {
                // this is $. i -> ('$', 0)
                indexToOccurence.put(i, new SimpleEntry<Character, Integer>('$', 1));
            }
        }
    }

    void createInvMap(String str, List<Integer> aOccurence, List<Integer> cOccurence,
                      List<Integer> gOccurence, List<Integer> tOccurence) {
        for (int i = 0; i < str.length(); i++) {
            if (aOccurence.contains(i)) {
                int occurence = aOccurence.indexOf(i) + 1;
                occurenceToIndex.put(new SimpleEntry<Character, Integer>('A', occurence), i);
            } else if (cOccurence.contains(i)) {
                int occurence = cOccurence.indexOf(i) + 1;
                occurenceToIndex.put(new SimpleEntry<Character, Integer>('C', occurence), i);
            } else if (gOccurence.contains(i)) {
                int occurence = gOccurence.indexOf(i) + 1;
                occurenceToIndex.put(new SimpleEntry<Character, Integer>('G', occurence), i);
            } else if (tOccurence.contains(i)) {
                int occurence = tOccurence.indexOf(i) + 1;
                occurenceToIndex.put(new SimpleEntry<Character, Integer>('T', occurence), i);
            } else {
                occurenceToIndex.put(new SimpleEntry<Character, Integer>('$', 1), i);
            }
        }
    }

    static public void main(String[] args) throws IOException {
        new InverseBWT().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        System.out.println(inverseBWT(bwt));
    }
}
