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

import java.util.*;

public class RealNumber extends Number implements OrderedTrigonometricAlgebraic<RealNumber> {
    public static final RealNumber ZERO = new RealNumber(0);
    public static final RealNumber ONE = new RealNumber(1);
    private static final long serialVersionUID = -1417799779268676071L;
    private final double value;
    public RealNumber(final double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    @Override
    public com.syncleus.dann.math.OrderedField<RealNumber> getField() {
        return Field.FIELD;
    }

    @Override
    public RealNumber max(final RealNumber maxValue) {
        if (this.compareTo(maxValue) > 0)
            return this;
        return maxValue;
    }

    @Override
    public RealNumber min(final RealNumber minValue) {
        if (this.compareTo(minValue) < 0)
            return this;
        return minValue;
    }

    @Override
    public RealNumber add(final RealNumber operand) {
        return new RealNumber(this.value + operand.value);
    }

    public RealNumber add(final double operand) {
        return new RealNumber(this.value + operand);
    }

    @Override
    public RealNumber subtract(final RealNumber operand) {
        return new RealNumber(this.value - operand.value);
    }

    public RealNumber subtract(final double operand) {
        return new RealNumber(this.value - operand);
    }

    @Override
    public RealNumber multiply(final RealNumber operand) {
        return new RealNumber(this.value * operand.value);
    }

    public RealNumber multiply(final double operand) {
        return new RealNumber(this.value * operand);
    }

    @Override
    public RealNumber divide(final RealNumber operand) {
        return new RealNumber(this.value / operand.value);
    }

    public RealNumber divide(final double operand) {
        return new RealNumber(this.value / operand);
    }

    @Override
    public RealNumber negate() {
        return this.multiply(-1.0);
    }

    @Override
    public RealNumber reciprocal() {
        return new RealNumber(1.0 / this.value);
    }

    @Override
    public RealNumber abs() {
        if (this.value < 0.0)
            return new RealNumber(Math.abs(this.value));
        else
            return this;
    }

    @Override
    public RealNumber exp() {
        return new RealNumber(Math.exp(this.value));
    }

    @Override
    public RealNumber log() {
        return new RealNumber(Math.log(this.value));
    }

    @Override
    public RealNumber pow(final RealNumber exponent) {
        return new RealNumber(Math.pow(this.value, exponent.value));
    }

    public RealNumber pow(final double exponent) {
        return new RealNumber(Math.pow(this.value, exponent));
    }

    @Override
    public List<RealNumber> root(final int number) {
        final List<RealNumber> roots = new ArrayList<RealNumber>();
        final double positiveRoot = Math.pow(this.value, 1.0 / ((double) number));
        roots.add(new RealNumber(positiveRoot));
        if (number % 2 == 0)
            roots.add(new RealNumber(-positiveRoot));
        return Collections.unmodifiableList(roots);
    }

    @Override
    public RealNumber sqrt() {
        return new RealNumber(Math.sqrt(this.value));
    }

    @Override
    public RealNumber hypot(final RealNumber operand) {
        return new RealNumber(Math.hypot(this.value, operand.value));
    }

    @Override
    public RealNumber sin() {
        return new RealNumber(Math.sin(this.value));
    }

    @Override
    public RealNumber asin() {
        return new RealNumber(Math.asin(this.value));
    }

    @Override
    public RealNumber sinh() {
        return new RealNumber(Math.sinh(this.value));
    }

    @Override
    public RealNumber cos() {
        return new RealNumber(Math.cos(this.value));
    }

    @Override
    public RealNumber acos() {
        return new RealNumber(Math.acos(this.value));
    }

    @Override
    public RealNumber cosh() {
        return new RealNumber(Math.cosh(this.value));
    }

    @Override
    public RealNumber tan() {
        return new RealNumber(Math.tan(this.value));
    }

    @Override
    public RealNumber atan() {
        return new RealNumber(Math.atan(this.value));
    }

    @Override
    public RealNumber tanh() {
        return new RealNumber(Math.tanh(this.value));
    }

    @Override
    public short shortValue() {
        return (short) this.value;
    }

    @Override
    public int intValue() {
        return (int) this.value;
    }

    @Override
    public long longValue() {
        return (long) this.value;
    }

    @Override
    public float floatValue() {
        return (float) this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(this.value).hashCode();
    }

    @Override
    public boolean equals(final Object compareObj) {
        if (!(compareObj instanceof RealNumber))
            return false;
        final RealNumber compareWith = (RealNumber) compareObj;
        return (compareWith.value == this.value);
    }

    @Override
    public int compareTo(final RealNumber compareWith) {
        if (this.value > compareWith.value)
            return 1;
        else if (this.value < compareWith.value)
            return -1;
        return 0;
    }

    public static final class Field implements com.syncleus.dann.math.OrderedField<RealNumber> {
        public static final Field FIELD = new Field();

        private Field() {
        }

        @Override
        public RealNumber getZero() {
            return RealNumber.ZERO;
        }

        @Override
        public RealNumber getOne() {
            return RealNumber.ONE;
        }
    }
}
