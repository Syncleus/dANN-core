/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
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
 * @author Jeffrey Phillips Freeman
 * @param <SN> The type of Neuron allowed to connect to this Neuron.
 * @param <DN> The type of Neuron this Neuron is allowed to connect to.
 * @since 1.0
 *
 */
public interface InputNeuron extends Neuron
{
	/**
	 * Sets the current input for this neuron.
	 *
	 *
	 * @param inputToSet The new input value you want to set.
	 */
	public void setInput(double inputToSet);
}
