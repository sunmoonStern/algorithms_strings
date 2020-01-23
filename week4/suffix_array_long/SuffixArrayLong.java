import java.util.*;
import java.io.*;
import java.util.zip.CheckedInputStream;

public class SuffixArrayLong {
    private static final Map<Character, Integer> charToInt = Collections.unmodifiableMap(new HashMap<Character, Integer>() {
        {
            put('$', 0);
            put('A', 1);
            put('C', 2);
            put('G', 3);
            put('T', 4);
        }
    });

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

    public class Suffix implements Comparable {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Object o) {
            Suffix other = (Suffix) o;
            return suffix.compareTo(other.suffix);
        }
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        int[] result = new int[text.length()];

        int[] order = sortCharacters(text); // AG$ -> [2, 0, 1] // $ < A < G
        int[] cls = computeCharClasses(text, order); // 各文字がどのクラスに対応するか // AG$ -> [1, 2, 0]

        int L = 1;

        while (L < text.length()) {
            order = sortDoubled(text, L, order, cls);
            cls = updateClasses(order, cls, L);
            L *= 2;
        }

        return order;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayLong().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffix_array = computeSuffixArray(text);
        print(suffix_array);
    }

    int[] sortCharacters(String text) {
        int[] order = new int[text.length()];
        int[] count = new int[5]; // A, C, G, T, $

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            count[charToInt.get(ch)] += 1;
        }
        for (int i = 1; i < charToInt.keySet().size(); i++) {
            count[i] = count[i - 1] + count[i]; // last position of i-th chracter + 1
        }
        for (int i = text.length() - 1; i >= 0; i--) {
            char ch = text.charAt(i);
            count[charToInt.get(ch)] -= 1;
            order[count[charToInt.get(ch)]] = i;
        }
        return order;
    }

    int[] computeCharClasses(String text, int[] order) {
        int[] cls = new int[text.length()];
        cls[order[0]] = 0;
        for (int i = 1; i < text.length(); i++) {
            char ch = text.charAt(order[i]);
            char chPrev = text.charAt(order[i - 1]);
            if (ch != chPrev) {
                cls[order[i]] = cls[order[i-1]] + 1;
            } else {
                cls[order[i]] = cls[order[i-1]];
            }
        }
        return cls;
    }

    int[] sortDoubled(String text, int L, int[] order, int[] cls) {
        int[] count = new int[text.length()];
        int[] newOrder = new int[text.length()];
        for (int i = 0; i < text.length(); i++) {
            count[cls[i]] += 1;
        }
        for (int i = 1; i < text.length(); i++) {
            count[i] = count[i-1] + count[i];
        }
        for (int i = text.length() - 1; i >= 0; i--) {
            int start = takeMod(order[i] - L, text.length());
            int cl = cls[start];
            count[cl] -= 1;
            newOrder[count[cl]] = start;
        }
        return newOrder;
    }

    int[] updateClasses(int[] newOrder, int[] cls, int L) {
        int n = newOrder.length;
        int[] newCls = new int[n];
        newCls[newOrder[0]] = 0;

        for (int i = 1; i < n; i++) {
            int prev = newOrder[i-1];
            int cur = newOrder[i];
            int prev2 = takeMod(prev + L, n);
            int cur2 = takeMod(cur + L, n);
            if (cls[cur] != cls[prev] || cls[cur2] != cls[prev2]) {
                newCls[cur] = newCls[prev] + 1;
            } else {
                newCls[cur] = newCls[prev];
            }
        }
        return newCls;
    }

    private int takeMod(int x, int m) {
        return (x + m) % m;
    }
}
