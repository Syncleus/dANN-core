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

public final class Decompositions
{
	public static EigenvalueDecomposition createEigenvalueDecomposition(final RealMatrix matrixToDecompose)
	{
		if( matrixToDecompose.isSymmetric() )
			return new TridiagonalEignevalueDecomposition(matrixToDecompose);
		else
			return new SchurEigenvalueDecomposition(matrixToDecompose);
	}

	public static <M extends Matrix<M, F>, F extends OrderedAlgebraic<F>> CholeskyDecomposition<M, F> createCholeskyDecomposition(final M matrix)
	{
		return new CholeskyBanachiewiczCholeskyDecomposition<M, F>(matrix);
	}

	public static <M extends Matrix<M, F>, F extends OrderedAlgebraic<F>> LuDecomposition<M, F> createLuDecomposition(final M matrix)
	{
		return new DoolittleLuDecomposition<M, F>(matrix);
	}

	public static <M extends Matrix<M, F>, F extends OrderedAlgebraic<F>> QrDecomposition<M, F> createQrDecomposition(final M matrix)
	{
		return new HouseholderQrDecomposition<M, F>(matrix);
	}

	public static SingularValueDecomposition createSingularValueDecomposition(final RealMatrix matrix)
	{
		return new StewartSingularValueDecomposition(matrix);
	}
}
