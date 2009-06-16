/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.tests.dann.backprop;

import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.backprop.*;
import org.junit.*;

public class TestBackpropNeuronGroup
{
	@Test
	public void testLinkage() throws InvalidConnectionTypeDannException
	{
		BackpropNeuron firstNeuron = new BackpropNeuron();
		BackpropNeuronGroup firstGroup = new BackpropNeuronGroup();
		BackpropNeuron middleNeuron = new BackpropNeuron();
		BackpropNeuronGroup middleGroup = new BackpropNeuronGroup();
		BackpropNeuron lastNeuron = new BackpropNeuron();
		BackpropNeuronGroup lastGroup = new BackpropNeuronGroup();

		firstGroup.add(firstNeuron);
		middleGroup.add(middleNeuron);
		lastGroup.add(lastNeuron);

		firstNeuron.connectTo(middleNeuron);
		middleNeuron.connectTo(lastNeuron);

		Assert.assertTrue(middleGroup.getDestinationNeighbors().contains(lastNeuron));
		Assert.assertTrue(middleGroup.getSourceNeighbors().contains(firstNeuron));
		Assert.assertTrue(middleGroup.getNeighbors().contains(firstNeuron));
		Assert.assertTrue(middleGroup.getNeighbors().contains(lastNeuron));
	}

	@Test
	public void testPropogation() throws InvalidConnectionTypeDannException
	{
		BackpropNeuron firstNeuron = new BackpropNeuron();
		BackpropNeuronGroup firstGroup = new BackpropNeuronGroup();
		BackpropNeuron middleNeuron = new BackpropNeuron();
		BackpropNeuronGroup middleGroup = new BackpropNeuronGroup();
		BackpropNeuron lastNeuron = new BackpropNeuron();
		BackpropNeuronGroup lastGroup = new BackpropNeuronGroup();

		firstGroup.add(firstNeuron);
		middleGroup.add(middleNeuron);
		lastGroup.add(lastNeuron);

		firstNeuron.connectTo(middleNeuron);
		middleNeuron.connectTo(lastNeuron);

		firstGroup.propagate();
		middleGroup.propagate();

		lastGroup.backPropagate();
		middleGroup.backPropagate();
	}
}
