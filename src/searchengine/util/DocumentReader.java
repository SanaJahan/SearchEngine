package searchengine.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DocumentReader {


  public String readFile(Charset encoding, String fileName) throws IOException, URISyntaxException {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    URL url = classloader.getResource(fileName);
    System.out.println(url.toURI().getPath());
    byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));
    return new String(encoded, encoding);
  }

  /*public static void getFiles(String location) {
    Path dir = Paths.get(location);
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for (Path path : stream) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        File file = new File(path.toString());
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        // TODO : 1. Convert nodes to term
        computeNodes(doc.getDocumentElement());

      }
    } catch (DirectoryIteratorException | IOException | ParserConfigurationException | SAXException x) {
      System.err.println(x);
    }
  }

  */
}


