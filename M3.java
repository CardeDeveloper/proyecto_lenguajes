import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class M3 {

    public static void DFS(int state, List<Transition> transitions, boolean[] visited, List<Integer>[] epsilon_c,
            List<Integer> controlList) {
        visited[state] = true;
        controlList.add(state);
        if (!epsilon_c[state].contains(state)) {
            epsilon_c[state].add(state);
        }
        // epsilon_c[state].add(state);
        // System.out.println(state);

        Iterator<Transition> ite = transitions.listIterator();
        while (ite.hasNext()) {
            Transition next = ite.next();
            // See if transition.begin is equal to id
            // if it is, then see if the transition is
            // with epsilon, if this is also true, then
            // go that transition.end with DFS after
            // adding the .end to the epsilon_c list
            // with index state
            if (next.getFrom() == state && next.getCharacter() == 'Δ') {
                // System.out.println(state + "=" + next.getFrom() + " -> " + next.getTo());
                for (int i = 0; i < controlList.size(); i++) {
                    // if(!visited[next.getTo()]) DFS(next.getTo(), transitions, visited, epsilon_c,
                    // controlList);
                    if (!epsilon_c[controlList.get(i)].contains(next.getTo())) {
                        epsilon_c[controlList.get(i)].add(next.getTo());
                    }
                } // 0 --> 5 -e-> 6 -e-> 4
                  // epsilon_c[i].add()

                // for(int i = 0; i < DFA.IDCOUNTER; i++){
                // System.out.println("DESPUES DE STATE " + state + ": " +
                // epsilon_c[i].toString());
                // }

                if (!visited[next.getTo()]) {
                    DFS(next.getTo(), transitions, visited, epsilon_c, controlList);
                    // controlList.remove(controlList.size() - 1);
                    // visited[next.getTo()] = false;
                }
            }
        }
        controlList.remove(controlList.size() - 1);
    }

    public static void epsilonRemover(DFA automaton) {
        boolean[] visited = new boolean[DFA.IDCOUNTER];
        List<Integer>[] epsilon_c = new List[DFA.IDCOUNTER];
        List<Integer> controlList = new ArrayList<Integer>();

        for (int i = 0; i < DFA.IDCOUNTER; i++)
            epsilon_c[i] = new ArrayList<Integer>();

        DFS(automaton.obtenerPrimeroEstado().obtenerId(), automaton.getTransitions(), visited, epsilon_c, controlList);

        for (int i = 0; i < DFA.IDCOUNTER; i++) {
            visited = new boolean[DFA.IDCOUNTER/* + 1 */];

            if (automaton.obtenerEstadoPorId(i) != null && !visited[i]) {
                DFS(i, automaton.getTransitions(), visited, epsilon_c, controlList);
            }
        }
        // System.out.println("epsilons: ");
        for (int i = 0; i < DFA.IDCOUNTER; i++) {
            // System.out.println(i + ": " + epsilon_c[i].toString());
        }

        // Iterate through states, then through
        // transitions and remove epsilon transitions
        // after that, iterate through alphabet, and do the
        // e-c ((d(e-c(Qn)), a)), function, adding the new
        // transitions

        Iterator<State> iteStates = automaton.getStates().listIterator();
        while (iteStates.hasNext()) {
            State nextState = iteStates.next();
            int id = nextState.obtenerId();

            // remove epsilon transitions
            Iterator<Transition> iteTrans = automaton.getTransitions().listIterator();
            while (iteTrans.hasNext()) {
                Transition trans = iteTrans.next();
                if (trans.getFrom() == id && trans.getCharacter() == 'Δ') {
                    iteTrans.remove();
                }
            }
            Iterator<Character> iteAlphabet = automaton.getAlphabet().listIterator();

            while (iteAlphabet.hasNext()) {
                // symbol to proccess
                char symbol = iteAlphabet.next();

                // iterator epsilon clousure of the current estate
                Iterator<Integer> epsilonTrans = epsilon_c[id].listIterator();

                while (epsilonTrans.hasNext()) {
                    int estado = epsilonTrans.next();

                    iteTrans = automaton.getTransitions().listIterator();
                    List<Transition> tempTrans = new ArrayList<Transition>();
                    while (iteTrans.hasNext()) {
                        Transition trans = iteTrans.next();
                        if (trans.getFrom() == estado && trans.getCharacter() == symbol) {

                            int estado2 = trans.getTo(); // Checar el e-c de este y agregar transiciones
                            Iterator<Integer> epsilonTrans2 = epsilon_c[estado2].listIterator();

                            while (epsilonTrans2.hasNext()) {
                                int estadoTrans = epsilonTrans2.next();
                                tempTrans.add(new Transition(id, estadoTrans, symbol));
                            }

                        }
                    }
                    automaton.addTransitions(tempTrans);
                }
            }
        }
        // System.out.println("--------------------");
        for (Transition t : automaton.getTransitions()) {
            // System.out.println(t);
        }
    }

}