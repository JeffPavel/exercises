package websearchexercise;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UrlLoaderFromFile implements UrlLoader {

	private String filename;
	
	public UrlLoaderFromFile(String filename) {
	   this.filename = filename;
	}
	
	@Override
	public List<String> getUrls() throws Exception {
		
		try (Stream<String> stream = Files.lines(Paths.get(filename))) {
			return stream
					.skip(1)
					.map(str -> str.split(","))
					.map(arr -> "http://" + arr[1].replaceAll("\"", ""))
					.collect(Collectors.toList());
		}
	}
}