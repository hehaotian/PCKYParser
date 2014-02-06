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
}