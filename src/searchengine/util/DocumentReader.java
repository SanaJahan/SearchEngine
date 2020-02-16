package searchengine.util;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import searchengine.tokenizer.Posting;
import searchengine.tokenizer.Tuple;


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

  public void printInvertedIndex(HashMap<String, Tuple> map) {
    PrintWriter out = null;
    try {
      out = new PrintWriter(new BufferedWriter(new FileWriter("data.properties", true)));
     /* *//* get value by key *//*
      InputStream input = new FileInputStream("data.properties");
      Properties prop = new Properties();
      // load a properties file
      prop.load(input);*/
      for (Map.Entry<String, Tuple> entry : map.entrySet()) {
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


