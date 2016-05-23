package com.amansoni;

import java.util.Comparator;

/**
 * Created by Aman on 14/05/2016.
 */
public class State implements Comparable<State>{
    public int height=30;
    public int width=2;
    public int center=5;

    public State(){
    }

    public State(int center){
        this.center = center;
    }


    @Override
    public int compareTo(State state) {
        return new Integer(state.center).compareTo(this.center);
    }

    @Override
    public String toString() {
        return "State:" + this.center;
    }

//    public static Comparator<State> StateComparator
//            = new Comparator<State>() {
//
//        public int compare(State state1, State state2) {
//
//            //ascending order
//            return new Integer(state1.center).compareTo(state2.center);
//
//        }
//
//    };
}
