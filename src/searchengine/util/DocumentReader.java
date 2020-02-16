package searchengine.util;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class DocumentReader {

  public String readFile(Charset encoding, NodeList nodeList, int index) {
    String output = "";
    String content = "";
    Node node = nodeList.item(index);
    String attrStr = listAllAttributes(node);
    content +=  node.getTextContent();
    content += attrStr + "\n";
    content = content.replaceAll("[.|,|-]", " ");
    byte[] encoded = content.getBytes();
    output = new String(encoded, encoding);
    return output;
  }

  public String listAllAttributes(Node element) {
    NamedNodeMap attributes = element.getAttributes();
    String attrStr = "";
    int numAttrs = attributes.getLength();
    for (int i = 0; i < numAttrs; i++) {
      Attr attr = (Attr) attributes.item(i);
      String attrValue = attr.getNodeValue();
      attrStr += attrValue + "\n";
    }
    return attrStr;

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

}


