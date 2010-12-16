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

import java.util.List;
import com.syncleus.dann.math.RealNumber;
import com.syncleus.dann.math.linear.RealMatrix;

/**
 * Singular Value Decomposition.
 * <p/>
 * For an m-by-n matrix A with m >= n, the singular value decomposition is an
 * m-by-n orthogonal matrix U, an n-by-n diagonal matrix S, and an n-by-n
 * orthogonal matrix V so that A = U*S*V'.
 * <p/>
 * The singular values, sigma[k] = S[k][k], are ordered so that sigma[0] >=
 * sigma[1] >= ... >= sigma[n-1].
 * <p/>
 * The singular value decompostion always exists, so the constructor will never
 * fail.  The matrix condition number and the effective numerical rank can be
 * computed from this decomposition.<br/>
 */
public interface SingularValueDecomposition extends Decomposition<RealMatrix>
{
	RealMatrix getLeftSingularMatrix();
	RealMatrix getRightSingularMatrix();
	List<RealNumber> getSingularValues();
	RealNumber norm2();
	RealNumber norm2Condition();
	int rank();
}
