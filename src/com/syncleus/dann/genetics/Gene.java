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
package com.syncleus.dann.genetics;

/**
 * Represents a Gene which can mutate and expresses some activity. All New
 * types of Gene's will inherit from this class.
 *
 * @author Syncleus, Inc.
 * @since 2.0
 *
 */
public interface Gene extends Mutable
{
	/**
	 * All children of this class should override this method and return
	 * their own class type even if it is abstract. It should return a copy
	 * without any mutation.
	 *
	 * @return an exact copy of this object.
	 * @since 2.0
	 */
	public Gene clone();
	
	/**
	 * This will make a copy of the object and mutate it. The mutation has
	 * a normal distribution multiplied by the deviation.
	 *
	 * @param deviation A double indicating how extreme the mutation will be.
	 * The greater the deviation the more drastically the object will mutate.
	 * A deviation of 0 should cause no mutation.
	 * @return A copy of the current object with potential mutations.
	 * @since 2.0
	 */
	public Gene mutate(double deviation);

	/**
	 * The current expression activity. The meaning of this value depends on the
	 * type of gene and the genetic system being used.
	 *
	 * @return The current expression activity.
	 * @since 2.0
	 */
	public double expressionActivity();
}
