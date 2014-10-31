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
package com.syncleus.dann.graph.drawing.hyperassociativemap;

import com.syncleus.dann.neural.*;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public class BrainHyperassociativeMap extends HyperassociativeMap<Brain<InputNeuron, OutputNeuron, Neuron, Synapse<Neuron>>, Neuron> {
    public BrainHyperassociativeMap(final Brain graph, final int dimensions, final double equilibriumDistance, final boolean useWeights, final ExecutorService threadExecutor) {
        super(graph, dimensions, equilibriumDistance, useWeights, threadExecutor);
    }

    public BrainHyperassociativeMap(final Brain graph, final int dimensions, final boolean useWeights, final ExecutorService threadExecutor) {
        super(graph, dimensions, 1.0, useWeights, threadExecutor);
    }

    public BrainHyperassociativeMap(final Brain graph, final int dimensions, final double equilibriumDistance, final boolean useWeights) {
        super(graph, dimensions, equilibriumDistance, useWeights, null);
    }

    public BrainHyperassociativeMap(final Brain graph, final int dimensions, final boolean useWeights) {
        super(graph, dimensions, 1.0, useWeights, null);
    }

    public BrainHyperassociativeMap(final Brain graph, final int dimensions, final double equilibriumDistance, final ExecutorService threadExecutor) {
        super(graph, dimensions, equilibriumDistance, true, threadExecutor);
    }

    public BrainHyperassociativeMap(final Brain graph, final int dimensions, final ExecutorService threadExecutor) {
        super(graph, dimensions, 1.0, true, threadExecutor);
    }

    public BrainHyperassociativeMap(final Brain graph, final int dimensions, final double equilibriumDistance) {
        super(graph, dimensions, equilibriumDistance, true, null);
    }

    public BrainHyperassociativeMap(final Brain graph, final int dimensions) {
        super(graph, dimensions, 1.0, true, null);
    }

    @Override
    Map<Neuron, Double> getNeighbors(final Neuron nodeToQuery) {
        final Map<Neuron, Double> associations = super.getNeighbors(nodeToQuery);
        if (nodeToQuery instanceof InputNeuron)
            for (final InputNeuron neuron : this.getGraph().getInputNeurons())
                associations.put(neuron, this.getEquilibriumDistance());
        else if (nodeToQuery instanceof OutputNeuron)
            for (final OutputNeuron neuron : this.getGraph().getOutputNeurons())
                associations.put(neuron, this.getEquilibriumDistance());
        associations.remove(nodeToQuery);

        return associations;
    }
}
