import ru.itbrains.gate.morph.MorphInfo;
import ru.itbrains.gate.morph.MorphParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.io.StreamTokenizer.TT_EOF;
import static java.io.StreamTokenizer.TT_NUMBER;
import static java.io.StreamTokenizer.TT_WORD;

public class Utils {

  private static boolean isValid(String name) {
    return name.endsWith(".txt");
  }

  public static void findAllFiles(String path, List<File> collector) {
    File root = new File( path );

    File[] list = root.listFiles();

    if (list == null) return;
    for ( File f : list ) {
      if ( f.isDirectory() ) {
        findAllFiles(f.getAbsolutePath(), collector);
      } else {
        if (isValid(f.getName())) {
          collector.add(f);
        }
      }
    }
  }

  public static Iterable<String> parseFile(File file) {
    FileReader reader;
    try {
      reader = new FileReader(file);
    } catch (FileNotFoundException ex) {
      System.out.println("file not found: " + file);
      assert false;
      return null;
    }
    final StreamTokenizer in = new StreamTokenizer(new BufferedReader(reader));

    final Iterator<String> iterator = new Iterator<String>() {
      int curToken = -1;

      {
        advanceIterator();
      }

      private void advanceIterator() {
        try {
          curToken = in.nextToken();
          while (curToken != TT_WORD && curToken != TT_NUMBER && curToken != TT_EOF) {
            curToken = in.nextToken();
          }
        } catch (IOException ex) {
          System.out.println("fail!");
          curToken = TT_EOF;
        }
      }

      @Override
      public boolean hasNext() {
        return curToken == TT_WORD || curToken == TT_NUMBER;
      }

      @Override
      public String next() {
        assert curToken == TT_WORD || curToken == TT_NUMBER;
        String token = curToken == TT_WORD ? in.sval : Double.toString(in.nval);
        advanceIterator();
        return token;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };

    return new Iterable<String>() {
      @Override
      public Iterator<String> iterator() {
        return iterator;
      }
    };
  }


  static String tokenize(String string) {
    System.out.println(string);
    int i = 0;
    string = string.toLowerCase();
    while (i < string.length() && !isLetter(string.charAt(i))) {
      i++;
    }

    int j = string.length() - 1;
    while (j >= 0 && !isLetter(string.charAt(j))) {
      j--;
    }

    return i < j ? string.substring(i, j + 1) : "";
  }

  private static boolean isLetter(char ch) {
    return ('a' <= ch && ch <= 'z') || ('а' <= ch && ch <= 'я');
  }

  /**
   * @param line
   * @return unique sequence of normalized words
   * @throws java.io.IOException
   */
  public static Iterable<String> normalizedWords(MorphParser parser, String line) throws IOException {
    ArrayList<String> res = new ArrayList<String>();

    List<MorphInfo> morphInfos = parser.runParser(line, "utf-8");

    for (MorphInfo info : morphInfos) {
      for (Map<String, String> map : info.getHomonymGrammems()) {
        String baseForm = map.get("baseForm");
        if (!res.contains(baseForm)) {
          res.add(baseForm);
        }
      }
    }
    return res;
  }
}
