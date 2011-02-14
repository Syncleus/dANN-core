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
package com.syncleus.dann.math.linear;

import com.syncleus.dann.math.FieldElement;

public interface Matrix<M extends Matrix<? extends M, ? extends F>, F extends FieldElement<? extends F>> //extends Algebraic<F>
{
	com.syncleus.dann.math.Field<F> getElementField();
	M blank();
	M flip();
	boolean isSymmetric();
	boolean isSquare();
	/**
	 * Get a single element.
	 *
	 * @param heightIndex Row index.
	 * @param widthIndex Column index.
	 * @return value of the specified element.
	 */
	F get(int heightIndex, int widthIndex);
	/**
	 * Set a single element.
	 *
	 * @param heightIndex Row index.
	 * @param widthIndex Column index.
	 * @param fillValue value to set
	 */
	M set(final int heightIndex, final int widthIndex, final F fillValue);
	/**
	 * Get column dimension.
	 *
	 * @return height, the number of columns.
	 */
	int getWidth();
	/**
	 * Get row dimension.
	 *
	 * @return height, the number of rows.
	 */
	int getHeight();
	/**
	 * SimpleRealMatrix transpose.
	 *
	 * @return matrixElements'
	 */
	M transpose();
	/**
	 * Solve matrixElements*resultMatrix = operand.
	 *
	 * @param operand right hand side
	 * @return solution if matrixElements is square, least squares solution
	 *         otherwise
	 */
	M solve(M operand);
	/**
	 * Solve resultMatrix*matrixElements = operand, which is also
	 * matrixElements'*resultMatrix' = operand'.
	 *
	 * @param operand right hand side
	 * @return solution if matrixElements is square, least squares solution
	 *         otherwise.
	 */
	M solveTranspose(M operand);
	F[][] toArray();
	/**
	 * Get a sub-matrix.
	 *
	 * @param heightStart Initial row index
	 * @param heightEnd Final row index
	 * @param widthStart Initial column index
	 * @param widthEnd Final column index
	 * @return matrixElements(heightStart:heightEnd,widthStart:widthEnd)
	 * @throws ArrayIndexOutOfBoundsException Sub-matrix indices
	 */
	M getSubmatrix(int heightStart, int heightEnd, int widthStart, int widthEnd);
	/**
	 * Get a sub-matrix.
	 *
	 * @param heightIndexes Array of row indices.
	 * @param widthIndexes Array of column indices.
	 * @return matrixElements(heightIndexes(:), widthIndexes(:))
	 * @throws ArrayIndexOutOfBoundsException Sub-matrix indices
	 */
	M getSubmatrix(int[] heightIndexes, int[] widthIndexes);
	/**
	 * Get a sub-matrix.
	 *
	 * @param heightStart Initial row index
	 * @param heightEnd Final row index
	 * @param widthIndexes Array of column indices.
	 * @return matrixElements(heightStart:heightEnd,widthIndexes(:))
	 * @throws ArrayIndexOutOfBoundsException Sub-matrix indices
	 */
	M getSubmatrix(int heightStart, int heightEnd, int[] widthIndexes);
	/**
	 * Get a sub-matrix.
	 *
	 * @param heightIndexes Array of row indices.
	 * @param widthStart Initial column index
	 * @param widthEnd Final column index
	 * @return A RealMatrix represented the elements specified
	 * @throws ArrayIndexOutOfBoundsException Sub-matrix indices
	 */
	M getSubmatrix(int[] heightIndexes, int widthStart, int widthEnd);
	/**
	 * Element-by-element left division, resultArray = matrixElements.\operand.
	 *
	 * @param operand another matrix
	 * @return matrixElements.\operand
	 */
	M arrayLeftDivide(M operand);
	/**
	 * Element-by-element left division in place, matrixElements =
	 * matrixElements.\operand.
	 *
	 * @param operand another matrix
	 * @return matrixElements.\operand
	 */
	M arrayLeftDivideEquals(M operand);
	/**
	 * Element-by-element right division, resultArray = matrixElements./operand.
	 *
	 * @param operand another matrix
	 * @return matrixElements./operand
	 */
	M arrayRightDivide(M operand);
	/**
	 * Element-by-element right division in place, matrixElements =
	 * matrixElements./operand.
	 *
	 * @param operand another matrix
	 * @return matrixElements./operand
	 */
	M arrayRightDivideEquals(M operand);
	/**
	 * Element-by-element multiplication, resultArray = matrixElements.*operand.
	 *
	 * @param operand another matrix
	 * @return matrixElements.*operand
	 */
	M arrayTimes(M operand);
	/**
	 * Element-by-element multiplication in place, matrixElements =
	 * matrixElements.*operand.
	 *
	 * @param operand another matrix
	 * @return matrixElements.*operand
	 */
	M arrayTimesEquals(M operand);
	/**
	 * matrixElements = matrixElements + operand.
	 *
	 * @param operand another matrix
	 * @return matrixElements + operand
	 */
	M addEquals(M operand);
	/**
	 * matrixElements = matrixElements - operand.
	 *
	 * @param operand another matrix
	 * @return matrixElements - operand
	 */
	M subtractEquals(M operand);
	M multiplyEquals(F value);
	/**
	 * scalar addition matrixElements + operand.
	 *
	 * @param operand scalar value to add to each element in this matrix.
	 * @return a new element containing the result of this scalar addition.
	 */
	M add(F operand);
	M subtract(F value);
	/**
	 * Multiply a matrix by a scalar, resultArray = scalar*matrixElements.
	 *
	 * @param scalar scalar
	 * @return scalar*matrixElements
	 */
	M multiply(F scalar);
	M divide(F value);
	/**
	 * resultArray = matrixElements + operand.
	 *
	 * @param operand another matrix
	 * @return matrixElements + operand
	 */
	M add(M operand);
	/**
	 * resultArray = matrixElements - operand.
	 *
	 * @param operand another matrix
	 * @return matrixElements - operand
	 */
	M subtract(M operand);
	/**
	 * Linear algebraic matrix multiplication, matrixElements * operand.
	 *
	 * @param operand another matrix
	 * @return SimpleRealMatrix product, matrixElements * operand
	 * @throws IllegalArgumentException SimpleRealMatrix inner dimensions must
	 * agree.
	 */
	M multiply(M operand);
	/**
	 * Unary subtract.
	 *
	 * @return -matrixElements
	 */
	M negate();
	/**
	 * SimpleRealMatrix reciprocal or pseudo-inverse.
	 *
	 * @return reciprocal(matrixElements) if matrixElements is square,
	 *         pseudo-inverse otherwise.
	 */
	M reciprocal();
}
