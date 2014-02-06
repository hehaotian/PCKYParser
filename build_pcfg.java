import java.io.*;
import java.util.*;

public class build_pcfg {
   public static void main(String[] args) throws IOException {
      String cfg_path = args[0];
      PrintStream pcfg = new PrintStream(args[1]);
      
      PCFG grammar = new PCFG(cfg_path);
      grammar.build(pcfg);
   }
}