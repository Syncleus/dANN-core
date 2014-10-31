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

public class SchurEigenvalueDecomposition implements java.io.Serializable, EigenvalueDecomposition {
    private static final long serialVersionUID = -2044063442141081048L;
    /**
     * Arrays for internal storage of eigenvalues.
     */
    private List<RealNumber> realEigenvalues, imaginaryEigenvalues;
    /**
     * Array for internal storage of eigenvectors.
     */
    private RealMatrix matrix;
    /**
     * Array for internal storage of non-symmetric Hessenberg form.
     */
    private RealMatrix hessenbergMatrix;

    // Complex scalar division.
    private transient RealNumber cdivr, cdivi;

    /**
     * Check for symmetry, then construct the eigenvalue decomposition. Gives
     * access to D and matrixElements.
     *
     * @param matrixToDecompose Elements Square matrix
     */
    public SchurEigenvalueDecomposition(final RealMatrix matrixToDecompose) {
        final double[][] matrixToDecomposeElements = matrixToDecompose.toDoubleArray();
        final int width = matrixToDecompose.getWidth();
        final double[][] matrixElements = new double[width][width];
        final double[][] hessenbergMatrixElements = new double[width][width];

        this.realEigenvalues = new ArrayList<RealNumber>(width);
        this.realEigenvalues.addAll(Collections.nCopies(width, new RealNumber(0.0)));
        this.imaginaryEigenvalues = new ArrayList<RealNumber>(width);
        this.imaginaryEigenvalues.addAll(Collections.nCopies(width, new RealNumber(0.0)));

        for (int j = 0; j < width; j++)
            for (int i = 0; i < width; i++)
                hessenbergMatrixElements[i][j] = matrixToDecomposeElements[i][j];
        this.hessenbergMatrix = new SimpleRealMatrix(hessenbergMatrixElements);
        this.matrix = new SimpleRealMatrix(matrixElements);

        final HessenbergDecomposition hessenberbDecomposition = new NonsymetricHessenbergReduction(this.hessenbergMatrix);
        this.matrix = hessenberbDecomposition.getMatrix();
        this.hessenbergMatrix = hessenberbDecomposition.getHessenbergMatrix();

        // Reduce Hessenberg to real Schur form.
        this.schurReduction();
    }

    public final int getDimensionSize() {
        return this.matrix.getHeight();
    }

    private void cdiv(final double xr, final double xi, final double yr, final double yi) {
        final double r;
        final double d;
        if (Math.abs(yr) > Math.abs(yi)) {
            r = yi / yr;
            d = yr + r * yi;
            this.cdivr = new RealNumber((xr + r * xi) / d);
            this.cdivi = new RealNumber((xi - r * xr) / d);
        }
        else {
            r = yr / yi;
            d = yi + r * yr;
            this.cdivr = new RealNumber((r * xr + xi) / d);
            this.cdivi = new RealNumber((r * xi - xr) / d);
        }
    }


    // Nonsymmetric reduction from Hessenberg to real Schur form.

