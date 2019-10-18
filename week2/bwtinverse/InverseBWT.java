import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;

public class InverseBWT {
    String[] indexToOccurence = new String[1000000];
    int[] aOccurToIndex = new int[1000000];
    int[] cOccurToIndex = new int[1000000];
    int[] gOccurToIndex = new int[1000000];
    int[] tOccurToIndex = new int[1000000];
    String DELIMITER = "\t";

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
            String tmp = indexToOccurence[index];
            // string, int
            String letter = tmp.split(DELIMITER)[0];
            int occurence = Integer.parseInt(tmp.split(DELIMITER)[1]);
            switch (letter) {
                case "A":
                    index = aOccurToIndex[occurence];
                    break;
                case "C":
                    index = cOccurToIndex[occurence];
                    break;
                case "G":
                    index = gOccurToIndex[occurence];
                    break;
                case "T":
                    index = tOccurToIndex[occurence];
                    break;
                case "$":
                    break;
            }
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
                int occurence = aOccurence.indexOf(i);
                indexToOccurence[i] = String.valueOf('A') + DELIMITER + occurence;
            } else if (cOccurence.contains(i)) {
                int occurence = cOccurence.indexOf(i);
                indexToOccurence[i] = String.valueOf('C') + DELIMITER + occurence;
            } else if (gOccurence.contains(i)) {
                int occurence = gOccurence.indexOf(i);
                indexToOccurence[i] = String.valueOf('G') + DELIMITER + occurence;
            } else if (tOccurence.contains(i)) {
                int occurence = tOccurence.indexOf(i);
                indexToOccurence[i] = String.valueOf('T') + DELIMITER + occurence;
            } else {
                indexToOccurence[i] = String.valueOf('$') + DELIMITER + 0;
            }
        }
    }

    void createInvMap(String str, List<Integer> aOccurence, List<Integer> cOccurence,
                      List<Integer> gOccurence, List<Integer> tOccurence) {
        for (int i = 0; i < str.length(); i++) {
            if (aOccurence.contains(i)) {
                int occurence = aOccurence.indexOf(i);
                aOccurToIndex[occurence] = i;
            } else if (cOccurence.contains(i)) {
                int occurence = cOccurence.indexOf(i);
                cOccurToIndex[occurence] = i;
            } else if (gOccurence.contains(i)) {
                int occurence = gOccurence.indexOf(i);
                gOccurToIndex[occurence] = i;
            } else if (tOccurence.contains(i)) {
                int occurence = tOccurence.indexOf(i);
                tOccurToIndex[occurence] = i;
            }
            // no need to process "$"
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
