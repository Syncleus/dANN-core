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

import java.util.*;
import com.syncleus.dann.math.RealNumber;
import com.syncleus.dann.math.linear.*;

/**
 * Singular Value Decomposition.
 * <p/>
 * For an m-by-n matrix A with m >= n, the singular value decomposition is an
 * m-by-n orthogonal matrix leftSingularMatrix, an n-by-n diagonal matrix S, and
 * an n-by-n orthogonal matrix rightSingularMatrix so that A =
 * leftSingularMatrix*S*rightSingularMatrix'.
 * <p/>
 * The singular values, sigma[k] = S[k][k], are ordered so that sigma[0] >=
 * sigma[1] >= ... >= sigma[n-1].
 * <p/>
 * The singular value decompostion always exists, so the constructor will never
 * fail.  The matrix condition number and the effective numerical rank can be
 * computed from this decomposition.<br/> Algorithm taken from: g.w. stewart,
 * university of maryland, argonne national lab. c
 */
public class StewartSingularValueDecomposition implements java.io.Serializable, SingularValueDecomposition
{
	private static final long serialVersionUID = 325638354441525023L;
	/**
	 * Arrays for internal storage of leftSingularMatrix and rightSingularMatrix.
	 */
	private final double[][] leftSingularMatrix;
	private final double[][] rightSingularMatrix;
	private final boolean hasRightSingularMatrix;
	private final boolean hasLeftSingularMatrix;
	/**
	 * Array for internal storage of singular values.
	 */
	private final double[] matrix;
	/**
	 * Row and column dimensions.
	 */
	private final int m;
	private final int n;

	/**
	 * Construct the singular value decomposition
	 *
	 * @param matrix Rectangular matrix. Gives access to leftSingularMatrix, S and
	 * rightSingularMatrix.
	 */
	public StewartSingularValueDecomposition(final RealMatrix matrix)
	{
		this(matrix, true, true);
	}

