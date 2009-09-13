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
 * Derived from Public-Domain source as indicated at
 * http://math.nist.gov/javanumerics/jama/ as of 9/13/2009.
 */
package com.syncleus.dann.math.linear.decomposition;

import com.syncleus.dann.math.OrderedAlgebraic;
import com.syncleus.dann.math.linear.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** QR Decomposition.
<P>
For an m-by-n matrix matrixToDecompose with m >= n, the QR decomposition is an m-by-n
orthogonal matrix factor and an n-by-n upper triangular matrix factor so that
matrixToDecompose = factor*factor.
<P>
The QR decompostion always exists, even if the matrix does not have
full rank, so the constructor will never fail.  The primary use of the
QR decomposition is in the least squares solution of nonsquare systems
of simultaneous linear equations.  This will fail if isFullRank()
returns false.
 */
public class QRDecomposition<M extends Matrix<M, F>, F extends OrderedAlgebraic<F>> implements java.io.Serializable, SolvableDecomposition<M>
{
	/** Array for internal storage of decomposition.
	@serial internal array storage.
	 */
	private M matrix;

	/** Row and column dimensions.
	@serial column dimension.
	@serial row dimension.
	 */
//	private int m,  n;

	/** Array for internal storage of diagonal of factor.
	@serial diagonal of factor.
	 */
	private List<F> rDiagonal;

	/** QR Decomposition, computed by Householder reflections.
	@param matrixToDecompose    Rectangular matrix
	@return     Structure to access factor and the Householder vectors and compute factor.
	 */
	public QRDecomposition(M matrixToDecompose)
	{
		// Initialize.
//		QR = matrixToDecompose.toDoubleArray();
//		m = matrixToDecompose.getHeight();
//		n = matrixToDecompose.getWidth();
//		Rdiag = new double[n];
		this.matrix = matrixToDecompose;
		this.rDiagonal = new ArrayList<F>(this.getWidth());
		this.rDiagonal.addAll(Collections.nCopies(this.getWidth(), this.matrix.getElementField().getZero()));

		// Main loop.
		for(int k = 0; k < this.getWidth(); k++)
		{
			// Compute 2-norm of k-th column without under/overflow.
			F nrm = this.matrix.getElementField().getZero();
			for(int i = k; i < this.getHeight(); i++)
				nrm = nrm.hypot(this.matrix.get(i,k));

			if(!nrm.equals(this.matrix.getElementField().getZero()))
			{
				// Form k-th Householder vector.
				if(this.matrix.get(k,k).compareTo(this.matrix.getElementField().getZero()) < 0)
					nrm = nrm.negate();
				for(int i = k; i < this.getHeight(); i++)
					this.matrix = this.matrix.set(i, k, this.matrix.get(i,k).divide(nrm));
				this.matrix = this.matrix.set(k,k, this.matrix.get(k, k).add(this.matrix.getElementField().getOne()));

				// Apply transformation to remaining columns.
				for(int j = k + 1; j < this.getWidth(); j++)
				{
					F s = this.matrix.getElementField().getZero();
					for(int i = k; i < this.getHeight(); i++)
						s = s.add(this.matrix.get(i,k).multiply(this.matrix.get(i,j)));
					s = s.negate().divide(this.matrix.get(k,k));
					for(int i = k; i < this.getHeight(); i++)
						this.matrix = this.matrix.set(i, j, this.matrix.get(i,j).add(s.multiply(this.matrix.get(i,k))));
				}
			}
			this.rDiagonal.set(k, nrm.negate());
		}
	}

	public M getMatrix()
	{
		return this.matrix;
	}

	public int getHeight()
	{
		return this.matrix.getHeight();
	}

	public int getWidth()
	{
		return this.matrix.getWidth();
	}

	/** Is the matrix full rank?
	@return     true if factor, and hence matrixToDecompose, has full rank.
	 */
	public boolean isFullRank()
	{
		for(int j = 0; j < this.getWidth(); j++)
			if(this.rDiagonal.get(j).equals(this.matrix.getElementField().getZero()))
				return false;
		return true;
	}

	/** Return the Householder vectors
	@return     Lower trapezoidal matrix whose columns define the reflections
	 */
	public M getHouseholderMatrix()
	{
//		SimpleRealMatrix X = new SimpleRealMatrix(m, n);
//		double[][] householderMatrix = X.getArray();
//		double[][] householderMatrix = new double[this.getHeight()][this.getWidth()];
		M householderMatrix = this.matrix.blank();
		for(int i = 0; i < this.getHeight(); i++)
			for(int j = 0; j < this.getWidth(); j++)
				if(i >= j)
					householderMatrix = householderMatrix.set(i, j, this.matrix.get(i,j));
//				else
//					householderMatrix[i][j] = 0.0;
//		return new SimpleRealMatrix(householderMatrix);
		return householderMatrix;
	}

