/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.tests.dann.neural;

import com.syncleus.dann.neural.NeuronGroup;
import com.syncleus.dann.neural.AbstractLocalBrain;
import com.syncleus.dann.neural.Neuron;
import com.syncleus.dann.neural.backprop.BackpropNeuron;
import org.junit.*;

public class TestNeuronGroup
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

	@Test
	public void testCollection()
	{
		TestBrain brain = new TestBrain();

		NeuronGroup<BackpropNeuron> newGroup = new NeuronGroup<BackpropNeuron>();
		NeuronGroup<BackpropNeuron> subGroup = new NeuronGroup<BackpropNeuron>();
		BackpropNeuron newNeuron = new BackpropNeuron(brain);
		BackpropNeuron subNeuron = new BackpropNeuron(brain);

		subGroup.add(subNeuron);
		newGroup.add(subGroup);
		newGroup.add(newNeuron);

		Assert.assertTrue(newGroup.getChildrenNeurons().contains(newNeuron));
		Assert.assertTrue(newGroup.getChildrenNeuronGroups().contains(subGroup));
		Assert.assertTrue(newGroup.getChildrenNeuronsRecursivly().contains(subNeuron));
	}
}
