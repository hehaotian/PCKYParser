import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.*;

public class PCFG {
   
   private static String cfg_path;
   // key: lhs; value: list of rhs
   private static Map<String, ArrayList<String>> rules = new HashMap<String, ArrayList<String>>();
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
            if (!rules_count.containsKey(rule)) {
               rules_count.put(rule, 1);
            } else {
               rules_count.put(rule, rules_count.get(rule) + 1);
            }
         }
      } else {
         ArrayList<String> new_tokens = new ArrayList<String>();
         for (int i = 0; i < tokens.size(); i ++) {
            if (tokens.get(i) == "(" && tokens.get(i + 3) == ")") {
               String rule = tokens.get(i + 1) + " -> " + tokens.get(i + 2);
               if (!rules_count.containsKey(rule)) {
                  rules_count.put(rule, 1);
               } else {
                  rules_count.put(rule, rules_count.get(rule) + 1);
               }
               new_tokens.add(tokens.get(i + 1));
               System.out.print(tokens.get(i + 1) + "\t");
               i += 4;
            } else if (tokens.get(i) == "(" && tokens.get(i + 4) == ")") {
               System.out.println("works!");
               String rule = tokens.get(i + 1) + " -> " + tokens.get(i + 2) + " " + tokens.get(i + 3);
               if (!rules_count.containsKey(rule)) {
                  rules_count.put(rule, 1);
               } else {
                  rules_count.put(rule, rules_count.get(rule) + 1);
               }
               new_tokens.add(tokens.get(i + 1));
               System.out.print(tokens.get(i + 1) + "\t");
               i += 5;
            } else {
               new_tokens.add(tokens.get(i));
               System.out.print(tokens.get(i) + "\t");
            }
         }
         put_rules(new_tokens);
         System.out.println("");
      }
   }
   
   public void build(PrintStream pcfg) {
      pcfg.println(cfg_path);
   }
   
}