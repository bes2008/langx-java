package com.jn.langx.test.text.lexer.simple;

import com.jn.langx.util.Objs;

import java.util.*;


public class Test1 {

    static class DFA {
        private final Map<State, Map<Character, State>> transitions;
        private final State initialState;
        private final Set<State> acceptingStates;

        public DFA(Map<State, Map<Character, State>> transitions, State initialState, Set<State> acceptingStates) {
            this.transitions = transitions;
            this.initialState = initialState;
            this.acceptingStates = acceptingStates;
        }

        public boolean accepts(String input) {
            State currentState = initialState;

            for (char c : input.toCharArray()) {
                currentState = getNextState(currentState, c);
                if (currentState == null) {
                    return false; // Invalid transition
                }
            }

            return isAcceptingState(currentState);
        }

        private State getNextState(State currentState, char input) {
            Map<Character, State> m = transitions.get(currentState);
            if (m == null) {
                m = Collections.emptyMap();
            }
            return m.get(input);
        }

        private boolean isAcceptingState(State state) {
            return acceptingStates.contains(state);
        }
    }

    static class State {
        private final int id;

        public State(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return id == state.id;
        }

        @Override
        public int hashCode() {
            return Objs.hash(id);
        }

        @Override
        public String toString() {
            return "State{" +
                    "id=" + id +
                    '}';
        }
    }

    public static void main(String[] args) {
        // Define the DFA states
        State s0 = new State(0);
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);
        State s4 = new State(4);

        // Define the accepting states
        Set<State> acceptingStates = new HashSet<State>();
        acceptingStates.add(s4);

        // Define the input alphabet
        Map<State, Map<Character, State>> transitions = new HashMap();
        Map<Character, State> s0Transitions = new HashMap();
        s0Transitions.put('a', s1);
        s0Transitions.put('b', s2);
        transitions.put(s0, s0Transitions);

        Map<Character, State> s1Transitions = new HashMap();
        s1Transitions.put('a', s3);
        s1Transitions.put('b', s0);
        transitions.put(s1, s1Transitions);

        Map<Character, State> s2Transitions = new HashMap();
        s2Transitions.put('a', s0);
        s2Transitions.put('b', s3);
        transitions.put(s2, s2Transitions);

        Map<Character, State> s3Transitions = new HashMap();
        s3Transitions.put('a', s2);
        s3Transitions.put('b', s1);
        transitions.put(s3, s3Transitions);

        // No transitions for s4, as it is an accepting state

        DFA dfa = new DFA(transitions, s0, acceptingStates);

        System.out.println(dfa.accepts("abba")); // Output: true
        System.out.println(dfa.accepts("abb"));  // Output: false
    }

}