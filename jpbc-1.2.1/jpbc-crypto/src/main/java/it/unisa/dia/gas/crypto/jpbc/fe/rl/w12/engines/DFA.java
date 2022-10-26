package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class DFA {

    private Map<String, Transition> index;
    private List<Transition> transitions;
    private List<Integer> finalStates;
    private int numStates;


    public DFA(int numStates) {
        this.numStates = numStates;

        this.transitions = new ArrayList<Transition>();
        this.index = new HashMap<String, Transition>();
        this.finalStates = new ArrayList<Integer>();
    }

    public void addFinalState(int state) {
        finalStates.add(state);
    }

    public void addTransition(int from, Character reading, int to) {
        Transition transition = new Transition(from, reading, to);

        this.transitions.add(transition);
        this.index.put(String.format("%d_%s", from, reading), transition);
    }

    public Transition getTransitionAt(int index) {
        return transitions.get(index);
    }

    public int getNumTransitions() {
        return transitions.size();
    }

    public boolean accept(String w) {
        int currentState = 0; // Initial state

        for (int i = 0; i < w.length(); i++) {
            currentState = getTransition(currentState, w.charAt(i)).getTo();
        }

        return finalStates.contains(currentState);
    }

    public int getNumStates() {
        return numStates;
    }

    public int getNumFinalStates() {
        return finalStates.size();
    }

    public int getFinalStateAt(int index) {
        return finalStates.get(index);
    }


    protected Transition getTransition(int state, char c) {
        return index.get(String.format("%d_%s", state, c));
    }


    public static class Transition {
        private int from;
        private Character reading;
        private int to;

        public Transition(int from, Character reading, int to) {
            this.from = from;
            this.reading = reading;
            this.to = to;
        }

        public int getFrom() {
            return from;
        }

        public Character getReading() {
            return reading;
        }

        public int getTo() {
            return to;
        }

    }


    public static void main(String[] args) {
        DFA dfa = new DFA(2);

        dfa.addFinalState(0);

        dfa.addTransition(0, '0', 1);
        dfa.addTransition(0, '1', 0);
        dfa.addTransition(1, '0', 0);
        dfa.addTransition(1, '1', 1);

        boolean accepted = dfa.accept("0111100");

        System.out.println("accepted = " + accepted);
    }

}
