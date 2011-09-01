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
package com.syncleus.dann.math;

public interface OrderedFieldElement<F extends OrderedFieldElement<? extends F>> extends FieldElement<F>, Comparable<F>
{
	@Override
	OrderedField<F> getField();

	/**
	 * Returns a constant holding the maximum value a field of this type can
	 * have.
	 * @return the maximum value a field of this type can have.
	 */
	F max(F value);

	/**
	 * Returns a constant holding the minimum value a field of this type can
	 * have.
	 * @return the minimum value a field of this type can have.
	 */
	F min(F value);
}
