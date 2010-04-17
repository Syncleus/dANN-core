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
import com.syncleus.dann.math.linear.RealMatrix;
import com.syncleus.dann.math.linear.SimpleRealMatrix;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchurEigenvalueDecomposition implements java.io.Serializable, EigenvalueDecomposition
{
	/** Arrays for internal storage of eigenvalues.
	@serial internal storage of eigenvalues.
	 */
	private List<RealNumber> realEigenvalues,  imaginaryEigenvalues;

	/** Array for internal storage of eigenvectors.
	@serial internal storage of eigenvectors.
	 */
	private RealMatrix matrix;

	/** Array for internal storage of nonsymmetric Hessenberg form.
	@serial internal storage of nonsymmetric Hessenberg form.
	 */
	private RealMatrix hessenbergMatrix;

	// Complex scalar division.
	private transient RealNumber cdivr,  cdivi;


	/** Check for symmetry, then construct the eigenvalue decomposition
	@param matrixToDecomposeElements    Square matrix
	@return     Structure to access D and matrixElements.
	 */
	public SchurEigenvalueDecomposition(RealMatrix matrixToDecompose)
	{
		double[][] matrixToDecomposeElements = matrixToDecompose.toDoubleArray();
		int n = matrixToDecompose.getWidth();
		double[][] matrixElements = new double[n][n];
		double[][] hessenbergMatrixElements = new double[n][n];

		this.realEigenvalues = new ArrayList<RealNumber>(n);
		this.realEigenvalues.addAll(Collections.nCopies(n, new RealNumber(0.0)));
		this.imaginaryEigenvalues = new ArrayList<RealNumber>(n);
		this.imaginaryEigenvalues.addAll(Collections.nCopies(n, new RealNumber(0.0)));

		for(int j = 0; j < n; j++)
			for(int i = 0; i < n; i++)
				hessenbergMatrixElements[i][j] = matrixToDecomposeElements[i][j];
		this.hessenbergMatrix = new SimpleRealMatrix(hessenbergMatrixElements);
		this.matrix = new SimpleRealMatrix(matrixElements);

		HessenbergDecomposition hessenberbDecomposition = new NonsymetricHessenbergReduction(this.hessenbergMatrix);
		this.matrix = hessenberbDecomposition.getMatrix();
		this.hessenbergMatrix = hessenberbDecomposition.getHessenbergMatrix();

		// Reduce Hessenberg to real Schur form.
		schurReduction();
	}

	final public int getDimensionSize()
	{
		return this.matrix.getHeight();
	}

	private void cdiv(double xr, double xi, double yr, double yi)
	{
		double r, d;
		if(Math.abs(yr) > Math.abs(yi))
		{
			r = yi / yr;
			d = yr + r * yi;
			cdivr = new RealNumber((xr + r * xi) / d);
			cdivi = new RealNumber((xi - r * xr) / d);
		}
		else
		{
			r = yr / yi;
			d = yi + r * yr;
			cdivr = new RealNumber((r * xr + xi) / d);
			cdivi = new RealNumber((r * xi - xr) / d);
		}
	}


	// Nonsymmetric reduction from Hessenberg to real Schur form.
	private void schurReduction()
	{
		int n = this.getDimensionSize();
		double[] d = new double[this.realEigenvalues.size()];
		for(int valueIndex = 0; valueIndex < d.length; valueIndex++)
			d[valueIndex] = this.realEigenvalues.get(valueIndex).getValue();
		double[] e = new double[this.imaginaryEigenvalues.size()];
		for(int valueIndex = 0; valueIndex < d.length; valueIndex++)
			e[valueIndex] = this.imaginaryEigenvalues.get(valueIndex).getValue();
		double[][] V = this.matrix.toDoubleArray();
		double[][] H = this.hessenbergMatrix.toDoubleArray();

		//  This is derived from the Algol procedure schurReduction,
		//  by Martin and Wilkinson, Handbook for Auto. Comp.,
		//  Vol.ii-Linear Algebra, and the corresponding
		//  Fortran subroutine in EISPACK.

		// Initialize
		int nn = n;
		n = nn - 1;
		int low = 0;
		int high = nn - 1;
		double eps = Math.pow(2.0, -52.0);
		double exshift = 0.0;
		double p = 0, q = 0, r = 0, s = 0, z = 0, t, w, x, y;

		// Store roots isolated by balanc and compute matrix norm
		double norm = 0.0;
		for(int i = 0; i < nn; i++)
		{
			if(i < low | i > high)
			{
				d[i] = H[i][i];
				e[i] = 0.0;
			}
			for(int j = Math.max(i - 1, 0); j < nn; j++)
				norm = norm + Math.abs(H[i][j]);
		}

		// Outer loop over eigenvalue index
		int iter = 0;
		while(n >= low)
		{

			// Look for single small sub-diagonal element
			int l = n;
			while(l > low)
			{
				s = Math.abs(H[l - 1][l - 1]) + Math.abs(H[l][l]);
				if(s == 0.0)
					s = norm;
				if(Math.abs(H[l][l - 1]) < eps * s)
					break;
				l--;
			}

			// Check for convergence
			// One root found
			if(l == n)
			{
				H[n][n] = H[n][n] + exshift;
				d[n] = H[n][n];
				e[n] = 0.0;
				n--;
				iter = 0;

			// Two roots found
			}
			else if(l == n - 1)
			{
				w = H[n][n - 1] * H[n - 1][n];
				p = (H[n - 1][n - 1] - H[n][n]) / 2.0;
				q = p * p + w;
				z = Math.sqrt(Math.abs(q));
				H[n][n] = H[n][n] + exshift;
				H[n - 1][n - 1] = H[n - 1][n - 1] + exshift;
				x = H[n][n];

				// Real pair
				if(q >= 0)
				{
					if(p >= 0)
						z = p + z;
					else
						z = p - z;
					d[n - 1] = x + z;
					d[n] = d[n - 1];
					if(z != 0.0)
						d[n] = x - w / z;
					e[n - 1] = 0.0;
					e[n] = 0.0;
					x = H[n][n - 1];
					s = Math.abs(x) + Math.abs(z);
					p = x / s;
					q = z / s;
					r = Math.sqrt(p * p + q * q);
					p = p / r;
					q = q / r;

					// Row modification
					for(int j = n - 1; j < nn; j++)
					{
						z = H[n - 1][j];
						H[n - 1][j] = q * z + p * H[n][j];
						H[n][j] = q * H[n][j] - p * z;
					}

					// Column modification
					for(int i = 0; i <= n; i++)
					{
						z = H[i][n - 1];
						H[i][n - 1] = q * z + p * H[i][n];
						H[i][n] = q * H[i][n] - p * z;
					}

					// Accumulate transformations
					for(int i = low; i <= high; i++)
					{
						z = V[i][n - 1];
						V[i][n - 1] = q * z + p * V[i][n];
						V[i][n] = q * V[i][n] - p * z;
					}

				// Complex pair
				}
				else
				{
					d[n - 1] = x + p;
					d[n] = x + p;
					e[n - 1] = z;
					e[n] = -z;
				}
				n = n - 2;
				iter = 0;

			// No convergence yet
			}
			else
			{
				// Form shift
				x = H[n][n];
				y = 0.0;
				w = 0.0;
				if(l < n)
				{
					y = H[n - 1][n - 1];
					w = H[n][n - 1] * H[n - 1][n];
				}

				// Wilkinson's original ad hoc shift

				if(iter == 10)
				{
					exshift += x;
					for(int i = low; i <= n; i++)
						H[i][i] -= x;
					s = Math.abs(H[n][n - 1]) + Math.abs(H[n - 1][n - 2]);
					x = y = 0.75 * s;
					w = -0.4375 * s * s;
				}

				// MATLAB's new ad hoc shift

				if(iter == 30)
				{
					s = (y - x) / 2.0;
					s = s * s + w;
					if(s > 0)
					{
						s = Math.sqrt(s);
						if(y < x)
							s = -s;
						s = x - w / ((y - x) / 2.0 + s);
						for(int i = low; i <= n; i++)
							H[i][i] -= s;
						exshift += s;
						x = y = w = 0.964;
					}
				}

				iter = iter + 1;   // (Could check iteration count here.)

				// Look for two consecutive small sub-diagonal elements
				int m = n - 2;
				while(m >= l)
				{
					z = H[m][m];
					r = x - z;
					s = y - z;
					p = (r * s - w) / H[m + 1][m] + H[m][m + 1];
					q = H[m + 1][m + 1] - z - r - s;
					r = H[m + 2][m + 1];
					s = Math.abs(p) + Math.abs(q) + Math.abs(r);
					p = p / s;
					q = q / s;
					r = r / s;
					if(m == l)
						break;
					if(Math.abs(H[m][m - 1]) * (Math.abs(q) + Math.abs(r)) <
					   eps * (Math.abs(p) * (Math.abs(H[m - 1][m - 1]) + Math.abs(z) +
											 Math.abs(H[m + 1][m + 1]))))
						break;
					m--;
				}

				for(int i = m + 2; i <= n; i++)
				{
					H[i][i - 2] = 0.0;
					if(i > m + 2)
						H[i][i - 3] = 0.0;
				}

				// Double QR step involving rows l:n and columns m:n
				for(int k = m; k <= n - 1; k++)
				{
					boolean notlast = (k != n - 1);
					if(k != m)
					{
						p = H[k][k - 1];
						q = H[k + 1][k - 1];
						r = (notlast ? H[k + 2][k - 1] : 0.0);
						x = Math.abs(p) + Math.abs(q) + Math.abs(r);
						if(x != 0.0)
						{
							p = p / x;
							q = q / x;
							r = r / x;
						}
					}
					if(x == 0.0)
						break;
					s = Math.sqrt(p * p + q * q + r * r);
					if(p < 0)
						s = -s;
					if(s != 0)
					{
						if(k != m)
							H[k][k - 1] = -s * x;
						else if(l != m)
							H[k][k - 1] = -H[k][k - 1];
						p = p + s;
						x = p / s;
						y = q / s;
						z = r / s;
						q = q / p;
						r = r / p;

						// Row modification
						for(int j = k; j < nn; j++)
						{
							p = H[k][j] + q * H[k + 1][j];
							if(notlast)
							{
								p = p + r * H[k + 2][j];
								H[k + 2][j] = H[k + 2][j] - p * z;
							}
							H[k][j] = H[k][j] - p * x;
							H[k + 1][j] = H[k + 1][j] - p * y;
						}

						// Column modification
						for(int i = 0; i <= Math.min(n, k + 3); i++)
						{
							p = x * H[i][k] + y * H[i][k + 1];
							if(notlast)
							{
								p = p + z * H[i][k + 2];
								H[i][k + 2] = H[i][k + 2] - p * r;
							}
							H[i][k] = H[i][k] - p;
							H[i][k + 1] = H[i][k + 1] - p * q;
						}

						// Accumulate transformations
						for(int i = low; i <= high; i++)
						{
							p = x * V[i][k] + y * V[i][k + 1];
							if(notlast)
							{
								p = p + z * V[i][k + 2];
								V[i][k + 2] = V[i][k + 2] - p * r;
							}
							V[i][k] = V[i][k] - p;
							V[i][k + 1] = V[i][k + 1] - p * q;
						}
					}  // (s != 0)
				}  // k loop
			}  // check convergence
		}  // while (n >= low)

		// Backsubstitute to find vectors of upper triangular form
		if(norm == 0.0)
			return;

		for(n = nn - 1; n >= 0; n--)
		{
			p = d[n];
			q = e[n];

			// Real vector
			if(q == 0)
			{
				int l = n;
				H[n][n] = 1.0;
				for(int i = n - 1; i >= 0; i--)
				{
					w = H[i][i] - p;
					r = 0.0;
					for(int j = l; j <= n; j++)
						r = r + H[i][j] * H[j][n];
					if(e[i] < 0.0)
					{
						z = w;
						s = r;
					}
					else
					{
						l = i;
						if(e[i] == 0.0)
							if(w != 0.0)
								H[i][n] = -r / w;
							else
								H[i][n] = -r / (eps * norm);
						else
						{
							x = H[i][i + 1];
							y = H[i + 1][i];
							q = (d[i] - p) * (d[i] - p) + e[i] * e[i];
							t = (x * s - z * r) / q;
							H[i][n] = t;
							if(Math.abs(x) > Math.abs(z))
								H[i + 1][n] = (-r - w * t) / x;
							else
								H[i + 1][n] = (-s - y * t) / z;
						}

						// Overflow control
						t = Math.abs(H[i][n]);
						if((eps * t) * t > 1)
							for(int j = i; j <= n; j++)
								H[j][n] = H[j][n] / t;
					}
				}

			// Complex vector
			}
			else if(q < 0)
			{
				int l = n - 1;

				// Last vector component imaginary so matrix is triangular
				if(Math.abs(H[n][n - 1]) > Math.abs(H[n - 1][n]))
				{
					H[n - 1][n - 1] = q / H[n][n - 1];
					H[n - 1][n] = -(H[n][n] - p) / H[n][n - 1];
				}
				else
				{
					cdiv(0.0, -H[n - 1][n], H[n - 1][n - 1] - p, q);
					H[n - 1][n - 1] = cdivr.getValue();
					H[n - 1][n] = cdivi.getValue();
				}
				H[n][n - 1] = 0.0;
				H[n][n] = 1.0;
				for(int i = n - 2; i >= 0; i--)
				{
					double ra, sa, vr, vi;
					ra = 0.0;
					sa = 0.0;
					for(int j = l; j <= n; j++)
					{
						ra = ra + H[i][j] * H[j][n - 1];
						sa = sa + H[i][j] * H[j][n];
					}
					w = H[i][i] - p;

					if(e[i] < 0.0)
					{
						z = w;
						r = ra;
						s = sa;
					}
					else
					{
						l = i;
						if(e[i] == 0)
						{
							cdiv(-ra, -sa, w, q);
							H[i][n - 1] = cdivr.getValue();
							H[i][n] = cdivi.getValue();
						}
						else
						{
							// Solve complex equations
							x = H[i][i + 1];
							y = H[i + 1][i];
							vr = (d[i] - p) * (d[i] - p) + e[i] * e[i] - q * q;
							vi = (d[i] - p) * 2.0 * q;
							if(vr == 0.0 & vi == 0.0)
								vr = eps * norm * (Math.abs(w) + Math.abs(q) +
												   Math.abs(x) + Math.abs(y) + Math.abs(z));
							cdiv(x * r - z * ra + q * sa, x * s - z * sa - q * ra, vr, vi);
							H[i][n - 1] = cdivr.getValue();
							H[i][n] = cdivi.getValue();
							if(Math.abs(x) > (Math.abs(z) + Math.abs(q)))
							{
								H[i + 1][n - 1] = (-ra - w * H[i][n - 1] + q * H[i][n]) / x;
								H[i + 1][n] = (-sa - w * H[i][n] - q * H[i][n - 1]) / x;
							}
							else
							{
								cdiv(-r - y * H[i][n - 1], -s - y * H[i][n], z, q);
								H[i + 1][n - 1] = cdivr.getValue();
								H[i + 1][n] = cdivi.getValue();
							}
						}

						// Overflow control
						t = Math.max(Math.abs(H[i][n - 1]), Math.abs(H[i][n]));
						if((eps * t) * t > 1)
							for(int j = i; j <= n; j++)
							{
								H[j][n - 1] = H[j][n - 1] / t;
								H[j][n] = H[j][n] / t;
							}
					}
				}
			}
		}

		// Vectors of isolated roots
		for(int i = 0; i < nn; i++)
			if(i < low | i > high)
				for(int j = i; j < nn; j++)
					V[i][j] = H[i][j];

		// Back transformation to get eigenvectors of original matrix
		for(int j = nn - 1; j >= low; j--)
			for(int i = low; i <= high; i++)
			{
				z = 0.0;
				for(int k = low; k <= Math.min(j, high); k++)
					z = z + V[i][k] * H[k][j];
				V[i][j] = z;
			}

		this.realEigenvalues = new ArrayList<RealNumber>(d.length);
		for(double realValue : d)
			this.realEigenvalues.add(new RealNumber(realValue));
		this.imaginaryEigenvalues = new ArrayList<RealNumber>(e.length);
		for(double imaginaryValue : e)
			this.imaginaryEigenvalues.add(new RealNumber(imaginaryValue));
		this.matrix = new SimpleRealMatrix(V);
		this.hessenbergMatrix = new SimpleRealMatrix(H);
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
