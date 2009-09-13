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

import com.syncleus.dann.math.RealNumber;
import com.syncleus.dann.math.linear.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Eigenvalues and eigenvectors of a real matrix.
<P>
If matrixToDecomposeElements is symmetric, then matrixToDecomposeElements = matrixElements*D*matrixElements' where the eigenvalue matrix D is
diagonal and the eigenvector matrix matrixElements is orthogonal.
I.e. matrixToDecomposeElements = matrixElements.times(D.times(matrixElements.transpose())) and
matrixElements.times(matrixElements.transpose()) equals the identity matrix.
<P>
If matrixToDecomposeElements is not symmetric, then the eigenvalue matrix D is block diagonal
with the real eigenvalues in 1-by-1 blocks and any complex eigenvalues,
lambda + i*mu, in 2-by-2 blocks, [lambda, mu; -mu, lambda].  The
columns of matrixElements represent the eigenvectors in the sense that matrixToDecomposeElements*matrixElements = matrixElements*D,
i.e. matrixToDecomposeElements.times(matrixElements) equals matrixElements.times(D).  The matrix matrixElements may be badly
conditioned, or even singular, so the validity of the equation
matrixToDecomposeElements = matrixElements*D*inverse(matrixElements) depends upon matrixElements.cond().
 **/
public class TridiagonalEignevalueDecomposition implements java.io.Serializable, EigenvalueDecomposition
{
	/** Arrays for internal storage of eigenvalues.
	@serial internal storage of eigenvalues.
	 */
	private List<RealNumber> realEigenvalues,  imaginaryEigenvalues;

	/** Array for internal storage of eigenvectors.
	@serial internal storage of eigenvectors.
	 */
	private RealMatrix matrix;


	/** Check for symmetry, then construct the eigenvalue decomposition
	@param matrixToDecomposeElements    Square matrix
	@return     Structure to access D and matrixElements.
	 */
	public TridiagonalEignevalueDecomposition(RealMatrix matrixToDecompose)
	{
		if(!matrixToDecompose.isSymmetric())
			throw new IllegalArgumentException("matrixToDecompose must be symmetric");

		double[][] matrixToDecomposeElements = matrixToDecompose.toDoubleArray();
		int n = matrixToDecompose.getWidth();
		double[][] matrixElements = new double[n][n];

		this.realEigenvalues = new ArrayList<RealNumber>(n);
		this.realEigenvalues.addAll(Collections.nCopies(n, new RealNumber(0.0)));
		this.imaginaryEigenvalues = new ArrayList<RealNumber>(n);
		this.imaginaryEigenvalues.addAll(Collections.nCopies(n, new RealNumber(0.0)));

		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				matrixElements[i][j] = matrixToDecomposeElements[i][j];
		this.matrix = new SimpleRealMatrix(matrixElements);

		// Tridiagonalize.
		householderTridiagonalReduction();

		// Diagonalize.
		qlTridiagonalReduction();
	}

	public int getDimensionSize()
	{
		return this.matrix.getHeight();
	}

