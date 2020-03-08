package searchengine;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import searchengine.tokenizer.Token;

@WebServlet(name = "mainservlet", urlPatterns = "/SelectFile")
public class MainServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		File f = new File("../resource/stoplist.txt");
		System.out.println(f.getAbsolutePath());
		String file = req.getParameter("myfile");
		Token token = new Token();

		String indexedResults = token.readFromFile(file);

		req.setAttribute("result", file);
		RequestDispatcher view = req.getRequestDispatcher("result.jsp");
		view.forward(req, resp);
	}
}
