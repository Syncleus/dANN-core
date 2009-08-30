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
import java.util.Hashtable;
import org.junit.*;

public class TestBrainHyperassociativeMap
{
	private class TestBrain extends AbstractLocalBrain
	{
		public void addNeuron(Neuron newNeuron)
		{
			super.addNeuron(newNeuron);
		}
	}

	private class TestMap extends BrainHyperassociativeMap
	{
		public TestMap(AbstractLocalBrain brain, int dimensions)
		{
			super(brain, dimensions);
		}

		public Hashtable<Neuron, NeuronHyperassociativeNode> getNeurons()
		{
			return this.neurons;
		}
	}

	@Test
	public void testRefresh() throws InvalidConnectionTypeDannException
	{
		TestBrain testBrain = new TestBrain();

		BackpropNeuron neuron1 = new BackpropNeuron();
		BackpropNeuron neuron2 = new BackpropNeuron();

		neuron1.connectTo(neuron2);

		testBrain.addNeuron(neuron1);
		testBrain.addNeuron(neuron2);

		TestMap testMap = new TestMap(testBrain, 3);
		testMap.refresh();

		Assert.assertTrue("neuron1 is not in the map", testMap.getNeurons().get(neuron1) != null);
		Assert.assertTrue("neuron2 is not in the map", testMap.getNeurons().get(neuron2) != null);

		NeuronHyperassociativeNode neuron1Node = testMap.getNeurons().get(neuron1);
		NeuronHyperassociativeNode neuron2Node = testMap.getNeurons().get(neuron2);

		Assert.assertTrue("neuron1 is not associated to neuron2", neuron1Node.getNeighbors().contains(neuron2Node));
		Assert.assertTrue("neuron2 is not associated to neuron1", neuron2Node.getNeighbors().contains(neuron1Node));
	}
}
