import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import groovy.json.JsonSlurper;

public class TestSession extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession()
		if (session != null) {
			out.println("Session was created !!!");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// Process when received post request
	}

	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) {
		// Process when received put request
	}
}
