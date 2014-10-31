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

public interface Trigonometric<F extends Trigonometric<? extends F>> extends FieldElement<F> {
    /**
     * Returns the trigonometric sine of this value.
     *
     * @return the trigonometric sine of this value.
     * @see Math#sin(double)
     */
    F sin();

    /**
     * Returns the arc sine of a value.
     *
     * @return the trigonometric arc sine of this value.
     * @see Math#asin(double)
     */
    F asin();

    /**
     * Returns the hyperbolic sine of this value.
     *
     * @return the hyperbolic sine of this value.
     * @see Math#sinh(double)
     */
    F sinh();

    /**
     * Returns the trigonometric cosine of this value.
     *
     * @return the trigonometric cosine of this value.
     * @see Math#cos(double)
     */
    F cos();

    /**
     * Returns the arc cosine of this value.
     *
     * @return the arc cosine of this value.
     * @see Math#acos(double)
     */
    F acos();

    /**
     * Returns the hyperbolic cosine of this value.
     *
     * @return the hyperbolic cosine of this value.
     * @see Math#cosh(double)
     */
    F cosh();

    /**
     * Returns the trigonometric tangent of this value.
     *
     * @return the trigonometric tangent of this value.
     * @see Math#tan(double)
     */
    F tan();

    /**
     * Returns the arc tangent of this value.
     *
     * @return the arc tangent of this value.
     * @see Math#atan(double)
     */
    F atan();

    /**
     * Returns the hyperbolic tangent of this value.
     *
     * @return the hyperbolic tangent of this value.
     * @see Math#tanh(double)
     */
    F tanh();
}
