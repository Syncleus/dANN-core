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
package com.syncleus.dann.math.linear;

import com.syncleus.dann.math.RealNumber;
import com.syncleus.dann.math.linear.decomposition.DoolittleLuDecomposition;
import com.syncleus.dann.math.linear.decomposition.HouseholderQrDecomposition;
import com.syncleus.dann.math.linear.decomposition.StewartSingularValueDecomposition;
import java.io.Serializable;

/**
The Java SimpleRealMatrix Class provides the fundamental operations of numerical
linear algebra.  Various constructors create Matrices from two dimensional
arrays of double precision floating point numbers.  Various "gets" and
"sets" provide access to submatrices and matrix elements.  Several methods 
implement basic matrix arithmetic, including matrix addition and
multiplication, matrix norms, and element-by-element array operations.
Methods for reading and printing matrices are also included.  All the
operations in this version of the SimpleRealMatrix Class involve real matrices.
Complex matrices may be handled in a future version.
<P>
Five fundamental matrix decompositions, which consist of pairs or triples
of matrices, permutation vectors, and the like, produce results in five
decomposition classes.  These decompositions are accessed by the SimpleRealMatrix
class to compute solutions of simultaneous linear equations, determinants,
inverses and other matrix functions.  The five decompositions are:
<P><UL>
<LI>Cholesky Decomposition of symmetric, positive definite matrices.
<LI>LU Decomposition of rectangular matrices.
<LI>QR Decomposition of rectangular matrices.
<LI>Singular Value Decomposition of rectangular matrices.
<LI>Eigenvalue Decomposition of both symmetric and nonsymmetric square matrices.
</UL>
 */
public class SimpleRealMatrix implements Cloneable, Serializable, RealMatrix
{
	/** Array for internal storage of elements.
	@serial internal array storage.
	 */
	private final double[][] matrixElements;
	/** Row and column dimensions.
	@serial row dimension.
	@serial column dimension.
	 */
	private int height,  width;

	/** Construct an height-by-height matrix of zeros.
	@param height    Number of rows.
	@param height    Number of colums.
	 */
	public SimpleRealMatrix(int height, int width)
	{
		this.height = height;
		this.width = width;
		matrixElements = new double[height][width];
	}

