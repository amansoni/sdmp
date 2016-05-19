package test;

import com.amansoni.Action;
import com.amansoni.Environment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Aman on 19/05/2016.
 */
public class EDOTest {

    @Test
    public void TestBiasZero() {
        Environment environment = new Environment(0);
        assertEquals(0, environment.getBias());
        assertEquals(0, environment.getTimeStep());

        // state
        assertEquals(5, environment.getState().center);
        assertEquals(30, environment.getState().height);
        assertEquals(2, environment.getState().width);

        // take action 5
        int reward = environment.takeAction(new Action(5));
        int totalReward = reward;
        assertEquals(30, reward);
        assertEquals(30, totalReward);
        assertEquals(1, environment.getTimeStep());

        // take action -5
        reward = environment.takeAction(new Action(-5));
        totalReward += reward;
        assertEquals(30, reward);
        assertEquals(60, totalReward);
        assertEquals(2, environment.getTimeStep());

        // take action 5
        reward = environment.takeAction(new Action(5));
        totalReward += reward;
        assertEquals(30, reward);
        assertEquals(90, totalReward);
        assertEquals(3, environment.getTimeStep());

        // take action -5
        reward = environment.takeAction(new Action(-5));
        totalReward += reward;
        assertEquals(30, reward);
        assertEquals(120, totalReward);
        assertEquals(4, environment.getTimeStep());

        // take action 5
        reward = environment.takeAction(new Action(5));
        totalReward += reward;
        assertEquals(30, reward);
        assertEquals(150, totalReward);
        assertEquals(5, environment.getTimeStep());

    }

    @Test
    public void TestBiasHundred() {
        Environment environment = new Environment(100);
        assertEquals(100, environment.getBias());
        assertEquals(0, environment.getTimeStep());

        // state
        assertEquals(5, environment.getState().center);
        assertEquals(30, environment.getState().height);
        assertEquals(2, environment.getState().width);

        // take action 5
        int reward = environment.takeAction(new Action(5));
        int totalReward = reward;
        assertEquals(130, reward);
        assertEquals(130, totalReward);
        assertEquals(1, environment.getTimeStep());

        // take action 10
        reward = environment.takeAction(new Action(0));
        totalReward += reward;
        assertEquals(120, reward);
        assertEquals(250, totalReward);
        assertEquals(2, environment.getTimeStep());

        // take action 5
        reward = environment.takeAction(new Action(5));
        totalReward += reward;
        assertEquals(130, reward);
        assertEquals(380, totalReward);
        assertEquals(3, environment.getTimeStep());

        // take action 10
        reward = environment.takeAction(new Action(0));
        totalReward += reward;
        assertEquals(120, reward);
        assertEquals(500, totalReward);
        assertEquals(4, environment.getTimeStep());

        // take action 5
        reward = environment.takeAction(new Action(5));
        totalReward += reward;
        assertEquals(130, reward);
        assertEquals(630, totalReward);
        assertEquals(5, environment.getTimeStep());
    }

    @Test
    public void TestBiasFifteen() {
        Environment environment = new Environment(15);
        assertEquals(15, environment.getBias());
        assertEquals(0, environment.getTimeStep());

        // state
        assertEquals(5, environment.getState().center);
        assertEquals(30, environment.getState().height);
        assertEquals(2, environment.getState().width);

        // take action 5
        int reward = environment.takeAction(new Action(5));
        int totalReward = reward;
        assertEquals(45, reward);
        assertEquals(45, totalReward);
        assertEquals(1, environment.getTimeStep());

        // take action 10
        reward = environment.takeAction(new Action(-5));
        totalReward += reward;
        assertEquals(45, reward);
        assertEquals(90, totalReward);
        assertEquals(2, environment.getTimeStep());

        // take action 5
        reward = environment.takeAction(new Action(5));
        totalReward += reward;
        assertEquals(15, reward);
        assertEquals(105, totalReward);
        assertEquals(3, environment.getTimeStep());

        // take action 10
        reward = environment.takeAction(new Action(-5));
        totalReward += reward;
        assertEquals(45, reward);
        assertEquals(150, totalReward);
        assertEquals(4, environment.getTimeStep());

        // take action 5
        reward = environment.takeAction(new Action(5));
        totalReward += reward;
        assertEquals(15, reward);
        assertEquals(165, totalReward);
        assertEquals(5, environment.getTimeStep());
    }

    @Test
    public void TestEDOBias15() {
        Environment environment = new Environment(15);
        int totalReward = 0;
        for (int i = 0; i < 1000; i++) {
            if (environment.getState().center == 5)
                totalReward += environment.takeAction(new Action(5));
            else
                totalReward += environment.takeAction(new Action(-5));
        }
        assertEquals(30030, totalReward);
//        System.out.println(totalReward);
    }

    @Test
    public void TestEDOBias100() {
        Environment environment = new Environment(100);
        int totalReward = 0;
            for (int i = 0; i < 1000; i++) {
            if (environment.getState().center == 5)
                totalReward += environment.takeAction(new Action(5));
            else
                totalReward += environment.takeAction(new Action(0));
        }
        System.out.println(totalReward);
    }

}
