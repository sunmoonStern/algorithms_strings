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
        createMap(bwt);
        createInvMap(firstColumn);
        int index = 0;
        for (int i = 0; i < n; i++) {
            // insert only once, and mostly use append for performance issues (reverse later)
            if (i == n - 1) {
                result.insert(0, bwt.charAt(index));
            } else {
                result.append(bwt.charAt(index));
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
        return result.reverse().toString();
    }

    void createMap(String str) {
        int aCounter = 0;
        int cCounter = 0;
        int gCounter = 0;
        int tCounter = 0;
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case 'A':
                    indexToOccurence[i] = String.valueOf('A') + DELIMITER + aCounter;
                    aCounter += 1;
                    break;
                case 'C':
                    indexToOccurence[i] = String.valueOf('C') + DELIMITER + cCounter;
                    cCounter += 1;
                    break;
                case 'G':
                    indexToOccurence[i] = String.valueOf('G') + DELIMITER + gCounter;
                    gCounter += 1;
                    break;
                case 'T':
                    indexToOccurence[i] = String.valueOf('T') + DELIMITER + tCounter;
                    tCounter += 1;
                    break;
                case '$':
                    indexToOccurence[i] = String.valueOf('$') + DELIMITER + 0;
                    break;
            }
        }
    }

    void createInvMap(String str) {
        int aCounter = 0;
        int cCounter = 0;
        int gCounter = 0;
        int tCounter = 0;
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case 'A':
                    aOccurToIndex[aCounter] = i;
                    aCounter += 1;
                    break;
                case 'C':
                    cOccurToIndex[cCounter] = i;
                    cCounter += 1;
                    break;
                case 'G':
                    gOccurToIndex[gCounter] = i;
                    gCounter += 1;
                    break;
                case 'T':
                    tOccurToIndex[tCounter] = i;
                    tCounter += 1;
                    break;
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
