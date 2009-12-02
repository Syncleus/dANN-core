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
package com.syncleus.dann.math.linear;

import com.syncleus.dann.math.FieldElement;

public interface Matrix<M extends Matrix<? extends M, ? extends F>, F extends FieldElement<? extends F>> //extends Algebraic<F>
{
	com.syncleus.dann.math.Field<F> getElementField();

	M blank();
	M flip();

	boolean isSymmetric();
	boolean isSquare();

	F get(int i, int j);
	M set(int i, int j, F s);
	int getWidth();
	int getHeight();
	M transpose();
	M solve(M operand);
	M solveTranspose(M operand);
	F[][] toArray();

	M getSubmatrix(int heightStart, int heightEnd, int widthStart, int widthEnd);
	M getSubmatrix(int[] heightIndexes, int[] widthIndexes);
	M getSubmatrix(int heightStart, int heightEnd, int[] widthIndexes);
	M getSubmatrix(int[] heightIndexes, int widthStart, int widthEnd);

	M arrayLeftDivide(M operand);
	M arrayLeftDivideEquals(M operand);
	M arrayRightDivide(M operand);
	M arrayRightDivideEquals(M operand);
	M arrayTimes(M operand);
	M arrayTimesEquals(M operand);

	M addEquals(M value);
	M subtractEquals(M value);
	M multiplyEquals(F value);
	M add(F value);
	M subtract(F value);
	M multiply(F value);
	M divide(F value);

	M add(M value);
	M subtract(M value);
	M multiply(M value);
	M negate();
	M reciprocal();
}
