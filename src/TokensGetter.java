import java.io.*;
import java.util.Iterator;

public class TokensGetter {
  public static Iterator<String> read(File file) throws FileNotFoundException {
    StreamTokenizer in = new StreamTokenizer(new BufferedReader(new FileReader(file)));
    return null;
  }
}
