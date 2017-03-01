/** @file
 * Copyright (C) 2008 John D Lamb (J.D.Lamb@btinternet.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package jscicalc.graph;

/**
 * This represents a list of Point objects in a View.
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class PointList extends java.util.Vector<Point> {
    /**
     * Constructor. Does nothing.
     */
    public PointList(){
    }
    /**
     * Add points to a Path. Actually constructs a B&eacute;zier cubic spline.
     * @param restart If this is true, return immediately
     * @param path The path to add some points to
     */
    public void addToPath( java.awt.geom.Path2D.Double path,
			   java.util.concurrent.atomic.AtomicBoolean restart ){
	System.out.println( "add to path" );
	if( size() == 0 )
	    return; // nothing to add
	else if( size() == 1 ){
	    // add a single, isolated point
	    Point p = firstElement();
	    path.moveTo( p.getX(), p.getY() );
	} else {
	    double[][] c = controlPoints( restart );
	    if( restart.get() ) return;
 	    // first calculate a cubic spline function
 	    Point p = firstElement();
 	    double x0 = p.getX();
 	    double y0 = p.getY();
 	    path.moveTo( x0, y0 );
 	    for( int j = 1; j < size(); ++j ){
		// control points
		double x1 = c[0][2 * j - 2];
		double x2 = c[0][2 * j - 1];
		double y1 = c[1][2 * j - 2];
		double y2 = c[1][2 * j - 1];
 		// next point
 		p = elementAt( j );
 		double x3 = p.getX();
 		double y3 = p.getY();
 		path.curveTo( x1, y1, x2, y2, x3, y3 );
 		// update x0, y0
 		x0 = x3;
 		y0 = y3;
	    }
	}
    }
    /**
     * Used exclusively by addToPath().
     * @param j The point at which to evaluate the gradient
     * @param derivative2 An array of second derivatives
     * @return A gradient
     */
    private double gradient( final int j, final double[] derivative2 ){
	if( j == 0 ){
	    Point pjm = elementAt( j );
	    Point pj = elementAt( j + 1 );
	    double x = pj.getX() - pjm.getX();
	    double y = pj.getY() - pjm.getY();
	    return y / x - x * (derivative2[j + 1] - derivative2[j]) / 6
		+ x * derivative2[j + 1] / 2;
	} else {
	    Point pjm = elementAt( j - 1 );
	    Point pj = elementAt( j );
	    double x = pj.getX() - pjm.getX();
	    double y = pj.getY() - pjm.getY();
	    return y / x - x * (derivative2[j] - derivative2[j - 1]) / 6
		- x * derivative2[j - 1] / 2;
	}
    }

    /**
     * Used internally to get the control points for a set of cubic splines. These are
     * in the order x0+, x1-, x1+, x2-, ..., xn- and there are 2n - 1 of them where n
     * is the number of points we have. The first index tells us if we are dealing with 
     * the x coordinate (0) or the y (1).
     * @param restart If this is true, return immediately
     * @return A set of control points
     */
    private double[][] controlPoints( java.util.concurrent.atomic.AtomicBoolean restart ){
	// set up return matrix
	int N = size();
	double[][] c = new double[2][2 * N - 2];
	// set up matrix for calculation
	double[][] A = new double[2 * N - 2][2 *N - 2];
	A[0][0] = 3;
	for( int i = 1; i < N - 1; ++i ){
	    if( restart.get() ) return null;
	    A[2 * i - 1][2 * i - 2] = 1;
	    A[2 * i - 1][2 * i - 1] = -2;
	    A[2 * i - 1][2 * i] = 2;
	    A[2 * i - 1][2 * i + 1] = -1;
	    A[2 * i][2 * i - 1] = 1;
	    A[2 * i][2 * i] = 1;
	}
	A[2 * N - 3][2 * N - 3] = 3;
	// LU decomposition
	LU_decompose( A, restart );
	if( restart.get() ) return null;
	// set upvector
	double[] b = new double[2 * N - 2];
	// substitute for x
	b[0] = 2 * elementAt( 0 ).getX() + elementAt( 1 ).getX();
	for( int i = 1; i < N - 1; ++i ){
	    if( restart.get() ) return null;
	    b[2 * i] = 2 * elementAt( i ).getX();
	}
	b[2 * N - 3] = elementAt( N - 2 ).getX() + 2 * elementAt( N - 1 ).getX();
	// solve for x
	c[0] = LU_solve( A, b, restart );
	if( restart.get() ) return null;
	// substitute for y
	b[0] = 2 * elementAt( 0 ).getY() + elementAt( 1 ).getY();
	for( int i = 1; i < N - 1; ++i ){
	    b[2 * i] = 2 * elementAt( i ).getY();
	}
	b[2 * N - 3] = elementAt( N - 2 ).getY() + 2 * elementAt( N - 1 ).getY();
	// solve for x
	c[1] = LU_solve( A, b, restart );
	if( restart.get() ) return null;
	return c;
    }

    /**
     * A number close to zero
     */
    private final double eps = 1e-10;
    
    static class MyView extends javax.swing.JPanel {
	MyView( java.awt.geom.Path2D.Double path ){
	    this.path = path;
	}
	public void paint( java.awt.Graphics g ){
	    java.awt.Graphics2D graphics2d = (java.awt.Graphics2D)g;
	    graphics2d.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING,
					 java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
	    graphics2d.draw( path );
	}
	/**
	 * A Path.
	 */
	java.awt.geom.Path2D.Double path;
	private static final long serialVersionUID = 1L;   
    }
    
    /**
     * In place LU decomposition of a matrix A. This should only be used internally. It
     * is not suitable for general LU decomposition.
     * @param restart If this is true, return immediately
     * @param A A square (not checked) matrix
     */
    private static void LU_decompose( double[][] A,
				      java.util.concurrent.atomic.AtomicBoolean restart ){
	int n = A.length;
	for( int i = 0; i < n - 1; ++i ){
	    System.out.print( "-" );
	    for( int j = i + 1; j < n; ++j ){
		if( restart.get() ) return;
		A[j][i] /= A[i][i];
		for( int k = i + 1; k < n; ++k ){
		    A[j][k] -= A[j][i] * A[i][k];
		}
	    }
	}
    }
    /**
     * Solve for LUx = b. Checks nothing. Only use internally.
     * @param LU An LU-decomposed matrix
     * @param b The vector of doubles
     * @param restart If this is true, return immediately
     * @return A solution vector
     */
    private static double[] LU_solve( final double[][] LU, final double[] b,
				      java.util.concurrent.atomic.AtomicBoolean restart ){
	int n = b.length;
	double[] x = new double[n];
	// Lx = b;
	x[0] = b[0];
	for( int i = 1; i < n; ++i ){
	    System.out.print( "+" );
	    if( restart.get() ) return null;
	    double total = 0;
	    for( int j = 0; j < i; ++j ){
		total += LU[i][j] * x[j];
	    }
	    x[i] = b[i] - total;
	}
	// Ux = x;
	x[n - 1] /= LU[n - 1][n - 1];
	for( int i = n - 2; i >= 0; --i ){
	    System.out.print( "=" );
	    if( restart.get() ) return null;
	    double total = 0;
	    for( int j = i + 1; j < n; ++j ){
		total += LU[i][j] * x[j];
	    }
	    x[i] -= total;
	    x[i] /= LU[i][i];
	}
	return x;
    }

    /**
     * Test code
     * @param args A dummy argument
     */
    public static void main( String[] args ){
	java.awt.geom.Path2D.Double path = new java.awt.geom.Path2D.Double();
	PointList pointList = new PointList();
	pointList.add( new Point( 100, 280 ) ); // -2 -8
	pointList.add( new Point( 200, 220 ) ); // -1 -1
	pointList.add( new Point( 300, 200 ) ); //  0  0
	pointList.add( new Point( 400, 180 ) ); //  1  1
	pointList.add( new Point( 500, 120 ) ); //  2  8
	java.util.concurrent.atomic.AtomicBoolean restart = 
	    new java.util.concurrent.atomic.AtomicBoolean( false );
	pointList.addToPath( path, restart );
	MyView view = new MyView( path );
	javax.swing.JFrame frame = new javax.swing.JFrame( "PointList Test" );
	frame.setSize( 600, 400 );
	frame.setContentPane( view );
	frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
	frame.setVisible( true );
    }
    private static final long serialVersionUID = 1L;   
}
