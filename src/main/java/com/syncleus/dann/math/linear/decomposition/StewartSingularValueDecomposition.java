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

import java.util.*;

/**
 * Singular Value Decomposition.
 * <p/>
 * For an m-by-n matrix A with m &gt;= n, the singular value decomposition is an
 * m-by-n orthogonal matrix leftSingularMatrix, an n-by-n diagonal matrix S, and
 * an n-by-n orthogonal matrix rightSingularMatrix so that A =
 * leftSingularMatrix*S*rightSingularMatrix'.
 * <p/>
 * The singular values, sigma[k] = S[k][k], are ordered so that sigma[0] &gt;=
 * sigma[1] &gt;= ... &gt;= sigma[n-1].
 * <p/>
 * The singular value decomposition always exists, so the constructor will never
 * fail.  The matrix condition number and the effective numerical rank can be
 * computed from this decomposition. Algorithm taken from: G.W. Stewart,
 * university of Maryland, Argonne national lab. C
 *
 * @author Jeffrey Phillips Freeman
 */
public class StewartSingularValueDecomposition implements java.io.Serializable, SingularValueDecomposition {
    private static final long serialVersionUID = 325638354441525023L;
    /**
     * Arrays for internal storage of leftSingularMatrix and rightSingularMatrix.
     */
    private final double[][] leftSingularMatrix;
    private final double[][] rightSingularMatrix;
    private final boolean hasRightSingularMatrix;
    private final boolean hasLeftSingularMatrix;
    /**
     * Array for internal storage of singular values.
     */
    private final double[] matrix;
    /**
     * Row and column dimensions.
     */
    private final int m;
    private final int n;

    /**
     * Construct the singular value decomposition.
     * Gives access to S, leftSingularMatrix and rightSingularMatrix.
     *
     * @param rectangularMatrix Rectangular matrix.
     */
    public StewartSingularValueDecomposition(final RealMatrix rectangularMatrix) {
        this(rectangularMatrix, true, true);
    }

    /**
     * Construct the singular value decomposition.
     * Gives access to S and optionally leftSingularMatrix and
     * rightSingularMatrix.
     *
     * @param rectangularMatrix            Rectangular matrix
     * @param calculateRightSingularMatrix if true, allows accessing the
     *                                     rightSingularMatrix
     * @param calculateLeftSingularMatrix  if true, allows accessing the
     *                                     leftSingularMatrix
     */
    public StewartSingularValueDecomposition(final RealMatrix rectangularMatrix, final boolean calculateRightSingularMatrix, final boolean calculateLeftSingularMatrix) {
        this.m = rectangularMatrix.getHeight();
        this.n = rectangularMatrix.getWidth();
        final int nu = Math.min(this.m, this.n);
        this.hasRightSingularMatrix = calculateRightSingularMatrix;
        this.hasLeftSingularMatrix = calculateLeftSingularMatrix;
        if (this.hasLeftSingularMatrix)
            this.leftSingularMatrix = new double[m][nu];
        else
            this.leftSingularMatrix = null;
        if (this.hasRightSingularMatrix)
            this.rightSingularMatrix = new double[n][n];
        else
            this.rightSingularMatrix = null;

        // Derived from LINPACK code.
        // Initialize.
        this.matrix = new double[Math.min(this.m + 1, this.n)];
        final double[] e = new double[n];
        final int nct = Math.min(this.m - 1, this.n);
        final int nrt = Math.max(0, Math.min(this.n - 2, this.m));
        int p = reduceToBidiagonal(rectangularMatrix, e, nct, nrt);

        generateLeftSingularMatrix(nct, nu);

        generateRightSingularMatrix(e, nrt, nu);

        // Main iteration loop for the singular values.
        final int pp = p - 1;
        int iter = 0;
        final double eps = Math.pow(2.0, -52.0);
        final double tiny = Math.pow(2.0, -966.0);
        while (p > 0) {
            int k;
            // Here is where a test for too many iterations would go.
            // This section of the program inspects for
            // negligible elements in the matrix and e arrays.  On
            // completion the variable k is set as follows.
            // _task_
            // deflate      if matrix(p) and e[k-1] are negligible and k < p
            // split        if matrix(k) is negligible and k < p
            // QR-step      if e[k-1] is negligible, k < p, and
            //              matrix(k), ..., matrix(p) are not negligible.
            // convergence  if e(p-1) is negligible.
            for (k = p - 2; k >= -1; k--) {
                if (k == -1)
                    break;
                if (Math.abs(e[k])
                            <= tiny + eps * (Math.abs(this.matrix[k]) + Math.abs(this.matrix[k + 1]))) {
                    e[k] = 0.0;
                    break;
                }
            }
            if (k == p - 2) {
                k++;
                convergence(k, pp);
                iter = 0;
                p--;
            }
            else {
                int ks;
                for (ks = p - 1; ks >= k; ks--) {
                    if (ks == k)
                        break;
                    final double t = ((ks == p) ? 0.0 : Math.abs(e[ks]))
                                             + ((ks == (k + 1)) ? 0.0 : Math.abs(e[ks - 1]));
                    if (Math.abs(this.matrix[ks]) <= tiny + eps * t) {
                        this.matrix[ks] = 0.0;
                        break;
                    }
                }
                if (ks == k) {
                    k++;
                    oneQrStep(e, p, k);
                    iter = iter + 1;
                }
                else if (ks == p - 1) {
                    k++;
                    deflateNegligibleMatrix(e, p, k);
                }
                else {
                    k = ks;
                    k++;
                    splitAtNegligibleMatrix(e, p, k);
                }
            }
        }
    }

