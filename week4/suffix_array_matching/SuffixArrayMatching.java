import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class SuffixArrayMatching {
    private static final Map<Character, Integer> charToInt = Collections.unmodifiableMap(new HashMap<Character, Integer>() {
        {
            put('$', 0);
            put('A', 1);
            put('C', 2);
            put('G', 3);
            put('T', 4);
        }
    });

    class fastscanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        fastscanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextint() throws IOException {
            return Integer.parseInt(next());
        }
    }


    public int[] computeSuffixArray(String text) {

        int[] suffixArray = new int[text.length()];
        suffixArray = sortCharacters(text); // AG$ -> [2, 0, 1] // $ < A < G
        int[] cls = computeCharClasses(text,  suffixArray); // 各文字がどのクラスに対応するか // AG$ -> [1, 2, 0]

        int L = 1;
        while (L < text.length()) {
            suffixArray = sortDoubled(text, L,  suffixArray, cls);
            cls = updateClasses( suffixArray, cls, L);
            L *= 2;
        }

        return suffixArray;
    }

    public List<Integer> findOccurrences(String pattern, String text, int[] suffixArray) {
        List<Integer> result = new ArrayList<>();
        int minIndex = 0;
        int maxIndex = text.length();
        int midIndex;

        // find starting index (minIndex)
        while (minIndex < maxIndex) {
            midIndex = (minIndex + maxIndex)/2;
            // suffix
            String suffix = text.substring(suffixArray[midIndex]);
            suffix = suffix.substring(0, suffix.length() - 1);

            // if pattern > suffix
            if (pattern.compareTo(suffix) > 0) {
                minIndex = midIndex + 1;
            } else {
                maxIndex = midIndex;
            }
        }
        int start = minIndex;
        if (start == text.length() - 1) { // index == text.length() - 1 corresponds to $ at the end
            return result;
        }

        // at this point, we found at least one match
        // find ending index (maxIndex)
        maxIndex = text.length();
        while (minIndex < maxIndex) {
            midIndex = (minIndex + maxIndex)/2;
            String suffix = text.substring(suffixArray[midIndex]);
            suffix = suffix.substring(0, suffix.length() - 1);

            if (pattern.compareTo(suffix) < 0) {
                if (suffix.startsWith(pattern)) {
                    minIndex = midIndex; // loop does not stop
                } else {
                    maxIndex = midIndex - 1;
                }
            } else {
                minIndex = midIndex;
            }
            if (minIndex == text.length() - 1) {
                maxIndex = minIndex;
                break;
            }
            if (minIndex == maxIndex - 1) {
                break;
            }
        }
        int end = maxIndex;
        if (start <= end) {
            for (int i = start; i <= end; i++) {
                result.add(suffixArray[i]);
            }
        }
        return result;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayMatching().run();
    }

    public void print(boolean[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public void run() throws IOException {
        fastscanner scanner = new fastscanner();
        String text = scanner.next() + "$";
        int[] suffixArray = computeSuffixArray(text);
        int patternCount = scanner.nextint();
        boolean[] occurs = new boolean[text.length()];
        for (int patternIndex = 0; patternIndex < patternCount; ++patternIndex) {
            String pattern = scanner.next();
            List<Integer> occurrences = findOccurrences(pattern, text, suffixArray);
            for (int x : occurrences) {
                occurs[x] = true;
            }
        }
        print(occurs);
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