	/** Return the upper triangular factor
	@return     factor
	 */
	public M getUpperTriangularFactor()
	{
//		SimpleRealMatrix X = new SimpleRealMatrix(n, n);
//		double[][] factor = X.getArray();
//		double[][] factor = new double[this.getWidth()][this.getWidth()];
		M factor = this.matrix.blank();
		for(int i = 0; i < this.getWidth(); i++)
			for(int j = 0; j < this.getWidth(); j++)
				if(i < j)
					factor = factor.set(i, j, this.matrix.get(i,j));
				else if(i == j)
					factor = factor.set(i, j, this.rDiagonal.get(i));
//				else
//					factor[i][j] = 0.0;
//		return new SimpleRealMatrix(factor);
		return factor;
	}

	/** Generate and return the (economy-sized) orthogonal factor
	@return     factor
	 */
	public M getOrthogonalFactor()
	{
//		SimpleRealMatrix X = new SimpleRealMatrix(m, n);
//		double[][] factor = X.getArray();
//		double[][] factor = new double[this.getHeight()][this.getWidth()];
		M factor = this.matrix.blank();
		for(int k = this.getWidth() - 1; k >= 0; k--)
		{
			for(int i = 0; i < this.getHeight(); i++)
				factor = factor.set(i, k, this.matrix.getElementField().getZero());
			factor = factor.set(k, k, this.matrix.getElementField().getOne());
			for(int j = k; j < this.getWidth(); j++)
				if(!this.matrix.get(k,k).equals(this.matrix.getElementField().getOne()))
				{
					F s = this.matrix.getElementField().getZero();
					for(int i = k; i < this.getHeight(); i++)
						s = s.add(this.matrix.get(i,k).multiply(factor.get(i,j)));
					s = s.negate().divide(this.matrix.get(k,k));
					for(int i = k; i < this.getHeight(); i++)
						factor = factor.set(i, j, factor.get(i,j).add(s.multiply(this.matrix.get(i,k))));
				}
		}
		return factor;
	}

	/** Least squares solution of matrixToDecompose*X = solutionMatrix
	@param solutionMatrix    matrixToDecompose SimpleRealMatrix with as many rows as matrixToDecompose and any number of columns.
	@return     X that minimizes the two norm of factor*factor*X-solutionMatrix.
	@exception  IllegalArgumentException  SimpleRealMatrix row dimensions must agree.
	@exception  RuntimeException  SimpleRealMatrix is rank deficient.
	 */
	public M solve(M solutionMatrix)
	{
		if(solutionMatrix.getHeight() != this.getHeight())
			throw new IllegalArgumentException("Matrix row dimensions must agree.");
		if(!this.isFullRank())
			throw new RuntimeException("Matrix is rank deficient.");

		// Copy right hand side
		int nx = solutionMatrix.getWidth();
//		double[][] X = solutionMatrix.toDoubleArray();
		M solved = solutionMatrix;

		// Compute Y = transpose(factor)*solutionMatrix
		for(int k = 0; k < this.getWidth(); k++)
			for(int j = 0; j < nx; j++)
			{
				F s = this.matrix.getElementField().getZero();
				for(int i = k; i < this.getHeight(); i++)
					s = s.add(this.matrix.get(i,k).multiply(solved.get(i,j)));
				s = s.negate().divide(this.matrix.get(k,k));
				for(int i = k; i < this.getHeight(); i++)
					solved = solved.set(i, j, solved.get(i,j).add(s.multiply(this.matrix.get(i,k))));
			}
		// Solve factor*X = Y;
		for(int k = this.getWidth() - 1; k >= 0; k--)
		{
			for(int j = 0; j < nx; j++)
				solved = solved.set(k, j, solved.get(k,j).divide(this.rDiagonal.get(k)));
			for(int i = 0; i < k; i++)
				for(int j = 0; j < nx; j++)
					solved = solved.set(i, j, solved.get(i,j).subtract(solved.get(k,j).multiply(this.matrix.get(i,k))));
		}
		
		return solved.getSubmatrix(0, this.getWidth() - 1, 0, nx - 1);
	}
}
