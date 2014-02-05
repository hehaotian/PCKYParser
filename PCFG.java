import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.*;

public class PCFG {
   
   private static String cfg_path;
   // key: lhs; value: list of rhs
   private static Map<String, List<String>> rules = new HashMap<String, List<String>>();
   
   public PCFG(String cfg_path) throws IOException {
      this.cfg_path = cfg_path; 
      get_rules();   
   }
   
   private void get_rules() throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(cfg_path));
      String parseLine = "";
      while ((parseLine = br.readLine()) != null) {
         System.out.println(parseLine);
      }
   }
   
   public void build(PrintStream pcfg) {
      pcfg.println(cfg_path);
   }
   
}