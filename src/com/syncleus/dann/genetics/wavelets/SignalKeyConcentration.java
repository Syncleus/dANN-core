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
package com.syncleus.dann.genetics.wavelets;

/**
 * A SignalKeyConcentration is a SignalKey with a known concentration value.
 */
public class SignalKeyConcentration
{
	private final SignalKey signal;
	private double concentration;

	/**
	 * Creates a new SignalKeyConcentration from the supplied SignalKey with a given concentration.
	 * @param signal The SignalKey to use
	 */
	public SignalKeyConcentration(final SignalKey signal)
	{
		this.signal = signal;
	}

	/**
	 * Gets the SignalKey in use.
	 * @return The SignalKey in use
	 */
	public final SignalKey getSignal()
	{
		return this.signal;
	}

	/**
	 * Gets the current concentration.
	 * @return The concentration in use
	 */
	public final double getConcentration()
	{
		return this.concentration;
	}

	/**
	 * Sets the concentration.
	 * @param concentration The concentration to use
	 */
	public final void setConcentration(final double concentration)
	{
		this.concentration = concentration;
	}
}
