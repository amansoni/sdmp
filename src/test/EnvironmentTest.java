package test;

import com.amansoni.rl.Action;
import com.amansoni.rl.Environment;
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
