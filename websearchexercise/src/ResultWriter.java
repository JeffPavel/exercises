package websearchexercise;

import java.util.List;

public interface ResultWriter {
	  public void writeResults(String filename, String regex, List<MatchResult> results)  throws Exception;
}
