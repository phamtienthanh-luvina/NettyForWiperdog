import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse
public class TestInitServlet extends HttpServlet {
	@Override
	public void init() {
		System.out.println("TestInitServlet init ");
		File f = new File("src/test/resources/servlet/TestInit/output.txt")
		if(!f.exists()){
			f.createNewFile();
		}
		f.setText("TestInitServlet init() invoked");
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public void destroy() {
		System.out.println("TestInitServlet destroyed ");
	}
}