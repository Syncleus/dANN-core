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

/**
 * A Neuron which allows you to retreive its output. These neurons allow you to
 * output data from the brain after processing.
 *
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Syncleus, Inc.
 * @param <SN> The type of Neuron allowed to connect to this Neuron.
 * @param <DN> The type of Neuron this Neuron is allowed to connect to.
 * @since 1.0
 * @version 1.0
 */
public interface OutputNeuron<SN extends NeuronImpl, DN extends NeuronImpl> extends Neuron<SN, DN>
{
	/**
	 * Obtains the current output for this neuron.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return The current output of the neuron.
	 */
	public double getOutput();
}
