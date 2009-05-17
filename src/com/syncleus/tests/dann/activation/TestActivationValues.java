package com.syncleus.tests.dann.activation;

import org.junit.*;

import com.syncleus.dann.activation.*;
import java.util.ArrayList;
import java.util.Random;

public class TestActivationValues
{
	private static Random random = new Random();
	
	private GausianActivationFunction gausianActivationFunction = new GausianActivationFunction();
	private HyperbolicSecantActivationFunction hyperbolicSecantActivationFunction = new HyperbolicSecantActivationFunction();
	private HyperbolicTangentActivationFunction hyperbolicTangentActivationFunction = new HyperbolicTangentActivationFunction();
	private SineActivationFunction sineActivationFunction = new SineActivationFunction();
	
	private ArrayList<ActivationFunction> activationFunctions = new ArrayList<ActivationFunction>();
	
	public TestActivationValues()
	{
		activationFunctions.add(this.gausianActivationFunction);
		activationFunctions.add(this.hyperbolicSecantActivationFunction);
		activationFunctions.add(this.hyperbolicTangentActivationFunction);
		activationFunctions.add(this.sineActivationFunction);
	}
	
	@Test
	public void testBounds()
	{
		for(ActivationFunction currentActivationFunction : this.activationFunctions)
		{
			double currentIn = 1000000000.0;
			while(currentIn >= 100.0)
			{
				double result =currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= 1.0)&&(result >= -1.0));
				
				currentIn = currentIn/10.0;
			}
			while(currentIn > 0.0)
			{
				double result =currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= 1.0)&&(result >= -1.0));
				
				currentIn--;
			}
			
			currentIn = -1000000000.0;
			while(currentIn <= -100.0)
			{
				double result =currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= 1.0)&&(result >= -1.0));
				
				currentIn = currentIn/10.0;
			}
			while(currentIn <= 0.0)
			{
				double result =currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= 1.0)&&(result >= -1.0));
				
				currentIn++;
			}
			
			for(int count = 0;count < 10000; count++)
			{
				currentIn = ((random.nextDouble()*2.0)-1.0)*1000.0;
				double result =currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= 1.0)&&(result >= -1.0));
			}
		}
	}
}
