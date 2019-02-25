package websearchexercise;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class WebsiteSearcher {
	final static String regex = "config";
	final static String caseInsensitiveRegex = "(?is).*" + regex + ".*";

	public static void main(String[] argv) {
		final String input = "urls.txt";
		final String output = "results.txt";

		UrlLoader urlLoader = new UrlLoaderFromJar(input);

		try {
			List<String> urls = urlLoader.getUrls();
			ConcurrencyController pool = new ConcurrencyController(20);
			Collection<Callable<MatchResult>> tasks = urls.stream().map(WebPageRetriever::new)
					.collect(Collectors.toList());
			pool.invokeTasks(tasks);
			List<MatchResult> results = pool.getResults();
			ResultWriter writer = new ResultFileWriter();

			writer.writeResults(output, regex, results);
			WebsiteSearcher.log("Done.");
		} catch (Exception e) {
			WebsiteSearcher.log("File not found " + input, true);
			e.printStackTrace();
		}
	}

	public static boolean matches(String str, String pattern) {
		return str.matches(pattern);
	}

	public static String getPattern() {
		return caseInsensitiveRegex;
	}

	public static void log(String str) {
		log(str, false);
	}

	public static void log(String str, boolean error) {
		if (error) {
			System.err.println(str);
		} else {
			System.out.println(str);
		}
	}
}