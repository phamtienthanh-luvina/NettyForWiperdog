package example.netty;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {

			String params1 = this.getInitParameter("params1");
			System.out.println("go into TestServlet.doGet");
			PrintWriter out = response.getWriter();
			out.println("TestServlet work params1 : " + params1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {

	}
}
