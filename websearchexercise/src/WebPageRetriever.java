package websearchexercise;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;

public class WebPageRetriever implements Callable<MatchResult> {

	private String urlString;

	public WebPageRetriever(String urlString) {
		this.urlString = urlString;
	}

	@Override
	public MatchResult call() {
		try {
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setInstanceFollowRedirects(true);
			con.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			con.addRequestProperty("User-Agent", "Mozilla");
			con.addRequestProperty("Referer", "google.com");
		
			con.connect();
			boolean redirect = checkForRedirect(con.getResponseCode());

			while (redirect) {
				// get redirect url from "location" header field
				String newUrl = con.getHeaderField("Location");
				WebsiteSearcher.log(String.format("Redirecting from %s to %s ", urlString, newUrl));
				con = (HttpURLConnection) new URL(newUrl).openConnection();
				redirect = false;
				con.connect();
				redirect = checkForRedirect(con.getResponseCode());
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			StringBuilder response = new StringBuilder();
			String inputLine;

			String newLine = System.getProperty("line.separator");
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine + newLine);
			}

            String responseAsString = response.toString();
            Files.write(Paths.get("foo"), responseAsString.getBytes(), StandardOpenOption.CREATE);
            in.close();
			return new MatchResult(urlString,
					WebsiteSearcher.matches(responseAsString, WebsiteSearcher.getPattern()));
		} catch (Exception e) {
			return new MatchResult(urlString, e);
		}
	}

	@Override
	public String toString() {
		return urlString;
	}

	public static boolean checkForRedirect(int status) {
		return (status != HttpURLConnection.HTTP_OK && (status == HttpURLConnection.HTTP_MOVED_TEMP
				|| status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER));
	}

}
