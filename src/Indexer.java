import ru.itbrains.gate.morph.MorphInfo;
import ru.itbrains.gate.morph.MorphParser;

import java.io.*;
import java.util.*;

public class Indexer {
  final public String pathToMyStem;
  final public String pathToDocuments;
  final public String pathToIndex;
  final public MorphParser parser;

  public Indexer(String pathToMyStem, String pathToDocuments, String pathToIndex) {
    this.pathToMyStem = pathToMyStem;
    this.pathToDocuments = pathToDocuments;
    this.pathToIndex = pathToIndex;
    this.parser = new MorphParser(this.pathToMyStem);
  }

  public static void main0(String[] args) throws Exception {
    String text = "Что мы тут забыли? Толком не понятно. Возьми со шкафа ручку и записывай: дураки тут ни при чем";
    String[] words = text.split("[ ?.!:]");
    System.out.println(Arrays.toString(words));

    System.out.println("hello world!");
    String folder = "/home/evjava/edownloads/";
    MorphParser parser = new MorphParser(folder);
//    for (String word : words) {
//      MorphInfo info = new MorphInfo(word);
    List<MorphInfo> morphInfos = parser.runParser(text, "utf-8");
    for (MorphInfo morphInfo : morphInfos) {
      System.out.println(morphInfo.getOriginalWord() + " " + morphInfo.getHomonymGrammems());
    }
  }

  public static void main(String[] args) throws IOException {
    String pathToMyStem = args[0], pathToDocuments = args[1], pathToIndex = args[2];
    System.out.printf("path to stem: %s\npath to documents: %s\npath to index: %s\n", args);

    Indexer indexer = new Indexer(pathToMyStem, pathToDocuments, pathToIndex);
    WordIndex index = indexer.recreateIndex();
    index.save(pathToIndex);
  }

  private WordIndex recreateIndex() throws IOException {
    ArrayList<File> docsCollector = new ArrayList<File>();
    Utils.findAllFiles(pathToDocuments, docsCollector);
    Collections.sort(docsCollector);

    List<String> docNames = new ArrayList<String>(docsCollector.size());
    for (File file : docsCollector) {
      docNames.add(file.getAbsolutePath());
    }
    WordIndex index = new WordIndex(docNames);

    System.out.println("current documents: " + docsCollector);
    int i = 0;
    for (File file : docsCollector) {
      assert file.getAbsolutePath().equals(docNames.get(i));
      System.out.print("process file: " + file);

      FileReader in;
      try {
        in = new FileReader(file);
      } catch (FileNotFoundException ex) {
        System.out.println("FileNotFound: " + file);
        continue;
      }
      BufferedReader br = new BufferedReader(in);
      String line;
      int counter = 0;
      while ((line = br.readLine()) != null) {
        for (String word : Utils.normalizedWords(parser, line)) {
          index.addLink(word, counter++, i);
        }
      }
      System.out.println("/// document processed. index status: " + index.status());
      i++;
    }
    return index;
  }

}