    private void schurReduction() {
        int n = this.getDimensionSize();
        final double[] d = new double[this.realEigenvalues.size()];
        for (int valueIndex = 0; valueIndex < d.length; valueIndex++)
            d[valueIndex] = this.realEigenvalues.get(valueIndex).getValue();
        final double[] e = new double[this.imaginaryEigenvalues.size()];
        for (int valueIndex = 0; valueIndex < d.length; valueIndex++)
            e[valueIndex] = this.imaginaryEigenvalues.get(valueIndex).getValue();
        final double[][] eigenVectors = this.matrix.toDoubleArray();
        final double[][] hessenberg = this.hessenbergMatrix.toDoubleArray();

        //  This is derived from the Algol procedure schurReduction,
        //  by Martin and Wilkinson, Handbook for Auto. Comp.,
        //  Vol.ii-Linear Algebra, and the corresponding
        //  Fortran subroutine in EISPACK.

        // Initialize
        final int nn = n;
        n = nn - 1;
        final int low = 0;
        final int high = nn - 1;
        final double eps = Math.pow(2.0, -52.0);
        double exshift = 0.0;
        double p = 0, q = 0, r = 0, s = 0, z = 0, t, w, x, y;

        // Store roots isolated by balanc and compute matrix norm
        double norm = 0.0;
        for (int i = 0; i < nn; i++) {
            if (i < low | i > high) {
                d[i] = hessenberg[i][i];
                e[i] = 0.0;
            }
            for (int j = Math.max(i - 1, 0); j < nn; j++)
                norm = norm + Math.abs(hessenberg[i][j]);
        }

        // Outer loop over eigenvalue index
        int iter = 0;
        while (n >= low) {

            // Look for single small sub-diagonal element
            int l = n;
            while (l > low) {
                s = Math.abs(hessenberg[l - 1][l - 1]) + Math.abs(hessenberg[l][l]);
                if (s == 0.0)
                    s = norm;
                if (Math.abs(hessenberg[l][l - 1]) < eps * s)
                    break;
                l--;
            }

            // Check for convergence
            // One root found
            if (l == n) {
                hessenberg[n][n] = hessenberg[n][n] + exshift;
                d[n] = hessenberg[n][n];
                e[n] = 0.0;
                n--;
                iter = 0;

                // Two roots found
            }
            else if (l == n - 1) {
                w = hessenberg[n][n - 1] * hessenberg[n - 1][n];
                p = (hessenberg[n - 1][n - 1] - hessenberg[n][n]) / 2.0;
                q = p * p + w;
                z = Math.sqrt(Math.abs(q));
                hessenberg[n][n] = hessenberg[n][n] + exshift;
                hessenberg[n - 1][n - 1] = hessenberg[n - 1][n - 1] + exshift;
                x = hessenberg[n][n];

                // Real pair
                if (q >= 0) {
                    if (p >= 0)
                        z = p + z;
                    else
                        z = p - z;
                    d[n - 1] = x + z;
                    d[n] = d[n - 1];
                    if (z != 0.0)
                        d[n] = x - w / z;
                    e[n - 1] = 0.0;
                    e[n] = 0.0;
                    x = hessenberg[n][n - 1];
                    s = Math.abs(x) + Math.abs(z);
                    p = x / s;
                    q = z / s;
                    r = Math.sqrt(p * p + q * q);
                    p = p / r;
                    q = q / r;

                    // Row modification
                    for (int j = n - 1; j < nn; j++) {
                        z = hessenberg[n - 1][j];
                        hessenberg[n - 1][j] = q * z + p * hessenberg[n][j];
                        hessenberg[n][j] = q * hessenberg[n][j] - p * z;
                    }

                    // Column modification
                    for (int i = 0; i <= n; i++) {
                        z = hessenberg[i][n - 1];
                        hessenberg[i][n - 1] = q * z + p * hessenberg[i][n];
                        hessenberg[i][n] = q * hessenberg[i][n] - p * z;
                    }

                    // Accumulate transformations
                    for (int i = low; i <= high; i++) {
                        z = eigenVectors[i][n - 1];
                        eigenVectors[i][n - 1] = q * z + p * eigenVectors[i][n];
                        eigenVectors[i][n] = q * eigenVectors[i][n] - p * z;
                    }

                    // Complex pair
                }
                else {
                    d[n - 1] = x + p;
                    d[n] = x + p;
                    e[n - 1] = z;
                    e[n] = -z;
                }
                n = n - 2;
                iter = 0;

                // No convergence yet
            }
            else {
                // Form shift
                x = hessenberg[n][n];
                y = 0.0;
                w = 0.0;
                if (l < n) {
                    y = hessenberg[n - 1][n - 1];
                    w = hessenberg[n][n - 1] * hessenberg[n - 1][n];
                }

                // Wilkinson's original ad hoc shift

                if (iter == 10) {
                    exshift += x;
                    for (int i = low; i <= n; i++)
                        hessenberg[i][i] -= x;
                    s = Math.abs(hessenberg[n][n - 1]) + Math.abs(hessenberg[n - 1][n - 2]);
                    x = 0.75 * s;
                    y = x;
                    w = -0.4375 * s * s;
                }

                // MATLAB's new ad hoc shift

                if (iter == 30) {
                    s = (y - x) / 2.0;
                    s = s * s + w;
                    if (s > 0) {
                        s = Math.sqrt(s);
                        if (y < x)
                            s = -s;
                        s = x - w / ((y - x) / 2.0 + s);
                        for (int i = low; i <= n; i++)
                            hessenberg[i][i] -= s;
                        exshift += s;
                        x = 0.964;
                        y = x;
                        w = x;
                    }
                }

                iter = iter + 1;   // (Could check iteration count here.)

                // Look for two consecutive small sub-diagonal elements
                int m = n - 2;
                while (m >= l) {
                    z = hessenberg[m][m];
                    r = x - z;
                    s = y - z;
                    p = (r * s - w) / hessenberg[m + 1][m] + hessenberg[m][m + 1];
                    q = hessenberg[m + 1][m + 1] - z - r - s;
                    r = hessenberg[m + 2][m + 1];
                    s = Math.abs(p) + Math.abs(q) + Math.abs(r);
                    p = p / s;
                    q = q / s;
                    r = r / s;
                    if (m == l)
                        break;
                    if (Math.abs(hessenberg[m][m - 1]) * (Math.abs(q) + Math.abs(r))
                                < eps * (Math.abs(p) * (Math.abs(hessenberg[m - 1][m - 1]) + Math.abs(z)
                                                                + Math.abs(hessenberg[m + 1][m + 1]))))
                        break;
                    m--;
                }

                for (int i = m + 2; i <= n; i++) {
                    hessenberg[i][i - 2] = 0.0;
                    if (i > m + 2)
                        hessenberg[i][i - 3] = 0.0;
                }

                // Double QR step involving rows l:n and columns m:n
                for (int k = m; k <= n - 1; k++) {
                    final boolean notlast = (k != n - 1);
                    if (k != m) {
                        p = hessenberg[k][k - 1];
                        q = hessenberg[k + 1][k - 1];
                        r = (notlast ? hessenberg[k + 2][k - 1] : 0.0);
                        x = Math.abs(p) + Math.abs(q) + Math.abs(r);
                        if (x != 0.0) {
                            p = p / x;
                            q = q / x;
                            r = r / x;
                        }
                    }
                    if (x == 0.0)
                        break;
                    s = Math.sqrt(p * p + q * q + r * r);
                    if (p < 0)
                        s = -s;
                    if (s != 0) {
                        if (k != m)
                            hessenberg[k][k - 1] = -s * x;
                        else if (l != m)
                            hessenberg[k][k - 1] = -hessenberg[k][k - 1];
                        p = p + s;
                        x = p / s;
                        y = q / s;
                        z = r / s;
                        q = q / p;
                        r = r / p;

                        // Row modification
                        for (int j = k; j < nn; j++) {
                            p = hessenberg[k][j] + q * hessenberg[k + 1][j];
                            if (notlast) {
                                p = p + r * hessenberg[k + 2][j];
                                hessenberg[k + 2][j] = hessenberg[k + 2][j] - p * z;
                            }
                            hessenberg[k][j] = hessenberg[k][j] - p * x;
                            hessenberg[k + 1][j] = hessenberg[k + 1][j] - p * y;
                        }

                        // Column modification
                        for (int i = 0; i <= Math.min(n, k + 3); i++) {
                            p = x * hessenberg[i][k] + y * hessenberg[i][k + 1];
                            if (notlast) {
                                p = p + z * hessenberg[i][k + 2];
                                hessenberg[i][k + 2] = hessenberg[i][k + 2] - p * r;
                            }
                            hessenberg[i][k] = hessenberg[i][k] - p;
                            hessenberg[i][k + 1] = hessenberg[i][k + 1] - p * q;
                        }

                        // Accumulate transformations
                        for (int i = low; i <= high; i++) {
                            p = x * eigenVectors[i][k] + y * eigenVectors[i][k + 1];
                            if (notlast) {
                                p = p + z * eigenVectors[i][k + 2];
                                eigenVectors[i][k + 2] = eigenVectors[i][k + 2] - p * r;
                            }
                            eigenVectors[i][k] = eigenVectors[i][k] - p;
                            eigenVectors[i][k + 1] = eigenVectors[i][k + 1] - p * q;
                        }
                    }  // (s != 0)
                }  // k loop
            }  // check convergence
        }  // while (n >= low)

        // Backsubstitute to find vectors of upper triangular form
        if (norm == 0.0)
            return;

        for (n = nn - 1; n >= 0; n--) {
            p = d[n];
            q = e[n];

            // Real vector
            if (q == 0) {
                int l = n;
                hessenberg[n][n] = 1.0;
                for (int i = n - 1; i >= 0; i--) {
                    w = hessenberg[i][i] - p;
                    r = 0.0;
                    for (int j = l; j <= n; j++)
                        r = r + hessenberg[i][j] * hessenberg[j][n];
                    if (e[i] < 0.0) {
                        z = w;
                        s = r;
                    }
                    else {
                        l = i;
                        if (e[i] == 0.0)
                            if (w == 0.0)
                                hessenberg[i][n] = -r / (eps * norm);
                            else
                                hessenberg[i][n] = -r / w;
                        else {
                            x = hessenberg[i][i + 1];
                            y = hessenberg[i + 1][i];
                            q = (d[i] - p) * (d[i] - p) + e[i] * e[i];
                            t = (x * s - z * r) / q;
                            hessenberg[i][n] = t;
                            if (Math.abs(x) > Math.abs(z))
                                hessenberg[i + 1][n] = (-r - w * t) / x;
                            else
                                hessenberg[i + 1][n] = (-s - y * t) / z;
                        }

                        // Overflow control
                        t = Math.abs(hessenberg[i][n]);
                        if ((eps * t) * t > 1)
                            for (int j = i; j <= n; j++)
                                hessenberg[j][n] = hessenberg[j][n] / t;
                    }
                }

                // Complex vector
            }
            else if (q < 0) {
                int l = n - 1;

                // Last vector component imaginary so matrix is triangular
                if (Math.abs(hessenberg[n][n - 1]) > Math.abs(hessenberg[n - 1][n])) {
                    hessenberg[n - 1][n - 1] = q / hessenberg[n][n - 1];
                    hessenberg[n - 1][n] = -(hessenberg[n][n] - p) / hessenberg[n][n - 1];
                }
                else {
                    this.cdiv(0.0, -hessenberg[n - 1][n], hessenberg[n - 1][n - 1] - p, q);
                    hessenberg[n - 1][n - 1] = this.cdivr.getValue();
                    hessenberg[n - 1][n] = this.cdivi.getValue();
                }
                hessenberg[n][n - 1] = 0.0;
                hessenberg[n][n] = 1.0;
                for (int i = n - 2; i >= 0; i--) {
                    double ra;
                    double sa;
                    double vr;
                    final double vi;
                    ra = 0.0;
                    sa = 0.0;
                    for (int j = l; j <= n; j++) {
                        ra = ra + hessenberg[i][j] * hessenberg[j][n - 1];
                        sa = sa + hessenberg[i][j] * hessenberg[j][n];
                    }
                    w = hessenberg[i][i] - p;

                    if (e[i] < 0.0) {
                        z = w;
                        r = ra;
                        s = sa;
                    }
                    else {
                        l = i;
                        if (e[i] == 0) {
                            this.cdiv(-ra, -sa, w, q);
                            hessenberg[i][n - 1] = this.cdivr.getValue();
                            hessenberg[i][n] = this.cdivi.getValue();
                        }
                        else {
                            // Solve complex equations
                            x = hessenberg[i][i + 1];
                            y = hessenberg[i + 1][i];
                            vr = (d[i] - p) * (d[i] - p) + e[i] * e[i] - q * q;
                            vi = (d[i] - p) * 2.0 * q;
                            if (vr == 0.0 & vi == 0.0)
                                vr = eps * norm * (Math.abs(w) + Math.abs(q)
                                                           + Math.abs(x) + Math.abs(y) + Math.abs(z));
                            this.cdiv(x * r - z * ra + q * sa, x * s - z * sa - q * ra, vr, vi);
                            hessenberg[i][n - 1] = this.cdivr.getValue();
                            hessenberg[i][n] = this.cdivi.getValue();
                            if (Math.abs(x) > (Math.abs(z) + Math.abs(q))) {
                                hessenberg[i + 1][n - 1] = (-ra - w * hessenberg[i][n - 1] + q * hessenberg[i][n]) / x;
                                hessenberg[i + 1][n] = (-sa - w * hessenberg[i][n] - q * hessenberg[i][n - 1]) / x;
                            }
                            else {
                                this.cdiv(-r - y * hessenberg[i][n - 1], -s - y * hessenberg[i][n], z, q);
                                hessenberg[i + 1][n - 1] = this.cdivr.getValue();
                                hessenberg[i + 1][n] = this.cdivi.getValue();
                            }
                        }

                        // Overflow control
                        t = Math.max(Math.abs(hessenberg[i][n - 1]), Math.abs(hessenberg[i][n]));
                        if ((eps * t) * t > 1)
                            for (int j = i; j <= n; j++) {
                                hessenberg[j][n - 1] = hessenberg[j][n - 1] / t;
                                hessenberg[j][n] = hessenberg[j][n] / t;
                            }
                    }
                }
            }
        }

        // Vectors of isolated roots
        for (int i = 0; i < nn; i++)
            if (i < low | i > high)
                System.arraycopy(hessenberg[i], i, eigenVectors[i], i, nn - i);

        // Back transformation to get eigenvectors of original matrix
        for (int j = nn - 1; j >= low; j--)
            for (int i = low; i <= high; i++) {
                z = 0.0;
                for (int k = low; k <= Math.min(j, high); k++)
                    z = z + eigenVectors[i][k] * hessenberg[k][j];
                eigenVectors[i][j] = z;
            }

        this.realEigenvalues = new ArrayList<RealNumber>(d.length);
        for (final double realValue : d)
            this.realEigenvalues.add(new RealNumber(realValue));
        this.imaginaryEigenvalues = new ArrayList<RealNumber>(e.length);
        for (final double imaginaryValue : e)
            this.imaginaryEigenvalues.add(new RealNumber(imaginaryValue));
        this.matrix = new SimpleRealMatrix(eigenVectors);
        this.hessenbergMatrix = new SimpleRealMatrix(hessenberg);
    }