    /**
     * Reduces the matrix to a bidiagonal form, storing the diagonal elements in
     * matrix and the super-diagonal elements in e.
     *
     * @return order p
     */
    private int reduceToBidiagonal(final RealMatrix rectangularMatrix, final double[] e, final int nct, final int nrt) {
        final double[][] transform = rectangularMatrix.toDoubleArray();
        final double[] work = new double[m];
        // Reduce A to bidiagonal form, storing the diagonal elements
        // in matrix and the super-diagonal elements in e.
        for (int k = 0; k < Math.max(nct, nrt); k++) {
            if (k < nct) {
                // Compute the transformation for the k-th column and
                // place the k-th diagonal in matrix[k].
                // Compute 2-norm of k-th column without under/overflow.
                this.matrix[k] = 0;
                for (int i = k; i < this.m; i++)
                    this.matrix[k] = Math.hypot(this.matrix[k], transform[i][k]);
                if (this.matrix[k] != 0.0) {
                    if (transform[k][k] < 0.0)
                        this.matrix[k] = -this.matrix[k];
                    for (int i = k; i < this.m; i++)
                        transform[i][k] /= this.matrix[k];
                    transform[k][k] += 1.0;
                }
                this.matrix[k] = -this.matrix[k];
            }
            for (int j = k + 1; j < this.n; j++) {
                if ((k < nct) && (this.matrix[k] != 0.0)) {
                    // Apply the transformation.
                    double t = 0;
                    for (int i = k; i < this.m; i++)
                        t += transform[i][k] * transform[i][j];
                    t = -t / transform[k][k];
                    for (int i = k; i < this.m; i++)
                        transform[i][j] += t * transform[i][k];
                }
                // Place the k-th row of A into e for the
                // subsequent calculation of the row transformation.
                e[j] = transform[k][j];
            }
            if (this.hasLeftSingularMatrix && (k < nct))
                // Place the transformation in leftSingularMatrix for subsequent back
                // multiplication.
                for (int i = k; i < this.m; i++)
                    this.leftSingularMatrix[i][k] = transform[i][k];
            if (k < nrt) {
                // Compute the k-th row transformation and place the
                // k-th super-diagonal in e[k].
                // Compute 2-norm without under/overflow.
                e[k] = 0;
                for (int i = k + 1; i < this.n; i++)
                    e[k] = Math.hypot(e[k], e[i]);
                if (e[k] != 0.0) {
                    if (e[k + 1] < 0.0)
                        e[k] = -e[k];
                    for (int i = k + 1; i < this.n; i++)
                        e[i] /= e[k];
                    e[k + 1] += 1.0;
                }
                e[k] = -e[k];
                if ((k + 1 < this.m) && (e[k] != 0.0)) {
                    // Apply the transformation.
                    for (int i = k + 1; i < this.m; i++)
                        work[i] = 0.0;
                    for (int j = k + 1; j < this.n; j++)
                        for (int i = k + 1; i < this.m; i++)
                            work[i] += e[j] * transform[i][j];
                    for (int j = k + 1; j < this.n; j++) {
                        final double t = -e[j] / e[k + 1];
                        for (int i = k + 1; i < this.m; i++)
                            transform[i][j] += t * work[i];
                    }
                }
                if (this.hasRightSingularMatrix)
                    // Place the transformation in rightSingularMatrix for subsequent
                    // back multiplication.
                    for (int i = k + 1; i < this.n; i++)
                        this.rightSingularMatrix[i][k] = e[i];
            }
        }
        // Set up the final bidiagonal matrix or order p.
        final int p = Math.min(this.n, this.m + 1);
        if (nct < this.n)
            this.matrix[nct] = transform[nct][nct];
        if (this.m < p)
            this.matrix[p - 1] = 0.0;
        if (nrt + 1 < p)
            e[nrt] = transform[nrt][p - 1];
        e[p - 1] = 0.0;

        return p;
    }


