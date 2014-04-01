package org.wiperdog.netty.data;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet2 extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {

			String params2 = this.getInitParameter("params2");
			String urlParam = request.getParameter("param");
			PrintWriter out = response.getWriter();
			out.println("TestServlet2 work params2 : " + params2);
			out.println("TestServlet2 url params: " + urlParam);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {

	}
}

