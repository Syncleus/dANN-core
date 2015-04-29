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
import com.syncleus.dann.activation.ActivationFunction;

public abstract class AbstractBackpropNeuron extends AbstractActivationNeuron implements BackpropNeuron {
    private static final double DEFAULT_LEARNING_RATE = 0.0175;

    @Override
    public void init() {
        super.init();
        this.setLearningRate(AbstractBackpropNeuron.DEFAULT_LEARNING_RATE);
        this.setDeltaTrain(0.0);
    }

    @Override
    public void backpropagate() {
        for (final BackpropSynapse synapse : this.getTargetEdges(BackpropSynapse.class)) {
            final BackpropNeuron target = synapse.getTarget();
            synapse.setWeight(synapse.getWeight() + (target.getDeltaTrain() * target.getLearningRate() * this.getSignal()));
        }

        double newDeltaTrain = 0.0;
        for (final BackpropSynapse synapse : this.getTargetEdges(BackpropSynapse.class)) {
            final BackpropNeuron target = synapse.getTarget();
            assert synapse.getWeight() != null;
            assert target.getDeltaTrain() != null;
            newDeltaTrain += (synapse.getWeight() * target.getDeltaTrain());
        }
        final ActivationFunction activation = this.getActivationFunction();
        if( activation == null)
            throw new IllegalStateException("No activation function was configured for this node");
        final Double activity = this.getActivity();
        if( activity == null)
            throw new IllegalStateException("Node's activity is null.");
        newDeltaTrain *= activation.activateDerivative(activity);
        this.setDeltaTrain(newDeltaTrain);
    }
}