	public StewartSingularValueDecomposition(final RealMatrix matrix, final boolean calculateRightSingularMatrix, final boolean calculateLeftSingularMatrix)
	{
		this.m = matrix.getHeight();
		this.n = matrix.getWidth();
		final int nu = Math.min(this.m, this.n);
		this.hasRightSingularMatrix = calculateRightSingularMatrix;
		this.hasLeftSingularMatrix = calculateLeftSingularMatrix;
		if( this.hasLeftSingularMatrix )
			this.leftSingularMatrix = new double[m][nu];
		else
			this.leftSingularMatrix = null;
		if( this.hasRightSingularMatrix )
			this.rightSingularMatrix = new double[n][n];
		else
			this.rightSingularMatrix = null;

		// Derived from LINPACK code.
		// Initialize.
		final double[][] A = matrix.toDoubleArray();
		this.matrix = new double[Math.min(this.m + 1, this.n)];
		final double[] e = new double[n];
		final double[] work = new double[m];
		// Reduce A to bidiagonal form, storing the diagonal elements
		// in matrix and the super-diagonal elements in e.
		final int nct = Math.min(this.m - 1, this.n);
		final int nrt = Math.max(0, Math.min(this.n - 2, this.m));
		for(int k = 0; k < Math.max(nct, nrt); k++)
		{
			if( k < nct )
			{
				// Compute the transformation for the k-th column and
				// place the k-th diagonal in matrix[k].
				// Compute 2-norm of k-th column without under/overflow.
				this.matrix[k] = 0;
				for(int i = k; i < this.m; i++)
					this.matrix[k] = Math.hypot(this.matrix[k], A[i][k]);
				if( this.matrix[k] != 0.0 )
				{
					if( A[k][k] < 0.0 )
						this.matrix[k] = -this.matrix[k];
					for(int i = k; i < this.m; i++)
						A[i][k] /= this.matrix[k];
					A[k][k] += 1.0;
				}
				this.matrix[k] = -this.matrix[k];
			}
			for(int j = k + 1; j < this.n; j++)
			{
				if( (k < nct) && (this.matrix[k] != 0.0) )
				{
					// Apply the transformation.
					double t = 0;
					for(int i = k; i < this.m; i++)
						t += A[i][k] * A[i][j];
					t = -t / A[k][k];
					for(int i = k; i < this.m; i++)
						A[i][j] += t * A[i][k];
				}
				// Place the k-th row of A into e for the
				// subsequent calculation of the row transformation.
				e[j] = A[k][j];
			}
			if( this.hasLeftSingularMatrix && (k < nct) )
				// Place the transformation in leftSingularMatrix for subsequent back
				// multiplication.
				for(int i = k; i < this.m; i++)
					this.leftSingularMatrix[i][k] = A[i][k];
			if( k < nrt )
			{
				// Compute the k-th row transformation and place the
				// k-th super-diagonal in e[k].
				// Compute 2-norm without under/overflow.
				e[k] = 0;
				for(int i = k + 1; i < this.n; i++)
					e[k] = Math.hypot(e[k], e[i]);
				if( e[k] != 0.0 )
				{
					if( e[k + 1] < 0.0 )
						e[k] = -e[k];
					for(int i = k + 1; i < this.n; i++)
						e[i] /= e[k];
					e[k + 1] += 1.0;
				}
				e[k] = -e[k];
				if( (k + 1 < this.m) && (e[k] != 0.0) )
				{
					// Apply the transformation.
					for(int i = k + 1; i < this.m; i++)
						work[i] = 0.0;
					for(int j = k + 1; j < this.n; j++)
						for(int i = k + 1; i < this.m; i++)
							work[i] += e[j] * A[i][j];
					for(int j = k + 1; j < this.n; j++)
					{
						final double t = -e[j] / e[k + 1];
						for(int i = k + 1; i < this.m; i++)
							A[i][j] += t * work[i];
					}
				}
				if( this.hasRightSingularMatrix )
					// Place the transformation in rightSingularMatrix for subsequent
					// back multiplication.
					for(int i = k + 1; i < this.n; i++)
						this.rightSingularMatrix[i][k] = e[i];
			}
		}
		// Set up the final bidiagonal matrix or order p.
		int p = Math.min(this.n, this.m + 1);
		if( nct < this.n )
			this.matrix[nct] = A[nct][nct];
		if( this.m < p )
			this.matrix[p - 1] = 0.0;
		if( nrt + 1 < p )
			e[nrt] = A[nrt][p - 1];
		e[p - 1] = 0.0;
		// If required, generate leftSingularMatrix.
		if( this.hasLeftSingularMatrix )
		{
			for(int j = nct; j < nu; j++)
			{
				for(int i = 0; i < this.m; i++)
					this.leftSingularMatrix[i][j] = 0.0;
				this.leftSingularMatrix[j][j] = 1.0;
			}
			for(int k = nct - 1; k >= 0; k--)
				if( this.matrix[k] != 0.0 )
				{
					for(int j = k + 1; j < nu; j++)
					{
						double t = 0;
						for(int i = k; i < this.m; i++)
							t += this.leftSingularMatrix[i][k] * this.leftSingularMatrix[i][j];
						t = -t / this.leftSingularMatrix[k][k];
						for(int i = k; i < this.m; i++)
							this.leftSingularMatrix[i][j] += t * this.leftSingularMatrix[i][k];
					}
					for(int i = k; i < this.m; i++)
						this.leftSingularMatrix[i][k] = -this.leftSingularMatrix[i][k];
					this.leftSingularMatrix[k][k] = 1.0 + this.leftSingularMatrix[k][k];
					for(int i = 0; i < k - 1; i++)
						this.leftSingularMatrix[i][k] = 0.0;
				}
				else
				{
					for(int i = 0; i < this.m; i++)
						this.leftSingularMatrix[i][k] = 0.0;
					this.leftSingularMatrix[k][k] = 1.0;
				}
		}
		// If required, generate rightSingularMatrix.
		if( this.hasRightSingularMatrix )
			for(int k = this.n - 1; k >= 0; k--)
			{
				if( (k < nrt) && (e[k] != 0.0) )
					for(int j = k + 1; j < nu; j++)
					{
						double t = 0;
						for(int i = k + 1; i < this.n; i++)
							t += this.rightSingularMatrix[i][k] * this.rightSingularMatrix[i][j];
						t = -t / this.rightSingularMatrix[k + 1][k];
						for(int i = k + 1; i < this.n; i++)
							this.rightSingularMatrix[i][j] += t * this.rightSingularMatrix[i][k];
					}
				for(int i = 0; i < this.n; i++)
					this.rightSingularMatrix[i][k] = 0.0;
				this.rightSingularMatrix[k][k] = 1.0;
			}
		// Main iteration loop for the singular values.
		final int pp = p - 1;
		int iter = 0;
		final double eps = Math.pow(2.0, -52.0);
		final double tiny = Math.pow(2.0, -966.0);
		while( p > 0 )
		{
			int k;
			final int kase;
			// Here is where a test for too many iterations would go.
			// This section of the program inspects for
			// negligible elements in the matrix and e arrays.  On
			// completion the variables kase and k are set as follows.
			// kase = 1     if matrix(p) and e[k-1] are negligible and k<p
			// kase = 2     if matrix(k) is negligible and k<p
			// kase = 3     if e[k-1] is negligible, k<p, and
			//              matrix(k), ..., matrix(p) are not negligible (qr step).
			// kase = 4     if e(p-1) is negligible (convergence).
			for(k = p - 2; k >= -1; k--)
			{
				if( k == -1 )
					break;
				if( Math.abs(e[k]) <=
						tiny + eps * (Math.abs(this.matrix[k]) + Math.abs(this.matrix[k + 1])) )
				{
					e[k] = 0.0;
					break;
				}
			}
			if( k == p - 2 )
				kase = 4;
			else
			{
				int ks;
				for(ks = p - 1; ks >= k; ks--)
				{
					if( ks == k )
						break;
					final double t = (ks != p ? Math.abs(e[ks]) : 0.) +
							(ks != k + 1 ? Math.abs(e[ks - 1]) : 0.);
					if( Math.abs(this.matrix[ks]) <= tiny + eps * t )
					{
						this.matrix[ks] = 0.0;
						break;
					}
				}
				if( ks == k )
					kase = 3;
				else if( ks == p - 1 )
					kase = 1;
				else
				{
					kase = 2;
					k = ks;
				}
			}
			k++;
			// Perform the task indicated by kase.
			switch(kase)
			{
			// Deflate negligible matrix(p).
			case 1:
			{
				double f = e[p - 2];
				e[p - 2] = 0.0;
				for(int j = p - 2; j >= k; j--)
				{
					double t = Math.hypot(this.matrix[j], f);
					final double cs = this.matrix[j] / t;
					final double sn = f / t;
					this.matrix[j] = t;
					if( j != k )
					{
						f = -sn * e[j - 1];
						e[j - 1] = cs * e[j - 1];
					}
					if( this.hasRightSingularMatrix )
						for(int i = 0; i < this.n; i++)
						{
							t = cs * this.rightSingularMatrix[i][j] + sn * this.rightSingularMatrix[i][p - 1];
							this.rightSingularMatrix[i][p - 1] = -sn * this.rightSingularMatrix[i][j] + cs * this.rightSingularMatrix[i][p - 1];
							this.rightSingularMatrix[i][j] = t;
						}
				}
			}
			break;
			// Split at negligible matrix(k).
			case 2:
			{
				double f = e[k - 1];
				e[k - 1] = 0.0;
				for(int j = k; j < p; j++)
				{
					double t = Math.hypot(this.matrix[j], f);
					final double cs = this.matrix[j] / t;
					final double sn = f / t;
					this.matrix[j] = t;
					f = -sn * e[j];
					e[j] = cs * e[j];
					if( this.hasLeftSingularMatrix )
						for(int i = 0; i < this.m; i++)
						{
							t = cs * this.leftSingularMatrix[i][j] + sn * this.leftSingularMatrix[i][k - 1];
							this.leftSingularMatrix[i][k - 1] = -sn * this.leftSingularMatrix[i][j] + cs * this.leftSingularMatrix[i][k - 1];
							this.leftSingularMatrix[i][j] = t;
						}
				}
			}
			break;
			// Perform one qr step.
			case 3:
			{
				// Calculate the shift.
				final double scale = Math.max(Math.max(Math.max(Math.max(
						Math.abs(this.matrix[p - 1]), Math.abs(this.matrix[p - 2])), Math.abs(e[p - 2])),
						Math.abs(this.matrix[k])), Math.abs(e[k]));
				final double sp = this.matrix[p - 1] / scale;
				final double spm1 = this.matrix[p - 2] / scale;
				final double epm1 = e[p - 2] / scale;
				final double sk = this.matrix[k] / scale;
				final double ek = e[k] / scale;
				final double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0;
				final double c = (sp * epm1) * (sp * epm1);
				double shift = 0.0;
				if( (b != 0.0) | (c != 0.0) )
				{
					shift = Math.sqrt(b * b + c);
					if( b < 0.0 )
						shift = -shift;
					shift = c / (b + shift);
				}
				double f = (sk + sp) * (sk - sp) + shift;
				double g = sk * ek;
				// Chase zeros.
				for(int j = k; j < p - 1; j++)
				{
					double t = Math.hypot(f, g);
					double cs = f / t;
					double sn = g / t;
					if( j != k )
						e[j - 1] = t;
					f = cs * this.matrix[j] + sn * e[j];
					e[j] = cs * e[j] - sn * this.matrix[j];
					g = sn * this.matrix[j + 1];
					this.matrix[j + 1] = cs * this.matrix[j + 1];
					if( this.hasRightSingularMatrix )
						for(int i = 0; i < this.n; i++)
						{
							t = cs * this.rightSingularMatrix[i][j] + sn * this.rightSingularMatrix[i][j + 1];
							this.rightSingularMatrix[i][j + 1] = -sn * this.rightSingularMatrix[i][j] + cs * this.rightSingularMatrix[i][j + 1];
							this.rightSingularMatrix[i][j] = t;
						}
					t = Math.hypot(f, g);
					cs = f / t;
					sn = g / t;
					this.matrix[j] = t;
					f = cs * e[j] + sn * this.matrix[j + 1];
					this.matrix[j + 1] = -sn * e[j] + cs * this.matrix[j + 1];
					g = sn * e[j + 1];
					e[j + 1] = cs * e[j + 1];
					if( this.hasLeftSingularMatrix && (j < this.m - 1) )
						for(int i = 0; i < this.m; i++)
						{
							t = cs * this.leftSingularMatrix[i][j] + sn * this.leftSingularMatrix[i][j + 1];
							this.leftSingularMatrix[i][j + 1] = -sn * this.leftSingularMatrix[i][j] + cs * this.leftSingularMatrix[i][j + 1];
							this.leftSingularMatrix[i][j] = t;
						}
				}
				e[p - 2] = f;
				iter = iter + 1;
			}
			break;
			// Convergence.
			case 4:
			{
				// Make the singular values positive.
				if( this.matrix[k] <= 0.0 )
				{
					this.matrix[k] = (this.matrix[k] < 0.0 ? -this.matrix[k] : 0.0);
					if( this.hasRightSingularMatrix )
						for(int i = 0; i <= pp; i++)
							this.rightSingularMatrix[i][k] = -this.rightSingularMatrix[i][k];
				}
				// Order the singular values.
				while( k < pp )
				{
					if( this.matrix[k] >= this.matrix[k + 1] )
						break;
					double t = this.matrix[k];
					this.matrix[k] = this.matrix[k + 1];
					this.matrix[k + 1] = t;
					if( this.hasRightSingularMatrix && (k < this.n - 1) )
						for(int i = 0; i < this.n; i++)
						{
							t = this.rightSingularMatrix[i][k + 1];
							this.rightSingularMatrix[i][k + 1] = this.rightSingularMatrix[i][k];
							this.rightSingularMatrix[i][k] = t;
						}
					if( this.hasLeftSingularMatrix && (k < this.m - 1) )
						for(int i = 0; i < this.m; i++)
						{
							t = this.leftSingularMatrix[i][k + 1];
							this.leftSingularMatrix[i][k + 1] = this.leftSingularMatrix[i][k];
							this.leftSingularMatrix[i][k] = t;
						}
					k++;
				}
				iter = 0;
				p--;
			}
			break;
			}
		}
	}

