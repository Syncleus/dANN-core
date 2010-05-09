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

import java.util.Set;
import com.syncleus.dann.graph.BidirectedGraph;

/**
 * Represents a single artificial brain typically belonging to a single
 * artificial organism. It will contain a set of input and output neurons which
 * corelates to a specific dataset pattern.<br/> <br/> This class is abstract
 * and must be extended in order to be used.
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 */
public interface Brain extends BidirectedGraph<Neuron, Synapse>
{
	/**
	 * Obtains all InputNeurons contained within the brain.
	 *
	 * @return An unmodifiable Set of InputNeurons.
	 * @since 1.0
	 */
	public abstract Set<InputNeuron> getInputNeurons();
	/**
	 * Obtains all OutputNeurons contained within the brain.
	 *
	 * @return An unmodifiable Set of OutputNeurons
	 * @since 1.0
	 */
	public abstract Set<OutputNeuron> getOutputNeurons();
}
