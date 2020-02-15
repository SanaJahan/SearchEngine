package searchengine.util;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class DocumentReader {

  public String readFile(Charset encoding, String location) {
    String output = "";
    File file = new File(location);

    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);
      doc.getDocumentElement().normalize();
      NodeList nodeList = doc.getElementsByTagName("*");
      for (int i = 0; i < nodeList.getLength(); i++) {
        String content = "";
        Node node = nodeList.item(i);
        String attrStr = listAllAttributes(node);
        content +=  " " + node.getTextContent() + " ";
        content += attrStr + "\n";
        content = content.replaceAll("[.|,|-]", " ");
        byte[] encoded = content.getBytes();
        output += new String(encoded, encoding);
      }
      return output;
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
      return null;
    }
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


