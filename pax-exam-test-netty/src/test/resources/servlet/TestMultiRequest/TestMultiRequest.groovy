import java.net.HttpCookie.CookieAttributeAssignor;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse
public class TestMultiRequest extends HttpServlet {
	@Override
	public void init() {
		System.out.println("TestMultiRequest init ");
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		//everytime request come,the servlet will write the identity hash code of sevlet instance
		//to a text file 
		File instance = new File("src/test/resources/servlet/TestMultiRequest/instance.txt")
		if(!instance.exists()){
			instance.createNewFile()
		}		
		instance.setText(System.identityHashCode(this).toString())
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public void destroy() {
		System.out.println("TestMultiRequest destroyed ");
	}
}