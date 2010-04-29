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

import com.syncleus.dann.math.linear.RealMatrix;
import com.syncleus.dann.math.linear.SimpleRealMatrix;

public class NonsymetricHessenbergReduction implements java.io.Serializable, HessenbergDecomposition
{
	private static final long serialVersionUID = 9001289842017174236L;
	
	/** Array for internal storage of eigenvectors.
	@serial internal storage of eigenvectors.
	 */
	private RealMatrix matrix;

	/**
	 * Array for internal storage of nonsymmetric Hessenberg form.
	 * @serial internal storage of nonsymmetric Hessenberg form.
	 */
	private RealMatrix hessenbergMatrix;

	/**
	 * Check for symmetry, then construct the eigenvalue decomposition. Gives
	 * access to D and matrixElements.
	 *
	 * @param matrixToDecompose Elements Square matrix
	 */
	public NonsymetricHessenbergReduction(RealMatrix matrixToDecompose)
	{
		int n = matrixToDecompose.getWidth();

		// Reduce to Hessenberg form.
		hessenbergReduction(matrixToDecompose.getSubmatrix(0, n, 0, n));
	}

	public int getDimensionSize()
	{
		return this.matrix.getHeight();
	}

	// Nonsymmetric reduction to Hessenberg form.
	private void hessenbergReduction(RealMatrix matrixToReduce)
	{
		int n = matrixToReduce.getHeight();
		double[][] V = matrixToReduce.blank().toDoubleArray();
		double[][] H = matrixToReduce.toDoubleArray();
		double[] ort = new double[matrixToReduce.getHeight()];

		//  This is derived from the Algol procedures hessenbergReduction and ortran,
		//  by Martin and Wilkinson, Handbook for Auto. Comp.,
		//  Vol.ii-Linear Algebra, and the corresponding
		//  Fortran subroutines in EISPACK.

		int low = 0;
		int high = n - 1;

		for(int m = low + 1; m <= high - 1; m++)
		{
			// Scale column.
			double scale = 0.0;
			for(int i = m; i <= high; i++)
				scale = scale + Math.abs(H[i][m - 1]);
			if(scale != 0.0)
			{
				// Compute Householder transformation.
				double h = 0.0;
				for(int i = high; i >= m; i--)
				{
					ort[i] = H[i][m - 1] / scale;
					h += ort[i] * ort[i];
				}
				double g = Math.sqrt(h);
				if(ort[m] > 0)
					g = -g;
				h = h - ort[m] * g;
				ort[m] = ort[m] - g;

				// Apply Householder similarity transformation
				// hessenbergMatrixElements = (I-u*u'/h)*hessenbergMatrixElements*(I-u*u')/h)

				for(int j = m; j < n; j++)
				{
					double f = 0.0;
					for(int i = high; i >= m; i--)
						f += ort[i] * H[i][j];
					f = f / h;
					for(int i = m; i <= high; i++)
						H[i][j] -= f * ort[i];
				}

				for(int i = 0; i <= high; i++)
				{
					double f = 0.0;
					for(int j = high; j >= m; j--)
						f += ort[j] * H[i][j];
					f = f / h;
					for(int j = m; j <= high; j++)
						H[i][j] -= f * ort[j];
				}
				ort[m] = scale * ort[m];
				H[m][m - 1] = scale * g;
			}
		}

		// Accumulate transformations (Algol's ortran).
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				V[i][j] = (i == j ? 1.0 : 0.0);

		for(int m = high - 1; m >= low + 1; m--)
			if(H[m][m - 1] != 0.0)
			{
				for(int i = m + 1; i <= high; i++)
					ort[i] = H[i][m - 1];
				for(int j = m; j <= high; j++)
				{
					double g = 0.0;
					for(int i = m; i <= high; i++)
						g += ort[i] * V[i][j];
					// Double division avoids possible underflow
					g = (g / ort[m]) / H[m][m - 1];
					for(int i = m; i <= high; i++)
						V[i][j] += g * ort[i];
				}
			}

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

	public RealMatrix getHessenbergMatrix()
	{
		return this.hessenbergMatrix;
	}
}
