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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public abstract class Brain implements Serializable
{
    private HashSet<Neuron> neurons = new HashSet<Neuron>();
    private HashSet<OutputNeuron> outputNeurons = new HashSet<OutputNeuron>();
    private HashSet<InputNeuron> inputNeurons = new HashSet<InputNeuron>();



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

	protected void addNeurons(Collection<Neuron> newNeurons)
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



    public Set<InputNeuron> getInputNeurons()
    {
        return Collections.unmodifiableSet(this.inputNeurons);
    }



    public Set<OutputNeuron> getOutputNeurons()
    {
        return Collections.unmodifiableSet(this.outputNeurons);
    }



    public Set<Neuron> getNeurons()
    {
        return Collections.unmodifiableSet(this.neurons);
    }
}
