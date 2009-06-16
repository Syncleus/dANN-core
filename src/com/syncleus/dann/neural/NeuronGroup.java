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

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * A special NetworkNode which can contain other NetworkNodes as children.
 * 
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Syncleus, Inc.
 * @since 1.0
 */
public class NeuronGroup<N extends NeuronImpl> implements java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * This contains all the neurons considered to be a part of this layer. Any
     * one neuron can only belong to one layer. But one layer owns many neurons.
     * <BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    protected HashSet<N> childrenNeurons = new HashSet<N>();

    /**
     * This contains all the neuronGroups considered to be a part of this layer. Any
     * one neuron can only belong to one layer. But one layer owns many neurons.
     * <BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
	protected HashSet<NeuronGroup<? extends N>> childrenNeuronGroups = new HashSet<NeuronGroup<? extends N>>();

	/**
	 * The random number generator used for this class
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 1.0
	 */
	protected static Random random = new Random();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">

    /**
     * Creates a new empty instance of NeuronGroup
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public NeuronGroup()
    {
    }

	/**
	 * Creates a new NeuronGroup that is a copy of the specified group.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param copyGroup NeuronGroup to copy.
	 * @since 1.0
	 */
	public NeuronGroup(NeuronGroup<? extends N> copyGroup)
	{
		this.childrenNeurons.addAll(copyGroup.childrenNeurons);
		for(NeuronGroup<? extends N> currentGroup : copyGroup.childrenNeuronGroups)
		{
			this.childrenNeuronGroups.add(currentGroup);
		}
	}

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">

    /**
     * Adds another Neuron to this layer.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @param toAdd the Neuron to add.
     */
    public void add(N toAdd)
    {
        this.childrenNeurons.add(toAdd);
    }

    /**
     * Adds another NeuronGroup to this layer.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @param toAdd the NeuronGroup to add.
     */
    public void add(NeuronGroup<? extends N> toAdd)
    {
        this.childrenNeuronGroups.add(toAdd);
    }



    /**
     * Connects all the NetworkNodes in this layer recursivly to all the
     * NetworkNodes in another layer.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @param toConnectTo This is the layer the neurons will be connecting
     * 	to.
     */
	@SuppressWarnings("unchecked")
    public void connectAllTo(N toConnectTo) throws InvalidConnectionTypeDannException
    {
		try
		{
			for (N currentChild : this.childrenNeurons)
				currentChild.connectTo(toConnectTo);
			for(NeuronGroup currentChild : this.childrenNeuronGroups)
				currentChild.connectAllTo(toConnectTo);
		}
		catch(ClassCastException caughtException)
		{
			throw new AssertionError(caughtException);
		}
    }

	/**
	 * Connects all the Neurons in this group, as well as children groups
	 * recursivly to all the neurons in the specified NeuronGroup, also
	 * recurisvly.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param toConnectTo The NeuronGroup to connect to.
	 * @throws com.syncleus.dann.InvalidConnectionTypeDannException Thrown if
	 * any of the neurons can not connect due to invalid types.
	 * @since 1.0
	 */
	@SuppressWarnings("unchecked")
	public void connectAllTo(NeuronGroup<? extends N> toConnectTo) throws InvalidConnectionTypeDannException
	{
		try
		{
			for (N currentChild : this.childrenNeurons)
			{
				Set<? extends N> toConnectToChildren = toConnectTo.getChildrenNeuronsRecursivly();
				for (N currentOut : toConnectToChildren)
					currentChild.connectTo(currentOut);
			}
			for(NeuronGroup currentChild : this.childrenNeuronGroups)
			{
				Set<? extends N> toConnectToChildren = toConnectTo.getChildrenNeuronsRecursivly();
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
     * @since 1.0
     */
    public Set<N> getChildrenNeurons()
    {
        return Collections.unmodifiableSet(this.childrenNeurons);
    }

    /**
     * Obtains all the NeuronGroups directly owned by this NeuronGroup.
     * @since 1.0
     */
    public Set<NeuronGroup<? extends N>> getChildrenNeuronGroups()
    {
        return Collections.unmodifiableSet(this.childrenNeuronGroups);
    }



    /**
     * Obtains all the NetworkNodes owned recursivly excluding
     * NeuronGroups.<BR>
     * @since 1.0
     */
    public Set<N> getChildrenNeuronsRecursivly()
    {
        HashSet<N> returnList = new HashSet<N>();

		returnList.addAll(this.childrenNeurons);
		for (NeuronGroup<? extends N> currentChild : this.childrenNeuronGroups)
			returnList.addAll(currentChild.getChildrenNeuronsRecursivly());

        return Collections.unmodifiableSet(returnList);
    }



    /**
     * Causes the NetworkNode to disconnect all outgoing connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @see com.syncleus.dann.NeuronGroup#disconnectAllSources
     * @see com.syncleus.dann.NeuronGroup#disconnectAll
     */
    public void disconnectAllDestinations()
    {
        for (N currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.disconnectAllDestinations();
    }



    /**
     * Causes the NetworkNode to disconnect all incomming connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @see com.syncleus.dann.NeuronGroup#disconnectAllDestinations
     * @see com.syncleus.dann.NeuronGroup#disconnectAll
     */
    public void disconnectAllSources()
    {
        for (N currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.disconnectAllSources();
    }

    /**
     * Causes the NetworkNode to disconnect all connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @see com.syncleus.dann.NeuronGroup#disconnectAllSources
     * @see com.syncleus.dann.NeuronGroup#disconnectAllDestinations
     */
    public void disconnectAll()
    {
        this.disconnectAllDestinations();
        this.disconnectAllSources();
    }
    // </editor-fold>
}
