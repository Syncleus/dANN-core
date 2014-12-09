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

import com.syncleus.grail.graph.unit.SignalerVertex;
import com.syncleus.grail.graph.unit.SignalMultiplyingEdge;
import com.syncleus.grail.graph.unit.Signaler;
import com.syncleus.dann.activation.ActivationFunction;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.Incidence;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.grail.graph.*;
import com.syncleus.grail.graph.unit.action.Action;
import com.tinkerpop.blueprints.Direction;

public interface ActivationNeuron extends SignalerVertex {
    @Action("propagate")
    void propagate();

    @Property("activity")
    Double getActivity();

    @Property("activity")
    void setActivity(double activity);

    @Property("activationFunction")
    Class<? extends ActivationFunction> getActivationFunctionClass();

    @Property("activationFunction")
    void setActivationFunctionClass(Class<? extends ActivationFunction> activationFunctionClass);

    @Adjacency(label="signals", direction= Direction.IN)
    Iterable<?> getSources();

    @Adjacency(label="signals", direction=Direction.IN)
    <N> Iterable<? extends N> getSources(Class<? extends N> type);

    @Adjacency(label="signals", direction=Direction.IN)
    void setSources(Iterable<? extends SignalerVertex> targets);

    @Adjacency(label="signals", direction=Direction.IN)
    void removeSource(SignalerVertex target);

    @Adjacency(label="signals", direction=Direction.IN)
    <N extends SignalerVertex> N addSource(N target);

    @Adjacency(label="signals", direction=Direction.IN)
    Signaler addSource();

    @Adjacency(label="signals", direction=Direction.IN)
    <N extends SignalerVertex> N addSource(Class<? extends N> type);

    @Incidence(label = "signals", direction=Direction.IN)
    Iterable<? extends SignalMultiplyingEdge> getSourceEdges();

    @Incidence(label="signals", direction=Direction.IN)
    <E extends SignalMultiplyingEdge> Iterable<? extends E> getSourceEdges(Class<? extends E> type);

    @Incidence(label = "signals", direction=Direction.IN)
    void removeSourceEdge(SignalMultiplyingEdge source);
}
