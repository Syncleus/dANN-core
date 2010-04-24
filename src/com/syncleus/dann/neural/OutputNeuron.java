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
 * A Neuron which allows you to retreive its output. These neurons allow you to
 * output data from the brain after processing.
 *
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 *
 */
public interface OutputNeuron extends Neuron
{
	/**
	 * Obtains the current output for this neuron.
	 *
	 *
	 * @return The current output of the neuron.
	 */
	public double getOutput();
}
