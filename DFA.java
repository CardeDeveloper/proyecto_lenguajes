import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DFA {

    private ArrayList<State> states = null;
    private ArrayList<Transition> transitions = null;
    private State initialState = null;
    private State finalState = null;
    private List<Integer> finalStates = null;
    public static int IDCOUNTER = 0;
    private List<Character> alphabet = null;

    public DFA() {
        this.states = new ArrayList<State>();
        this.transitions = new ArrayList<Transition>();
        this.finalStates = new ArrayList<Integer>();
        this.alphabet = new ArrayList<Character>();
    }

    public List<Character> getAlphabet() {
        return this.alphabet;
    }

    public void addState(State state) {

        this.states.add(state);
        if (state.isFinal) {
            this.finalStates.add(state.obtenerId());
        }

        if (state.isInitial && this.initialState == null) {
            this.initialState = state;
        }
        if (!this.states.contains(state)) {
            this.states.add(state);
        }
    }

    public void addStateAndFinal(State state) {
        if (!this.states.contains(state)) {
            this.states.add(state);
        }
        if (state.getFinal())
            this.finalStates.add(state.obtenerId());
    }

    public void añadirEstado(State... states) {
        for (State state : states) {

            this.states.add(state);
            if (state.isFinal) {
                this.finalStates.add(state.obtenerId());
            }

            if (state.isInitial && this.initialState == null) {
                this.initialState = state;
            }

        }
    }

    public List<Transition> removeAllTransitions() {
        ArrayList<Transition> tmp = (ArrayList<Transition>) this.transitions.clone();
        this.transitions.clear();
        return tmp;
    }

    public void agregarTransicion(Transition transition) {
        if (!this.alphabet.contains(transition.getCharacter())) {
            this.alphabet.add(transition.getCharacter());
        }
        this.transitions.add(transition);
    }

    public void addTransitions(Transition... transitions) {
        for (Transition transition : transitions) {
            this.transitions.add(transition);
        }
    }

    public void addSymbolToAlphabet(char symbol) {
        this.alphabet.add(symbol);
    }

    public void setAlphabet(List<Character> alphabet) {
        this.alphabet = alphabet;
    }

    public void addTransitions(List<Transition> list) {
        Iterator<Transition> ite = list.listIterator();
        while (ite.hasNext()) {
            Transition tmp = ite.next();
            if (!this.transitions.contains(tmp)) {
                this.transitions.add(tmp);
            }
        }
        // this.transitions.addAll(list);
    }

    public void addStates(List<State> list) {
        Iterator<State> ite = list.listIterator();
        while (ite.hasNext()) {
            State tmp = ite.next();
            if (!this.states.contains(tmp)) {
                this.states.add(tmp);
            }
        }
    }

    public State obtenerUltimoEstado() {
        return this.finalState;
    }

    public void setInitialState(State state, boolean changeState) {
        this.initialState = (this.initialState == null || changeState) ? state : this.initialState;
        state.setInitial(true);
    }

    public void definirEstadoFinal(State state, boolean changeState) {
        this.finalState = (this.finalState == null || changeState) ? state : this.finalState;
        state.setFinal(true);
        this.finalStates.add(state.obtenerId());
    }

    public void removeFinalState() {
        this.finalState = null;
    }

    public int getNumberOfStates() {

        return this.states.size();
    }

    public List<State> getStates() {
        return this.states;
    }

    public List<Transition> getTransitions() {
        return this.transitions;
    }

    public State obtenerPrimeroEstado() {
        return this.initialState;
    }

    public int transitionsFunction(int from, char symbol) {
        int state = -1;
        for (int i = 0; i < this.getTransitions().size(); i++) {
            if (this.getTransitions().get(i).getCharacter() == symbol
                    && this.getTransitions().get(i).getFrom() == from) {
                state = this.getTransitions().get(i).getTo();
            }
        }
        return state;
    }

    public List<Integer> getFinalStates() {
        return this.finalStates;
    }

    public State obtenerEstadoPorId(int id) {
        for (int i = 0; i < this.states.size(); i++) {
            if (this.states.get(i).id == id)
                return this.states.get(i);
        }
        return null;
    }

    public void deleteStateById(int id) {
        for (int i = 0; i < this.states.size(); i++) {
            if (this.states.get(i).id == id)
                this.states.remove(i);
        }
    }

    public void deleteTransitionsByFrom(int from) {
        for (int i = 0; i < this.transitions.size(); i++) {
            if (this.transitions.get(i).getFrom() == from) {
                this.transitions.remove(i);
                i--;
            }
        }
    }

    public void definirTransiciones(int oldValue, int newValue) {
        // System.out.println(oldValue + ":" + " " + oldValue + ", newValue: " +
        // newValue);
        for (int i = 0; i < this.transitions.size(); i++) {
            Transition transt = this.transitions.get(i);
            if (transt.getFrom() == oldValue) {
                transt.setFrom(newValue);
            }
        }
    }

    public String toString() {
        return this.states.toString() + "\n" + this.transitions.toString();
    }

    public void updateTransitionByTo(int from, int oldTo, int newTo) {
        for (int i = 0; i < this.transitions.size(); i++) {
            if (this.transitions.get(i).getTo() == oldTo) {
                this.transitions.get(i).setTo(newTo);
            }
        }
    }

    public void deleteRepeatedTransitions() {
        for (int i = 0; i < this.getTransitions().size(); i++) {
            Transition tmp = this.getTransitions().get(i);
            for (int j = i + 1; j < this.getTransitions().size(); j++) {
                Transition tmp2 = this.getTransitions().get(j);
                if (tmp.getFrom() == tmp2.getFrom() && tmp.getTo() == tmp2.getTo()
                        && tmp.getCharacter() == tmp2.getCharacter()) {
                    this.getTransitions().remove(tmp2);
                    i--;
                }
            }
        }
    }

    public void rewriteDFA() {
        List<Integer> states = new ArrayList<>();
        for (State s : this.getStates()) {
            states.add(s.obtenerId());
        }
        Collections.sort(states);
        for (int j = 0; j < this.getTransitions().size(); j++) {
            this.transitions.get(j).setFrom(states.indexOf(transitions.get(j).getFrom()));
            this.transitions.get(j).setTo(states.indexOf(transitions.get(j).getTo()));
        }

        for (int k = 0; k < this.getStates().size(); k++) {
            this.getStates().get(k).setId(states.indexOf(this.getStates().get(k).obtenerId()));
        }

    }
}