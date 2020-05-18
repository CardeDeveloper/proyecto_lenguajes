import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Controlador {

   private String regularExpression = "";
   private String text = "";
   private DFA automaton;

   public Controlador() {
      automaton = new DFA();
   }

   public void setRegularExpression(String regularExpression) {
      this.regularExpression = regularExpression;
   }

   public String getRegularExpression() {
      return this.regularExpression;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getText() {
      return this.text;
   }

   public Boolean isValidString(String str, DFA automaton, State currenState, int index) {
      if (index == str.length() && currenState.isFinal) {
         return true;
      }
      if (index == str.length() && !currenState.isFinal) {
         // System.out.println(currenState);
         return false;
      }
      char ch = str.charAt(index);

      for (Transition t : automaton.getTransitions()) {

         if (t.getFrom() == currenState.obtenerId()
               && (t.getCharacter() == ch || t.getCharacter() == '&' || t.getCharacter() == 'Δ')) {

            Boolean res = false;

            if (t.getCharacter() == 'Δ') {
               res = isValidString(str, automaton, automaton.obtenerEstadoPorId(t.getTo()), index);
            } else {
               res = isValidString(str, automaton, automaton.obtenerEstadoPorId(t.getTo()), index + 1);
            }

            if (res) {
               return true;
            }
         }
      }
      return false;
   }

   public String compute() {

      String inputStr = this.getText();

      // String regularExp = M1.infixToPostfixNotation("(f-o-r)-(&*)-(\n)");
      String regularExp = M1.infixToPostfixNotation(this.getRegularExpression().replace(',', '|'));
      System.out.println("************** MODULO 1 **************");
      System.out.println("********** NOTACIÓN POSFIJA **********");
      System.out.println(regularExp);

      System.out.println("\n\n");
      System.out.println("************** MODULO 2 **************");
      System.out.println("************** AFN - € **************");
      DFA.IDCOUNTER = 0;
      DFA a = M2.thompson(regularExp);
      for (State s : a.getStates()) {
         System.out.println(s);
      }
      for (Transition t : a.getTransitions()) {
         System.out.println(t);
      }

      System.out.println("\n\n");
      System.out.println("************** MODULO 3 **************");
      System.out.println("**************** AFN *****************");
      M3.epsilonRemover(a);
      for (State s : a.getStates()) {
         System.out.println(s);
      }
      for (Transition t : a.getTransitions()) {
         System.out.println(t);
      }

      System.out.println("\n\n");
      System.out.println("************** MODULO 4 **************");
      System.out.println("**************** AFD *****************");
      M4.AFDConverter(a);
      for (State s : a.getStates()) {
         System.out.println(s);
      }
      for (Transition t : a.getTransitions()) {
         System.out.println(t);
      }

      System.out.println("\n\n");
      System.out.println("************** MODULO 6 **************");
      System.out.println("*************** MATCHS ***************");
      String out = "";
      for (int i = 0; i < inputStr.length(); i++) {
         for (int j = i + 1; j <= inputStr.length(); j++) {

            String sub = inputStr.substring(i, j);

            if (isValidString(sub, a, a.obtenerPrimeroEstado(), 0)) {
               System.out.println("Match: " + sub);
               out += ("Match: " + sub+"\n");
            }
         }
      }

      try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("matches.txt"), "utf-8"))) {
         writer.write(out);
      } catch (IOException ex) {
         // Report
      }

      return "asdfasdf";
   }
}