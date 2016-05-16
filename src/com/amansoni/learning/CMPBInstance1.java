package com.amansoni.learning;

/**
 * Created by Aman on 13/05/2016.
 */
public class CMPBInstance1 {
    // bias
    private int b = -100;
    // dimension of peak n
    private int n = 1;
    // number of peaks
    private int m = 1;
    // action space
    private int[] actions; // length = n, range [-10,10]
    // state space
    private int[] states; // length = (n + 2) * m + 1
    int height=30;
    int width=2;

    // current timestep
    private int timestep = 0;
    private int currentState = -5;

    public CMPBInstance1(){
        init();
    }

    void init(){
        // actions
        int noOfActions = 21;
        int[] temp = new int[noOfActions];
        int val=-10;
        for(int i=0; i<noOfActions; i++){
            temp[i] = val++;
        }
        // state space

    }

    private int g(){
        b = -b;
        return b;
    }

    private double reward(int x){
        currentState = -currentState;
        System.out.print("currentState: " + currentState);
        System.out.print("height: " + height);
        System.out.print("width: " + width);
        System.out.print("b: " + b);
        return height - width * Math.abs(x - currentState) + g();
    }

    private void run() {

        for(int i=0; i<5; i++){
            long time = System.currentTimeMillis();
            System.out.println("Time: " + time / 1000.0 + " sec. t:" + timestep + " reward:" + this.reward(i) );
        }
    }

    public static void main(String[] args) {
        long BEGIN = System.currentTimeMillis();

        CMPBInstance1 obj = new CMPBInstance1();

        obj.run();
        //obj.printResult();
        //obj.showPolicy();

        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
    }
}
