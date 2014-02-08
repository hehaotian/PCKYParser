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
      String best_parse = "";
      ArrayList<String> possible_parses = new ArrayList<String>();

      Map<Integer, Map<Integer, Map<String, Double>>> table = new HashMap<Integer, Map<Integer, Map<String, Double>>>();
      List<Tree> forest = new ArrayList<Tree>();

      for (int j = 1; j < sentLength; j ++) {
         String curWord = words.get(j);
         
         table.put(j - 1, new HashMap<Integer, Map<String, Double>>());   
         List<String> a = new ArrayList<String>();     
         if (back_rules.containsKey(curWord)) {
            a = back_rules.get(curWord); 
            /* DEBUG: traverse the back_rules
            for (int i = 0; i < a.size(); i ++) {
               System.out.println(a.get(i));
            }
            */
         } else {
            break;
         }
         Map<String, Double> prod_probs = new HashMap<String, Double>();
         prod_probs = get_prod_probs(a, curWord, forest);
         table.get(j - 1).put(j, prod_probs);

         for (int i = j - 2; i >= 0; i --) {
            for (int k = i + 1; k <= j - 1; k ++) {
               Map<String, Double> abc = new HashMap<String, Double>();
               
               if (table.get(i).get(k) != null && table.get(k).get(j) != null) {
                  Map<String, Double> b_tab = table.get(i).get(k);
                  Map<String, Double> c_tab = table.get(k).get(j); 
                  for (String b : b_tab.keySet()) {
                     for (String c : c_tab.keySet()) {
                        String temp_rhs = b + c;
                        // System.out.println(temp_rhs);
                        if (back_rules.containsKey(temp_rhs)) {
                           if (back_rules.containsKey(temp_rhs)) {
                              List<String> a_list = back_rules.get(temp_rhs);
                              for (int p = 0; p < a_list.size(); p ++) {
                                 String temp_rule = a_list.get(p) + "->" + b + c;
                                 String real_rule = a_list.get(p) + " -> " + b + " " + c + " ";
                                 double temp_rule_prob = rules_prob.get(temp_rule);
                                 // System.out.println(real_rule);
                                 abc.put(real_rule, temp_rule_prob);
                                 Tree t = new Tree(a_list.get(p), b, c, temp_rule_prob);
                                 forest.add(t);
                              }
                           }                             
                        }
                     }
                  }
               }

               for (String rule : abc.keySet()) {
                  String curA = get_lhs(rule);
                  String curBC = get_rhs(rule);
                  String curB = get_b(curBC);
                  String curC = get_c(curBC);
                  if (table.get(i).get(k).get(curB) != null && table.get(k).get(j).get(curC) != null) {
                     String abc_rule = curA + "->" + curB + curC;
                     double abc_prob = rules_prob.get(abc_rule);
                     double temp_prob = table.get(i).get(k).get(curB) * table.get(k).get(j).get(curC) * abc_prob;
                     // System.out.println(abc_rule + "\t" + abc_prob + "\t" + temp_prob);
                     if (table.get(i) == null) {
                        table.put(i, new HashMap<Integer, Map<String, Double>>());
                     }
                     if (table.get(i).get(j) == null) {
                        Map<String, Double> ij_map = new HashMap<String, Double>();
                        ij_map.put(curA, 0.0);
                        table.get(i).put(j, ij_map);
                     }

                     if (table.get(i).containsKey(j) && table.get(i).get(j).containsKey(curA)) {                        
                        if (table.get(i).get(j).get(curA) < temp_prob) {
                           table.get(i).get(j).put(curA, temp_prob);
                        }
                     }
                  }
               }
            }
         }
      }
      /* DEBUG: TABLE AND FOREST
      System.out.println(table);
      */
      for (int q = 0; q < forest.size(); q ++) {
         Tree temp_tree = forest.get(q);
         temp_tree.print();
      }
       
      System.out.println(table);
      best_parse = backpointer(forest);
      return best_parse;
   }
   
   private String get_rule(String line) {
      String[] tokens = line.split(" ");
      String rule = "";
      for (int i = 0; i < tokens.length - 1; i ++) {
         rule += tokens[i];
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

   private Map<String, Double> get_prod_probs(List<String> left, String right, List<Tree> forest) {
      Map<String, Double> prod_probs = new HashMap<String, Double>();
      for (int i = 0; i < left.size(); i ++) {
         String rule = left.get(i) + "->" + right;
         double prob = rules_prob.get(rule);
         prod_probs.put(left.get(i), rules_prob.get(rule));
         Tree t = new Tree(left.get(i), right, rules_prob.get(rule));
         forest.add(t);
      }
      return prod_probs;
   }

   private List<String> sort_bc(List<String> bc) {
      List<String> real_bc = new ArrayList<String>();
      for (int i = 0; i < bc.size(); i ++) {
         String[] tokens = bc.get(i).split(" ");
         if (tokens.length > 1) {
            real_bc.add(bc.get(i));
         }
      }
      return real_bc;
   }

   private String get_b(String bc) {
      String[] tokens = bc.split(" ");
      String b = tokens[0].trim();
      return b;
   }

   private String get_c(String bc) {
      String[] tokens = bc.split(" ");
      String c = tokens[1].trim();
      return c;
   }

   private String backpointer(List<Tree> forest) {
      String parse = "";
      if (forest.size() != 0) {
         for (int i = 0; i < forest.size(); i ++) {

         }
      }
      return parse;
   }

}