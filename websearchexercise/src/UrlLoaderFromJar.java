package websearchexercise;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UrlLoaderFromJar implements UrlLoader {

	private String filename;
	
	public UrlLoaderFromJar(String filename) {
	   this.filename = "/" + filename;
	}
	
	@Override
	public List<String> getUrls() throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
		try (Stream<String> stream = br.lines()) {
			return stream
					.skip(1)
					.map(str -> str.split(","))
					.map(arr -> "http://" + arr[1].replaceAll("\"", ""))
					.collect(Collectors.toList());
		}
	}
}