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
public class NeuronGroup<N extends NeuronImpl> implements java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * This contains all the neurons considered to be a part of this layer. Any
     * one neuron can only belong to one layer. But one layer owns many neurons.
     * <BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected HashSet<N> childrenNeurons = new HashSet<N>();

    /**
     * This contains all the neuronGroups considered to be a part of this layer. Any
     * one neuron can only belong to one layer. But one layer owns many neurons.
     * <BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
	protected HashSet<NeuronGroup<N>> childrenNeuronGroups = new HashSet<NeuronGroup<N>>();

	protected Random random = new Random();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new instance of NeuronGroup<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public NeuronGroup()
    {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">
    /**
     * Adds another Neuron to this layer.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param toAdd the Neuron to add.
     */
    public void add(N toAdd)
    {
        this.childrenNeurons.add(toAdd);
    }

    /**
     * Adds another NeuronGroup to this layer.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param toAdd the NeuronGroup to add.
     */
    public void add(NeuronGroup<N> toAdd)
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
	@SuppressWarnings("unchecked")
    public void connectAllTo(N toConnectTo) throws InvalidConnectionTypeDannException
    {
		try
		{
			for (N currentChild : this.childrenNeurons)
				currentChild.connectTo(toConnectTo);
			for(NeuronGroup<N> currentChild : this.childrenNeuronGroups)
				currentChild.connectAllTo(toConnectTo);
		}
		catch(ClassCastException caughtException)
		{
			throw new AssertionError(caughtException);
		}
    }

	@SuppressWarnings("unchecked")
	public void connectAllTo(NeuronGroup<N> toConnectTo) throws InvalidConnectionTypeDannException
	{
		try
		{
			for (N currentChild : this.childrenNeurons)
			{
				Set<N> toConnectToChildren = toConnectTo.getChildrenNeuronsRecursivly();
				for (N currentOut : toConnectToChildren)
					currentChild.connectTo(currentOut);
			}
			for(NeuronGroup<N> currentChild : this.childrenNeuronGroups)
			{
				Set<N> toConnectToChildren = toConnectTo.getChildrenNeuronsRecursivly();
				for (N currentOut : toConnectToChildren)
					currentChild.connectAllTo(currentOut);
			}
		}
		catch(ClassCastException caughtException)
		{
			throw new AssertionError(caughtException);
		}
	}



    /**
     * Obtains all the Neurons directly owned by this NeuronGroup.
     * @since 0.1
     */
    public Set<N> getChildrenNeurons()
    {
        return Collections.unmodifiableSet(this.childrenNeurons);
    }

    /**
     * Obtains all the NeuronGroups directly owned by this NeuronGroup.
     * @since 0.1
     */
    public Set<NeuronGroup<N>> getChildrenNeuronGroups()
    {
        return Collections.unmodifiableSet(this.childrenNeuronGroups);
    }



    /**
     * Obtains all the NetworkNodes owned recursivly excluding
     * NeuronGroups.<BR>
     * @since 0.1
     */
    public Set<N> getChildrenNeuronsRecursivly()
    {
        HashSet<N> returnList = new HashSet<N>();

		returnList.addAll(this.childrenNeurons);
		for (NeuronGroup<N> currentChild : this.childrenNeuronGroups)
			returnList.addAll(currentChild.getChildrenNeuronsRecursivly());

        return Collections.unmodifiableSet(returnList);
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
        for (N currentChild : this.getChildrenNeuronsRecursivly())
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
        for (N currentChild : this.getChildrenNeuronsRecursivly())
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
}