    /**
     * Generates the left-singular-matrix, if required.
     */
    private void generateLeftSingularMatrix(final int nct, final int nu) {
        if (this.hasLeftSingularMatrix) {
            for (int j = nct; j < nu; j++) {
                for (int i = 0; i < this.m; i++)
                    this.leftSingularMatrix[i][j] = 0.0;
                this.leftSingularMatrix[j][j] = 1.0;
            }
            for (int k = nct - 1; k >= 0; k--) {
                if (this.matrix[k] == 0.0) {
                    for (int i = 0; i < this.m; i++)
                        this.leftSingularMatrix[i][k] = 0.0;
                    this.leftSingularMatrix[k][k] = 1.0;
                }
                else {
                    for (int j = k + 1; j < nu; j++) {
                        double t = 0;
                        for (int i = k; i < this.m; i++)
                            t += this.leftSingularMatrix[i][k] * this.leftSingularMatrix[i][j];
                        t = -t / this.leftSingularMatrix[k][k];
                        for (int i = k; i < this.m; i++)
                            this.leftSingularMatrix[i][j] += t * this.leftSingularMatrix[i][k];
                    }
                    for (int i = k; i < this.m; i++)
                        this.leftSingularMatrix[i][k] = -this.leftSingularMatrix[i][k];
                    this.leftSingularMatrix[k][k] = 1.0 + this.leftSingularMatrix[k][k];
                    for (int i = 0; i < k - 1; i++)
                        this.leftSingularMatrix[i][k] = 0.0;
                }
            }
        }
    }

    /**
     * Generates the right-singular-matrix, if required.
     */
    private void generateRightSingularMatrix(final double[] e, final int nrt, final int nu) {
        if (this.hasRightSingularMatrix) {
            for (int k = this.n - 1; k >= 0; k--) {
                if ((k < nrt) && (e[k] != 0.0))
                    for (int j = k + 1; j < nu; j++) {
                        double t = 0;
                        for (int i = k + 1; i < this.n; i++)
                            t += this.rightSingularMatrix[i][k] * this.rightSingularMatrix[i][j];
                        t = -t / this.rightSingularMatrix[k + 1][k];
                        for (int i = k + 1; i < this.n; i++)
                            this.rightSingularMatrix[i][j] += t * this.rightSingularMatrix[i][k];
                    }
                for (int i = 0; i < this.n; i++)
                    this.rightSingularMatrix[i][k] = 0.0;
                this.rightSingularMatrix[k][k] = 1.0;
            }
        }
    }

