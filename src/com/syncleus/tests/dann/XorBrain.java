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

import java.io.BufferedReader;
import java.util.ArrayList;

import com.syncleus.dann.DNA;
import com.syncleus.dann.InputNeuron;
import com.syncleus.dann.NeuronGroup;
import com.syncleus.dann.Neuron;
import com.syncleus.dann.OutputNeuron;


/**
 * An example class that shows using dANN to solve an XOR problem. An XOR
 * is a circuit that returns true (1) when only one of its inputs is true. It
 * returns false (-1) if none all of its inputs are false or if more then one
 * of its inputs are true.
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 */

public class XorBrain {
	
	private DNA myDNA = new DNA();
	private BufferedReader inReader = null;
	private InputNeuron inputA = null;
	private InputNeuron inputB = null;
	private InputNeuron inputC = null;
	private NeuronGroup firstLayer = null;
	private NeuronGroup secondLayer = null;
	private OutputNeuron output = null;
	private String saveLocation = "default.dann";

	/**
	 * XorBrain is a modified com.syncleus.core.dann.examples.xor.Main
	 * which is not static -> to use in GUIs
	 * <!-- Author: chickenf -->
	 */

	public XorBrain() {

		//Adjust the learning rate
		myDNA.learningRate = 0.05;
			
		//creates the first layer which holds all the input neurons
		inputA = new InputNeuron(myDNA);
		inputB = new InputNeuron(myDNA);
		inputC = new InputNeuron(myDNA);
		firstLayer = new NeuronGroup(myDNA);
		firstLayer.add(inputA);
		firstLayer.add(inputB);
		firstLayer.add(inputC);

		//creates the second layer of neurons containing 10 neurons.
		secondLayer = new NeuronGroup(myDNA);
		for( int lcv = 0; lcv < 10; lcv++ )
		{
			secondLayer.add(new Neuron(myDNA));
		}

		//the output layer is just a single neuron
		output = new OutputNeuron(myDNA);
		
		//connects the network in a feedforward fasion.
		firstLayer.connectAllTo(secondLayer);
		secondLayer.connectAllTo(output);
	}
	
	private void propagateOutput() {
		firstLayer.propagate();
		secondLayer.propagate();
		output.propagate();
	}
	
	private void backPropagateTraining() {
		output.backPropagate();
		secondLayer.backPropagate();
		firstLayer.backPropagate();
	}
	
	private void setCurrentInput(double[] inputToSet) {
		inputA.setInput(inputToSet[0]);
		inputB.setInput(inputToSet[1]);
		inputC.setInput(inputToSet[2]);
	}
	
	public ArrayList<Double> testOutput() {
		
		ArrayList<Double> resultsList = new ArrayList<Double>();  
//		String results = "";
		
        double[] curInput = {0, 0, 0};
        setCurrentInput(curInput);
        propagateOutput();
        double[] curOutput;
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
        
        
        curInput[0] = 1;
        curInput[1] = 0;
        curInput[2] = 0;
        setCurrentInput(curInput);
        propagateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 0;
        curInput[1] = 1;
        curInput[2] = 0;
        setCurrentInput(curInput);
        propagateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 0;
        curInput[1] = 0;
        curInput[2] = 1;
        setCurrentInput(curInput);
        propagateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 1;
        curInput[1] = 1;
        curInput[2] = 0;
        setCurrentInput(curInput);
        propagateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 0;
        curInput[1] = 1;
        curInput[2] = 1;
        setCurrentInput(curInput);
        propagateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 1;
        curInput[1] = 0;
        curInput[2] = 1;
        setCurrentInput(curInput);
        propagateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 1;
        curInput[1] = 1;
        curInput[2] = 1;
        setCurrentInput(curInput);
        propagateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());

        return resultsList;
	}
	
	public void train(int count) {

		for(int lcv = 0; lcv < count; lcv++) {

			double[] curInput = {0, 0, 0};
            double curTrain = -1;
            setCurrentInput(curInput);
            propagateOutput();
            output.setDesired(curTrain);
            backPropagateTraining();
				
            curInput[0] = 1;
            curInput[1] = 0;
            curInput[2] = 0;
            curTrain = 1;
            setCurrentInput(curInput);
            propagateOutput();
            output.setDesired(curTrain);
            backPropagateTraining();
				
            curInput[0] = 0;
            curInput[1] = 1;
            curInput[2] = 0;
            curTrain = 1;
            setCurrentInput(curInput);
            propagateOutput();
            output.setDesired(curTrain);
            backPropagateTraining();
				
            curInput[0] = 0;
            curInput[1] = 0;
            curInput[2] = 1;
            curTrain = 1;
            setCurrentInput(curInput);
            propagateOutput();
            output.setDesired(curTrain);
            backPropagateTraining();
				
            curInput[0] = 1;
            curInput[1] = 1;
            curInput[2] = 0;
            curTrain = -1;
            setCurrentInput(curInput);
            propagateOutput();
            output.setDesired(curTrain);
            backPropagateTraining();
				
            curInput[0] = 0;
            curInput[1] = 1;
            curInput[2] = 1;
            curTrain = -1;
            setCurrentInput(curInput);
            propagateOutput();
            output.setDesired(curTrain);
            backPropagateTraining();
				
            curInput[0] = 1;
            curInput[1] = 0;
            curInput[2] = 1;
            curTrain = -1;
            setCurrentInput(curInput);
            propagateOutput();
            output.setDesired(curTrain);
            backPropagateTraining();
				
            curInput[0] = 1;
            curInput[1] = 1;
            curInput[2] = 1;
            curTrain = -1;
            setCurrentInput(curInput);
            propagateOutput();
            output.setDesired(curTrain);
            backPropagateTraining();
        }
	}
	
	public NeuronGroup getSecondLayer() {
		return this.secondLayer;
	}
}


