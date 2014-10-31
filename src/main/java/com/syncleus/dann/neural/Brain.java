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
package com.syncleus.dann.neural;

import com.syncleus.dann.graph.DirectedGraph;

import java.util.Set;

/**
 * Represents a single artificial brain typically belonging to a single
 * artificial organism. It will contain a set of input and output neurons which
 * correlate to a specific data-set pattern.
 *
 * @param <IN> The input-neuron type
 * @param <ON> The output-neuron type
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 */
public interface Brain<IN extends InputNeuron, ON extends OutputNeuron, N extends Neuron, S extends Synapse<N>> extends DirectedGraph<N, S> {
    /**
     * Obtains all InputNeurons contained within the brain.
     *
     * @return An unmodifiable Set of InputNeurons.
     * @since 1.0
     */
    Set<IN> getInputNeurons();

    /**
     * Obtains all OutputNeurons contained within the brain.
     *
     * @return An unmodifiable Set of OutputNeurons
     * @since 1.0
     */
    Set<ON> getOutputNeurons();
}
