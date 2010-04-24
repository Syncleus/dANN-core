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
package com.syncleus.tests.dann.graph.drawing.hyperassociativemap;

import com.syncleus.dann.graph.drawing.hyperassociativemap.*;
import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.backprop.*;
import org.junit.*;

public class TestHyperassociativeMap
{
	private class TestBrain extends AbstractLocalBrain
	{
		@Override
		public boolean add(Neuron newNeuron)
		{
			return super.add(newNeuron);
		}

		@Override
		public boolean connect(Neuron source, Neuron destination)
		{
			return super.connect(source, destination);
		}
	}

	private class TestMap extends HyperassociativeMap<AbstractLocalBrain, Neuron>
	{
		public TestMap(AbstractLocalBrain brain, int dimensions)
		{
			super(brain, dimensions);
		}
	}

	@Test
	public void testRefresh() throws InvalidConnectionTypeDannException
	{
		TestBrain testBrain = new TestBrain();

		BackpropNeuron neuron1 = new BackpropNeuron(testBrain);
		BackpropNeuron neuron2 = new BackpropNeuron(testBrain);

		testBrain.add(neuron1);
		testBrain.add(neuron2);

		testBrain.connect(neuron1, neuron2);

		TestMap testMap = new TestMap(testBrain, 3);
		testMap.align();

		Assert.assertTrue("neuron1 is not in the map", testMap.getGraph().getNodes().contains(neuron1));
		Assert.assertTrue("neuron2 is not in the map", testMap.getGraph().getNodes().contains(neuron2));


		Assert.assertTrue("neuron1 is not associated to neuron2", testMap.getGraph().getAdjacentNodes(neuron1).contains(neuron2));
		Assert.assertTrue("neuron2 is not associated to neuron1", testMap.getGraph().getAdjacentNodes(neuron2).contains(neuron1));
	}
}
