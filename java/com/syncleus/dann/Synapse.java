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

package com.syncleus.dann;

/**
 * The synapse acts as a bridge between connected neurons. It is also where the
 * connection weights are stores and manipulated.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.Neuron
 */
public class Synapse
{
	// <editor-fold defaultstate="collapsed" desc="Attributes">
	
    /**
	  * The outgoing neuron connection.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private Neuron destinationNeuron;
	 
    /**
	  * The incomming neuron connection.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private Neuron sourceNeuron;
	 
    /**
	  * The current weight of the synapse<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private double weight = 0.0;
	 
    /**
	  * The current output of the synapse<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private double output = 0.0;
	 
    /**
	  * The current input from the synapse<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private double input = 0.0;
	 
    /**
	  * The current synapse's deltaTrain<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private double deltaTrain = 0.0;
	 
	 // </editor-fold>
	 
	// <editor-fold defaultstate="collapsed" desc="Constructors">
    
    /**
	  * Creates a new instance of Synapse<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param SourceToSet The incomming neuron connection.
	  * @param DestinationToSet The outgoing neuron connection.
	  */
    public Synapse(Neuron sourceToSet, Neuron destinationToSet, double initialWeight)
    {
        this.destinationNeuron = destinationToSet;
        this.sourceNeuron = sourceToSet;
        this.weight = initialWeight;
    }
	 
	 // </editor-fold>
	 
	 // <editor-fold defaultstate="collapsed" desc="Link Traversal">
	 
	 /**
	  * Obtains the incomming neuron.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @return The source neuron.
	  */
	 public Neuron getSource()
	 {
		 return this.sourceNeuron;
	 }

	 /**
	  * Obtains the outgoing neuron.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @return The destination neuron.
	  */
	 public Neuron getDestination()
	 {
		 return this.destinationNeuron;
	 }
	 
	 // </editor-fold>
    
	// <editor-fold defaultstate="collapsed" desc="Propogation">
	 
    /**
	  * learns the new weight based on the current training set<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#CalculateDeltaTrain
	  */
    public void learnWeight(double deltaTrainToSet, double learningRate)
    {
		 this.deltaTrain = deltaTrainToSet;
        this.weight += learningRate * this.input * this.deltaTrain;
    }

    /**
	  * Calculates the current output of the synapse based on the input and
	  * weight<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#Propogate
	  * @return the current synapse output.
	  */
    public double getOutput()
    {
        this.output = this.input * this.weight;
		 return this.output;
    }
	 
    /**
	  * Set the current input for the synapse<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#Propogate
	  * @param newInput The new input value to set.
	  */
    public void setInput(double newInput)
    {
		 this.input = newInput;
    }
    
    /**
	  * Calculates the synapse differential. This is used when back propogating
	  * training sets.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#BackPropogate
	  * @return the current synapse differential.
	  */
    public double getDifferential()
    {
        return this.deltaTrain * this.weight;
    }
	 
	 // </editor-fold>
}