	// Symmetric Householder reduction to tridiagonal form.
	private void householderTridiagonalReduction()
	{
		int n = this.getDimensionSize();
		double[] d = new double[this.realEigenvalues.size()];
		for(int valueIndex = 0; valueIndex < d.length; valueIndex++)
			d[valueIndex] = this.realEigenvalues.get(valueIndex).getValue();
		double[] e = new double[this.imaginaryEigenvalues.size()];
		for(int valueIndex = 0; valueIndex < d.length; valueIndex++)
			e[valueIndex] = this.imaginaryEigenvalues.get(valueIndex).getValue();
		double[][] V = this.matrix.toDoubleArray();


		//  This is derived from the Algol procedures householderTridiagonalReduction by
		//  Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
		//  Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
		//  Fortran subroutine in EISPACK.

		for(int j = 0; j < n; j++)
			d[j] = V[n - 1][j];

		// Householder reduction to tridiagonal form.

		for(int i = n - 1; i > 0; i--)
		{

			// Scale to avoid under/overflow.

			double scale = 0.0;
			double h = 0.0;
			for(int k = 0; k < i; k++)
				scale = scale + Math.abs(d[k]);
			if(scale == 0.0)
			{
				e[i] = d[i - 1];
				for(int j = 0; j < i; j++)
				{
					d[j] = V[i - 1][j];
					V[i][j] = 0.0;
					V[j][i] = 0.0;
				}
			}
			else
			{

				// Generate Householder vector.

				for(int k = 0; k < i; k++)
				{
					d[k] /= scale;
					h += d[k] * d[k];
				}
				double f = d[i - 1];
				double g = Math.sqrt(h);
				if(f > 0)
					g = -g;
				e[i] = scale * g;
				h = h - f * g;
				d[i - 1] = f - g;
				for(int j = 0; j < i; j++)
					e[j] = 0.0;

				// Apply similarity transformation to remaining columns.

				for(int j = 0; j < i; j++)
				{
					f = d[j];
					V[j][i] = f;
					g = e[j] + V[j][j] * f;
					for(int k = j + 1; k <= i - 1; k++)
					{
						g += V[k][j] * d[k];
						e[k] += V[k][j] * f;
					}
					e[j] = g;
				}
				f = 0.0;
				for(int j = 0; j < i; j++)
				{
					e[j] /= h;
					f += e[j] * d[j];
				}
				double hh = f / (h + h);
				for(int j = 0; j < i; j++)
					e[j] -= hh * d[j];
				for(int j = 0; j < i; j++)
				{
					f = d[j];
					g = e[j];
					for(int k = j; k <= i - 1; k++)
						V[k][j] -= (f * e[k] + g * d[k]);
					d[j] = V[i - 1][j];
					V[i][j] = 0.0;
				}
			}
			d[i] = h;
		}

		// Accumulate transformations.

		for(int i = 0; i < n - 1; i++)
		{
			V[n - 1][i] = V[i][i];
			V[i][i] = 1.0;
			double h = d[i + 1];
			if(h != 0.0)
			{
				for(int k = 0; k <= i; k++)
					d[k] = V[k][i + 1] / h;
				for(int j = 0; j <= i; j++)
				{
					double g = 0.0;
					for(int k = 0; k <= i; k++)
						g += V[k][i + 1] * V[k][j];
					for(int k = 0; k <= i; k++)
						V[k][j] -= g * d[k];
				}
			}
			for(int k = 0; k <= i; k++)
				V[k][i + 1] = 0.0;
		}
		for(int j = 0; j < n; j++)
		{
			d[j] = V[n - 1][j];
			V[n - 1][j] = 0.0;
		}
		V[n - 1][n - 1] = 1.0;
		e[0] = 0.0;

		this.realEigenvalues = new ArrayList<RealNumber>(d.length);
		for(double realValue : d)
			this.realEigenvalues.add(new RealNumber(realValue));
		this.imaginaryEigenvalues = new ArrayList<RealNumber>(e.length);
		for(double imaginaryValue : e)
			this.imaginaryEigenvalues.add(new RealNumber(imaginaryValue));
		this.matrix = new SimpleRealMatrix(V);
	}

