import java.util.Stack;

public class M2 {

    private static class ReIndexes {
        int begin = 0;
        int end = 0;

        public ReIndexes(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int obtenerPrimero() {
            return this.begin;
        }

        public int obtenerUltimo() {
            return this.end;
        }

        public String toString() {
            return "(" + this.begin + "," + this.end + ");";
        }

    }


    public static DFA thompson(String regex) {
        DFA automata = new DFA();
        Stack<ReIndexes> expresionRegular = new Stack<ReIndexes>();
        ReIndexes primerERIndices;
        ReIndexes segundoERIndices;

        int from2 = 0, to2 = 0;

        for (int i = 0; i < regex.length(); i++) {

            char character = regex.charAt(i);

            // Si el caracter es operador: | * + -
            if (isOperator(character)) {
                
                // Si el caracter es concatenación
                if (character == '-') {
                    // Indices de las ER para concatenar
                    primerERIndices = expresionRegular.pop();
                    segundoERIndices = expresionRegular.pop();

                    // Borrar último estado
                    automata.deleteStateById(primerERIndices.obtenerPrimero());

                    // Quitar actual estado final
                    automata.obtenerEstadoPorId(segundoERIndices.obtenerUltimo()).setFinal(false);

                    // Actualizar estado final
                    automata.definirEstadoFinal(automata.obtenerEstadoPorId(primerERIndices.obtenerUltimo()), true);

                    // Actualizar transiciones
                    automata.definirTransiciones(primerERIndices.obtenerPrimero(), segundoERIndices.obtenerUltimo());
                    automata.definirTransiciones(primerERIndices.obtenerPrimero(), segundoERIndices.obtenerUltimo());

                    // Agregar er a la pila
                    expresionRegular.add(new ReIndexes(segundoERIndices.obtenerPrimero(), primerERIndices.obtenerUltimo()));

                } else if (character == '|') {
                    // Indices de las ER para union
                    primerERIndices = expresionRegular.pop();
                    segundoERIndices = expresionRegular.pop();

                    // Nuevos estados
                    State newInitialState = new State(DFA.IDCOUNTER++);
                    State newFinalState = new State(DFA.IDCOUNTER++);

                    // Agregar transiciones con epsilon
                    automata.addTransitions(
                            new Transition(newInitialState.obtenerId(),
                                    automata.obtenerEstadoPorId(segundoERIndices.obtenerPrimero()).obtenerId(), 'Δ'),
                            new Transition(newInitialState.obtenerId(),
                                    automata.obtenerEstadoPorId(primerERIndices.obtenerPrimero()).obtenerId(), 'Δ'),
                            new Transition(automata.obtenerEstadoPorId(segundoERIndices.obtenerUltimo()).obtenerId(),
                                    newFinalState.obtenerId(), 'Δ'),
                            new Transition(automata.obtenerEstadoPorId(primerERIndices.obtenerUltimo()).obtenerId(),
                                    newFinalState.obtenerId(), 'Δ'));

                    // Agregar nuevos estados al automata                       
                    automata.añadirEstado(newInitialState, newFinalState);

                    // Desactivar actuales finales
                    automata.obtenerEstadoPorId(primerERIndices.obtenerPrimero()).setInitial(false);
                    automata.obtenerEstadoPorId(primerERIndices.obtenerUltimo()).setFinal(false);
                    automata.obtenerEstadoPorId(segundoERIndices.obtenerPrimero()).setInitial(false);
                    automata.obtenerEstadoPorId(segundoERIndices.obtenerUltimo()).setFinal(false);
                    
                    // Definir nuevos estados finales
                    automata.setInitialState(newInitialState, true);
                    automata.definirEstadoFinal(newFinalState, true);

                    // Agregar er a la pila
                    expresionRegular.add(new ReIndexes(newInitialState.obtenerId(), newFinalState.obtenerId()));

                } else if (character == '*') {
                    // Nuevos estados
                    State newInitialState = new State(DFA.IDCOUNTER++);
                    State newFinalState = new State(DFA.IDCOUNTER++);

                    // Agregar estados y transiciones epsilon para cerradura de Kleen
                    segundoERIndices = expresionRegular.pop();
                    automata.addTransitions(new Transition(segundoERIndices.obtenerUltimo(), segundoERIndices.obtenerPrimero(), 'Δ'),
                            new Transition(newInitialState.obtenerId(), segundoERIndices.obtenerPrimero(), 'Δ'),
                            new Transition(segundoERIndices.obtenerUltimo(), newFinalState.obtenerId(), 'Δ'),
                            new Transition(newInitialState.obtenerId(), newFinalState.obtenerId(), 'Δ'));
                    if (automata.obtenerUltimoEstado().obtenerId() == segundoERIndices.obtenerUltimo()) {
                        automata.obtenerUltimoEstado().setFinal(false);
                        automata.definirEstadoFinal(newFinalState, true);
                    }
                    if (automata.obtenerPrimeroEstado().obtenerId() == segundoERIndices.obtenerPrimero()) {
                        automata.obtenerPrimeroEstado().setInitial(false);
                        automata.setInitialState(newInitialState, true);
                    }

                    // Agregar nuevos estados
                    automata.añadirEstado(newInitialState, newFinalState);

                    // Eliminar antiguos estados finales
                    automata.obtenerEstadoPorId(segundoERIndices.obtenerPrimero()).setInitial(false);
                    automata.obtenerEstadoPorId(segundoERIndices.obtenerUltimo()).setFinal(false);

                    // Agregar er a la pila
                    expresionRegular.add(new ReIndexes(newInitialState.obtenerId(), newFinalState.obtenerId()));

                } else if (character == '+') {
                    // Obtener ER de la pila
                    segundoERIndices = expresionRegular.pop();

                    // Agregar transicion epsilon para cerradura de estrella
                    automata.agregarTransicion(new Transition(segundoERIndices.obtenerUltimo(), segundoERIndices.obtenerPrimero(), 'Δ'));
                    expresionRegular.add(segundoERIndices);
                }

            } else {
                from2 = DFA.IDCOUNTER++;
                to2 = DFA.IDCOUNTER++;
                State from = new State(from2);
                from.setInitial(true);
                State to = new State(to2);
                to.setFinal(true);
                automata.añadirEstado(from, to);
                automata.setInitialState(from, from2 == 0);
                automata.definirEstadoFinal(to, to2 == 1);
                automata.agregarTransicion(new Transition(from.obtenerId(), to.obtenerId(), character));

                // Add indexes of regular expression to the Stack
                expresionRegular.add(new ReIndexes(from.obtenerId(), to.obtenerId()));
            }

        }
        return automata;
    }

    private static boolean isOperator(char op) {
        return op == '|' || op == '*' || op == '+' || op == '-';
    }

}
