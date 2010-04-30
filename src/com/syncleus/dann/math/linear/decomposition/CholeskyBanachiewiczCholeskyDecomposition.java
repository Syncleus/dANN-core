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

/** Cholesky Decomposition.
<P>
For a symmetric, positive definite matrix A, the Cholesky decomposition
is an lower triangular matrix L so that A = L*L'.
<P>
If the matrix is not symmetric or positive definite, the constructor
returns a partial decomposition and sets an internal flag that may
be queried by the isSpd() method.
 */
public class CholeskyBanachiewiczCholeskyDecomposition<M extends Matrix<M, F>, F extends OrderedAlgebraic<F>> implements CholeskyDecomposition<M,F>
{
	private static final long serialVersionUID = 3272683691654431613L;
	
	private final M matrix;

	/** Symmetric and positive definite flag.
	@serial is symmetric and positive definite flag.
	 */
	private boolean isSpd;

	/** Cholesky algorithm for symmetric and positive definite matrix.
	 *  @param matrix Square, symmetric matrix.
	 */
	public CholeskyBanachiewiczCholeskyDecomposition(M matrix)
	{
		// Initialize.
		M newMatrix = matrix;
		isSpd = (matrix.getWidth() == matrix.getHeight());
		// Main loop.
		for(int j = 0; j < matrix.getHeight(); j++)
		{
			F d = newMatrix.getElementField().getZero();
			for(int k = 0; k < j; k++)
			{
				F s = newMatrix.getElementField().getZero();
				for(int i = 0; i < k; i++)
					s = s.add(newMatrix.get(k,i).multiply(newMatrix.get(j,i)));
				s = (matrix.get(j,k).subtract(s)).divide(newMatrix.get(k,k));
				newMatrix = newMatrix.set(j,k,s);
				d = d.add(s.multiply(s));
				isSpd = isSpd && (matrix.get(k,j) == matrix.get(j,k));
			}
			d = matrix.get(j,j).subtract(d);
			isSpd = isSpd && (d.compareTo(newMatrix.getElementField().getZero()) > 0);
			newMatrix = newMatrix.set(j, j, d.max(newMatrix.getElementField().getZero()).sqrt());
			for(int k = j + 1; k < matrix.getHeight(); k++)
				newMatrix = newMatrix.set(j,k,newMatrix.getElementField().getZero());
		}
		this.matrix = newMatrix;
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
		M solvedMatrix = solutionMatrix;
		if(solutionMatrix.getHeight() != this.matrix.getHeight())
			throw new IllegalArgumentException("solutionMatrix row dimensions must agree.");
		if(!isSpd)
			throw new ArithmeticException("this is not symmetric positive definite.");

		// Solve L*Y = solutionMatrix;
		for(int k = 0; k < this.matrix.getHeight(); k++)
			for(int j = 0; j < solvedMatrix.getWidth(); j++)
			{
				for(int i = 0; i < k; i++)
					solvedMatrix = solvedMatrix.set(k, j, solvedMatrix.get(k,j).subtract(solvedMatrix.get(i,j).multiply(this.matrix.get(k,i))));
				solvedMatrix = solvedMatrix.set(k, j, solvedMatrix.get(k,j).divide(this.matrix.get(k,k)));
			}

		// Solve L'*X = Y;
		for(int k = this.matrix.getHeight() - 1; k >= 0; k--)
			for(int j = 0; j < solvedMatrix.getWidth(); j++)
			{
				for(int i = k + 1; i < this.matrix.getHeight(); i++)
					solvedMatrix = solvedMatrix.set(k,j, solvedMatrix.get(k,j).subtract(solvedMatrix.get(i,j).multiply(this.matrix.get(i,k))));
				solvedMatrix = solvedMatrix.set(k,j, solvedMatrix.get(k,j).divide(this.matrix.get(k,k)));
			}


		return solvedMatrix;
	}
}