	/**
	 * Return the left singular vectors
	 *
	 * @return U
	 */
	public RealMatrix getLeftSingularMatrix()
	{
		if( !this.hasLeftSingularMatrix )
			return null;
		return new SimpleRealMatrix(this.leftSingularMatrix).getSubmatrix(0, this.m, 0, Math.min(this.m + 1, this.n));
	}

	/**
	 * Return the right singular vectors
	 *
	 * @return V
	 */
	public RealMatrix getRightSingularMatrix()
	{
		if( this.hasRightSingularMatrix )
			return null;

		return new SimpleRealMatrix(this.rightSingularMatrix).getSubmatrix(0, this.n, 0, this.n);
	}

	/**
	 * Return the one-dimensional array of singular values
	 *
	 * @return diagonal of S.
	 */
	public List<RealNumber> getSingularValues()
	{
		final List<RealNumber> singularValues = new ArrayList<RealNumber>(this.matrix.length);
		for(final double singularValue : this.matrix)
			singularValues.add(new RealNumber(singularValue));
		return Collections.unmodifiableList(singularValues);
	}

	/**
	 * Return the diagonal matrix of singular values
	 *
	 * @return S
	 */
	public RealMatrix getMatrix()
	{
		final double[][] S = new double[n][n];
		for(int i = 0; i < this.n; i++)
		{
			for(int j = 0; j < this.n; j++)
				S[i][j] = 0.0;
			S[i][i] = this.matrix[i];
		}
		return new SimpleRealMatrix(S);
	}

	/**
	 * Two norm
	 *
	 * @return max(S)
	 */
	public double norm2Double()
	{
		return this.matrix[0];
	}

	public RealNumber norm2()
	{
		return new RealNumber(this.norm2Double());
	}

	/**
	 * Two norm condition number
	 *
	 * @return max(S)/min(S)
	 */
	public double norm2ConditionDouble()
	{
		return this.matrix[0] / this.matrix[Math.min(this.m, this.n) - 1];
	}

	public RealNumber norm2Condition()
	{
		return new RealNumber(this.norm2ConditionDouble());
	}

	/**
	 * Effective numerical matrix rank
	 *
	 * @return Number of nonnegligible singular values.
	 */
	public int rank()
	{
		final double eps = Math.pow(2.0, -52.0);
		final double tol = Math.max(this.m, this.n) * this.matrix[0] * eps;
		int r = 0;
		for(final double value : this.matrix)
			if( value > tol )
				r++;
		return r;
	}
}
