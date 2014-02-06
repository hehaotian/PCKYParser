import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.*;

public class PckyParser {
   
   private static String pcfg_path;
   private static Map<String, Double> rules_prob = new HashMap<String, Double>();
   
   public PckyParser(String pcfg_path) throws IOException {
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

   public String best_parse(String[] tokens) {
      int sentLength = tokens.length;
      String best_parse = "";

      

      return best_parse;
   }
   
   private String get_rule(String line) {
      String[] tokens = line.split(" ");
      String rule = "";
      for (int i = 0; i < tokens.length - 1; i ++) {
         rule += tokens[i] + " ";
      }
      System.out.println(rule);
      return rule;
   }
   
   private double get_prob(String line) {
      String[] tokens = line.split(" ");
      String p_str = tokens[tokens.length - 1].replaceAll("[\\[\\]]", "");
      double prob = Double.parseDouble(p_str);
      System.out.println(p_str);
      return prob;
   }
}