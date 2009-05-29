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
package com.syncleus.tests.dann;

import com.syncleus.dann.*;
import com.syncleus.dann.backprop.*;
import org.junit.*;

public class TestSynapse
{
	@Test
	public void testAccessors()
	{
		BackpropNeuron sourceNeuron = new BackpropNeuron();
		BackpropNeuron destinationNeuron = new BackpropNeuron();

		Synapse testSynapse = new Synapse(sourceNeuron, destinationNeuron, 0.01);

		testSynapse.setInput(2.0d);
		Assert.assertTrue(testSynapse.getInput() == 2.0d);
		testSynapse.setWeight(3.0d);
		Assert.assertTrue(testSynapse.getWeight() == 3.0d);
		Assert.assertTrue(testSynapse.getOutput() == 6.0d);
		Assert.assertTrue(testSynapse.getSource() == sourceNeuron);
		Assert.assertTrue(testSynapse.getDestination() == destinationNeuron);
	}
}
