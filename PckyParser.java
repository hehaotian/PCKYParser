import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.*;

public class PckyParser {
   
   private static String pcfg_path;
   // key: rule; value: probability
   private static Map<String, Double> rules_prob = new HashMap<String, Double>();
   // key: rhs; value: list of lhs
   private static Map<String, List<String>> back_rules = new HashMap<String, List<String>>();

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

         String lhs = get_lhs(rule);
         String rhs = get_rhs(rule);
         if (back_rules.get(rhs) == null) {
            List<String> temp = new ArrayList<String>();
            back_rules.put(rhs, temp);
         }
         List<String> temp = back_rules.get(rhs);
         temp.add(lhs);
         back_rules.put(rhs, temp);
      }
      /* DEBUG: rules_prob map
      for (String s : rules_prob.keySet()) {
         System.out.println(s + "\t" + rules_prob.get(s));
      }
      */
      /* DEBUG: back_rules map
      for (String s : back_rules.keySet()) {
         System.out.println(s + "\t" + back_rules.get(s));
      }
      */
   }

   public String best_parse(String[] tokens) {
      ArrayList<String> words = sort(tokens);
      int sentLength = words.size();
      String best_parse = "yes!";
      ArrayList<String> possible_parses = new ArrayList<String>();

      Map<Integer, Map<Integer, Map<String, List<String>>>> table = new HashMap<Integer, Map<Integer, Map<String, List<String>>>>();

      for (int j = 1; j < sentLength; j ++) {
         String curWord = words.get(j);
         
         table.put(j - 1, new HashMap<Integer, Map<String, List<String>>>());
         table.get(j - 1).put(j, new HashMap<String, List<String>>());
         List<String> possible_a = back_rules.get(curWord);
         table.get(j - 1).get(j).put(curWord, possible_a);

         for (int i = j - 2; i >= 0; i --) {
            for (int k = i + 1; k <= j - 1; k ++) {

            }
         }

      }

      return best_parse;
   }
   
   private String get_rule(String line) {
      String[] tokens = line.split(" ");
      String rule = "";
      for (int i = 0; i < tokens.length - 1; i ++) {
         rule += tokens[i] + " ";
      }
      // System.out.println(rule);
      return rule;
   }
   
   private double get_prob(String line) {
      String[] tokens = line.split(" ");
      String p_str = tokens[tokens.length - 1].replaceAll("[\\[\\]]", "");
      double prob = Double.parseDouble(p_str);
      // System.out.println(p_str);
      return prob;
   }

   private String get_lhs(String rule) {
      String temp = rule.replaceAll("->.+", "");
      return temp.trim();
   }

   private String get_rhs(String rule) {
      String temp = rule.replaceAll(".+->", "");
      return temp.trim();
   }

   private ArrayList<String> sort(String[] tokens) {
      ArrayList<String> words = new ArrayList<String>();
      words.add("void");
      for (String t : tokens) {
         words.add(t);
      }
      return words;
   }
}