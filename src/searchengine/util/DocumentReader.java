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
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DocumentReader {

  public static void main(String[] args) throws IOException, URISyntaxException, ParserConfigurationException {
    DocumentReader documentReader = new DocumentReader();
    documentReader.readFile(Charset.forName("utf-8"), "../../../Information Retrieval/SearchEngine/cfc-xml");
  }


  public String readFile(Charset encoding, String location) {
    Path dir = Paths.get(location);
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for (Path path : stream) {
        File file = new File(path.toString());
        this.readFile(Charset.forName("utf-8"), file.getName());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("*");
        String content = "";
        for (int i = 0; i < nodeList.getLength(); i++) {
          Node node = nodeList.item(i);
          String attrStr = listAllAttributes(node);
          content += node.getParentNode().getNodeName() + ": " + node.getNodeName() + ": " + node.getTextContent();
          content += attrStr + "\n";
        }
        System.out.println(content);
        return content;

      }
      return null;
    } catch (DirectoryIteratorException | IOException | ParserConfigurationException | SAXException x) {
      System.err.println(x);
      return null;
    }
  }

  public String listAllAttributes(Node element) {

    System.out.println("List attributes for node: " + element);
    // get a map containing the attributes of this node
    NamedNodeMap attributes = element.getAttributes();
    String attrStr = "";
    // get the number of nodes in this map
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


