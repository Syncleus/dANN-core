/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                    *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by syncleus at http://www.syncleus.com.  *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact syncleus at the information below if you cannot find   *
 *  a license:                                                                 *
 *                                                                             *
 *  Syncleus                                                                   *
 *  1116 McClellan St.                                                         *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
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
