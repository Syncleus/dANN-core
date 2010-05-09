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
package com.syncleus.dann.math.linear.decomposition;

import com.syncleus.dann.math.OrderedAlgebraic;
import com.syncleus.dann.math.linear.Matrix;

/**
 * QR Decomposition.
 * <p/>
 * For an m-by-n matrix matrixToDecompose with m >= n, the QR decomposition is
 * an m-by-n orthogonal matrix factor and an n-by-n upper triangular matrix
 * factor so that matrixToDecompose = factor*factor.
 * <p/>
 * The QR decompostion always exists, even if the matrix does not have full
 * rank, so the constructor will never fail.  The primary use of the QR
 * decomposition is in the least squares solution of nonsquare systems of
 * simultaneous linear equations.  This will fail if isFullRank() returns
 * false.
 */
public interface QrDecomposition<M extends Matrix<M, F>, F extends OrderedAlgebraic<F>> extends SolvableDecomposition<M>
{
	M getHouseholderMatrix();
	M getOrthogonalFactor();
	M getUpperTriangularFactor();
	boolean isFullRank();
}
