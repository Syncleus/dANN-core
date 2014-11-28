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

import com.syncleus.dann.activation.*;
import com.syncleus.grail.graph.*;
import com.syncleus.dann.backprop.*;
import com.syncleus.grail.graph.GrailFramedVertex;

public abstract class AbstractActivationNeuron extends GrailFramedVertex implements ActivationNeuron {

    private ActivationFunction activationFunction;

    @Override
    public void init() {
        super.init();
        this.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
        this.setActivity(0.0);
    }

    protected ActivationFunction getActivationFunction() {
        final Class<? extends ActivationFunction> activationClass = this.getActivationFunctionClass();
        if( (this.activationFunction != null) && (this.activationFunction.getClass().equals(activationClass)) )
            return this.activationFunction;

        this.activationFunction = null;
        try {
            this.activationFunction = activationClass.newInstance();
        }
        catch( final InstantiationException caughtException ) {
            throw new IllegalStateException("activation function does not have a public default constructor", caughtException);
        }
        catch( final IllegalAccessException caughtException ) {
            throw new IllegalStateException("activation function does not have a public default constructor", caughtException);
        }

        return this.activationFunction;
    }

    @Override
    public void propagate() {
        this.setActivity(0.0);
        for (final SignalMultiplyingEdge currentSynapse : this.getSourceEdges(AbstractBackpropSynapse.class)) {
            currentSynapse.propagate();
            this.setActivity(this.getActivity() + currentSynapse.getSignal());
        }
        this.setSignal( this.getActivationFunction().activate(this.getActivity()) );
    }
}
