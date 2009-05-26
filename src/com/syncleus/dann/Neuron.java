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

import com.syncleus.dann.activation.ActivationFunction;
import com.syncleus.dann.activation.HyperbolicTangentActivationFunction;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


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
public class Neuron implements java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * Represents the current excitation of the neuron from input
     * signals<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected double activity;
    /**
     * Represents the current output of the neuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected double output;
    /**
     * The current weight of the bias input. The bias is an input that is always
     * set to an on position. The bias weight usually adapts in the same manner
     * as the rest of the synapse's weights.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected double biasWeight;
    /**
     * An array list of all the synapses that this neuron is currently
     * connection out to.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected HashSet<Synapse> destinations = new HashSet<Synapse>();
    /**
     * All the synapses currently connecting into this neuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private HashSet<Synapse> sources = new HashSet<Synapse>();
    /**
     * The DNA determines this neurons basic properties.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected DNA ownedDNA;
    /**
     * This represents the net effect of all the training data from all the
     * inputs. It is essentially the reverse of the activity value.
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#activity
     */
    protected double deltaTrain = 0;
    protected ActivationFunction activationFunction;
	protected Random random = new Random();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new instance of Neuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public Neuron(DNA ownedDNAToSet)
    {
        if (ownedDNAToSet == null)
            throw new NullPointerException("DNA can not be null");

        this.ownedDNA = ownedDNAToSet;
        this.biasWeight = ((this.random.nextDouble() * 2.0) - 1.0) / 1000.0;
        this.activationFunction = new HyperbolicTangentActivationFunction();
    }



    public Neuron(DNA ownedDNAToSet, ActivationFunction activationFunction)
    {
        if (activationFunction == null)
            throw new NullPointerException("activationFunction can not be null");
        if (ownedDNAToSet == null)
            throw new NullPointerException("DNA can not be null");

        this.ownedDNA = ownedDNAToSet;
        this.biasWeight = ((this.random.nextDouble() * 2.0) - 1.0) / 1000.0;
        this.activationFunction = activationFunction;
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
    public void connectTo(Neuron outUnit) throws DannException
    {
        //make sure you arent already connected to the neuron
        if (outUnit == null)
            throw new NullPointerException("outUnit can not be null!");

        //connect to the neuron
        Synapse newSynapse = new Synapse(this, outUnit, ((this.random.nextDouble() * 2.0) - 1.0) / 10000.0);
        this.destinations.add(newSynapse);
        outUnit.connectFrom(newSynapse);
    }



    /**
     * This method is called internally, between NetworkNodes, to
     * facilitate the connection process.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The synapse to connect from.
     * @see com.syncleus.dann.Neuron#connectTo
     */
    protected void connectFrom(Synapse inSynapse) throws DannException
    {
        //make sure you arent already connected fromt his neuron

        //add the synapse to the source list
        this.sources.add(inSynapse);
    }

    /**
     * Causes the NetworkNode to disconnect all connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#disconnectAllSources
     * @see com.syncleus.dann.NetworkNode#disconnectAllDestinations
     */
    public void disconnectAll()
    {
        this.disconnectAllDestinations();
        this.disconnectAllSources();
    }



    /**
     * Causes the NetworkNode to disconnect all outgoing connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#disconnectAllSources
     * @see com.syncleus.dann.Neuron#disconnectAll
     */
    public void disconnectAllDestinations()
    {
        for (Synapse currentDestination : this.destinations)
            try
            {
                this.disconnectDestination(currentDestination);
            }
            catch (SynapseNotConnectedException caughtException)
            {
                caughtException.printStackTrace();
                throw new InternalError("Unexpected Runtime Exception: " + caughtException);
            }
    }



    /**
     * Causes the NetworkNode to disconnect all incomming connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#disconnectAllDestinations
     * @see com.syncleus.dann.Neuron#disconnectAll
     */
    public void disconnectAllSources()
    {
        for (Synapse currentSource : this.sources)
            try
            {
                this.disconnectSource(currentSource);
            }
            catch (SynapseNotConnectedException caughtException)
            {
                caughtException.printStackTrace();
                throw new InternalError("Unexpected Runtime Exception: " + caughtException);
            }
    }



    /**
     * Disconnects from a perticular outgoing connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outSynapse The outgoing synapse to disconnect from.<BR>
     * @see com.syncleus.dann.Neuron#removeSource
     */
    public void disconnectDestination(Synapse outSynapse) throws SynapseNotConnectedException
    {
        if (this instanceof OutputNeuron)
            throw new InvalidParameterException("Can not disconnect a destination for a OutputNeuron");

        if (this.destinations.remove(outSynapse) == false)
            throw new SynapseNotConnectedException("can not disconnect destination, does not exist.");

        try
        {
            if (outSynapse.getDestination() != null)
                outSynapse.getDestination().removeSource(outSynapse);
        }
        catch (SynapseDoesNotExistException caughtException)
        {
            //do nothing, its a recoverable exception
        }
    }



    /**
     * Disconnects from a perticular incomming connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to disconnect from.<BR>
     * @see com.syncleus.dann.Neuron#removeDestination
     */
    public void disconnectSource(Synapse inSynapse) throws SynapseNotConnectedException
    {
        if (this instanceof InputNeuron)
            throw new InvalidParameterException("Can not disconnect a source for a InputNeuron");

        if (this.sources.remove(inSynapse) == false)
            throw new SynapseNotConnectedException("can not disconnect source, does not exist.");

        try
        {
            if (inSynapse.getSource() != null)
                inSynapse.getSource().removeDestination(inSynapse);
        }
        catch (SynapseDoesNotExistException caughtException)
        {
            //do nothing, its a recoverable exception
        }
    }



    /**
     * Called internally to facilitate the removal of a connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outSynapse The incomming synapse to remove from memory.<BR>
     * @see com.syncleus.dann.Neuron#disconnectSource
     */
    protected void removeDestination(Synapse outSynapse) throws SynapseDoesNotExistException
    {
        if (this instanceof OutputNeuron)
            throw new InvalidParameterException("Can not remove a destination for a OutputNeuron");

        if (this.destinations.remove(outSynapse) == false)
            throw new SynapseDoesNotExistException("Can not remove destination, does not exist.");
    }



    /**
     * Called internally to facilitate the removal of a connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to remove from memory.<BR>
     * @see com.syncleus.dann.Neuron#disconnectDestination
     */
    protected void removeSource(Synapse inSynapse) throws SynapseDoesNotExistException
    {
        if (this instanceof InputNeuron)
            throw new InvalidParameterException("Can not disconnect a source for a InputNeuron");

        if (this.sources.remove(inSynapse) == false)
            throw new SynapseDoesNotExistException("Can not remove destination, does not exist.");
    }



    public Set<Synapse> getDestinations()
    {
        return Collections.unmodifiableSet(this.destinations);
    }



    public Set<Neuron> getNeighbors()
    {
        HashSet<Neuron> neighbors = new HashSet<Neuron>();
        for (Synapse source : this.getSources())
            neighbors.add(source.getSource());
        for (Synapse destination : this.getDestinations())
            neighbors.add(destination.getDestination());

        return Collections.unmodifiableSet(neighbors);
    }



    public Set<Neuron> getSourceNeighbors()
    {
        HashSet<Neuron> neighbors = new HashSet<Neuron>();
        for (Synapse source : this.getSources())
            neighbors.add(source.getSource());

        return Collections.unmodifiableSet(neighbors);
    }



    public Set<Neuron> getDestinationNeighbors()
    {
        HashSet<Neuron> neighbors = new HashSet<Neuron>();
        for (Synapse destination : this.getDestinations())
            neighbors.add(destination.getDestination());

        return Collections.unmodifiableSet(neighbors);
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
        for (Synapse currentSynapse : this.sources)
            currentSynapse.learnWeight(this.deltaTrain, this.ownedDNA.learningRate);

        //learn the biases new weight
        this.biasWeight += this.ownedDNA.learningRate * this.deltaTrain;
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
            this.deltaTrain += currentSynapse.getDifferential();
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
        for (Synapse currentSynapse : this.sources)
            this.activity += currentSynapse.getOutput();
        //Add the bias to the activity
        this.activity += this.biasWeight;

        //calculate the activity function and set the result as the output
        this.setOutput(this.activate());
    }



    /**
     * sets the current output on all outgoing synapses.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#propagate
     * @param newOutput The output value.
     */
    protected void setOutput(double newOutput)
    {
        this.output = newOutput;

        for (Synapse current : this.destinations)
            current.setInput(newOutput);
    }



    /**
     * Gets the current output.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return The current output.
     */
    public double getOutput()
    {
        return this.output;
    }



    /**
     * obtains the output as a function of the current activity. This is a bound
     * function (usually between -1 and 1) based on the current activity of the
     * neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return a bound value (between -1 and 1 if this function is not
     * 	overwritten). It is a function of the neuron's current activity.
     * @see com.syncleus.dann.Neuron#propagate
     */
    protected double activate()
    {
        return this.activationFunction.activate(this.activity);
    }



    /**
     * This must be the derivity of the ActivityFunction. As such it's output is
     * also based on the current activity of the neuron. If the
     * activationFunction is overwritten then this method must also be
     * overwritten with the proper derivative.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return the derivative output of the activationFunction
     * @see com.syncleus.dann.Neuron#activationFunction
     */
    protected double activateDerivitive()
    {
        return this.activationFunction.activateDerivative(this.activity);
    }



    public double getDeltaTrain()
    {
        return deltaTrain;
    }



    public DNA getOwnedDNA()
    {
        return ownedDNA;
    }



    public Set<Synapse> getSources()
    {
        return Collections.unmodifiableSet(sources);
    }
    // </editor-fold>
}
