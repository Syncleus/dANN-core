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
 * A ValueGene is a gene that contains a numeric value.
 *
 * @param <N> The type of number to use
 */
public interface ValueGene<N extends MutableNumber> extends Gene
{
	/**
	 * Gets the mutable numeric value of the gene.
	 * @return The value of the gene
	 */
	N getValue();

	/**
	 * Mutates a gene by the maximum deviation.
	 * @param deviation The maximum deviation to change the gene by
	 * @return The resultant gene from the mutation
	 */
	ValueGene<N> mutate(double deviation);
}
