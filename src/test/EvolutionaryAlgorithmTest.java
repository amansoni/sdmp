package test;

import com.amansoni.Action;
import com.amansoni.Environment;
import com.amansoni.EvolutionaryAlgorithm;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by sonia2 on 16/06/2016.
 */
public class EvolutionaryAlgorithmTest {

    @Test
    public void TestFullStrategy() {
        Random random = new Random(0);

        Environment environment = new Environment(0);
        assertEquals(0, environment.getBias());
        assertEquals(0, environment.getTimeStep());

        Action[] actions = environment.getActions();
        assertEquals(21, actions.length);
        int val = -10;
        for (int i=0; i < 21; i++){
//            System.out.print(val + ",");
            assertEquals(val++, actions[i].getValue());
        }

        EvolutionaryAlgorithm evolutionaryAlgorithm = new EvolutionaryAlgorithm(environment, EvolutionaryAlgorithm.Strategy.Full, random, 0, false);
        actions = evolutionaryAlgorithm.getActions();
        assertEquals(21, actions.length);
        val = -10;
        for (int i=0; i < 21; i++){
//            System.out.print(val + ",");
            assertEquals(val++, actions[i].getValue());
        }
    }
}
