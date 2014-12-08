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

import com.syncleus.dann.backprop.NestedLayerVertex;
import com.syncleus.ferma.*;
import com.syncleus.grail.graph.*;
import static com.syncleus.dann.backprop.NestedLayerVertex.LAYER_KEY;

public abstract class SimpleNestedFullyConnectedEdge extends AbstractGrailEdgeFrame implements NestedFullyConnectedEdge {

    @Override
    protected void init() {
        super.init();
        
        if(!this.getSource().getNestedGraphName().equals(this.getTarget().getNestedGraphName()))
            throw new IllegalStateException("One of the source layers uses a different nested graph");
    }
    
    @Override
    public void reconnectSubedges(Class<? extends SignalMultiplyingEdge> type) {
        //disconnect any existing subedges
        for(final EdgeFrame existingEdge : this.getNestedEdges() )
            existingEdge.remove();
        
        //create the new edges
        for( final ActivationNeuron sourceNeuron  : this.getNestedGraph().v().has(NestedLayerVertex.LAYER_KEY, this.getSource().getId()).frame(ActivationNeuron.class) )
            for( final ActivationNeuron targetNeuron  : this.getNestedGraph().v().has(NestedLayerVertex.LAYER_KEY, this.getTarget().getId()).frame(ActivationNeuron.class) )
                this.getNestedGraph().addFramedEdge(sourceNeuron, targetNeuron, "signals", type);
    }

    private GrailGraph getNestedGraph() {
        return this.getGraph().subgraph(this.getSource().getNestedGraphName());
    }
    
    private EdgeTraversal<?,?,?> getNestedEdges() {
        return this.getNestedGraph().e().has(LAYER_KEY, this.getId());
    }
}