    private void deflateNegligibleMatrix(final double[] e, final int p, final int k) {
        double f = e[p - 2];
        e[p - 2] = 0.0;
        for (int j = p - 2; j >= k; j--) {
            double t = Math.hypot(this.matrix[j], f);
            final double cs = this.matrix[j] / t;
            final double sn = f / t;
            this.matrix[j] = t;
            if (j != k) {
                f = -sn * e[j - 1];
                e[j - 1] = cs * e[j - 1];
            }
            if (this.hasRightSingularMatrix)
                for (int i = 0; i < this.n; i++) {
                    t = cs * this.rightSingularMatrix[i][j] + sn * this.rightSingularMatrix[i][p - 1];
                    this.rightSingularMatrix[i][p - 1] = -sn * this.rightSingularMatrix[i][j] + cs * this.rightSingularMatrix[i][p - 1];
                    this.rightSingularMatrix[i][j] = t;
                }
        }
    }

    private void splitAtNegligibleMatrix(final double[] e, final int p, final int k) {
        double f = e[k - 1];
        e[k - 1] = 0.0;
        for (int j = k; j < p; j++) {
            double t = Math.hypot(this.matrix[j], f);
            final double cs = this.matrix[j] / t;
            final double sn = f / t;
            this.matrix[j] = t;
            f = -sn * e[j];
            e[j] = cs * e[j];
            if (this.hasLeftSingularMatrix)
                for (int i = 0; i < this.m; i++) {
                    t = cs * this.leftSingularMatrix[i][j] + sn * this.leftSingularMatrix[i][k - 1];
                    this.leftSingularMatrix[i][k - 1] = -sn * this.leftSingularMatrix[i][j] + cs * this.leftSingularMatrix[i][k - 1];
                    this.leftSingularMatrix[i][j] = t;
                }
        }
    }

    private void oneQrStep(final double[] e, final int p, final int k) {
        // Calculate the shift.
        final double scale = Math.max(Math.max(Math.max(Math.max(
                                                                        Math.abs(this.matrix[p - 1]), Math.abs(this.matrix[p - 2])), Math.abs(e[p - 2])),
                                                      Math.abs(this.matrix[k])), Math.abs(e[k]));
        final double sp = this.matrix[p - 1] / scale;
        final double spm1 = this.matrix[p - 2] / scale;
        final double epm1 = e[p - 2] / scale;
        final double sk = this.matrix[k] / scale;
        final double ek = e[k] / scale;
        final double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0;
        final double c = (sp * epm1) * (sp * epm1);
        double shift = 0.0;
        if ((b != 0.0) | (c != 0.0)) {
            shift = Math.sqrt(b * b + c);
            if (b < 0.0)
                shift = -shift;
            shift = c / (b + shift);
        }
        double f = (sk + sp) * (sk - sp) + shift;
        double g = sk * ek;
        // Chase zeros.
        for (int j = k; j < p - 1; j++) {
            double t = Math.hypot(f, g);
            double cs = f / t;
            double sn = g / t;
            if (j != k)
                e[j - 1] = t;
            f = cs * this.matrix[j] + sn * e[j];
            e[j] = cs * e[j] - sn * this.matrix[j];
            g = sn * this.matrix[j + 1];
            this.matrix[j + 1] = cs * this.matrix[j + 1];
            if (this.hasRightSingularMatrix)
                for (int i = 0; i < this.n; i++) {
                    t = cs * this.rightSingularMatrix[i][j] + sn * this.rightSingularMatrix[i][j + 1];
                    this.rightSingularMatrix[i][j + 1] = -sn * this.rightSingularMatrix[i][j] + cs * this.rightSingularMatrix[i][j + 1];
                    this.rightSingularMatrix[i][j] = t;
                }
            t = Math.hypot(f, g);
            cs = f / t;
            sn = g / t;
            this.matrix[j] = t;
            f = cs * e[j] + sn * this.matrix[j + 1];
            this.matrix[j + 1] = -sn * e[j] + cs * this.matrix[j + 1];
            g = sn * e[j + 1];
            e[j + 1] = cs * e[j + 1];
            if (this.hasLeftSingularMatrix && (j < this.m - 1))
                for (int i = 0; i < this.m; i++) {
                    t = cs * this.leftSingularMatrix[i][j] + sn * this.leftSingularMatrix[i][j + 1];
                    this.leftSingularMatrix[i][j + 1] = -sn * this.leftSingularMatrix[i][j] + cs * this.leftSingularMatrix[i][j + 1];
                    this.leftSingularMatrix[i][j] = t;
                }
        }
        e[p - 2] = f;
    }

