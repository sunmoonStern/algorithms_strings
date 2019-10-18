import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InverseBWT {
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
        int n = bwt.length();
        String[] texts = new String[n];
        for (int i = 0; i < n; i++) {
            texts[i] = String.valueOf(chars[i]);
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n; j++) {
                texts[j] = String.valueOf(bwt.charAt(j)) + texts[j];
            }
            texts = sortElems(texts);
        }
        result.append(texts[0].substring(1, n));
        result.append(texts[0].substring(0, 1));
        return result.toString();
    }

    String[] sortElems(String[] texts) {
        int n = texts.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (texts[i].compareTo(texts[j]) > 0) {
                    String tmp = texts[j];
                    texts[j] = texts[i];
                    texts[i] = tmp;
                }
            }
        }
        return texts;
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
