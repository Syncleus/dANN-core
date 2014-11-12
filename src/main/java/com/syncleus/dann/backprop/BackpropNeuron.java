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

import com.syncleus.grail.graph.*;
import com.syncleus.grail.graph.action.Action;
import com.syncleus.dann.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("BackpropNeuron")
@JavaHandlerClass(AbstractBackpropNeuron.class)
public interface BackpropNeuron extends ActivationNeuron {
    @JavaHandler
    @Action("backpropagate")
    void backpropagate();

    @Property("learningRate")
    Double getLearningRate();

    @Property("learningRate")
    void setLearningRate(double learningRate);

    @Property("deltaTrain")
    Double getDeltaTrain();

    @Property("deltaTrain")
    void setDeltaTrain(double deltaTrain);

    @Adjacency(label="signals")
    Iterable<? extends BackpropNeuron> getTargets();

    @TypedAdjacency(label="signals")
    <N extends BackpropNeuron> Iterable<? extends N> getTargets(Class<? extends N> type);

    @Adjacency(label="signals")
    void setTargets(Iterable<? extends BackpropNeuron> targets);

    @Adjacency(label="signals")
    void removeTarget(BackpropNeuron target);

    @Adjacency(label="signals")
    <N extends BackpropNeuron> N addTarget(N target);

    @Adjacency(label="signals")
    BackpropNeuron addTarget();

    @TypedAdjacency(label="signals")
    <N extends BackpropNeuron> N addTarget(Class<? extends N> type);

    @Incidence(label = "signals")
    Iterable<? extends BackpropSynapse> getTargetEdges();

    @TypedIncidence(label="signals")
    <E extends BackpropSynapse> Iterable<? extends E> getTargetEdges(Class<? extends E> type);

    @Incidence(label = "signals")
    <E extends BackpropSynapse> E addTargetEdge(E target);

    @Incidence(label = "signals")
    void removeTargetEdge(BackpropSynapse target);

}
