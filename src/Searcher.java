import com.almworks.integers.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Searcher {
  // todo refactor
  public static IntIterable parseExpression(String string, WordIndex index) {
    string = string.toLowerCase().trim();
    String[] words = string.split(" ");
    if (words.length % 2 == 0) {
      throw new IllegalArgumentException("should be string [a or b or c or d] or other operations");
    }
    if (words.length == 1) {
      return index.searchWord(words[0]);
    }
    String op = words[1];
    if (!(op.equals("and") || op.equals("or") || op.equals("или") || op.equals("и"))) {
      throw new IllegalArgumentException("should be operation <and/и> or <or/или>");
    }
    List<IntIterable> documents = new ArrayList<IntIterable>(words.length / 2 + 1);

    for (int i = 0; i < words.length; i++) {
      if (i % 2 == 0) {
        documents.add(index.searchWord(words[i]));
      } else {
        if (!words[i].equals(op)) {
          throw new IllegalArgumentException("should be the same operator");
        }
      }
    }
    if (op.equals("and") || op.equals("и")) {
      return new IntIntersectionIterator(documents);
    } else {
      return new IntUnionIterator(documents);
    }
  }

  /**
   * @param args expected that args[0] is path to index
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    WordIndex index = WordIndex.loadIndex(args[0]);

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter String(enter 'exit' or 'выход' for exit)");
    String s;
    while (!(s = br.readLine()).equals("exit")) {
      IntIterator documents;
      try {
        documents = parseExpression(s, index).iterator();
      } catch (IllegalArgumentException ex) {
        System.out.println("   illegal expression: " + ex.getMessage());
        continue;
      }
      if (!documents.hasNext()) {
        System.out.println("    No documents found");
      } else {
        System.out.print("    found ");
        for (IntIterator doc : documents) {
          String document = extractName(index.getDocument(doc.value()));
          System.out.print(document + ", ");
        }
        System.out.println();
      }
    }
  }

  private static String extractName(String document) {
    int idx = document.lastIndexOf("/");
    return document.substring(idx + 1, document.length());
  }
}
