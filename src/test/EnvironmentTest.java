package test;

import com.amansoni.Action;
import com.amansoni.Environment;
import com.amansoni.State;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Aman on 16/05/2016.
 */
public class EnvironmentTest {


    @Test
    public void TestInitialEnvironment(){
        Environment environment = new Environment(100);
        assertEquals(100, environment.getBias());
        assertEquals(0, environment.getTimeStep());
        // state
        assertEquals(5, environment.getState().center);
        assertEquals(30, environment.getState().height);
        assertEquals(2, environment.getState().width);

        // check reward does not change without an action
        assertEquals(100, environment.getReward(new Action(-10)));
        assertEquals(110, environment.getReward(new Action(-5)));
        assertEquals(120, environment.getReward(new Action(0)));
        assertEquals(130, environment.getReward(new Action(5)));
        assertEquals(120, environment.getReward(new Action(10)));

        // take an action >= 0
        assertEquals(120, environment.takeAction(new Action(10)));
        assertEquals(1, environment.getTimeStep());

        // take an action < 0
        assertEquals(120, environment.takeAction(new Action(-10)));
        assertEquals(2, environment.getTimeStep());

        // take an action < 0
        assertEquals(-100, environment.takeAction(new Action(-10)));
        assertEquals(3, environment.getTimeStep());
    }

    @Test
    public void TestRewardFunction(){
        Environment environment = new Environment(100);
        assertEquals(100, environment.getBias());
        assertEquals(0, environment.getTimeStep());
        // state
        assertEquals(5, environment.getState().center);
        assertEquals(30, environment.getState().height);
        assertEquals(2, environment.getState().width);

        // check reward does not change without an action
        assertEquals(100, environment.getReward(new Action(-10)));
        assertEquals(110, environment.getReward(new Action(-5)));
        assertEquals(120, environment.getReward(new Action(0)));
        assertEquals(130, environment.getReward(new Action(5)));
        assertEquals(120, environment.getReward(new Action(10)));

        // take an action >= 0
        assertEquals(120, environment.takeAction(new Action(10)));
        assertEquals(1, environment.getTimeStep());
        testEnvironmentStep(environment, 100);


        // take an action < 0
        assertEquals(120, environment.takeAction(new Action(-10)));
        assertEquals(2, environment.getTimeStep());
        testEnvironmentStep(environment, -100);

        // take an action < 0
        assertEquals(-100, environment.takeAction(new Action(-10)));
        assertEquals(3, environment.getTimeStep());
        testEnvironmentStep(environment, -100);

        // take an action < 0
        assertEquals(-100, environment.takeAction(new Action(10)));
        assertEquals(4, environment.getTimeStep());
        testEnvironmentStep(environment, 100);

    }

    private void testEnvironmentStep(Environment environment, int bias) {
        int probableStateValue = -environment.getState().center;
        System.out.println("Time step:" + environment.getTimeStep()
//                + "\tLast action:" + environment.previousAction.getValue()
                + "\tState:" + environment.getState().center
                + "\t Next State:" + probableStateValue
                + "\tbias:" + bias);
        // test the reward function
        for (Action evalAction : environment.getActions()) {
            assertEquals(bias, environment.g());
            State probableState = new State(probableStateValue);
            System.out.println("action:" + evalAction.getValue() + "\ttest reward:" + environment.getReward(evalAction, probableState, evalAction));
        }
    }

    @Test
    public void TestActions(){
        Environment environment = new Environment(100);
        environment.initActions();
        Action[] actions = environment.getActions();
        assertEquals(21, actions.length);
        int val = -10;
        for (int i=0; i < 21; i++){
//            System.out.print(val + ",");
            assertEquals(val++, actions[i].getValue());
        }
     }

}
