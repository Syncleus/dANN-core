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

import com.syncleus.grail.graph.unit.SignalMultiplyingEdge;
import com.syncleus.dann.ActivationNeuron;
import com.syncleus.dann.activation.ActivationFunction;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.grail.graph.*;
import com.syncleus.grail.graph.unit.action.Action;
import com.tinkerpop.blueprints.Direction;

public interface NestedLayerVertex extends GrailVertexFrame {
    final static String LAYER_KEY = "parentLayer";
    
    @Property("nestedGraphName")
    String getNestedGraphName();
    
    @Property("nestedGraphName")
    void setNestedGraphName(String nestedName);
    
    @Property("activationFunction")
    Class<? extends ActivationFunction> getActivationFunctionClass();

    void setActivationFunctionClass(Class<? extends ActivationFunction> activationFunctionClass);
    
    @Property("learningRate")
    Double getLearningRate();

    void setLearningRate(double learningRate);
    
    void makeBias();
    
    long getVertexCount();
    
    void setVertexCount(long newVertexCount, Class<? extends ActivationNeuron> type, Class<? extends SignalMultiplyingEdge> edgeType);
    
    void removeVertexCount(long removeVertexCount);
    
    void addVertexCount(long newVertexCount, Class<? extends ActivationNeuron> type, Class<? extends SignalMultiplyingEdge> edgeType);
    
    GrailGraph getNestedGraph();
    
    Iterable<? extends BackpropNeuron> getNestedNeurons();
    
    @Action("propagate")
    void propagate();
    
    @Action("backpropagate")
    void backpropagate();
    
    @Adjacency(label = "signals", direction = Direction.IN)
    <N extends NestedLayerVertex> Iterable<? extends N> getSourceLayers(Class<N> type);
    
    @Adjacency(label = "signals", direction = Direction.OUT)
    <N extends NestedLayerVertex> Iterable<? extends N> getTargetLayers(Class<N> type);
}
