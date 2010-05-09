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
package com.syncleus.tests.dann.neural.backprop;


import com.syncleus.dann.DannException;
import com.syncleus.dann.neural.InputNeuron;
import com.syncleus.dann.neural.OutputNeuron;
import com.syncleus.dann.neural.activation.ActivationFunction;
import com.syncleus.dann.neural.activation.SineActivationFunction;
import com.syncleus.dann.neural.backprop.*;
import com.syncleus.dann.neural.backprop.brain.FullyConnectedFeedforwardBrain;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.*;

/**
 * An example main class that shows using dANN to solve an XOR problem. An XOR
 * is a circuit that returns true (1) when only one of its inputs is true. It
 * returns false (-1) if none all of its inputs are false or if more then one
 * of its inputs are true.
 *
 * @author Jeffrey Phillips Freeman
 */
public class TestXor
{
	private InputNeuron inputA = null;
	private InputNeuron inputB = null;
	private InputNeuron inputC = null;
	private OutputBackpropNeuron output = null;
	private FullyConnectedFeedforwardBrain brain = null;
	private final static int TRAINING_CYCLES = 1000;
	private final static double LEARNING_RATE = 0.0175;
	private final static int[] TOPOLOGY = {3, 3, 1};



	@Test
	public void testXor() throws DannException
	{
		//Adjust the learning rate
		final ActivationFunction activationFunction = new SineActivationFunction();

		final int cores = Runtime.getRuntime().availableProcessors();
		final ThreadPoolExecutor executor = new ThreadPoolExecutor(cores+1, cores*2, 20, TimeUnit.SECONDS, new LinkedBlockingQueue());
		try
		{
			this.brain = new FullyConnectedFeedforwardBrain(TOPOLOGY, LEARNING_RATE, activationFunction, executor);
			final ArrayList<InputNeuron> inputs = new ArrayList<InputNeuron>(this.brain.getInputNeurons());
			this.inputA = inputs.get(0);
			this.inputB = inputs.get(1);
			this.inputC = inputs.get(2);
			final ArrayList<OutputNeuron> outputs = new ArrayList<OutputNeuron>(this.brain.getOutputNeurons());
			this.output = (OutputBackpropNeuron) outputs.get(0);

			train(TRAINING_CYCLES);

			checkOutput();
		}
		finally
		{
			executor.shutdown();
		}
	}

	private void propogateOutput()
	{
		this.brain.propagate();
	}



	private void backPropogateTraining()
	{
		this.brain.backPropagate();
	}



	private void setCurrentInput(final double[] inputToSet)
	{
		inputA.setInput(inputToSet[0]);
		inputB.setInput(inputToSet[1]);
		inputC.setInput(inputToSet[2]);
	}

	private static boolean checkTruthTable(final double in1, final double in2, final double in3, final double result)
	{
		if( ((in1 > 0.0)&&(in2 <= 0.0)&&(in3 <= 0.0))||
			((in1 <= 0.0)&&(in2 > 0.0)&&(in3 <= 0.0))||
			((in1 <= 0.0)&&(in2 <= 0.0)&&(in3 > 0.0))
		  )
			return (result > 0.0);
		else
			return (result <= 0.0);
	}



	private void checkOutput()
	{
		final double[] curInput =
		{
			-1, -1, -1
		};
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));


		curInput[0] = 1;
		curInput[1] = -1;
		curInput[2] = -1;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = -1;
		curInput[1] = 1;
		curInput[2] = -1;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = -1;
		curInput[1] = -1;
		curInput[2] = 1;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = 1;
		curInput[1] = 1;
		curInput[2] = -1;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = -1;
		curInput[1] = 1;
		curInput[2] = 1;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = 1;
		curInput[1] = -1;
		curInput[2] = 1;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = 1;
		curInput[1] = 1;
		curInput[2] = 1;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));
	}



	private void train(final int count)
	{
		for(int lcv = 0;lcv < count;lcv++)
		{
			final double[] curInput =
			{
				-1, -1, -1
			};
			double curTrain = -1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = 1;
			curInput[1] = -1;
			curInput[2] = -1;
			curTrain = 1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = -1;
			curInput[1] = 1;
			curInput[2] = -1;
			curTrain = 1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = -1;
			curInput[1] = -1;
			curInput[2] = 1;
			curTrain = 1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = 1;
			curInput[1] = 1;
			curInput[2] = -1;
			curTrain = -1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = -1;
			curInput[1] = 1;
			curInput[2] = 1;
			curTrain = -1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = 1;
			curInput[1] = -1;
			curInput[2] = 1;
			curTrain = -1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = 1;
			curInput[1] = 1;
			curInput[2] = 1;
			curTrain = -1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();
		}
	}
}
