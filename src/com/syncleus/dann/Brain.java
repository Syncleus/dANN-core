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

import com.syncleus.dann.backprop.InputBackpropNeuron;
import com.syncleus.dann.backprop.BackpropNeuron;
import com.syncleus.dann.backprop.OutputBackpropNeuron;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public abstract class Brain implements Serializable
{
    private HashSet<BackpropNeuron> neurons = new HashSet<BackpropNeuron>();
    private HashSet<OutputBackpropNeuron> outputNeurons = new HashSet<OutputBackpropNeuron>();
    private HashSet<InputBackpropNeuron> inputNeurons = new HashSet<InputBackpropNeuron>();



    protected void addNeuron(BackpropNeuron newNeuron)
    {
        if (this.neurons.contains(newNeuron))
            return;

        this.neurons.add(newNeuron);

		if (newNeuron instanceof OutputBackpropNeuron)
			this.outputNeurons.add((OutputBackpropNeuron) newNeuron);

		if (newNeuron instanceof InputBackpropNeuron)
			this.inputNeurons.add((InputBackpropNeuron) newNeuron);
    }

	protected void addNeurons(Collection<BackpropNeuron> newNeurons)
	{
		this.neurons.addAll(newNeurons);

		for(BackpropNeuron newNeuron : newNeurons)
		{
			if (newNeuron instanceof OutputBackpropNeuron)
				this.outputNeurons.add((OutputBackpropNeuron) newNeuron);

			if (newNeuron instanceof InputBackpropNeuron)
				this.inputNeurons.add((InputBackpropNeuron) newNeuron);
		}
	}



    protected void removeNeuron(BackpropNeuron removeNeuron)
    {
        if (this.neurons.contains(removeNeuron) == false)
            return;

        this.neurons.remove(removeNeuron);

        if (removeNeuron instanceof OutputBackpropNeuron)
            this.outputNeurons.remove((OutputBackpropNeuron) removeNeuron);

        if (removeNeuron instanceof InputBackpropNeuron)
            this.inputNeurons.remove((InputBackpropNeuron) removeNeuron);
    }



    public Set<InputBackpropNeuron> getInputNeurons()
    {
        return Collections.unmodifiableSet(this.inputNeurons);
    }



    public Set<OutputBackpropNeuron> getOutputNeurons()
    {
        return Collections.unmodifiableSet(this.outputNeurons);
    }



    public Set<BackpropNeuron> getNeurons()
    {
        return Collections.unmodifiableSet(this.neurons);
    }
}
