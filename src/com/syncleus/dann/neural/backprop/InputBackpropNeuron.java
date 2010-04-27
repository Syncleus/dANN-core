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
package com.syncleus.dann.neural.backprop;

import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.activation.*;


/**
 * This is a special type of SimpleBackpropNeuron that receives input.
 *
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 *
 */
public class InputBackpropNeuron extends SimpleInputNeuron implements BackpropNeuron, InputNeuron
{
    public InputBackpropNeuron(Brain brain)
    {
        super(brain);
    }

    public InputBackpropNeuron(Brain brain, ActivationFunction activationFunction)
    {
        super(brain, activationFunction);
    }

	@Override
    public void backPropagate()
    {
        //Do nothing, this is an input neuron.
    }
}
