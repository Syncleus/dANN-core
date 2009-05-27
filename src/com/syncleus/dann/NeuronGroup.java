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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * A special NetworkNode which can contain other NetworkNodes as children.
 * <BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class NeuronGroup implements java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * This contains all the neurons considered to be a part of this layer. Any
     * one neuron can only belong to one layer. But one layer owns many neurons.
     * <BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected HashSet<Neuron> childrenNeurons = new HashSet<Neuron>();

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * This contains all the neuronGroups considered to be a part of this layer. Any
     * one neuron can only belong to one layer. But one layer owns many neurons.
     * <BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
	protected HashSet<NeuronGroup> childrenNeuronGroups = new HashSet<NeuronGroup>();
    /**
     * This will determine most of the properties of the layer.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    DNA ownedDNA;

	protected Random random = new Random();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new instance of NeuronGroup<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public NeuronGroup(DNA ownedDNAToSet)
    {
        this.ownedDNA = ownedDNAToSet;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">
    /**
     * Adds another Neuron to this layer.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param toAdd the Neuron to add.
     */
    public void add(Neuron toAdd)
    {
        this.childrenNeurons.add(toAdd);
    }

    /**
     * Adds another NeuronGroup to this layer.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param toAdd the NeuronGroup to add.
     */
    public void add(NeuronGroup toAdd)
    {
        this.childrenNeuronGroups.add(toAdd);
    }



    /**
     * Connects all the NetworkNodes in this layer recursivly to all the
     * NetworkNodes in another layer.
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param toConnectTo This is the layer the neurons will be connecting
     * 	to.
     * @see com.syncleus.dann.NeuronGroup#connectTo
     */
    public void connectAllTo(Neuron toConnectTo) throws InvalidConnectionTypeDannException
    {
		for (Neuron currentChild : this.childrenNeurons)
			currentChild.connectTo(toConnectTo);
		for(NeuronGroup currentChild : this.childrenNeuronGroups)
			currentChild.connectAllTo(toConnectTo);
    }

	public void connectAllTo(NeuronGroup toConnectTo) throws InvalidConnectionTypeDannException
	{
		for (Neuron currentChild : this.childrenNeurons)
		{
			Set<Neuron> toConnectToChildren = toConnectTo.getChildrenNeuronsRecursivly();
			for (Neuron currentOut : toConnectToChildren)
				currentChild.connectTo(currentOut);
		}
		for(NeuronGroup currentChild : this.childrenNeuronGroups)
		{
			Set<Neuron> toConnectToChildren = toConnectTo.getChildrenNeuronsRecursivly();
			for (Neuron currentOut : toConnectToChildren)
				currentChild.connectAllTo(currentOut);
		}
	}



    /**
     * Obtains all the Neurons directly owned by this NeuronGroup.
     * @since 0.1
     */
    public Set<Neuron> getChildrenNeurons()
    {
        return Collections.unmodifiableSet(this.childrenNeurons);
    }

    /**
     * Obtains all the NeuronGroups directly owned by this NeuronGroup.
     * @since 0.1
     */
    public Set<NeuronGroup> getChildrenNeuronGroups()
    {
        return Collections.unmodifiableSet(this.childrenNeuronGroups);
    }



    /**
     * Obtains all the NetworkNodes owned recursivly excluding
     * NeuronGroups.<BR>
     * @since 0.1
     */
    public Set<Neuron> getChildrenNeuronsRecursivly()
    {
        HashSet<Neuron> returnList = new HashSet<Neuron>();

		returnList.addAll(this.childrenNeurons);
		for (NeuronGroup currentChild : this.childrenNeuronGroups)
			returnList.addAll(currentChild.getChildrenNeuronsRecursivly());

        return Collections.unmodifiableSet(returnList);
    }



    /**
     * Randomly returns one of the children.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return A randomly selected child.
     */
    private Neuron getRandomChild()
    {
		Set<Neuron> childrenSet = this.getChildrenNeuronsRecursivly();

		Neuron[] allChildren = new Neuron[childrenSet.size()];
		childrenSet.toArray(allChildren);

		return allChildren[this.random.nextInt(allChildren.length)];
    }



    /**
     * Causes the NetworkNode to disconnect all outgoing connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#disconnectAllSources
     * @see com.syncleus.dann.NetworkNode#disconnectAll
     */
    public void disconnectAllDestinations()
    {
        for (Neuron currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.disconnectAllDestinations();
    }



    /**
     * Causes the NetworkNode to disconnect all incomming connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#disconnectAllDestinations
     * @see com.syncleus.dann.NetworkNode#disconnectAll
     */
    public void disconnectAllSources()
    {
        for (Neuron currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.disconnectAllSources();
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">
    /**
     * Propogates the output of the NetworkNodes from the incoming synapse to
     * the outgoign one.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#propagate
     */
    public void propagate()
    {
        for (Neuron currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.propagate();
    }



    /**
     * Back propogates the taining set of the NetworkNodes from the outgoing
     * synapse to the incomming one.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#backPropagate
     */
    public void backPropagate()
    {
        for (Neuron currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.backPropagate();
    }
    
   public Set<Neuron> getNeighbors()
    {
        throw new Error("Not yet implemented");
    }



    public Set<Neuron> getSourceNeighbors()
    {
        throw new Error("Not yet implemented");
    }



    public Set<Neuron> getDestinationNeighbors()
    {
        throw new Error("Not yet implemented");
    }
    
    
    // </editor-fold>
}
