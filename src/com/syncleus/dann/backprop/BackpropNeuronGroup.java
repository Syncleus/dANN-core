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
package com.syncleus.dann.backprop;

import com.syncleus.dann.*;
import java.util.*;

/**
 * A NeuronGroup which contains only BackpropNeurons.
 *
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @version 0.1
 */
public class BackpropNeuronGroup extends NeuronGroup<BackpropNeuron>
{
	// <editor-fold defaultstate="collapsed" desc="Constructors">

	/**
	 * Creates an instance of a default empty BackpropNeuronGroup
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
    public BackpropNeuronGroup()
    {
        super();
    }

	// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">

    /**
     * Propogates the output of the BackpropNeurons from the incoming synapse to
     * the outgoign one.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.backprop.BackpropNeuron#propagate
     */
    public void propagate()
    {
        for (BackpropNeuron currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.propagate();
    }



    /**
     * Back propogates the taining set of the BackpropNeuron from the outgoing
     * synapse to the incomming one.
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.backprop.BackpropNeuron#backPropagate
     */
    public void backPropagate()
    {
        for (BackpropNeuron currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.backPropagate();
    }

	/**
	 * Gets all the Neurons that either connect to, or are connected from, any
	 * of the BackpropNeurons in this group.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of source and destination BackpropNeurons.
	 * @since 0.1
	 */
	@SuppressWarnings("unchecked")
   public Set<Neuron> getNeighbors()
    {
        HashSet<Neuron> returnSet = new HashSet<Neuron>();
		for(Neuron currentNeuron : this.getChildrenNeuronsRecursivly())
			returnSet.addAll(currentNeuron.getNeighbors());
		return Collections.unmodifiableSet(returnSet);
    }



	/**
	 * Gets all the Neurons that connect to any of the BackpropNeurons in this
	 * group.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of source BackpropNeurons.
	 * @since 0.1
	 */
	@SuppressWarnings("unchecked")
    public Set<Neuron> getSourceNeighbors()
    {
        HashSet<Neuron> returnSet = new HashSet<Neuron>();
		for(Neuron currentNeuron : this.getChildrenNeuronsRecursivly())
			returnSet.addAll(currentNeuron.getSourceNeighbors());
		return Collections.unmodifiableSet(returnSet);
    }



	/**
	 * Gets all the Neurons that any of the BackpropNeurons in this group
	 * connect to.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of destination BackpropNeurons.
	 * @since 0.1
	 */
	@SuppressWarnings("unchecked")
    public Set<BackpropNeuron> getDestinationNeighbors()
    {
        HashSet<BackpropNeuron> returnSet = new HashSet<BackpropNeuron>();
		for(BackpropNeuron currentNeuron : this.getChildrenNeuronsRecursivly())
			returnSet.addAll(currentNeuron.getDestinationNeighbors());
		return Collections.unmodifiableSet(returnSet);
    }


    // </editor-fold>

}
