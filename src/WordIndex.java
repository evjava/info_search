import com.almworks.integers.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class WordIndex {
  private Map<String, IntAmortizedSet> myWords;
  private List<String> myDocuments;
  private final static String separator = " ___ ";

  public WordIndex(List<String> documents) {
    myWords = new HashMap<String, IntAmortizedSet>();
    myDocuments = documents;
  }

  public WordIndex(String ... documents) {
    this(Arrays.asList(documents));
  }

  // -------------------- dump and read index -------------------

  /**
   * dump index to {@code output}
   *
   * @return success status
   */
  public boolean save(Writer output) {
    PrintWriter out = (output instanceof PrintWriter) ? (PrintWriter) output : new PrintWriter(output);
    out.printf("index, created: %s\n", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
    out.printf("documents %d%s", myDocuments.size(), separator);
    for (String document : myDocuments) {
      out.printf(document);
      out.print(separator);
    }
    out.println();

    for (Map.Entry<String, IntAmortizedSet> entry : myWords.entrySet()) {
      IntAmortizedSet docs = entry.getValue();
      out.printf("%s ", entry.getKey());
      for (IntIterator doc : docs) {
        out.print(doc.value());
        out.print(' ');
      }
      out.println();
    }
    out.println();
    System.out.println("end-of-index");
    return true;
  }

  public boolean save(String outputName) throws IOException {
    return save(new FileWriter(outputName));
  }

  public boolean dumpIndexToScreen() {
    Writer writer = new PrintWriter(System.out, true);
    save(writer);
    try {
      writer.flush();
    } catch (IOException ex) {
      System.out.println("ops...");
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * read index from {@code input}
   *
   * @return success status
   */
  public static WordIndex loadIndex(Reader input) throws Exception {
    BufferedReader br = new BufferedReader(input);
    String line;
    line = br.readLine();
    // todo refactor, add Exception class
    if (!line.startsWith("index")) throw new Exception();

    line = br.readLine();
    if (!line.startsWith("documents")) {
      throw new Exception("second line should starts with documents");
    }
    String[] docs = line.split(separator);
    // todo fix - split by \t
    int expectedCount = Integer.valueOf(docs[0].split(" ")[1]);
    System.out.println(expectedCount + " " + docs.length);
    if (!(expectedCount == docs.length - 1)) {
      throw new Exception("bad with docs:" + Arrays.toString(docs));
    }

    WordIndex index = new WordIndex(Arrays.asList(docs).subList(1, docs.length));

    while ((line = br.readLine()) != null) {
      if (line.equals("")) continue;
      String[] values = line.split(" ");
      String word = values[0];
      for (int i = 1; i < values.length; i++) {
        index.addLink(word, Integer.valueOf(values[i]));
      }
    }
    return index;
  }

  // todo refactor exception
  public static WordIndex loadIndex(String input) throws Exception {
    return loadIndex(new FileReader(input));
  }

  // ------------------- search data in index


  /**
   * @return corresponding name of document
   */
  public String getDocument(int documentId) {
    return myDocuments.get(documentId);
  }

  /**
   * @param documentsId sorted unique sequence of documents
   * @return corresponding names of documents
   */
  public Iterator<String> getDocuments(final IntIterable documentsId) {
    return new Iterator<String>() {
      IntIterator iterator = documentsId.iterator();
      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public String next() {
        return myDocuments.get(iterator.nextValue());
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  /**
   * @return count of documents presented in index
   */
  public int documentsCount() {
    return myDocuments.size();
  }

  public String status() {
    return String.format("index: documents count = <%d> , words count = <%d>", documentsCount(), myWords.size());
  }

  public List<String> getAllDocuments() {
    return myDocuments;
  }

  public Set<String> getAllWords() {
    return myWords.keySet();
  }

  /**
   * @return sorted unique sequence of documents that contain {@code word}
   */
  public IntSizedIterable searchWord(String word) {
    IntAmortizedSet set = myWords.get(word);
    return set == null ? IntSet.EMPTY : set;
  }

  // --------------------- update index

  /**
   * @param word     expected normalized
   * @param document expected correct number [0.. documentsCount)
   * @return true if success otherwise false
   */
  public boolean addLink(String word, int document) {
    if (word.length() == 0) {
      return false;
    }
    IntAmortizedSet set = myWords.get(word);
    if (set == null) {
      set = new IntAmortizedSet();
      set.add(document);
      myWords.put(word, set);
      return true;
    } else {
      return set.include(document);
    }
  }

  public boolean addLink(String word, String document) {
    int docIdx = myDocuments.indexOf(document);

    if (docIdx == -1) {
      throw new IllegalArgumentException();
    }
    return addLink(word, docIdx);
  }
}
