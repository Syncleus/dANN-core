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

/*
** Derived from Public-Domain source as indicated at
** http://math.nist.gov/javanumerics/jama/ as of 9/13/2009.
*/
package com.syncleus.dann.math.linear.decomposition;

import com.syncleus.dann.math.OrderedAlgebraic;
import com.syncleus.dann.math.linear.Matrix;

public class CholeskyCroutCholeskyDecomposition<M extends Matrix<M, F>, F extends OrderedAlgebraic<F>> implements CholeskyDecomposition<M,F>
{
	private final M matrix;
	/** Symmetric and positive definite flag.
	@serial is symmetric and positive definite flag.
	 */
	private boolean isSpd;

	/** Right Triangular Cholesky Decomposition.
	<P>
	For a symmetric, positive definite matrix A, the Right Cholesky
	decomposition is an upper triangular matrix R so that A = R'*R.
	This constructor computes R with the Fortran inspired column oriented
	algorithm used in LINPACK and MATLAB.  In Java, we suspect a row oriented,
	lower triangular decomposition is faster.  We have temporarily included
	this constructor here until timing experiments confirm this suspicion.

	Cholesky algorithm for symmetric and positive definite matrix.
	@param  A           Square, symmetric matrix.
	@param  rightflag   Actual value ignored.
	@return             Structure to access R and isspd flag.
	 */
	public CholeskyCroutCholeskyDecomposition(M matrixToDecompose, int rightflag)
	{
		// Initialize.
		M newMatrix = matrixToDecompose;

		isSpd = true;
		// Main loop.
		for(int j = 0; j < matrixToDecompose.getWidth(); j++)
		{
			F d = newMatrix.getElementField().getZero();
			for(int k = 0; k < j; k++)
			{
				F s = matrixToDecompose.get(k,j);
				for(int i = 0; i < k; i++)
					s = s.subtract(newMatrix.get(i,k).multiply(newMatrix.get(i,j)));
				s = s.divide(newMatrix.get(k,k));
				newMatrix = newMatrix.set(k, j, s);
				d = d.add(s.multiply(s));
				isSpd = isSpd && (matrixToDecompose.get(k,j).equals(matrixToDecompose.get(j,k)));
			}
			d = matrixToDecompose.get(j,j).subtract(d);
			isSpd = isSpd && (d.compareTo(d.getField().getZero()) > 0);
			newMatrix = newMatrix.set(j, j, d.max(d.getField().getZero()).sqrt());
			for(int k = j + 1; k < matrixToDecompose.getWidth(); k++)
				newMatrix = newMatrix.set(k, j, newMatrix.getElementField().getZero());
		}

		this.matrix = newMatrix;
	}

	public int getWidth()
	{
		return this.matrix.getWidth();
	}

	public int getHeight()
	{
		return this.matrix.getHeight();
	}

	/** Is the matrix symmetric and positive definite?
	@return     true if A is symmetric and positive definite.
	 */
	public boolean isSpd()
	{
		return isSpd;
	}

	/** Return triangular factor.
	@return     L
	 */
	public M getMatrix()
	{
		return this.matrix;
	}

	/** Solve A*X = solutionMatrix
	@param  solutionMatrix   A SimpleRealMatrix with as many rows as A and any number of columns.
	@return     X so that L*L'*X = solutionMatrix
	@exception  IllegalArgumentException  SimpleRealMatrix row dimensions must agree.
	@exception  RuntimeException  SimpleRealMatrix is not symmetric positive definite.
	 */
	public M solve(M solutionMatrix)
	{
		if(solutionMatrix.getHeight() != this.matrix.getHeight())
			throw new IllegalArgumentException("Matrix row dimensions must agree.");
		if(!isSpd)
			throw new RuntimeException("Matrix is not symmetric positive definite.");

		// Solve L*Y = solutionMatrix;
		for(int k = 0; k < this.matrix.getHeight(); k++)
			for(int j = 0; j < solutionMatrix.getWidth(); j++)
			{
				for(int i = 0; i < k; i++)
					solutionMatrix = solutionMatrix.set(k, j, solutionMatrix.get(k, j).subtract(solutionMatrix.get(i, j).multiply(this.matrix.get(k, i))));
				solutionMatrix = solutionMatrix.set(k, j, solutionMatrix.get(k, j).divide(this.matrix.get(k, k)));
			}

		// Solve L'*X = Y;
		for(int k = this.matrix.getHeight() - 1; k >= 0; k--)
			for(int j = 0; j < solutionMatrix.getWidth(); j++)
			{
				for(int i = k + 1; i < this.matrix.getHeight(); i++)
					solutionMatrix = solutionMatrix.set(k, j, solutionMatrix.get(k, j).subtract(solutionMatrix.get(i, j).multiply(this.matrix.get(i, k))));
				solutionMatrix = solutionMatrix.set(k, j, solutionMatrix.get(k, j).divide(this.matrix.get(k, k)));
			}


		return solutionMatrix;
	}
}
