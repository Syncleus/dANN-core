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
package com.syncleus.dann.backprop;

import com.syncleus.dann.*;
import com.syncleus.dann.activation.ActivationFunction;
import java.util.Hashtable;


/**
 * The neuron is the most fundemental component of the network; it is also the
 * thinktank of the system. One neuron will usually connect to many other
 * NetworkNodes through synapses and receive input from many other
 * NetworkNodes in the same way.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.Synapse
 */
public class BackpropNeuron extends NeuronImpl<NeuronImpl, BackpropNeuron>
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * This represents the net effect of all the training data from all the
     * inputs. It is essentially the reverse of the activity value.
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#activity
     */
    protected double deltaTrain = 0;

	private double learningRate = 0.001;

	protected Hashtable<Synapse, Double> deltaTrainDestinations = new Hashtable<Synapse, Double>();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new instance of Neuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public BackpropNeuron()
    {
		super();
    }



    public BackpropNeuron(ActivationFunction activationFunction)
    {
        super(activationFunction);
    }


	public BackpropNeuron(double learningRate)
	{
		super();
		this.learningRate = learningRate;
	}

	public BackpropNeuron(ActivationFunction activationFunction, double learningRate)
	{
		super(activationFunction);
		this.learningRate = learningRate;
	}

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">
    /**
     * This method is called externally to connect to another NetworkNode.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outUnit The NetworkNode to connect to.
     * @see com.syncleus.dann.Neuron#connectFrom
     */
    public void connectTo(BackpropNeuron outUnit) throws InvalidConnectionTypeDannException
    {
        //make sure you arent already connected to the neuron
        if (outUnit == null)
            throw new NullPointerException("outUnit can not be null!");

        //connect to the neuron
        Synapse newSynapse = new Synapse(this, outUnit, ((this.random.nextDouble() * 2.0) - 1.0) / 10000.0);
        this.destinations.add(newSynapse);
		this.deltaTrainDestinations.put(newSynapse, Double.valueOf(0.0));
        outUnit.connectFrom(newSynapse);
    }

	protected void removeDestination(Synapse outSynapse) throws SynapseDoesNotExistException
	{
		super.removeDestination(outSynapse);
		this.deltaTrainDestinations.remove(outSynapse);
	}

	public void disconnectDestination(Synapse outSynapse) throws SynapseNotConnectedException
	{
		super.disconnectDestination(outSynapse);
		this.deltaTrainDestinations.remove(outSynapse);
	}

	// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">
    /**
     * Backpropogates the training data to all the incomming synapses.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public void backPropagate()
    {
        this.calculateDeltaTrain();

        //step thru source synapses and make them learn their new weight.
        for (Synapse currentSynapse : this.getSources())
		{
			NeuronImpl sourceNeuron = currentSynapse.getSource();
			if(sourceNeuron instanceof BackpropNeuron)
			{
				BackpropNeuron sourceBackpropNeuron = (BackpropNeuron) sourceNeuron;

				sourceBackpropNeuron.deltaTrainDestinations.put(currentSynapse, Double.valueOf(this.deltaTrain));

				currentSynapse.setWeight(currentSynapse.getWeight() + (this.deltaTrain * this.learningRate * currentSynapse.getInput()));
			}
		}

        //learn the biases new weight
        this.biasWeight += this.learningRate * this.deltaTrain;
    }



    /**
     * Calculates the Delta Train based on all the destination synapses<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#backPropagate
     */
    protected void calculateDeltaTrain()
    {
        this.deltaTrain = 0;
        for (Synapse currentSynapse : this.destinations)
            this.deltaTrain += (currentSynapse.getWeight() * this.deltaTrainDestinations.get(currentSynapse).doubleValue());
        this.deltaTrain *= this.activateDerivitive();
    }



    /**
     * Propogates the current output to all outgoing synapses.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public void propagate()
    {
        //calculate the current input activity
        this.activity = 0;
        for (Synapse currentSynapse : this.getSources())
            this.activity += currentSynapse.getOutput();
        //Add the bias to the activity
        this.activity += this.biasWeight;

        //calculate the activity function and set the result as the output
        this.setOutput(this.activate());
    }



    public double getDeltaTrain()
    {
        return deltaTrain;
    }
    // </editor-fold>
}