	// Symmetric tridiagonal QL algorithm.
	private void qlTridiagonalReduction()
	{

		//  This is derived from the Algol procedures qlTridiagonalReduction, by
		//  Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
		//  Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
		//  Fortran subroutine in EISPACK.

		int n = this.getDimensionSize();
		double[] d = new double[this.realEigenvalues.size()];
		for(int valueIndex = 0; valueIndex < d.length; valueIndex++)
			d[valueIndex] = this.realEigenvalues.get(valueIndex).getValue();
		double[] e = new double[this.imaginaryEigenvalues.size()];
		for(int valueIndex = 0; valueIndex < d.length; valueIndex++)
			e[valueIndex] = this.imaginaryEigenvalues.get(valueIndex).getValue();
		double[][] V = this.matrix.toDoubleArray();

		for(int i = 1; i < n; i++)
			e[i - 1] = e[i];
		e[n - 1] = 0.0;

		double f = 0.0;
		double tst1 = 0.0;
		double eps = Math.pow(2.0, -52.0);
		for(int l = 0; l < n; l++)
		{

			// Find small subdiagonal element

			tst1 = Math.max(tst1, Math.abs(d[l]) + Math.abs(e[l]));
			int m = l;
			while(m < n)
			{
				if(Math.abs(e[m]) <= eps * tst1)
					break;
				m++;
			}

			// If m == l, d[l] is an eigenvalue,
			// otherwise, iterate.

			if(m > l)
			{
				int iter = 0;
				do
				{
					iter = iter + 1;  // (Could check iteration count here.)

					// Compute implicit shift

					double g = d[l];
					double p = (d[l + 1] - g) / (2.0 * e[l]);
					double r = Math.hypot(p, 1.0);
					if(p < 0)
						r = -r;
					d[l] = e[l] / (p + r);
					d[l + 1] = e[l] * (p + r);
					double dl1 = d[l + 1];
					double h = g - d[l];
					for(int i = l + 2; i < n; i++)
						d[i] -= h;
					f = f + h;

					// Implicit QL transformation.

					p = d[m];
					double c = 1.0;
					double c2 = c;
					double c3 = c;
					double el1 = e[l + 1];
					double s = 0.0;
					double s2 = 0.0;
					for(int i = m - 1; i >= l; i--)
					{
						c3 = c2;
						c2 = c;
						s2 = s;
						g = c * e[i];
						h = c * p;
						r = Math.hypot(p, e[i]);
						e[i + 1] = s * r;
						s = e[i] / r;
						c = p / r;
						p = c * d[i] - s * g;
						d[i + 1] = h + s * (c * g + s * d[i]);

						// Accumulate transformation.

						for(int k = 0; k < n; k++)
						{
							h = V[k][i + 1];
							V[k][i + 1] = s * V[k][i] + c * h;
							V[k][i] = c * V[k][i] - s * h;
						}
					}
					p = -s * s2 * c3 * el1 * e[l] / dl1;
					e[l] = s * p;
					d[l] = c * p;

				// Check for convergence.

				}
				while(Math.abs(e[l]) > eps * tst1);
			}
			d[l] = d[l] + f;
			e[l] = 0.0;
		}

		// Sort eigenvalues and corresponding vectors.

		for(int i = 0; i < n - 1; i++)
		{
			int k = i;
			double p = d[i];
			for(int j = i + 1; j < n; j++)
				if(d[j] < p)
				{
					k = j;
					p = d[j];
				}
			if(k != i)
			{
				d[k] = d[i];
				d[i] = p;
				for(int j = 0; j < n; j++)
				{
					p = V[j][i];
					V[j][i] = V[j][k];
					V[j][k] = p;
				}
			}
		}

		this.realEigenvalues = new ArrayList<RealNumber>(d.length);
		for(double realValue : d)
			this.realEigenvalues.add(new RealNumber(realValue));
		this.imaginaryEigenvalues = new ArrayList<RealNumber>(e.length);
		for(double imaginaryValue : e)
			this.imaginaryEigenvalues.add(new RealNumber(imaginaryValue));
		this.matrix = new SimpleRealMatrix(V);
	}

	/** Return the eigenvector matrix
	@return     matrixElements
	 */
	public RealMatrix getMatrix()
	{
		return this.matrix;
	}

	/** Return the real parts of the eigenvalues
	@return     real(diag(D))
	 */
	public List<RealNumber> getRealEigenvalues()
	{
		return Collections.unmodifiableList(this.realEigenvalues);
	}

	/** Return the imaginary parts of the eigenvalues
	@return     imag(diag(D))
	 */
	public List<RealNumber> getImaginaryEigenvalues()
	{
		return Collections.unmodifiableList(this.imaginaryEigenvalues);
	}

	/** Return the block diagonal eigenvalue matrix
	@return     D
	 */
	public RealMatrix getBlockDiagonalMatrix()
	{
		int n = this.getDimensionSize();
		double[] d = new double[this.realEigenvalues.size()];
		for(int valueIndex = 0; valueIndex < d.length; valueIndex++)
			d[valueIndex] = this.realEigenvalues.get(valueIndex).getValue();
		double[] e = new double[this.imaginaryEigenvalues.size()];
		for(int valueIndex = 0; valueIndex < d.length; valueIndex++)
			e[valueIndex] = this.imaginaryEigenvalues.get(valueIndex).getValue();

//		Matrix X = new Matrix(n, n);
//		double[][] D = X.getArray();
		double[][] D = new double[n][n];
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
				D[i][j] = 0.0;
			D[i][i] = d[i];
			if(e[i] > 0)
				D[i][i + 1] = e[i];
			else if(e[i] < 0)
				D[i][i - 1] = e[i];
		}
		return new SimpleRealMatrix(D);
	}
}
