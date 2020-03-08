package searchengine.util;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import searchengine.ranking.CosineSimilarity;
import searchengine.ranking.CosineSimilarityImpl;
import searchengine.tokenizer.Posting;
import searchengine.tokenizer.Tuple;

/**
 * Utility class to read files, create inverted index and so forth.
 */
public class DocumentReader {

  private CosineSimilarity cosineSimilarity = new CosineSimilarityImpl();
  public static int TOTAL_DOCUMENTS = 0;



  public String readFile(Charset encoding, NodeList nodeList, int index) {
    TOTAL_DOCUMENTS++;
    String output;
    String content = "";
    Node node = nodeList.item(index);
    String attrStr = listAllAttributes(node);
    content +=  node.getTextContent();
    content += attrStr + "\n";
    // lemma
    content = content.replaceAll("[.|,-]", " ");
    byte[] encoded = content.getBytes();
    output = new String(encoded, encoding);
    return output;
  }

  private String listAllAttributes(Node element) {
    NamedNodeMap attributes = element.getAttributes();
    StringBuilder attrStr = new StringBuilder();
    int numAttrs = attributes.getLength();
    for (int i = 0; i < numAttrs; i++) {
      Attr attr = (Attr) attributes.item(i);
      String attrValue = attr.getNodeValue();
      attrStr.append(attrValue).append("\n");
    }
    return attrStr.toString();

  }

  public String normalizeStr(String input) {
    input = input.replaceAll("(?:&#[0-9]*;)", " ");
    input = input.replaceAll(",*", "");
    input = input.replaceAll("\\.*", "");
    input = input.replaceAll("\n|\r", " ");
    input = input.replaceAll("\"", "");
    input = input.replaceAll("&lt;", "");
    input = input.replaceAll("&gt;", "");
    input = input.replaceAll("\\+", "");
    input = input.replaceAll("\\(|\\)", "");
    input = input.replaceAll("\\*", "");
    input = input.replaceAll("'", "");
    input = input.replaceAll("&amp;", "");
    input = input.replaceAll("-", "");
    return input;
  }

  public String[] getStopWords(String filename) throws IOException {
    FileReader fileReader = new FileReader(filename);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    List<String> lines = new ArrayList<>();
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      lines.add(line);
    }
    bufferedReader.close();
    return lines.toArray(new String[lines.size()]);
  }

  // to display in html file
  public String writeToHTML(HashMap<java.lang.String, Tuple> map) {
    StringBuilder mapAsString = new StringBuilder("{");

    map.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEachOrdered(x ->
                    mapAsString.append(x.getKey() + "=" + x.getValue() + ", ")
            );
    /* mapAsString.delete(mapAsString.length()-1,*/ mapAsString.append("}");
    return mapAsString.toString();
  }
  //To be noted: Hashmap resizes internally so no need to resize if the dictionary is full
  public void createInvertedIndex(HashMap<String, Tuple> map) {
    // sort the index before writing it into the disk.
    LinkedHashMap<String, Tuple> sortedMap = new LinkedHashMap<>();
    map.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

    PrintWriter out = null;
    try {
      out = new PrintWriter(new BufferedWriter(new FileWriter("index.properties", true)));
      for (Map.Entry<String, Tuple> entry : sortedMap.entrySet()) {
        out.print(entry.getKey() + "= " + entry.getValue().getFrequencyOfTerms() + " ");
        for (Posting p : entry.getValue().getPostings()) {
          // store the tf for every term
            double tf = cosineSimilarity.calculateTF(p,entry.getKey());
            p.getSpecificTermFreq().put(entry.getKey(),tf);
            out.print( " " + p.getDocumentID() );
        }
        out.println();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }
}


