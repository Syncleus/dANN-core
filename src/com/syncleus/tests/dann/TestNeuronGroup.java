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
import com.syncleus.dann.backprop.BackpropNeuron;
import org.junit.*;

public class TestNeuronGroup
{
	@Test
	public void testCollection()
	{
		NeuronGroup<BackpropNeuron> newGroup = new NeuronGroup<BackpropNeuron>();
		NeuronGroup<BackpropNeuron> subGroup = new NeuronGroup<BackpropNeuron>();
		BackpropNeuron newNeuron = new BackpropNeuron();
		BackpropNeuron subNeuron = new BackpropNeuron();

		subGroup.add(subNeuron);
		newGroup.add(subGroup);
		newGroup.add(newNeuron);

		Assert.assertTrue(newGroup.getChildrenNeurons().contains(newNeuron));
		Assert.assertTrue(newGroup.getChildrenNeuronGroups().contains(subGroup));
		Assert.assertTrue(newGroup.getChildrenNeuronsRecursivly().contains(subNeuron));
	}

	@Test
	public void testConnecting() throws InvalidConnectionTypeDannException
	{
		NeuronGroup<BackpropNeuron> firstGroup = new NeuronGroup<BackpropNeuron>();
		for(int neuronIndex = 0; neuronIndex < 10; neuronIndex++)
			firstGroup.add(new BackpropNeuron());
		NeuronGroup<BackpropNeuron> secondGroup = new NeuronGroup<BackpropNeuron>();
		for(int neuronIndex = 0; neuronIndex < 10; neuronIndex++)
				secondGroup.add(new BackpropNeuron());
		BackpropNeuron lastNeuron = new BackpropNeuron();

		firstGroup.connectAllTo(secondGroup);
		secondGroup.connectAllTo(lastNeuron);

		firstGroup.disconnectAllDestinations();
		secondGroup.disconnectAllSources();

		firstGroup.connectAllTo(secondGroup);
		secondGroup.connectAllTo(lastNeuron);

		firstGroup.disconnectAll();
		secondGroup.disconnectAll();
	}
}
