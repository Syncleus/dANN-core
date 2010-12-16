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
package com.syncleus.dann.neural;

import com.syncleus.dann.neural.backprop.SimpleBackpropNeuron;
import org.junit.*;

public class TestSynapse
{
	private static class TestBrain extends AbstractLocalBrain
	{
		private static final long serialVersionUID = -7579268135961655455L;

		@Override
		public boolean add(final Neuron newNeuron)
		{
			return super.add(newNeuron);
		}
	}

	private static final double INITIAL_WEIGHT = 0.01;
	private static final double TEST_INPUT = 2.0;
	private static final double TEST_WEIGHT = 3.0;

	@Test
	public void testAccessors()
	{
		final TestBrain brain = new TestBrain();

		final SimpleBackpropNeuron sourceNeuron = new SimpleBackpropNeuron(brain);
		final SimpleBackpropNeuron destinationNeuron = new SimpleBackpropNeuron(brain);

		final SimpleSynapse testSynapse = new SimpleSynapse(sourceNeuron, destinationNeuron, INITIAL_WEIGHT);

		testSynapse.setInput(TEST_INPUT);
		Assert.assertTrue(Math.abs(testSynapse.getInput() - TEST_INPUT) < 0.000001);
		testSynapse.setWeight(TEST_WEIGHT);
		Assert.assertTrue(Math.abs(testSynapse.getWeight() - TEST_WEIGHT) < 0.000001);
		Assert.assertTrue(testSynapse.getSourceNode() == sourceNeuron);
		Assert.assertTrue(testSynapse.getDestinationNode() == destinationNeuron);
	}
}
