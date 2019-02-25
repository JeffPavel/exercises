package websearchexercise;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class ResultFileWriter implements ResultWriter {

	@Override
	public void writeResults(String filename, String regex, List<MatchResult> results) throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		for (MatchResult e :results) {
			if (e.getException() != null) {
				writer.write(String.format("Exception occured processing %s: %s\n", e.getUrl(), e.getException().getMessage()));
			} else if (e.isMatch()) {
				writer.write(String.format("%s contains %s\n", e.getUrl(), regex));
			} else {
				writer.write(String.format("%s does not contain %s\n", e.getUrl(), regex));				
			}
		}
	    writer.close();
	}

}
