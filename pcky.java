import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.*;

public class PCKY {
   
   private static String pcfg_path;
   private static Map<String, Double> rules_prob = new HashMap<String, Double>();
   
   public PCKY(String pcfg_path) throws IOException {
      this.pcfg_path = pcfg_path;
      rule_model();
   }
   
   private void rule_model() throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(pcfg_path));
      String ruleLine = "";
      while ((ruleLine = br.readLine()) != null) {
         String rule = get_rule(ruleLine);
         double prob = get_prob(ruleLine);
         rules_prob.put(rule, prob);
      }
   }

   public ArrayList<String> best_parse(String[] tokens) {
      int sentLength = tokens.length;
   }
   
   private String get_rule(String line) {
      return line.replaceAll("([.]+)([\\[])([0-9\\.]+)([\\]])", "$1");
   }
   
   private double get_prob(String line) {
      String p_str = line.replaceAll("([.]+)([\\[])([0-9\\.]+)([\\]])", "$3");
      return Double.parseDouble(p_str);
   }
}