	/** Construct an height-by-height constant matrix.
	@param height    Number of rows.
	@param height    Number of colums.
	@param scalar    Fill the matrix with this scalar value.
	 */
	public SimpleRealMatrix(int height, int width, double fillValue)
	{
		this.height = height;
		this.width = width;
		matrixElements = new double[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				matrixElements[i][j] = fillValue;
	}

	/** Construct a matrix from a 2-D array.
	@param matrixElements    Two-dimensional array of doubles.
	@exception  IllegalArgumentException All rows must have the same length
	@see        #constructWithCopy
	 */
	public SimpleRealMatrix(double[][] matrixElements)
	{
		width = matrixElements[0].length;
		height = matrixElements.length;
		for(int heightIndex = 0; heightIndex < matrixElements.length; heightIndex++)
			if(matrixElements[heightIndex].length != width)
				throw new IllegalArgumentException("All rows must have the same length.");
		this.matrixElements = matrixElements.clone();
	}

	/* Construct a matrix quickly without checking arguments.
	@param matrixElements    Two-dimensional array of doubles.
	@param height    Number of rows.
	@param height    Number of colums.
	 *
	public SimpleRealMatrix(double[][] matrixElements, int height, int width)
	{
		this.matrixElements = matrixElements;
		this.height = height;
		this.width = width;
	}*/

	/** Construct a matrix from a one-dimensional packed array
	@param packedMatrixElements One-dimensional array of doubles, packed by columns (ala Fortran).
	@param height    Number of rows.
	@exception  IllegalArgumentException Array length must be a multiple of height.
	 */
	public SimpleRealMatrix(double packedMatrixElements[], int height)
	{
		this.height = height;
		width = (height != 0 ? packedMatrixElements.length / height : 0);
		if(height * width != packedMatrixElements.length)
			throw new IllegalArgumentException("Array length must be a multiple of m.");
		matrixElements = new double[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				matrixElements[i][j] = packedMatrixElements[i + j * height];
	}

	public com.syncleus.dann.math.OrderedField<RealNumber> getElementField()
	{
		return RealNumber.ZERO.getField();
	}

	public boolean isSquare()
	{
		if(this.getWidth() != this.getHeight())
			return false;
		return true;
	}

	public boolean isSymmetric()
	{
		if(!this.isSquare())
			return false;

		for(int j = 0; j < this.getWidth(); j++)
			for(int i = 0; i < this.getWidth(); i++)
				if(matrixElements[i][j] != matrixElements[j][i])
					return false;
		return true;
	}

	/** Construct a matrix from a copy of a 2-D array.
	@param matrixElements    Two-dimensional array of doubles.
	@exception  IllegalArgumentException All rows must have the same length
	 */
	public static RealMatrix constructWithCopy(double[][] matrixElements)
	{
		return new SimpleRealMatrix(matrixElements);
	}

	/** Make a deep copy of a matrix
	 */
	public RealMatrix copy()
	{
		return new SimpleRealMatrix(this.matrixElements);
	}

	/** Clone the SimpleRealMatrix object.
	 */
	@Override
	public Object clone()
	{
		return this.copy();
	}

	public RealNumber[][] toArray()
	{
		RealNumber[][] C = new RealNumber[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				C[i][j] = new RealNumber(matrixElements[i][j]);
		return C;
	}

	/** Copy the internal two-dimensional array.
	@return     Two-dimensional array copy of matrix elements.
	 */
	public double[][] toDoubleArray()
	{
		double[][] C = new double[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				C[i][j] = matrixElements[i][j];
		return C;
	}

	/** Make a one-dimensional column packed copy of the internal array.
	@return     SimpleRealMatrix elements packed in a one-dimensional array by columns.
	 */
	public double[] getColumnPackedCopy()
	{
		double[] vals = new double[height * width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				vals[i + j * height] = matrixElements[i][j];
		return vals;
	}

	/** Make a one-dimensional row packed copy of the internal array.
	@return     SimpleRealMatrix elements packed in a one-dimensional array by rows.
	 */
	public double[] getRowPackedCopy()
	{
		double[] vals = new double[height * width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				vals[i * width + j] = matrixElements[i][j];
		return vals;
	}

	/** Get row dimension.
	@return     height, the number of rows.
	 */
	public int getHeight()
	{
		return height;
	}

	/** Get column dimension.
	@return     height, the number of columns.
	 */
	public int getWidth()
	{
		return width;
	}

	/** Get a single element.
	@param heightIndex    Row index.
	@param widthIndex    Column index.
	@return     matrixElements(heightIndex,widthIndex)
	@exception  ArrayIndexOutOfBoundsException
	 */
	public double getDouble(int i, int j)
	{
		return matrixElements[i][j];
	}

	/** Get a single element.
	@param heightIndex    Row index.
	@param widthIndex    Column index.
	@return     matrixElements(heightIndex,widthIndex)
	@exception  ArrayIndexOutOfBoundsException
	 */
	public RealNumber get(int i, int j)
	{
		return new RealNumber(this.getDouble(i, j));
	}

	public RealMatrix blank()
	{
		return new SimpleRealMatrix(this.height, this.width);
	}

	public RealMatrix flip()
	{
		double[][] flippedSolution = new double[this.getWidth()][this.getHeight()];
		for(int heightIndex = 0; heightIndex < this.getHeight(); heightIndex++)
			for(int widthIndex = 0; widthIndex < this.getWidth(); widthIndex++)
				flippedSolution[widthIndex][heightIndex] = this.matrixElements[heightIndex][widthIndex];
		return new SimpleRealMatrix(flippedSolution);
	}

	/** Get a submatrix.
	@param heightStart   Initial row index
	@param heightEnd   Final row index
	@param widthStart   Initial column index
	@param widthEnd   Final column index
	@return     matrixElements(heightStart:heightEnd,widthStart:widthEnd)
	@exception  ArrayIndexOutOfBoundsException Submatrix indices
	 */
	public RealMatrix getSubmatrix(int heightStart, int heightEnd, int widthStart, int widthEnd)
	{
		double[][] subMatrix = new double[heightEnd - heightStart + 1][widthEnd - widthStart + 1];
		for(int heightIndex = heightStart; heightIndex <= heightEnd; heightIndex++)
			for(int widthIndex = widthStart; widthIndex <= widthEnd; widthIndex++)
				subMatrix[heightIndex - heightStart][widthIndex - widthStart] = matrixElements[heightIndex][widthIndex];
		return new SimpleRealMatrix(subMatrix);
	}

	/** Get a submatrix.
	@param heightIndexes    Array of row indices.
	@param widthIndexes    Array of column indices.
	@return     matrixElements(heightIndexes(:),widthIndexes(:))
	@exception  ArrayIndexOutOfBoundsException Submatrix indices
	 */
	public RealMatrix getSubmatrix(int[] heightIndexes, int[] widthIndexes)
	{
		//SimpleRealMatrix newSimpleMatrix = new SimpleRealMatrix(heightIndexes.length, widthIndexes.length);
		//double[][] newMatrix = newSimpleMatrix.getArray();
		double[][] newMatrix = new double[heightIndexes.length][widthIndexes.length];

		for(int heightIndex = 0; heightIndex < heightIndexes.length; heightIndex++)
			for(int widthIndex = 0; widthIndex < widthIndexes.length; widthIndex++)
				newMatrix[heightIndex][widthIndex] = matrixElements[heightIndexes[heightIndex]][widthIndexes[widthIndex]];

		return new SimpleRealMatrix(newMatrix);
	}

	/** Get a submatrix.
	@param heightStart   Initial row index
	@param heightEnd   Final row index
	@param widthIndexes    Array of column indices.
	@return     matrixElements(heightStart:heightEnd,widthIndexes(:))
	@exception  ArrayIndexOutOfBoundsException Submatrix indices
	 */
	public RealMatrix getSubmatrix(int heightStart, int heightEnd, int[] widthIndexes)
	{
//		SimpleRealMatrix newSimpleMatrix = new SimpleRealMatrix(heightEnd - heightStart + 1, widthIndexes.length);
//		double[][] newMatrix = newSimpleMatrix.getArray();
		double[][] newMatrix = new double[heightEnd - heightStart + 1][widthIndexes.length];

		for(int heightIndex = heightStart; heightIndex <= heightEnd; heightIndex++)
			for(int widthIndex = 0; widthIndex < widthIndexes.length; widthIndex++)
				newMatrix[heightIndex - heightStart][widthIndex] = matrixElements[heightIndex][widthIndexes[widthIndex]];

		return new SimpleRealMatrix(newMatrix);
	}

	/** Get a submatrix.
	@param heightIndexes    Array of row indices.
	@param heightStart   Initial column index
	@param heightEnd   Final column index
	@return     matrixElements(heightIndexes(:),widthStart:widthEnd)
	@exception  ArrayIndexOutOfBoundsException Submatrix indices
	 */
	public RealMatrix getSubmatrix(int[] heightIndexes, int widthStart, int widthEnd)
	{
//		SimpleRealMatrix newSimpleMatrix = new SimpleRealMatrix(heightIndexes.length, widthEnd - widthStart + 1);
//		double[][] newMatrix = newSimpleMatrix.getArray();
		double[][] newMatrix = new double[heightIndexes.length][widthEnd - widthStart + 1];

		for(int heightIndex = 0; heightIndex < heightIndexes.length; heightIndex++)
			for(int widthIndex = widthStart; widthIndex <= widthEnd; widthIndex++)
				newMatrix[heightIndex][widthIndex - widthStart] = matrixElements[heightIndexes[heightIndex]][widthIndex];

		return new SimpleRealMatrix(newMatrix);
	}

	/** Set a single element.
	@param heightIndex    Row index.
	@param widthIndex    Column index.
	@param scalar    matrixElements(heightIndex,widthIndex).
	@exception  ArrayIndexOutOfBoundsException
	 */
	public RealMatrix set(int i, int j, RealNumber s)
	{
		double[][] copy = this.toDoubleArray();
		copy[i][j] = s.getValue();
		return new SimpleRealMatrix(copy);
	}

	/** Set a submatrix.
	@param heightStart   Initial row index
	@param heightEnd   Final row index
	@param widthStart   Initial column index
	@param widthEnd   Final column index
	@param resultMatrix    matrixElements(heightStart:heightEnd,widthStart:widthEnd)
	@exception  ArrayIndexOutOfBoundsException Submatrix indices
	 */
	public void setMatrix(int i0, int i1, int j0, int j1, SimpleRealMatrix X)
	{
		try
		{
			for(int i = i0; i <= i1; i++)
				for(int j = j0; j <= j1; j++)
					matrixElements[i][j] = X.getDouble(i - i0, j - j0);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
	}

	/** Set a submatrix.
	@param heightIndexes    Array of row indices.
	@param widthIndexes    Array of column indices.
	@param resultMatrix    matrixElements(heightIndexes(:),widthIndexes(:))
	@exception  ArrayIndexOutOfBoundsException Submatrix indices
	 */
	public void setMatrix(int[] r, int[] c, SimpleRealMatrix X)
	{
		try
		{
			for(int i = 0; i < r.length; i++)
				for(int j = 0; j < c.length; j++)
					matrixElements[r[i]][c[j]] = X.getDouble(i, j);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
	}

	/** Set a submatrix.
	@param heightIndexes    Array of row indices.
	@param widthStart   Initial column index
	@param widthEnd   Final column index
	@param resultMatrix    matrixElements(heightIndexes(:),widthStart:widthEnd)
	@exception  ArrayIndexOutOfBoundsException Submatrix indices
	 */
	public void setMatrix(int[] r, int j0, int j1, SimpleRealMatrix X)
	{
		try
		{
			for(int i = 0; i < r.length; i++)
				for(int j = j0; j <= j1; j++)
					matrixElements[r[i]][j] = X.getDouble(i, j - j0);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
	}

	/** Set a submatrix.
	@param heightStart   Initial row index
	@param heightEnd   Final row index
	@param widthIndexes    Array of column indices.
	@param resultMatrix    matrixElements(heightStart:heightEnd,widthIndexes(:))
	@exception  ArrayIndexOutOfBoundsException Submatrix indices
	 */
	public void setMatrix(int i0, int i1, int[] c, SimpleRealMatrix X)
	{
		try
		{
			for(int i = i0; i <= i1; i++)
				for(int j = 0; j < c.length; j++)
					matrixElements[i][c[j]] = X.getDouble(i - i0, j);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
	}

	/** SimpleRealMatrix transpose.
	@return    matrixElements'
	 */
	public RealMatrix transpose()
	{
//		SimpleRealMatrix newSimpleMatrix = new SimpleRealMatrix(width, height);
//		double[][] transposedMatrix = newSimpleMatrix.getArray();
		double[][] transposedMatrix = new double[width][height];
		for(int heightIndex = 0; heightIndex < height; heightIndex++)
			for(int widthIndex = 0; widthIndex < width; widthIndex++)
				transposedMatrix[widthIndex][heightIndex] = matrixElements[heightIndex][widthIndex];
		return new SimpleRealMatrix(transposedMatrix);
	}

	/** One norm
	@return    maximum column sum.
	 */
	public double norm1Double()
	{
		double f = 0;
		for(int j = 0; j < width; j++)
		{
			double s = 0;
			for(int i = 0; i < height; i++)
				s += Math.abs(matrixElements[i][j]);
			f = Math.max(f, s);
		}
		return f;
	}

	public RealNumber norm1()
	{
		return new RealNumber(this.norm1Double());
	}

	/** Two norm
	@return    maximum singular value.
	 */
	public double norm2Double()
	{
		return (new StewartSingularValueDecomposition(this).norm2Double());
	}

	public RealNumber norm2()
	{
		return new RealNumber(this.norm2Double());
	}

	/** Infinity norm
	@return    maximum row sum.
	 */
	public double normInfiniteDouble()
	{
		double f = 0;
		for(int i = 0; i < height; i++)
		{
			double s = 0;
			for(int j = 0; j < width; j++)
				s += Math.abs(matrixElements[i][j]);
			f = Math.max(f, s);
		}
		return f;
	}

	public RealNumber normInfinite()
	{
		return new RealNumber(this.normInfiniteDouble());
	}

	/** Frobenius norm
	@return    sqrt of sum of squares of all elements.
	 */
	public double normF()
	{
		double f = 0;
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				f = Math.hypot(f, matrixElements[i][j]);
		return f;
	}

	/**  Unary subtract
	@return    -matrixElements
	 */
	public RealMatrix negate()
	{
		//SimpleRealMatrix newSimpleMatrix = new SimpleRealMatrix(height, width);
		//double[][] negatedMatrix = newSimpleMatrix.getArray();
		double[][] negatedMatrix = new double[height][width];
		for(int heightIndex = 0; heightIndex < height; heightIndex++)
			for(int widthIndex = 0; widthIndex < width; widthIndex++)
				negatedMatrix[heightIndex][widthIndex] = -matrixElements[heightIndex][widthIndex];
		return new SimpleRealMatrix(negatedMatrix);
	}

	/** resultArray = matrixElements + operand
	@param operand    another matrix
	@return     matrixElements + operand
	 */
	public RealMatrix add(RealMatrix operand)
	{
		checkMatrixDimensions(operand);

//		SimpleRealMatrix resultMatrix = new SimpleRealMatrix(height, width);
//		double[][] resultArray = resultMatrix.getArray();
		double[][] resultArray = new double[height][width];
		for(int heightIndex = 0; heightIndex < height; heightIndex++)
			for(int widthIndex = 0; widthIndex < width; widthIndex++)
				resultArray[heightIndex][widthIndex] = matrixElements[heightIndex][widthIndex] + operand.getDouble(heightIndex, widthIndex);
		return new SimpleRealMatrix(resultArray);
	}

	/** scalar addition resultArray = matrixElements + b
	@param operand    another matrix
	@return     matrixElements + operand
	 */
	public RealMatrix add(RealNumber B)
	{
		SimpleRealMatrix newSimpleMatrix = new SimpleRealMatrix(height, width);
		double[][] newMatrix = newSimpleMatrix.matrixElements;
		for(int heightIndex = 0; heightIndex < height; heightIndex++)
			for(int widthIndex = 0; widthIndex < width; widthIndex++)
				newMatrix[heightIndex][widthIndex] = matrixElements[heightIndex][widthIndex] + B.getValue();
		return newSimpleMatrix;
	}

	/** scalar addition resultArray = matrixElements + b
	@param operand    another matrix
	@return     matrixElements + operand
	 */
	public RealMatrix add(double scalar)
	{
		SimpleRealMatrix newMatrix = new SimpleRealMatrix(height, width);
		double[][] newMatrixElements = newMatrix.matrixElements;
		for(int heightIndex = 0; heightIndex < height; heightIndex++)
			for(int widthIndex = 0; widthIndex < width; widthIndex++)
				newMatrixElements[heightIndex][widthIndex] = matrixElements[heightIndex][widthIndex] + scalar;
		return newMatrix;
	}

	/** matrixElements = matrixElements + operand
	@param operand    another matrix
	@return     matrixElements + operand
	 */
	public RealMatrix addEquals(RealMatrix operand)
	{
		checkMatrixDimensions(operand);

		double[][] newMatrixElements = this.matrixElements.clone();

		for(int heightIndex = 0; heightIndex < height; heightIndex++)
			for(int widthIndex = 0; widthIndex < width; widthIndex++)
				newMatrixElements[heightIndex][widthIndex] = newMatrixElements[heightIndex][widthIndex] + operand.getDouble(heightIndex,widthIndex);
		return new SimpleRealMatrix(newMatrixElements);
	}

	/** resultArray = matrixElements - operand
	@param operand    another matrix
	@return     matrixElements - operand
	 */
	public RealMatrix subtract(RealMatrix operand)
	{
		checkMatrixDimensions(operand);

		SimpleRealMatrix newMatrix = new SimpleRealMatrix(height, width);
		double[][] newMatrixElements = newMatrix.matrixElements;
		for(int heightIndex = 0; heightIndex < height; heightIndex++)
			for(int widthIndex = 0; widthIndex < width; widthIndex++)
				newMatrixElements[heightIndex][widthIndex] = matrixElements[heightIndex][widthIndex] - operand.getDouble(heightIndex,widthIndex);
		return newMatrix;
	}

	public RealMatrix subtract(RealNumber scalar)
	{
		return this.add(-1.0 * scalar.getValue());
	}

	public RealMatrix subtract(double scalar)
	{
		return this.add(-1.0 * scalar);
	}

	/** matrixElements = matrixElements - operand
	@param operand    another matrix
	@return     matrixElements - operand
	 */
	public RealMatrix subtractEquals(RealMatrix operand)
	{
		checkMatrixDimensions(operand);
		SimpleRealMatrix newMatrix = new SimpleRealMatrix(height, width);
		double[][] newMatrixElements = newMatrix.matrixElements;
		for(int heightIndex = 0; heightIndex < height; heightIndex++)
			for(int widthIndex = 0; widthIndex < width; widthIndex++)
				newMatrixElements[heightIndex][widthIndex] = matrixElements[heightIndex][widthIndex] - operand.getDouble(heightIndex,widthIndex);
		return newMatrix;
	}

	/** Element-by-element multiplication, resultArray = matrixElements.*operand
	@param operand    another matrix
	@return     matrixElements.*operand
	 */
	public RealMatrix arrayTimes(RealMatrix operand)
	{
		checkMatrixDimensions(operand);

		SimpleRealMatrix newMatrix = new SimpleRealMatrix(height, width);
		double[][] newMatrixElements = newMatrix.matrixElements;
		for(int heightIndex = 0; heightIndex < height; heightIndex++)
			for(int widthIndex = 0; widthIndex < width; widthIndex++)
				newMatrixElements[heightIndex][widthIndex] = matrixElements[heightIndex][widthIndex] * operand.getDouble(heightIndex,widthIndex);
		return newMatrix;
	}

	/** Element-by-element multiplication in place, matrixElements = matrixElements.*operand
	@param operand    another matrix
	@return     matrixElements.*operand
	 */
	public RealMatrix arrayTimesEquals(RealMatrix operand)
	{
		checkMatrixDimensions(operand);
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				matrixElements[i][j] = matrixElements[i][j] * operand.getDouble(i,j);
		return this;
	}

	/** Element-by-element right division, resultArray = matrixElements./operand
	@param operand    another matrix
	@return     matrixElements./operand
	 */
	public RealMatrix arrayRightDivide(RealMatrix operand)
	{
		checkMatrixDimensions(operand);
		SimpleRealMatrix X = new SimpleRealMatrix(height, width);
		double[][] C = X.matrixElements;
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				C[i][j] = matrixElements[i][j] / operand.getDouble(i,j);
		return X;
	}

	/** Element-by-element right division in place, matrixElements = matrixElements./operand
	@param operand    another matrix
	@return     matrixElements./operand
	 */
	public RealMatrix arrayRightDivideEquals(RealMatrix operand)
	{
		checkMatrixDimensions(operand);
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				matrixElements[i][j] = matrixElements[i][j] / operand.getDouble(i,j);
		return this;
	}

	/** Element-by-element left division, resultArray = matrixElements.\operand
	@param operand    another matrix
	@return     matrixElements.\operand
	 */
	public RealMatrix arrayLeftDivide(RealMatrix operand)
	{
		checkMatrixDimensions(operand);
		SimpleRealMatrix X = new SimpleRealMatrix(height, width);
		double[][] C = X.matrixElements;
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				C[i][j] = operand.getDouble(i,j) / matrixElements[i][j];
		return X;
	}

	/** Element-by-element left division in place, matrixElements = matrixElements.\operand
	@param operand    another matrix
	@return     matrixElements.\operand
	 */
	public RealMatrix arrayLeftDivideEquals(RealMatrix operand)
	{
		checkMatrixDimensions(operand);
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				matrixElements[i][j] = operand.getDouble(i,j) / matrixElements[i][j];
		return this;
	}

	/** Multiply a matrix by a scalar, resultArray = scalar*matrixElements
	@param scalar    scalar
	@return     scalar*matrixElements
	 */
	public RealMatrix multiply(double scalar)
	{
		SimpleRealMatrix X = new SimpleRealMatrix(height, width);
		double[][] C = X.matrixElements;
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				C[i][j] = scalar * matrixElements[i][j];
		return X;
	}

	/** Multiply a matrix by a scalar, resultArray = scalar*matrixElements
	@param scalar    scalar
	@return     scalar*matrixElements
	 */
	public RealMatrix multiply(RealNumber scalar)
	{
		return this.multiply(scalar.getValue());
	}

	public RealMatrix divide(RealNumber scalar)
	{
		return this.multiply(1.0/scalar.getValue());
	}

	public RealMatrix divide(double scalar)
	{
		return this.multiply(1.0/scalar);
	}

	/** Multiply a matrix by a scalar in place, matrixElements = scalar*matrixElements
	@param scalar    scalar
	@return     replace matrixElements by scalar*matrixElements
	 */
	public RealMatrix multiplyEquals(double scalar)
	{
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				matrixElements[i][j] = scalar * matrixElements[i][j];
		return this;
	}

	public RealMatrix multiplyEquals(RealNumber scalar)
	{
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				matrixElements[i][j] = scalar.getValue() * matrixElements[i][j];
		return this;
	}

	/** Linear algebraic matrix multiplication, matrixElements * operand
	@param operand    another matrix
	@return     SimpleRealMatrix product, matrixElements * operand
	@exception  IllegalArgumentException SimpleRealMatrix inner dimensions must agree.
	 */
	public RealMatrix multiply(RealMatrix operand)
	{
		if(operand.getHeight() != width)
			throw new IllegalArgumentException("Matrix inner dimensions must agree.");
		SimpleRealMatrix resultMatrix = new SimpleRealMatrix(height, operand.getWidth());
		double[][] resultArray = resultMatrix.matrixElements;
		double[] Bcolj = new double[width];
		for(int j = 0; j < operand.getWidth(); j++)
		{
			for(int k = 0; k < width; k++)
				Bcolj[k] = operand.getDouble(k,j);
			for(int i = 0; i < height; i++)
			{
				double[] Arowi = matrixElements[i];
				double s = 0;
				for(int k = 0; k < width; k++)
					s += Arowi[k] * Bcolj[k];
				resultArray[i][j] = s;
			}
		}
		return resultMatrix;
	}

	/** Solve matrixElements*resultMatrix = operand
	@param operand    right hand side
	@return     solution if matrixElements is square, least squares solution otherwise
	 */
	public RealMatrix solve(RealMatrix operand)
	{
		return (height == width ? (new DoolittleLuDecomposition<RealMatrix,RealNumber>(this)).solve(operand) : (new HouseholderQrDecomposition<RealMatrix,RealNumber>(this)).solve(operand));
	}

	/** Solve resultMatrix*matrixElements = operand, which is also matrixElements'*resultMatrix' = operand'
	@param operand    right hand side
	@return     solution if matrixElements is square, least squares solution otherwise.
	 */
	public RealMatrix solveTranspose(RealMatrix operand)
	{
		return this.transpose().solve(operand.transpose());
	}

	/** SimpleRealMatrix reciprocal or pseudoinverse
	@return     reciprocal(matrixElements) if matrixElements is square, pseudoinverse otherwise.
	 */
	public RealMatrix reciprocal()
	{
		return solve(identity(height, height));
	}

	/** SimpleRealMatrix determinant
	@return     determinant
	 */
	public double getDeterminant()
	{
		return new DoolittleLuDecomposition<RealMatrix,RealNumber>(this).getDeterminant().getValue();
	}

	/** SimpleRealMatrix rank
	@return     effective numerical rank, obtained from SVD.
	 */
	public int rank()
	{
		return new StewartSingularValueDecomposition(this).rank();
	}

	/** SimpleRealMatrix condition (2 norm)
	@return     ratio of largest to smallest singular value.
	 */
	public double cond()
	{
		return new StewartSingularValueDecomposition(this).norm2ConditionDouble();
	}

	/** SimpleRealMatrix trace.
	@return     sum of the diagonal elements.
	 */
	public double trace()
	{
		double t = 0;
		for(int i = 0; i < Math.min(height, width); i++)
			t += matrixElements[i][i];
		return t;
	}

	/** Generate matrix with random elements
	@param height    Number of rows.
	@param height    Number of colums.
	@return     An height-by-height matrix with uniformly distributed random elements.
	 */
	public static SimpleRealMatrix random(int m, int n)
	{
		SimpleRealMatrix A = new SimpleRealMatrix(m, n);
		double[][] X = A.matrixElements;
		for(int i = 0; i < m; i++)
			for(int j = 0; j < n; j++)
				X[i][j] = Math.random();
		return A;
	}

	/** Generate identity matrix
	@param height    Number of rows.
	@param height    Number of colums.
	@return     An height-by-height matrix with ones on the diagonal and zeros elsewhere.
	 */
	public static RealMatrix identity(int height, int width)
	{
		final double[][] identityValues = new double[height][width];
		for(int index = 0; index < (height < width ? height : width); index++)
			identityValues[index][index] = 1.0;
		return new SimpleRealMatrix(identityValues);
		/*
		SimpleRealMatrix A = new SimpleRealMatrix(height, width);
		double[][] X = A.matrixElements;
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				X[i][j] = (i == j ? 1.0 : 0.0);
		return A;*/
	}

	/** Check if size(matrixElements) == size(operand) **/
	private void checkMatrixDimensions(RealMatrix compareMatrix)
	{
		if(compareMatrix.getHeight() != height || compareMatrix.getWidth() != width)
			throw new IllegalArgumentException("Matrix dimensions must agree.");
	}

	@Override
	public String toString()
	{
		StringBuffer out = new StringBuffer("{");
		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
			{
				if(widthIndex == 0)
					out.append('{');
				out.append(this.matrixElements[heightIndex][widthIndex]);
				if(widthIndex < (this.width - 1))
					out.append(',');
				else
					out.append('}');
			}
		out.append('}');
		return out.toString();
	}
}
