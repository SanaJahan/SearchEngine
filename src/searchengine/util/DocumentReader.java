package searchengine.util;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        content += node.getParentNode().getNodeName() + ": " + node.getNodeName() + ": " + node.getTextContent();
        content += attrStr + "\n";
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
      String attrName = attr.getNodeName();
      String attrValue = attr.getNodeValue();
      attrStr += attrName + " : " + attrValue + "\n";
    }
    return attrStr;

  }

}


