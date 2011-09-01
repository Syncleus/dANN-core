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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.syncleus.dann.math.OrderedAlgebraic;
import com.syncleus.dann.math.linear.Matrix;

/**
 * QR Decomposition.
 * <p/>
 * For an m-by-n matrix matrixToDecompose with m >= n, the QR decomposition is
 * an m-by-n orthogonal matrix factor and an n-by-n upper triangular matrix
 * factor so that matrixToDecompose = factor*factor.
 * <p/>
 * The QR decomposition always exists, even if the matrix does not have full
 * rank, so the constructor will never fail.  The primary use of the QR
 * decomposition is in the least squares solution of non-square systems of
 * simultaneous linear equations.  This will fail if isFullRank() returns
 * false.
 */
public class HouseholderQrDecomposition<M extends Matrix<M, F>, F extends OrderedAlgebraic<F>> implements Serializable, QrDecomposition<M, F>
{
	private static final long serialVersionUID = -2181312959012242588L;
	/**
	 * Internal storage of the decomposition matrix.
	 */
	private final M matrix;
	/**
	 * Internal storage of the diagonal compute factor.
	 */
	private final List<F> rDiagonal;

	/**
	 * QR Decomposition, computed by Householder reflections. gives access to
	 * factor and the Householder vectors and compute factor.
	 *
	 * @param matrixToDecompose Rectangular matrix
	 */
	public HouseholderQrDecomposition(final M matrixToDecompose)
	{
		// Initialize.
		M myMatrix = matrixToDecompose;
		List<F> myRDiagonal = new ArrayList<F>(myMatrix.getWidth());
		myRDiagonal.addAll(Collections.nCopies(myMatrix.getWidth(), myMatrix.getElementField().getZero()));

		// Main loop.
		for(int k = 0; k < myMatrix.getWidth(); k++)
		{
			// Compute 2-norm of k-th column without under/overflow.
			F nrm = myMatrix.getElementField().getZero();
			for(int i = k; i < myMatrix.getHeight(); i++)
				nrm = nrm.hypot(myMatrix.get(i, k));

			if( !nrm.equals(myMatrix.getElementField().getZero()) )
			{
				// Form k-th Householder vector.
				if( myMatrix.get(k, k).compareTo(myMatrix.getElementField().getZero()) < 0 )
					nrm = nrm.negate();
				for(int i = k; i < myMatrix.getHeight(); i++)
					myMatrix = myMatrix.set(i, k, myMatrix.get(i, k).divide(nrm));
				myMatrix = myMatrix.set(k, k, myMatrix.get(k, k).add(myMatrix.getElementField().getOne()));

				// Apply transformation to remaining columns.
				for(int j = k + 1; j < myMatrix.getWidth(); j++)
				{
					F sum = myMatrix.getElementField().getZero();
					for(int i = k; i < myMatrix.getHeight(); i++)
						sum = sum.add(myMatrix.get(i, k).multiply(myMatrix.get(i, j)));
					sum = sum.negate().divide(myMatrix.get(k, k));
					for(int i = k; i < myMatrix.getHeight(); i++)
						myMatrix = myMatrix.set(i, j, myMatrix.get(i, j).add(sum.multiply(myMatrix.get(i, k))));
				}
			}
			myRDiagonal.set(k, nrm.negate());
		}

		this.matrix = myMatrix;
		this.rDiagonal = myRDiagonal;
	}

	@Override
	public final M getMatrix()
	{
		return this.matrix;
	}

	public final int getHeight()
	{
		return this.matrix.getHeight();
	}

	public final int getWidth()
	{
		return this.matrix.getWidth();
	}

	/**
	 * Is the matrix full rank?
	 *
	 * @return true if factor, and hence matrixToDecompose, has full rank.
	 */
	@Override
	public boolean isFullRank()
	{
		for(int j = 0; j < this.getWidth(); j++)
			if( this.rDiagonal.get(j).equals(this.matrix.getElementField().getZero()) )
				return false;
		return true;
	}

