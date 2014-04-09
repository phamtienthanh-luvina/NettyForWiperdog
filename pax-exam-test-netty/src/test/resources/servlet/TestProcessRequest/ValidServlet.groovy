import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import groovy.json.JsonSlurper;

public class ValidServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String param = request.getParameter("param");
			PrintWriter out = response.getWriter();
			out.println("Test doGet 1 work param: " + param);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		def slurper = new JsonSlurper()
		def dataRequest = slurper.parseText(request.getInputStream().getText())
		PrintWriter out = response.getWriter();
		out.println(dataRequest["doPost"]);
	}

	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) {
		def slurper = new JsonSlurper()
		def dataRequest = slurper.parseText(request.getInputStream().getText())
		PrintWriter out = response.getWriter();
		out.println(dataRequest["doPut"]);
	}
}
