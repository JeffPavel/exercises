package websearchexercise;

public class MatchResult {

	private String url;
	private boolean match;
	private Exception exception;
	
	public MatchResult(String url, boolean match) {
		this.url = url;
		this.match = match;
	}

	@Override
	public String toString() {
		return "MatchResult [url=" + url + ", match=" + match + ", exception=" + exception + "]";
	}

	public MatchResult(String url, Exception exception) {
		this.url = url;
		this.exception = exception;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isMatch() {
		return match;
	}
	public void setMatch(boolean match) {
		this.match = match;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
		
}
