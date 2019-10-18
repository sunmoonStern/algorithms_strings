import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BurrowsWheelerTransform {
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

    String BWT(String text) {
        StringBuilder result = new StringBuilder();
        int n = text.length();
        String[] texts = new String[n];
        for (int i = 0; i < n; i++) {
            String firstHalf = text.substring(0, i);
            String secondHalf = text.substring(i, n);
            texts[i] = secondHalf + firstHalf;
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (texts[i].compareTo(texts[j]) > 0) {
                    String tmp = texts[j];
                    texts[j] = texts[i];
                    texts[i] = tmp;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            result.append(texts[i].charAt(n - 1));
        }
        return result.toString();
    }

    static public void main(String[] args) throws IOException {
        new BurrowsWheelerTransform().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        System.out.println(BWT(text));
    }
}