	/**
	 * Return the Householder vectors.
	 *
	 * @return Lower trapezoidal matrix whose columns define the reflections
	 */
	@Override
	public M getHouseholderMatrix()
	{
		M householderMatrix = this.matrix.blank();
		for(int i = 0; i < this.getHeight(); i++)
			for(int j = 0; j < this.getWidth(); j++)
				if( i >= j )
					householderMatrix = householderMatrix.set(i, j, this.matrix.get(i, j));
		return householderMatrix;
	}

	/**
	 * Return the upper triangular factor.
	 *
	 * @return factor
	 */
	@Override
	public M getUpperTriangularFactor()
	{
		M factor = this.matrix.blank();
		for(int i = 0; i < this.getWidth(); i++)
			for(int j = 0; j < this.getWidth(); j++)
				if( i < j )
					factor = factor.set(i, j, this.matrix.get(i, j));
				else if( i == j )
					factor = factor.set(i, j, this.rDiagonal.get(i));
		return factor;
	}

	/**
	 * Generate and return the (economy-sized) orthogonal factor.
	 *
	 * @return factor
	 */
	@Override
	public M getOrthogonalFactor()
	{
		M factor = this.matrix.blank();
		for(int k = this.getWidth() - 1; k >= 0; k--)
		{
			for(int i = 0; i < this.getHeight(); i++)
				factor = factor.set(i, k, this.matrix.getElementField().getZero());
			factor = factor.set(k, k, this.matrix.getElementField().getOne());
			for(int j = k; j < this.getWidth(); j++)
				if( !this.matrix.get(k, k).equals(this.matrix.getElementField().getOne()) )
				{
					F sum = this.matrix.getElementField().getZero();
					for(int i = k; i < this.getHeight(); i++)
						sum = sum.add(this.matrix.get(i, k).multiply(factor.get(i, j)));
					sum = sum.negate().divide(this.matrix.get(k, k));
					for(int i = k; i < this.getHeight(); i++)
						factor = factor.set(i, j, factor.get(i, j).add(sum.multiply(this.matrix.get(i, k))));
				}
		}
		return factor;
	}

	/**
	 * Least squares solution of matrixToDecompose*X = solutionMatrix.
	 *
	 * @param solutionMatrix matrixToDecompose SimpleRealMatrix with as many rows
	 * as matrixToDecompose and any number of columns.
	 * @return X that minimizes the two norm of factor*factor*X-solutionMatrix.
	 * @throws IllegalArgumentException SimpleRealMatrix row dimensions must
	 * agree.
	 * @throws RuntimeException SimpleRealMatrix is rank deficient.
	 */
	@Override
	public M solve(final M solutionMatrix)
	{
		if( solutionMatrix.getHeight() != this.getHeight() )
			throw new IllegalArgumentException("solutionMatrix row dimensions must agree.");
		if( !this.isFullRank() )
			throw new ArithmeticException("Matrix is rank deficient.");

		// Copy right hand side
		final int width = solutionMatrix.getWidth();
		M solved = solutionMatrix;

		// Compute Y = transpose(factor)*solutionMatrix
		for(int k = 0; k < this.getWidth(); k++)
			for(int j = 0; j < width; j++)
			{
				F sum = this.matrix.getElementField().getZero();
				for(int i = k; i < this.getHeight(); i++)
					sum = sum.add(this.matrix.get(i, k).multiply(solved.get(i, j)));
				sum = sum.negate().divide(this.matrix.get(k, k));
				for(int i = k; i < this.getHeight(); i++)
					solved = solved.set(i, j, solved.get(i, j).add(sum.multiply(this.matrix.get(i, k))));
			}
		// Solve factor*X = Y;
		for(int k = this.getWidth() - 1; k >= 0; k--)
		{
			for(int j = 0; j < width; j++)
				solved = solved.set(k, j, solved.get(k, j).divide(this.rDiagonal.get(k)));
			for(int i = 0; i < k; i++)
				for(int j = 0; j < width; j++)
					solved = solved.set(i, j, solved.get(i, j).subtract(solved.get(k, j).multiply(this.matrix.get(i, k))));
		}

		return solved.getSubmatrix(0, this.getWidth() - 1, 0, width - 1);
	}
}
