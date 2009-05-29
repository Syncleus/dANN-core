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
import java.util.*;

public class BackpropNeuronGroup extends NeuronGroup<BackpropNeuron>
{
    public BackpropNeuronGroup()
    {
        super();
    }


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
        for (BackpropNeuron currentChild : this.getChildrenNeuronsRecursivly())
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
        for (BackpropNeuron currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.backPropagate();
    }

	@SuppressWarnings("unchecked")
   public Set<Neuron> getNeighbors()
    {
        HashSet<Neuron> returnSet = new HashSet<Neuron>();
		for(Neuron currentNeuron : this.getChildrenNeuronsRecursivly())
			returnSet.addAll(currentNeuron.getNeighbors());
		return Collections.unmodifiableSet(returnSet);
    }



	@SuppressWarnings("unchecked")
    public Set<Neuron> getSourceNeighbors()
    {
        HashSet<Neuron> returnSet = new HashSet<Neuron>();
		for(Neuron currentNeuron : this.getChildrenNeuronsRecursivly())
			returnSet.addAll(currentNeuron.getSourceNeighbors());
		return Collections.unmodifiableSet(returnSet);
    }



	@SuppressWarnings("unchecked")
    public Set<Neuron> getDestinationNeighbors()
    {
        HashSet<Neuron> returnSet = new HashSet<Neuron>();
		for(Neuron currentNeuron : this.getChildrenNeuronsRecursivly())
			returnSet.addAll(currentNeuron.getDestinationNeighbors());
		return Collections.unmodifiableSet(returnSet);
    }


    // </editor-fold>

}
