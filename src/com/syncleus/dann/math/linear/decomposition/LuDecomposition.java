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
import com.syncleus.dann.math.linear.*;

/** matrixToDecomposeElements Decomposition.
<P>
For an height-by-width matrix matrixToDecompose with height >= width, the matrixToDecomposeElements decomposition is an height-by-width
unit lower triangular matrix lowerTriangularFactor, an width-by-width upper triangular matrix U,
and a permutation vector pivot of length height so that matrixToDecompose(pivot,:) = lowerTriangularFactor*U.
If height < width, then lowerTriangularFactor is height-by-height and U is height-by-width.
<P>
The matrixToDecomposeElements decompostion with pivoting always exists, even if the matrix is
singular, so the constructor will never fail.  The primary use of the
matrixToDecomposeElements decomposition is in the solution of square systems of simultaneous
linear equations.  This will fail if isNonsingular() returns false.
 */
public interface LuDecomposition<M extends Matrix<M, F>, F extends OrderedAlgebraic<F>> extends java.io.Serializable, SolvableDecomposition<M>
{
	F getDeterminant();
	M getLowerTriangularFactor();
	M getUpperTriangularFactor();
	boolean isNonsingular();
	int[] getPivot();
}
