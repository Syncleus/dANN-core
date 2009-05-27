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


import com.syncleus.dann.backprop.*;
import com.syncleus.dann.*;
import org.junit.*;

/**
 * An example main class that shows using dANN to solve an XOR problem. An XOR
 * is a circuit that returns true (1) when only one of its inputs is true. It
 * returns false (-1) if none all of its inputs are false or if more then one
 * of its inputs are true.
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 */
public
	class TestXor
{
	private static
		DNA myDNA = new DNA();
	private static
		InputBackpropNeuron inputA = null;
	private static
		InputBackpropNeuron inputB = null;
	private static
		InputBackpropNeuron inputC = null;
	private static
		BackpropNeuronGroup firstLayer = null;
	private static
		BackpropNeuronGroup secondLayer = null;
	private static
		BackpropNeuronGroup thirdLayer = null;
	private static
		OutputBackpropNeuron output = null;



	@Test
	public void testXor() throws DannException
	{
		//Adjust the learning rate
		myDNA.learningRate = 0.01;

		//creates the first layer which holds all the input neurons
		inputA = new InputBackpropNeuron(myDNA);
		inputB = new InputBackpropNeuron(myDNA);
		inputC = new InputBackpropNeuron(myDNA);
		firstLayer = new BackpropNeuronGroup(myDNA);
		firstLayer.add(inputA);
		firstLayer.add(inputB);
		firstLayer.add(inputC);

		//creates the second layer of neurons
		secondLayer = new BackpropNeuronGroup(myDNA);
		for(int lcv = 0;lcv < 10;lcv++)
			secondLayer.add(new BackpropNeuron(myDNA));

		//creates the third layer of neurons
		thirdLayer = new BackpropNeuronGroup(myDNA);
		for(int lcv = 0;lcv < 10;lcv++)
			thirdLayer.add(new BackpropNeuron(myDNA));

		//the output layer is just a single neuron
		output = new OutputBackpropNeuron(myDNA);

		//connects the network in a feedforward fasion.
		firstLayer.connectAllTo(secondLayer);
		secondLayer.connectAllTo(thirdLayer);
		thirdLayer.connectAllTo(output);

		int cycles = 50000;
		train(cycles);

		testOutput();
	}

	private static
		void propogateOutput()
	{
		firstLayer.propagate();
		secondLayer.propagate();
		thirdLayer.propagate();
		output.propagate();
	}



	private static
		void backPropogateTraining()
	{
		output.backPropagate();
		thirdLayer.backPropagate();
		secondLayer.backPropagate();
		firstLayer.backPropagate();
	}



	private static
		void setCurrentInput(double[] inputToSet)
	{
		inputA.setInput(inputToSet[0]);
		inputB.setInput(inputToSet[1]);
		inputC.setInput(inputToSet[2]);
	}

	private static boolean checkTruthTable(double in1, double in2, double in3, double result)
	{
		if( ((in1 > 0.0)&&(in2 <= 0.0)&&(in3 <= 0.0))||
			((in1 <= 0.0)&&(in2 > 0.0)&&(in3 <= 0.0))||
			((in1 <= 0.0)&&(in2 <= 0.0)&&(in3 > 0.0))
		  )
		{
			if(result > 0.0)
				return true;
			else
				return false;
		}
		else
		{
			if(result <= 0.0)
				return true;
			else
				return false;
		}
	}



	private static void testOutput()
	{
		double[] curInput =
		{
			0, 0, 0
		};
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));


		curInput[0] = 1;
		curInput[1] = 0;
		curInput[2] = 0;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = 0;
		curInput[1] = 1;
		curInput[2] = 0;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = 0;
		curInput[1] = 0;
		curInput[2] = 1;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = 1;
		curInput[1] = 1;
		curInput[2] = 0;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = 0;
		curInput[1] = 1;
		curInput[2] = 1;
		setCurrentInput(curInput);
		propogateOutput();
		Assert.assertTrue("Failed Truth Table: curInput[0]:" + curInput[0] + " curInput[1]:" + curInput[1] + " curInput[2]:" + curInput[2] + " result:" + output.getOutput(), checkTruthTable(curInput[0], curInput[1], curInput[2], output.getOutput()));

		curInput[0] = 1;
		curInput[1] = 0;
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



	private static void train(int count)
	{
		for(int lcv = 0;lcv < count;lcv++)
		{
			double[] curInput =
			{
				0, 0, 0
			};
			double curTrain = -1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = 1;
			curInput[1] = 0;
			curInput[2] = 0;
			curTrain = 1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = 0;
			curInput[1] = 1;
			curInput[2] = 0;
			curTrain = 1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = 0;
			curInput[1] = 0;
			curInput[2] = 1;
			curTrain = 1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = 1;
			curInput[1] = 1;
			curInput[2] = 0;
			curTrain = -1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = 0;
			curInput[1] = 1;
			curInput[2] = 1;
			curTrain = -1;
			setCurrentInput(curInput);
			propogateOutput();
			output.setDesired(curTrain);
			backPropogateTraining();

			curInput[0] = 1;
			curInput[1] = 0;
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
