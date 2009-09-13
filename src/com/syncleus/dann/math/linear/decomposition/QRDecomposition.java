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

import com.syncleus.dann.math.linear.*;

/** QR Decomposition.
<P>
For an m-by-n matrix A with m >= n, the QR decomposition is an m-by-n
orthogonal matrix Q and an n-by-n upper triangular matrix R so that
A = Q*R.
<P>
The QR decompostion always exists, even if the matrix does not have
full rank, so the constructor will never fail.  The primary use of the
QR decomposition is in the least squares solution of nonsquare systems
of simultaneous linear equations.  This will fail if isFullRank()
returns false.
 */
public class QRDecomposition implements java.io.Serializable
{

	/* ------------------------
	Class variables
	 * ------------------------ */
	/** Array for internal storage of decomposition.
	@serial internal array storage.
	 */
	private double[][] QR;
	/** Row and column dimensions.
	@serial column dimension.
	@serial row dimension.
	 */
	private int m,  n;
	/** Array for internal storage of diagonal of R.
	@serial diagonal of R.
	 */
	private double[] Rdiag;

	/* ------------------------
	Constructor
	 * ------------------------ */
	/** QR Decomposition, computed by Householder reflections.
	@param A    Rectangular matrix
	@return     Structure to access R and the Householder vectors and compute Q.
	 */
	public QRDecomposition(SimpleRealMatrix A)
	{
		// Initialize.
		QR = A.toDoubleArray();
		m = A.getHeight();
		n = A.getWidth();
		Rdiag = new double[n];

		// Main loop.
		for(int k = 0; k < n; k++)
		{
			// Compute 2-norm of k-th column without under/overflow.
			double nrm = 0;
			for(int i = k; i < m; i++)
				nrm = Math.hypot(nrm, QR[i][k]);

			if(nrm != 0.0)
			{
				// Form k-th Householder vector.
				if(QR[k][k] < 0)
					nrm = -nrm;
				for(int i = k; i < m; i++)
					QR[i][k] /= nrm;
				QR[k][k] += 1.0;

				// Apply transformation to remaining columns.
				for(int j = k + 1; j < n; j++)
				{
					double s = 0.0;
					for(int i = k; i < m; i++)
						s += QR[i][k] * QR[i][j];
					s = -s / QR[k][k];
					for(int i = k; i < m; i++)
						QR[i][j] += s * QR[i][k];
				}
			}
			Rdiag[k] = -nrm;
		}
	}

	/* ------------------------
	Public Methods
	 * ------------------------ */
	/** Is the matrix full rank?
	@return     true if R, and hence A, has full rank.
	 */
	public boolean isFullRank()
	{
		for(int j = 0; j < n; j++)
			if(Rdiag[j] == 0)
				return false;
		return true;
	}

	/** Return the Householder vectors
	@return     Lower trapezoidal matrix whose columns define the reflections
	 */
	public SimpleRealMatrix getH()
	{
//		SimpleRealMatrix X = new SimpleRealMatrix(m, n);
//		double[][] H = X.getArray();
		double[][] H = new double[m][n];
		for(int i = 0; i < m; i++)
			for(int j = 0; j < n; j++)
				if(i >= j)
					H[i][j] = QR[i][j];
				else
					H[i][j] = 0.0;
		return new SimpleRealMatrix(H);
	}

	/** Return the upper triangular factor
	@return     R
	 */
	public SimpleRealMatrix getR()
	{
//		SimpleRealMatrix X = new SimpleRealMatrix(n, n);
//		double[][] R = X.getArray();
		double[][] R = new double[n][n];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				if(i < j)
					R[i][j] = QR[i][j];
				else if(i == j)
					R[i][j] = Rdiag[i];
				else
					R[i][j] = 0.0;
		return new SimpleRealMatrix(R);
	}

	/** Generate and return the (economy-sized) orthogonal factor
	@return     Q
	 */
	public SimpleRealMatrix getQ()
	{
//		SimpleRealMatrix X = new SimpleRealMatrix(m, n);
//		double[][] Q = X.getArray();
		double[][] Q = new double[m][n];
		for(int k = n - 1; k >= 0; k--)
		{
			for(int i = 0; i < m; i++)
				Q[i][k] = 0.0;
			Q[k][k] = 1.0;
			for(int j = k; j < n; j++)
				if(QR[k][k] != 0)
				{
					double s = 0.0;
					for(int i = k; i < m; i++)
						s += QR[i][k] * Q[i][j];
					s = -s / QR[k][k];
					for(int i = k; i < m; i++)
						Q[i][j] += s * QR[i][k];
				}
		}
		return new SimpleRealMatrix(Q);
	}

	/** Least squares solution of A*X = B
	@param B    A SimpleRealMatrix with as many rows as A and any number of columns.
	@return     X that minimizes the two norm of Q*R*X-B.
	@exception  IllegalArgumentException  SimpleRealMatrix row dimensions must agree.
	@exception  RuntimeException  SimpleRealMatrix is rank deficient.
	 */
	public SimpleRealMatrix solve(SimpleRealMatrix B)
	{
		if(B.getHeight() != m)
			throw new IllegalArgumentException("Matrix row dimensions must agree.");
		if(!this.isFullRank())
			throw new RuntimeException("Matrix is rank deficient.");

		// Copy right hand side
		int nx = B.getWidth();
		double[][] X = B.toDoubleArray();

		// Compute Y = transpose(Q)*B
		for(int k = 0; k < n; k++)
			for(int j = 0; j < nx; j++)
			{
				double s = 0.0;
				for(int i = k; i < m; i++)
					s += QR[i][k] * X[i][j];
				s = -s / QR[k][k];
				for(int i = k; i < m; i++)
					X[i][j] += s * QR[i][k];
			}
		// Solve R*X = Y;
		for(int k = n - 1; k >= 0; k--)
		{
			for(int j = 0; j < nx; j++)
				X[k][j] /= Rdiag[k];
			for(int i = 0; i < k; i++)
				for(int j = 0; j < nx; j++)
					X[i][j] -= X[k][j] * QR[i][k];
		}
		return (SimpleRealMatrix) (new SimpleRealMatrix(X)).getSubmatrix(0, n - 1, 0, nx - 1);
	}
}
