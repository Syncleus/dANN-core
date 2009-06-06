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
package com.syncleus.dann;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a single artificial brain typically belonging to a single
 * artificial organism. It will contain a set of input and output neurons which
 * corelates to a specific dataset pattern.<br/>
 * <br/>
 * This class is abstract and must be extended in order to be used.
 *
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @version 0.1
 */
public abstract class Brain implements Serializable
{
    private HashSet<Neuron> neurons = new HashSet<Neuron>();
    private HashSet<OutputNeuron> outputNeurons = new HashSet<OutputNeuron>();
    private HashSet<InputNeuron> inputNeurons = new HashSet<InputNeuron>();



	/**
	 * Adds a new neuron to the brain. The construction of the brain is done
	 * by the child class so this method is protected.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param newNeuron The neuron to add to the brain.
	 * @since 0.1
	 */
    protected void addNeuron(Neuron newNeuron)
    {
        if (this.neurons.contains(newNeuron))
            return;

        this.neurons.add(newNeuron);

		if (newNeuron instanceof OutputNeuron)
			this.outputNeurons.add((OutputNeuron) newNeuron);

		if (newNeuron instanceof InputNeuron)
			this.inputNeurons.add((InputNeuron) newNeuron);
    }

	/**
	 * Adds a new collection of neurons to the brain. The construction of the
	 * brain is done by the child class so this method is protected.
	 *
	 *  <!-- Author: Jeffrey Phillips Freeman -->
	 * @param newNeurons The collection of neurons to add.
	 * @since 0.1
	 */
	protected void addNeurons(Collection<? extends Neuron> newNeurons)
	{
		this.neurons.addAll(newNeurons);

		for(Neuron newNeuron : newNeurons)
		{
			if (newNeuron instanceof OutputNeuron)
				this.outputNeurons.add((OutputNeuron) newNeuron);

			if (newNeuron instanceof InputNeuron)
				this.inputNeurons.add((InputNeuron) newNeuron);
		}
	}



	/**
	 * Removes the specified neuron from the brain. This only removes it from
	 * the collection of neurons it does not disconnect it from other neurons.
	 *
	 *  <!-- Author: Jeffrey Phillips Freeman -->
	 * @param removeNeuron The neuron to remove.
	 * @since 0.1
	 */
    protected void removeNeuron(Neuron removeNeuron)
    {
        if (this.neurons.contains(removeNeuron) == false)
            return;

        this.neurons.remove(removeNeuron);

        if (removeNeuron instanceof OutputNeuron)
            this.outputNeurons.remove((OutputNeuron) removeNeuron);

        if (removeNeuron instanceof InputNeuron)
            this.inputNeurons.remove((InputNeuron) removeNeuron);
    }



	/**
	 * Obtains all InputNeurons contained within the brain.
	 *
	 *  <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of InputNeurons.
	 * @since 0.1
	 */
    public Set<InputNeuron> getInputNeurons()
    {
        return Collections.unmodifiableSet(this.inputNeurons);
    }



	/**
	 * Obtains all OutputNeurons contained within the brain.
	 *
	 *  <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of OutputNeurons
	 * @since 0.1
	 */
    public Set<OutputNeuron> getOutputNeurons()
    {
        return Collections.unmodifiableSet(this.outputNeurons);
    }



	/**
	 * Obtains all Neurons, including InputNeurons and OutputNeurons contained
	 * within the brain.
	 *
	 *  <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of all Neurons.
	 * @since 0.1
	 */
    public Set<Neuron> getNeurons()
    {
        return Collections.unmodifiableSet(this.neurons);
    }
}
