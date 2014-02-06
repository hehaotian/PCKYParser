import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.*;

public class PCFG {
   
   private static String cfg_path;
   // key: lhs; value: list of rhs
   private static Map<String, List<String>> rules = new HashMap<String, List<String>>();
   // key: lhs; value: <rhs, rule_count>
   private static Map<String, Integer> rules_count = new HashMap<String, Integer>();

   public PCFG(String cfg_path) throws IOException {
      this.cfg_path = cfg_path; 
      get_rules(); 
   }
   
   private void get_rules() throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(cfg_path));
      String parseLine = "";
      while ((parseLine = br.readLine()) != null) {
         ArrayList<String> parseTokens = parseTokenize(parseLine);
         put_rules(parseTokens);
      }
   }

   private ArrayList<String> parseTokenize(String line) {
      ArrayList<String> tokens = new ArrayList<String>();
      String[] initTokens = line.split(" ");
      for (int i = 0; i < initTokens.length; i ++) {
         if (initTokens[i].contains("(")) {
            tokens.add("(");
            tokens.add(initTokens[i].replace("(", ""));
         }
         if (initTokens[i].contains(")")) {
            tokens.add(initTokens[i].replace(")", ""));
            String[] chars = initTokens[i].replaceAll("[^)]+", "").split("");
            for (int j = 0; j < chars.length; j ++) {
               tokens.add(")");
            }
         }
      }
      /* DEBUG
      for (int k = 0; k < tokens.size(); k ++) {
         System.out.print(tokens.get(k) + "\t");
      }
      System.out.println(tokens.size());
      */
      return tokens;
   }

   private void put_rules(ArrayList<String> tokens) {
      if (tokens.size() == 5) {
         if (tokens.get(0) == "(" && tokens.get(4) == ")") {
            String rule = tokens.get(1) + " -> " + tokens.get(2) + " " + tokens.get(3);
            String lhs = tokens.get(1);
            String rhs = tokens.get(2) + " " + tokens.get(3);
            if (!rules_count.containsKey(rule)) {
               rules_count.put(rule, 1);
            } else {
               rules_count.put(rule, rules_count.get(rule) + 1);
            }
            if (rules.get(lhs) == null) {
               List<String> temp = new ArrayList<String>();
               rules.put(lhs, temp);
            }
            List<String> temp = rules.get(lhs);
            temp.add(rhs);
            rules.put(lhs, temp);
         }
      } else {
         ArrayList<String> new_tokens = new ArrayList<String>();
         for (int i = 0; i < tokens.size(); i ++) {
            if (tokens.get(i) == "(" && tokens.get(i + 3) == ")") {
               String rule = tokens.get(i + 1) + " -> " + tokens.get(i + 2);
               String lhs = tokens.get(i + 1);
               String rhs = tokens.get(i + 2);
               if (!rules_count.containsKey(rule)) {
                  rules_count.put(rule, 1);
               } else {
                  rules_count.put(rule, rules_count.get(rule) + 1);
               }
               if (rules.get(lhs) == null) {
                  List<String> temp = new ArrayList<String>();
                  rules.put(lhs, temp);
               }
               List<String> temp = rules.get(lhs);
               temp.add(rhs);
               rules.put(lhs, temp);
               new_tokens.add(tokens.get(i + 1));
               // System.out.print(tokens.get(i + 1) + "\t");
               i += 4;
            } else if (tokens.get(i) == "(" && tokens.get(i + 4) == ")") {
               String rule = tokens.get(i + 1) + " -> " + tokens.get(i + 2) + " " + tokens.get(i + 3);
               String lhs = tokens.get(i + 1);
               String rhs = tokens.get(i + 2) + " " + tokens.get(i + 3);
               if (!rules_count.containsKey(rule)) {
                  rules_count.put(rule, 1);
               } else {
                  rules_count.put(rule, rules_count.get(rule) + 1);
               }
               if (rules.get(lhs) == null) {
                  List<String> temp = new ArrayList<String>();
                  rules.put(lhs, temp);
               }
               List<String> temp = rules.get(lhs);
               temp.add(rhs);
               rules.put(lhs, temp);
               new_tokens.add(tokens.get(i + 1));
               // System.out.print(tokens.get(i + 1) + "\t");
               i += 4;
            } else {
               new_tokens.add(tokens.get(i));
               // System.out.print(tokens.get(i) + "\t");
            }
         }
         put_rules(new_tokens);
         // System.out.println("");
      }
   }

   /* DEBUG: PRINT ALL THE RULES WITH THEIR COUNTS
   private void print_rules_count() {
      for (String s : rules_count.keySet()) {
         System.out.println(s + "\t" + rules_count.get(s));
      }
   }
   */
   
   public void build(PrintStream pcfg) {
      for (String s : rules_count.keySet()) {
         String lhs = get_lhs(s);
         double prob = rules_count.get(s) * 1.0 / rules.get(lhs).size();
         pcfg.println(s + " [" + prob + "]");
         // pcfg.println(s);
      }
   }

   private String get_lhs(String rule) {
      String temp = rule.replaceAll("->.+", "");
      return temp.trim();
   }

   private String get_rhs(String rule) {
      String temp = rule.replaceAll(".+->", "");
      return temp.trim();
   }
  
}