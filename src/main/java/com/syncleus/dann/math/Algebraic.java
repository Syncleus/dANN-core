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

import java.util.List;

public interface Algebraic<F extends Algebraic<? extends F>> extends FieldElement<F> {
    /**
     * Returns the absolute value.
     *
     * @return the absolute value of this.
     * @see Math#abs(double)
     */
    F abs();

    /**
     * Returns Euler's number <i>e</i> raised to the power of a this value.
     *
     * @return the value <i>e</i><sup><code>this</code></sup>,
     * where <i>e</i> is the base of the natural logarithms.
     * @see Math#exp(double)
     */
    F exp();

    /**
     * Returns the natural logarithm (base <i>e</i>) of this value.
     *
     * @return the value ln&nbsp;<code>this</code>, the natural logarithm of
     * this value.
     * @see Math#log(double)
     */
    F log();

    /**
     * Returns this value raised to the power of the exponent.
     *
     * @param exponent the exponent.
     * @return the value <code>this<sup>exponent</sup></code>.
     * @see Math#pow(double, double)
     */
    F pow(F exponent);

    /**
     * Returns the n'th root of this value.
     *
     * @param number which root to return, for example 2 for the square root.
     * @return the value <code>this<sup>1/number</sup></code>.
     */
    List<F> root(int number);

    /**
     * Returns the square root of this value.
     *
     * @return the value <code>this<sup>1/2</sup></code>.
     * @see Math#sqrt(double)
     */
    F sqrt();

    /**
     * Returns sqrt(<i>this</i><sup>2</sup>&nbsp;+<i>operand</i><sup>2</sup>).
     *
     * @param operand the operand.
     * @return sqrt(<i>this</i><sup>2</sup>&nbsp;+<i>operand</i><sup>2</sup>).
     * @see Math#hypot(double, double)
     */
    F hypot(F operand);
}
