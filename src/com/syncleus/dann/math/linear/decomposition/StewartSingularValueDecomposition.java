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
 * m-by-n orthogonal matrix u, an n-by-n diagonal matrix S, and an n-by-n
 * orthogonal matrix v so that A = u*S*v'.
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
	 * Arrays for internal storage of u and v.
	 */
	private final double[][] u;
	private final double[][] v;
	/**
	 * Array for internal storage of singular values.
	 */
	private final double[] s;
	/**
	 * Row and column dimensions.
	 */
	private final int m;
	private final int n;

	/**
	 * Construct the singular value decomposition
	 *
	 * @param matrix Rectangular matrix. Gives access to u, S and v.
	 */
	public StewartSingularValueDecomposition(final RealMatrix matrix)
	{
		// Derived from LINPACK code.
		// Initialize.
		final double[][] A = matrix.toDoubleArray();
		this.m = matrix.getHeight();
		this.n = matrix.getWidth();
		final int nu = Math.min(this.m, this.n);
		this.s = new double[Math.min(this.m + 1, this.n)];
		this.u = new double[m][nu];
		this.v = new double[n][n];
		final double[] e = new double[n];
		final double[] work = new double[m];
		final boolean wantu = true;
		final boolean wantv = true;
		// Reduce A to bidiagonal form, storing the diagonal elements
		// in s and the super-diagonal elements in e.
		final int nct = Math.min(this.m - 1, this.n);
		final int nrt = Math.max(0, Math.min(this.n - 2, this.m));
		for(int k = 0; k < Math.max(nct, nrt); k++)
		{
			if (k < nct)
			{
				// Compute the transformation for the k-th column and
				// place the k-th diagonal in s[k].
				// Compute 2-norm of k-th column without under/overflow.
				this.s[k] = 0;
				for(int i = k; i < this.m; i++)
					this.s[k] = Math.hypot(this.s[k], A[i][k]);
				if (this.s[k] != 0.0)
				{
					if (A[k][k] < 0.0)
						this.s[k] = -this.s[k];
					for(int i = k; i < this.m; i++)
						A[i][k] /= this.s[k];
					A[k][k] += 1.0;
				}
				this.s[k] = -this.s[k];
			}
			for(int j = k + 1; j < this.n; j++)
			{
				if ((k < nct) && (this.s[k] != 0.0))
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
			if (wantu & (k < nct))
				// Place the transformation in u for subsequent back
				// multiplication.
				for(int i = k; i < this.m; i++)
					this.u[i][k] = A[i][k];
			if (k < nrt)
			{
				// Compute the k-th row transformation and place the
				// k-th super-diagonal in e[k].
				// Compute 2-norm without under/overflow.
				e[k] = 0;
				for(int i = k + 1; i < this.n; i++)
					e[k] = Math.hypot(e[k], e[i]);
				if (e[k] != 0.0)
				{
					if (e[k + 1] < 0.0)
						e[k] = -e[k];
					for(int i = k + 1; i < this.n; i++)
						e[i] /= e[k];
					e[k + 1] += 1.0;
				}
				e[k] = -e[k];
				if ((k + 1 < this.m) && (e[k] != 0.0))
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
				if (wantv)
					// Place the transformation in v for subsequent
					// back multiplication.
					for(int i = k + 1; i < this.n; i++)
						this.v[i][k] = e[i];
			}
		}
		// Set up the final bidiagonal matrix or order p.
		int p = Math.min(this.n, this.m + 1);
		if (nct < this.n)
			this.s[nct] = A[nct][nct];
		if (this.m < p)
			this.s[p - 1] = 0.0;
		if (nrt + 1 < p)
			e[nrt] = A[nrt][p - 1];
		e[p - 1] = 0.0;
		// If required, generate u.
		if (wantu)
		{
			for(int j = nct; j < nu; j++)
			{
				for(int i = 0; i < this.m; i++)
					this.u[i][j] = 0.0;
				this.u[j][j] = 1.0;
			}
			for(int k = nct - 1; k >= 0; k--)
				if (this.s[k] != 0.0)
				{
					for(int j = k + 1; j < nu; j++)
					{
						double t = 0;
						for(int i = k; i < this.m; i++)
							t += this.u[i][k] * this.u[i][j];
						t = -t / this.u[k][k];
						for(int i = k; i < this.m; i++)
							this.u[i][j] += t * this.u[i][k];
					}
					for(int i = k; i < this.m; i++)
						this.u[i][k] = -this.u[i][k];
					this.u[k][k] = 1.0 + this.u[k][k];
					for(int i = 0; i < k - 1; i++)
						this.u[i][k] = 0.0;
				}
				else
				{
					for(int i = 0; i < this.m; i++)
						this.u[i][k] = 0.0;
					this.u[k][k] = 1.0;
				}
		}
		// If required, generate v.
		if (wantv)
			for(int k = this.n - 1; k >= 0; k--)
			{
				if ((k < nrt) && (e[k] != 0.0))
					for(int j = k + 1; j < nu; j++)
					{
						double t = 0;
						for(int i = k + 1; i < this.n; i++)
							t += this.v[i][k] * this.v[i][j];
						t = -t / this.v[k + 1][k];
						for(int i = k + 1; i < this.n; i++)
							this.v[i][j] += t * this.v[i][k];
					}
				for(int i = 0; i < this.n; i++)
					this.v[i][k] = 0.0;
				this.v[k][k] = 1.0;
			}
		// Main iteration loop for the singular values.
		final int pp = p - 1;
		int iter = 0;
		final double eps = Math.pow(2.0, -52.0);
		final double tiny = Math.pow(2.0, -966.0);
		while (p > 0)
		{
			int k;
			final int kase;
			// Here is where a test for too many iterations would go.
			// This section of the program inspects for
			// negligible elements in the s and e arrays.  On
			// completion the variables kase and k are set as follows.
			// kase = 1     if s(p) and e[k-1] are negligible and k<p
			// kase = 2     if s(k) is negligible and k<p
			// kase = 3     if e[k-1] is negligible, k<p, and
			//              s(k), ..., s(p) are not negligible (qr step).
			// kase = 4     if e(p-1) is negligible (convergence).
			for(k = p - 2; k >= -1; k--)
			{
				if (k == -1)
					break;
				if (Math.abs(e[k]) <=
						tiny + eps * (Math.abs(this.s[k]) + Math.abs(this.s[k + 1])))
				{
					e[k] = 0.0;
					break;
				}
			}
			if (k == p - 2)
				kase = 4;
			else
			{
				int ks;
				for(ks = p - 1; ks >= k; ks--)
				{
					if (ks == k)
						break;
					final double t = (ks != p ? Math.abs(e[ks]) : 0.) +
							(ks != k + 1 ? Math.abs(e[ks - 1]) : 0.);
					if (Math.abs(this.s[ks]) <= tiny + eps * t)
					{
						this.s[ks] = 0.0;
						break;
					}
				}
				if (ks == k)
					kase = 3;
				else if (ks == p - 1)
					kase = 1;
				else
				{
					kase = 2;
					k = ks;
				}
			}
			k++;
			// Perform the task indicated by kase.
			switch (kase)
			{
			// Deflate negligible s(p).
			case 1:
			{
				double f = e[p - 2];
				e[p - 2] = 0.0;
				for(int j = p - 2; j >= k; j--)
				{
					double t = Math.hypot(this.s[j], f);
					final double cs = this.s[j] / t;
					final double sn = f / t;
					this.s[j] = t;
					if (j != k)
					{
						f = -sn * e[j - 1];
						e[j - 1] = cs * e[j - 1];
					}
					if (wantv)
						for(int i = 0; i < this.n; i++)
						{
							t = cs * this.v[i][j] + sn * this.v[i][p - 1];
							this.v[i][p - 1] = -sn * this.v[i][j] + cs * this.v[i][p - 1];
							this.v[i][j] = t;
						}
				}
			}
			break;
			// Split at negligible s(k).
			case 2:
			{
				double f = e[k - 1];
				e[k - 1] = 0.0;
				for(int j = k; j < p; j++)
				{
					double t = Math.hypot(this.s[j], f);
					final double cs = this.s[j] / t;
					final double sn = f / t;
					this.s[j] = t;
					f = -sn * e[j];
					e[j] = cs * e[j];
					if (wantu)
						for(int i = 0; i < this.m; i++)
						{
							t = cs * this.u[i][j] + sn * this.u[i][k - 1];
							this.u[i][k - 1] = -sn * this.u[i][j] + cs * this.u[i][k - 1];
							this.u[i][j] = t;
						}
				}
			}
			break;
			// Perform one qr step.
			case 3:
			{
				// Calculate the shift.
				final double scale = Math.max(Math.max(Math.max(Math.max(
						Math.abs(this.s[p - 1]), Math.abs(this.s[p - 2])), Math.abs(e[p - 2])),
						Math.abs(this.s[k])), Math.abs(e[k]));
				final double sp = this.s[p - 1] / scale;
				final double spm1 = this.s[p - 2] / scale;
				final double epm1 = e[p - 2] / scale;
				final double sk = this.s[k] / scale;
				final double ek = e[k] / scale;
				final double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0;
				final double c = (sp * epm1) * (sp * epm1);
				double shift = 0.0;
				if ((b != 0.0) | (c != 0.0))
				{
					shift = Math.sqrt(b * b + c);
					if (b < 0.0)
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
					if (j != k)
						e[j - 1] = t;
					f = cs * this.s[j] + sn * e[j];
					e[j] = cs * e[j] - sn * this.s[j];
					g = sn * this.s[j + 1];
					this.s[j + 1] = cs * this.s[j + 1];
					if (wantv)
						for(int i = 0; i < this.n; i++)
						{
							t = cs * this.v[i][j] + sn * this.v[i][j + 1];
							this.v[i][j + 1] = -sn * this.v[i][j] + cs * this.v[i][j + 1];
							this.v[i][j] = t;
						}
					t = Math.hypot(f, g);
					cs = f / t;
					sn = g / t;
					this.s[j] = t;
					f = cs * e[j] + sn * this.s[j + 1];
					this.s[j + 1] = -sn * e[j] + cs * this.s[j + 1];
					g = sn * e[j + 1];
					e[j + 1] = cs * e[j + 1];
					if (wantu && (j < this.m - 1))
						for(int i = 0; i < this.m; i++)
						{
							t = cs * this.u[i][j] + sn * this.u[i][j + 1];
							this.u[i][j + 1] = -sn * this.u[i][j] + cs * this.u[i][j + 1];
							this.u[i][j] = t;
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
				if (this.s[k] <= 0.0)
				{
					this.s[k] = (this.s[k] < 0.0 ? -this.s[k] : 0.0);
					if (wantv)
						for(int i = 0; i <= pp; i++)
							this.v[i][k] = -this.v[i][k];
				}
				// Order the singular values.
				while (k < pp)
				{
					if (this.s[k] >= this.s[k + 1])
						break;
					double t = this.s[k];
					this.s[k] = this.s[k + 1];
					this.s[k + 1] = t;
					if (wantv && (k < this.n - 1))
						for(int i = 0; i < this.n; i++)
						{
							t = this.v[i][k + 1];
							this.v[i][k + 1] = this.v[i][k];
							this.v[i][k] = t;
						}
					if (wantu && (k < this.m - 1))
						for(int i = 0; i < this.m; i++)
						{
							t = this.u[i][k + 1];
							this.u[i][k + 1] = this.u[i][k];
							this.u[i][k] = t;
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
		return new SimpleRealMatrix(this.u).getSubmatrix(0, this.m, 0, Math.min(this.m + 1, this.n));
	}

	/**
	 * Return the right singular vectors
	 *
	 * @return V
	 */
	public RealMatrix getRightSingularMatrix()
	{
		return new SimpleRealMatrix(this.v).getSubmatrix(0, this.n, 0, this.n);
	}

	/**
	 * Return the one-dimensional array of singular values
	 *
	 * @return diagonal of S.
	 */
	public List<RealNumber> getSingularValues()
	{
		final List<RealNumber> singularValues = new ArrayList<RealNumber>(this.s.length);
		for(final double singularValue : this.s)
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
			S[i][i] = this.s[i];
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
		return this.s[0];
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
		return this.s[0] / this.s[Math.min(this.m, this.n) - 1];
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
		final double tol = Math.max(this.m, this.n) * this.s[0] * eps;
		int r = 0;
		for(double value : s)
			if (value > tol)
				r++;
		return r;
	}
}