    private void convergence(final int k, final int pp) {
        // Make the singular values positive.
        if (this.matrix[k] <= 0.0) {
            this.matrix[k] = (this.matrix[k] < 0.0 ? -this.matrix[k] : 0.0);
            if (this.hasRightSingularMatrix)
                for (int i = 0; i <= pp; i++)
                    this.rightSingularMatrix[i][k] = -this.rightSingularMatrix[i][k];
        }
        // Order the singular values.
        int kk = k;
        while (kk < pp) {
            if (this.matrix[kk] >= this.matrix[kk + 1])
                break;
            double t = this.matrix[kk];
            this.matrix[kk] = this.matrix[kk + 1];
            this.matrix[kk + 1] = t;
            if (this.hasRightSingularMatrix && (kk < this.n - 1))
                for (int i = 0; i < this.n; i++) {
                    t = this.rightSingularMatrix[i][kk + 1];
                    this.rightSingularMatrix[i][kk + 1] = this.rightSingularMatrix[i][kk];
                    this.rightSingularMatrix[i][kk] = t;
                }
            if (this.hasLeftSingularMatrix && (kk < this.m - 1))
                for (int i = 0; i < this.m; i++) {
                    t = this.leftSingularMatrix[i][kk + 1];
                    this.leftSingularMatrix[i][kk + 1] = this.leftSingularMatrix[i][kk];
                    this.leftSingularMatrix[i][kk] = t;
                }
            kk++;
        }
    }

    /**
     * Return the left singular vectors.
     *
     * @return U
     */
    @Override
    public RealMatrix getLeftSingularMatrix() {
        if (!this.hasLeftSingularMatrix)
            return null;
        return new SimpleRealMatrix(this.leftSingularMatrix).getSubmatrix(0, this.m, 0, Math.min(this.m + 1, this.n));
    }

    /**
     * Return the right singular vectors.
     *
     * @return V
     */
    @Override
    public RealMatrix getRightSingularMatrix() {
        if (this.hasRightSingularMatrix)
            return null;

        return new SimpleRealMatrix(this.rightSingularMatrix).getSubmatrix(0, this.n, 0, this.n);
    }

    /**
     * Return the one-dimensional array of singular values.
     *
     * @return diagonal of S.
     */
    @Override
    public List<RealNumber> getSingularValues() {
        final List<RealNumber> singularValues = new ArrayList<RealNumber>(this.matrix.length);
        for (final double singularValue : this.matrix)
            singularValues.add(new RealNumber(singularValue));
        return Collections.unmodifiableList(singularValues);
    }

    /**
     * Return the diagonal matrix of singular values.
     *
     * @return S
     */
    @Override
    public RealMatrix getMatrix() {
        final double[][] singular = new double[n][n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++)
                singular[i][j] = 0.0;
            singular[i][i] = this.matrix[i];
        }
        return new SimpleRealMatrix(singular);
    }

    /**
     * Two norm.
     *
     * @return max(S)
     */
    public double norm2Double() {
        return this.matrix[0];
    }

    @Override
    public RealNumber norm2() {
        return new RealNumber(this.norm2Double());
    }

    /**
     * Two norm condition number.
     *
     * @return max(S)/min(S)
     */
    public double norm2ConditionDouble() {
        return this.matrix[0] / this.matrix[Math.min(this.m, this.n) - 1];
    }

    @Override
    public RealNumber norm2Condition() {
        return new RealNumber(this.norm2ConditionDouble());
    }

    /**
     * Effective numerical matrix rank.
     *
     * @return Number of non-negligible singular values.
     */
    @Override
    public int rank() {
        final double eps = Math.pow(2.0, -52.0);
        final double tol = Math.max(this.m, this.n) * this.matrix[0] * eps;
        int r = 0;
        for (final double value : this.matrix)
            if (value > tol)
                r++;
        return r;
    }
}