    /**
     * Return the eigenvector matrix.
     *
     * @return matrixElements
     */
    @Override
    public RealMatrix getMatrix() {
        return this.matrix;
    }

    /**
     * Return the real parts of the eigenvalues.
     *
     * @return real(diag(D))
     */
    @Override
    public List<RealNumber> getRealEigenvalues() {
        return Collections.unmodifiableList(this.realEigenvalues);
    }

    /**
     * Return the imaginary parts of the eigenvalues.
     *
     * @return imag(diag(D))
     */
    @Override
    public List<RealNumber> getImaginaryEigenvalues() {
        return Collections.unmodifiableList(this.imaginaryEigenvalues);
    }

    /**
     * Return the block diagonal eigenvalue matrix.
     *
     * @return D
     */
    @Override
    public RealMatrix getBlockDiagonalMatrix() {
        final int n = this.getDimensionSize();
        final double[] d = new double[this.realEigenvalues.size()];
        for (int valueIndex = 0; valueIndex < d.length; valueIndex++)
            d[valueIndex] = this.realEigenvalues.get(valueIndex).getValue();
        final double[] e = new double[this.imaginaryEigenvalues.size()];
        for (int valueIndex = 0; valueIndex < d.length; valueIndex++)
            e[valueIndex] = this.imaginaryEigenvalues.get(valueIndex).getValue();

        final double[][] blockDiagonalMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                blockDiagonalMatrix[i][j] = 0.0;
            blockDiagonalMatrix[i][i] = d[i];
            if (e[i] > 0)
                blockDiagonalMatrix[i][i + 1] = e[i];
            else if (e[i] < 0)
                blockDiagonalMatrix[i][i - 1] = e[i];
        }
        return new SimpleRealMatrix(blockDiagonalMatrix);
    }
}
