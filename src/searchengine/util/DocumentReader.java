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

import searchengine.tokenizer.Posting;
import searchengine.tokenizer.Tuple;

/**
 * Utility class to read files, create inverted index and so forth.
 */
public class DocumentReader {

  public String readFile(Charset encoding, NodeList nodeList, int index) {
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


