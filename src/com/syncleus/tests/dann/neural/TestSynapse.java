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

import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.backprop.*;
import org.junit.*;

public class TestSynapse
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

	private static final double INITIAL_WEIGHT = 0.01;
	private static final double TEST_INPUT = 2.0;
	private static final double TEST_WEIGHT = 3.0;

	@Test
	public void testAccessors()
	{
		TestBrain brain = new TestBrain();

		SimpleBackpropNeuron sourceNeuron = new SimpleBackpropNeuron(brain);
		SimpleBackpropNeuron destinationNeuron = new SimpleBackpropNeuron(brain);

		SimpleSynapse testSynapse = new SimpleSynapse(sourceNeuron, destinationNeuron, INITIAL_WEIGHT);

		testSynapse.setInput(TEST_INPUT);
		Assert.assertTrue(testSynapse.getInput() == TEST_INPUT);
		testSynapse.setWeight(TEST_WEIGHT);
		Assert.assertTrue(testSynapse.getWeight() == TEST_WEIGHT);
		Assert.assertTrue(testSynapse.getSourceNode() == sourceNeuron);
		Assert.assertTrue(testSynapse.getDestinationNode() == destinationNeuron);
	}
}
