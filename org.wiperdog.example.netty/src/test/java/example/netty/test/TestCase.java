package example.netty.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class TestCase {

	@Test
	public void Test1() {
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/servlet");
		listCmd.add("-v");
		ProcessBuilder pb = new ProcessBuilder(listCmd);
		pb.redirectErrorStream(true);
		try {
			Process proc = pb.start();
			BufferedReader bf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = bf.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
