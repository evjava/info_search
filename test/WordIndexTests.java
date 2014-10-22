import com.almworks.integers.LongAmortizedSet;
import com.almworks.integers.LongArray;
import org.junit.Test;
import ru.itbrains.gate.morph.MorphInfo;
import ru.itbrains.gate.morph.MorphParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WordIndexTests {
  @Test
  public void testInputOutputArray() {
    List<String> words = Arrays.asList("asdf", "asdf/vbvb", "asdfasd sdfasdf??!!");

    System.out.println(words);

    System.out.println(LongAmortizedSet.createFromSortedUnique(LongArray.create(0, 2, 5, 10)));
  }

  @Test
  public void testSimple() throws Exception {
    String[] documents = "abcdefghijklmnopqrstuvwxyz".split("");
    WordIndex index = new WordIndex(Arrays.asList(documents).subList(1, documents.length));
    String[] words = ("i am sure you were in a situation in which you wanted to join multiple " +
        "strings if you were using a programming language other than java you probably used the " +
        "join function provided by the programming language if you were using java you could not " +
        "do this there was no join method the java standard class library provided you tools for " +
        "building gui applications accessing databases sending stuff over the network doing xml " +
        "transformations or calling remote methods a simple method for joining a collection of " +
        "strings was not included for this you needed one of various third party libraries").split(" ");
    int counter = 0;
    for (String word : words) {
      index.addLink(word, counter, word.substring(0, 1));
      if (word.length() > 1) {
        index.addLink(word, counter, word.substring(1, 2));
      }
      counter++;
    }
    index.dumpIndexToScreen();
    System.out.println("docs: " + index.getAllDocuments());
    String outputName = "simpleIndex.db";
    FileWriter output = new FileWriter(outputName);
    index.save(output);
    output.close();
  }

  @Test
  public void tryLoadDocument() throws Exception {
    String inputName = "simpleIndex.db";
    WordIndex index = WordIndex.loadIndex(new FileReader(inputName));
    index.dumpIndexToScreen();
  }

//  @Test
  public void test() {
    String[] documents = "abcdefghijklmnopqrstuvwxyz".split("");
    System.out.println(Arrays.toString(documents));
  }

  @Test
  public void test2() throws IOException {
    Indexer indexer = new Indexer("./", "./files", "index.db");
    String pathToMyStem = "./";
    MorphParser parser = new MorphParser(pathToMyStem);
    List<MorphInfo> infos = parser.runParser("тише едешь дальше будешь пете коля сказал вечером", "utf-8");
    for (MorphInfo info : infos) {
      System.out.println(info.getHomonymGrammems().get(0));
    }

  }
}
