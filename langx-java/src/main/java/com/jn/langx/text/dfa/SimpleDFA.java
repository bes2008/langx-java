package com.jn.langx.text.dfa;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.*;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.struct.Pair;

import java.util.Map;

class SimpleDFA {
    public static final int INVALID_STATE = Integer.MIN_VALUE;
    @NonNull
    Map<Integer, Map<String, Integer>> stateTransactions = Maps.newHashMap();

    /**
     * 添加状态转换
     * @param fromState 从哪个状态开始计算
     * @param symbol    遇到的符号是什么
     * @param toState   到哪个状态
     */
    public void addTransaction(int fromState, String symbol, int toState) {
        Map<String, Integer> transactions = Maps.get(stateTransactions, fromState, new Supplier0<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> get() {
                return Maps.newHashMap();
            }
        });
        transactions.put(symbol, toState);
        Maps.putIfAbsent(stateTransactions, fromState, transactions);
    }

    public int getNextState(int currentState, String symbol) {
        Map<String, Integer> to = Maps.get(stateTransactions, currentState, Maps.<String, Integer>newImmutableMap());
        return Maps.get(to, symbol, INVALID_STATE);
    }

    public int transformState(int initState, String... symbols){
        final Holder<Integer> currentState = new Holder<Integer>(initState);
        Pipeline.of(symbols).forEach( new Consumer<String>() {
            @Override
            public void accept(String symbol) {
                int next = getNextState(currentState.get(), symbol);
                currentState.set(next);
            }
        }, new Predicate<String>() {
            @Override
            public boolean test(String symbol) {
                return currentState.get()==INVALID_STATE;
            }
        });
        return currentState.get();
    }

    public Pair<Integer,Integer> getLastValidState(int initState, String... symbols){
        // key: index
        // value: state
        final Holder<Pair<Integer, Integer>> currentState = new Holder<Pair<Integer, Integer>>(new Pair<Integer, Integer>(-1, initState));
        Pipeline.of(symbols).forEach( new Consumer2<Integer, String>() {
            @Override
            public void accept(Integer index, String symbol) {
                Pair<Integer, Integer> indexAndState= currentState.get();
                int next = getNextState(indexAndState.getValue(), symbol);
                if(next>INVALID_STATE) {
                    indexAndState.setKey(index);
                    indexAndState.setValue(next);
                }
            }
        }, new Predicate2<Integer, String>() {
            @Override
            public boolean test(Integer index, String symbol) {
                return currentState.get().getKey()<index;
            }
        });
        return currentState.get();
    }


}
