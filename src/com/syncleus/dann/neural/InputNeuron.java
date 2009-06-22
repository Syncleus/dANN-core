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

/**
 * A Neuron which receives an input from an outside source. These neurons allow
 * you to input data into the brain for processing.
 *
 *
 * @author Syncleus, Inc.
 * @param <SN> The type of Neuron allowed to connect to this Neuron.
 * @param <DN> The type of Neuron this Neuron is allowed to connect to.
 * @since 1.0
 *
 */
public interface InputNeuron<SN extends NeuronImpl, DN extends NeuronImpl> extends Neuron<SN, DN>
{
	/**
	 * Sets the current input for this neuron.
	 *
	 *
	 * @param inputToSet The new input value you want to set.
	 */
	public void setInput(double inputToSet);
}
