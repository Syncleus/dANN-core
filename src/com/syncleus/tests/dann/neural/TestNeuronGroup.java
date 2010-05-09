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
package com.syncleus.tests.dann.neural;

import com.syncleus.dann.neural.NeuronGroup;
import com.syncleus.dann.neural.AbstractLocalBrain;
import com.syncleus.dann.neural.Neuron;
import com.syncleus.dann.neural.backprop.SimpleBackpropNeuron;
import org.junit.*;

public class TestNeuronGroup
{
	private class TestBrain extends AbstractLocalBrain
	{
		@Override
		public boolean add(final Neuron newNeuron)
		{
			return super.add(newNeuron);
		}

		@Override
		public boolean connect(final Neuron source, final Neuron destination)
		{
			return super.connect(source, destination);
		}
	}

	@Test
	public void testCollection()
	{
		final TestBrain brain = new TestBrain();

		final NeuronGroup<SimpleBackpropNeuron> newGroup = new NeuronGroup<SimpleBackpropNeuron>();
		final NeuronGroup<SimpleBackpropNeuron> subGroup = new NeuronGroup<SimpleBackpropNeuron>();
		final SimpleBackpropNeuron newNeuron = new SimpleBackpropNeuron(brain);
		final SimpleBackpropNeuron subNeuron = new SimpleBackpropNeuron(brain);

		subGroup.add(subNeuron);
		newGroup.add(subGroup);
		newGroup.add(newNeuron);

		Assert.assertTrue(newGroup.getChildrenNeurons().contains(newNeuron));
		Assert.assertTrue(newGroup.getChildrenNeuronGroups().contains(subGroup));
		Assert.assertTrue(newGroup.getChildrenNeuronsRecursivly().contains(subNeuron));
	}
}
