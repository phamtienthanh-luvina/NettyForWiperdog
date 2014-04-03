import java.net.HttpCookie.CookieAttributeAssignor;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse
public class TestCookieServlet extends HttpServlet {
	@Override
	public void init() {
		System.out.println("TestCookieServlet init ");
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		File f = new File("src/test/resources/servlet/TestCookie/servletReceivedCookie.txt")
		if(!f.exists()){
			f.createNewFile()
		}
		String cookie = ""
		cookies.each {
				cookie += it.getName() +":"+it.getValue() + ";" 
		}
		f.setText(cookie)
		Cookie respCookie = new Cookie("USER_NAME","Wiperdog")
		response.addCookie(respCookie);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public void destroy() {
		System.out.println("TestCookieServlet destroyed ");
	}
}