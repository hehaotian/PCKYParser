import java.io.*;
import java.util.*;

public class parse {
	public static void main(String[] args) throws IOException {
		String pcfg_path = args[0];
		String sent_path = args[1];
		PrintStream ps = new PrintStream(args[2]);

		PCKY parser = new PCKY(pcfg_path);

		BufferedReader br = new BufferedReader(new FileReader(sent_path));
		String sentLine = "";
    	while ((sentLine = br.readLine()) != null) {
    		// String[] tokens = tokenizer(sentLine);
    		String[] tokens = line.split(" ");
			ArrayList<String> parses = parser.best_parse(tokens);

			for (String parse : parses) {
				ps.println(parse);
				ps.println("");
			}
		}
	}
}