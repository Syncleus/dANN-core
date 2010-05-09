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

import java.util.List;

/**
 * Represents a single Chromatid containing a single unique set of genes. This
 * is only one half of a chromosome which contains a pair of matching chromatid.
 * Typically only Chromatid are used, not chromosomes.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public interface Chromatid<G extends Gene> extends Cloneable
{
	/**
	 * Gets an unmodifiable List of all the genes. The index of the genes indicates
	 * its position in the chromatid.
	 *
	 * @return An unmodifiable List of all genes in the chromatid
	 * @since 2.0
	 */
	public List<? extends G> getGenes();
	/**
	 * The first step in crossover. This will return a segment of genes from the
	 * point specified to the end of the chromatid. This will not modify the
	 * Chromatid.
	 *
	 * @param point the point (index) in the chromatid to act as the crossover
	 * point
	 * @return A List of the genetic segment crossing over.
	 */
	public List<? extends G> crossover(int point);
	/**
	 * The second step in crossover. This will replace its own genetic code with
	 * the specefied genetic segment at the specefied crossover point.
	 *
	 * @param geneticSegment Segmet of genetic code crossing over.
	 * @param point Crossover point (index) where genes are spliced
	 * @see com.syncleus.dann.genetics.Chromatid#crossover(int)
	 * @since 2.0
	 */
	public void crossover(List<G> geneticSegment, int point);
	/**
	 * All children of this class should override this method and return their own
	 * class type even if it is abstract. It should return a copy without any
	 * mutation.
	 *
	 * @return an exact copy of this object.
	 * @since 2.0
	 */
	public Chromatid<G> clone();
}
