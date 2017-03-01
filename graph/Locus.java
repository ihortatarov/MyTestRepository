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
import jscicalc.OObject;
import jscicalc.complex.Complex;

/**
 * This class represents the locus of an OObject (usually as a graph y=f(x)).
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class Locus extends Element implements Runnable {
    /**
     * Construct the locus from an OObject, Model and View. This initialises the
     * locus of points to represent something sensible within the view.
     * @param oobject The OObject that this Locus is to represent.
     * @param view The View
     */
    public Locus( final OObject oobject, View view ){
	System.out.print( "*********** OObject is a " );
	System.out.println( oobject.toString() );
	lock = new Object();
	this.oobject = oobject;
	path = new java.awt.geom.Path2D.Double();
	// Create a substitution
	incrementValue = 2; // default value
	substitution = new jscicalc.Substitution();
	setup( view );
    }
    /**
     * Set or reset the information required. This should be done each time the view
     * transformation of view size changes.
     * @param view The View
     */
    public void setup( View view ){
	System.out.println( "Locus.setup()" );
	this.view = view;
	restart = new java.util.concurrent.atomic.AtomicBoolean( false );
	thread = new Thread( this );
	thread.start();
    }

    /**
     * Takes a view and updates the path from it using the OObject belonging to this.
     */
    public void run(){
	System.out.print( "Starting thread " );
	Thread thisThread = Thread.currentThread();
	System.out.println( thisThread.getName() );
	while( thread == thisThread ){
	    System.out.println( "Starting loop" );
	    restart.set( false );
	    double xv = -view.distance(); // this is where we start
	    java.util.LinkedList<Point> list = new java.util.LinkedList<Point>();
	    
	    // Alias for trasformation
	    Transformation t = view.getTransformation();
	    
	    // initialise list
	    for( double x = -view.distance(); x < view.getWidth() + view.distance();
		 x += increment() ){
		list.add( new Point( x, function( x, t ) ) );
		if( restart.get() == true ){
		    System.out.println( "restart 1" );
		    break;
		}
	    }
	    System.out.println( "restart 2" );
	    if( restart.get() == true ) continue;
	    
	    // Now try dealing with cases where we have infinite values
	    // or large distances
	    for( java.util.ListIterator<Point> i = list.listIterator();
		 i.hasNext() && restart.get() == false; ){
		Point p = i.next();
		if( !i.hasNext() ) break;
		Point q = i.next();
		i.previous(); // Go back to p
		double xp = p.getX();
		double xq = q.getX();
		double d = xq - xp;
		System.out.print( "Locus.run(): x = " );
		System.out.print( xp );
		System.out.print( ", d = " );
		System.out.println( d );
		double yp = p.getY();
		double yq = q.getY();
		System.out.print( "yp = " );
		System.out.print( yp );
		System.out.print( ", yq = " );
		System.out.println( yq );
		// first case p in range, q infinite
		if( yp < view.getHeight() && yp > 0 &&
		    (Double.isInfinite( yq ) || Double.isNaN( yq )) ){
		    if( d > delta ){
			i.add( new Point( xp + d / 2, function( xp + d / 2, t ) ) );
			i.previous();
			if( i.hasPrevious() ) i.previous();
		    }
		} else if( yq < view.getHeight() && yq > 0 &&
			   // p infinite, q in range
			   (Double.isInfinite( yp ) || Double.isNaN( yp )) ){
		    if( d > delta ){
			i.add( new Point( xp + d / 2, function( xp + d / 2, t ) ) );
			i.previous();
			if( i.hasPrevious() ) i.previous();
		    }
		} else if(  !(Double.isInfinite( yp ) || Double.isNaN( yp ) ||
			      Double.isInfinite( yq ) || Double.isNaN( yq )) &&
			    ((yp < view.getHeight() && yp > 0) ||
			     (yq < view.getHeight() && yq > 0)) ){
		    if( p.distance( q ) > distance ){
			if( d > delta ){
			    i.add( new Point( xp + d / 2, function( xp + d / 2, t ) ) );
			    i.previous();
			    if( i.hasPrevious() ) i.previous();
			}
		    }
		}	
	    }
	    System.out.println( "restart 3" );
	    if( restart.get() == true ) continue;
	    
	    double lb = 0;
	    double ub = view.getHeight();
	    System.out.print( "lb = " );
	    System.out.println( lb );
	    java.util.Vector<PointList> pl = new java.util.Vector<PointList>();
	    PointList pointList = new PointList();
	    for( java.util.ListIterator<Point> i = list.listIterator();
		 i.hasNext() && restart.get() == false; ){
		Point p = i.next();
		double yp = p.getY();
		if( Double.isInfinite( yp ) || Double.isNaN( yp ) )
		    // not sane point
		    continue;
		else if( yp > lb && yp < ub ){
		    // valid point: add it
		    pointList.add( p );
		} else {
		    /* point out of range: add it if
		     * (i) pointList empty and next point in range (if there is one)
		     * (ii) pointList not empty, but then add to vector and start
		     *      again
		     */
		    if( pointList.isEmpty() ){
			if( i.hasNext() ){
			    Point q = i.next();
			    double yq = q.getY();
			    i.previous();
			    if( !(Double.isInfinite( yq ) || Double.isNaN( yq )) && 
				yq > lb && yq < ub ){
				// next point is in range: add this
				pointList.add( p );
			    }
			}
		    } else { // pointlist is not empty
			pointList.add( p ); // added out or range point
			pl.add( pointList );
			pointList = new PointList();
		    }
		}
	    } // finished
	    System.out.println( "restart 4" );
	    if( restart.get() == true ) continue;

	    if( !pointList.isEmpty() )
		pl.add( pointList );

	    // clear path
	    java.awt.geom.Path2D.Double localPath = new java.awt.geom.Path2D.Double();
	    for( java.util.ListIterator<PointList> j = pl.listIterator();
		 j.hasNext(); ){
		PointList l = j.next();
		l.addToPath( localPath, restart );
	    }
	    synchronized( path ){ // don't try to draw simultaneously
		path = localPath;
	    }
	    view.setCursor( null );
	    view.repaint();
	    synchronized( lock ){
		if( restart.get() == true ) continue;
		try {
		    System.out.println( "Waiting" );
		    lock.wait();
		    System.out.println( "finished waiting" );
		} catch( InterruptedException exception ){
		    System.out.println( "interrupted" );
		}
	    }
	} // end while loop
    }
    
    /**
     * Update the path
     */
    public void updatePath(){
	if( thread == null ) return;
	restart.set( true );
	synchronized( lock ){
	    System.out.println( "updatePath()" );
	    lock.notifyAll();
	}
    }

    /**
     * Stop the thread as soon as possible.
     */
    public void stop(){
	thread = null;
    }
    
    /**
     * Set substitution to variable = x
     * @param x The value to be substituted
     * @return A substitued object
     */
    protected OObject substitute( double x ){
	substitution.add( variable, new Complex( x ) );
	OObject result = oobject.substitute( substitution ).auto_simplify();
// 	System.out.print( "Locus.substitute(): " );
// 	final int maxLength = 120;
// 	final int sigDigits = 32;
// 	jscicalc.Base base = jscicalc.Base.DECIMAL;
// 	jscicalc.Notation notation = new jscicalc.Notation();
// 	double factor = 1;
// 	System.out.println( result.toHTMLString( maxLength, sigDigits,
// 						 base, notation, factor ) );
	return result;
    }

    /**
     * Get value of function at x if it is real and NaN otherwise. Use view
     * co&ouml;rdinates and transform to Model to do the calculation
     * @param x The value to be substituted
     * @param t The Transformation used when representing this Locus
     * @return a double Representing the outcome
     */
    protected double function( double x, Transformation t ){
	System.out.print( "[function( " );
	System.out.print( t.toModelX( x ) );
	System.out.print( " ) = " );
	OObject p = substitute( t.toModelX( x ) );
	if( p instanceof Complex ){
	    Complex z = (Complex)p;
	    if( Math.abs( z.imaginary() ) < epsilon ){
		double y = z.real();
		System.out.print( y );
		System.out.print( "]" );
		return t.toViewY( z.real() );
	    }
	}
	return Double.NaN;
    }
    
    /**
     * Draw the element on the view using the graphics object supplied.
     * @param model A Model
     * @param view A View of the Model
     * @param graphics2d A graphics context
     */
    public void draw( Model model, View view, java.awt.Graphics2D graphics2d ){
	synchronized( path ){
	    java.awt.Rectangle r = path.getBounds();
	    System.out.print( "path x =  " );
	    System.out.println( r.x );
	    System.out.print( "path y =  " );
	    System.out.println( r.y );
	    System.out.print( "path width =  " );
	    System.out.println( r.width );
	    System.out.print( "path height =  " );
	    System.out.println( r.height );
	    java.awt.Rectangle v = view.getBounds();
	    System.out.print( "view x =  " );
	    System.out.println( v.x );
	    System.out.print( "view y =  " );
	    System.out.println( v.y );
	    System.out.print( "view width =  " );
	    System.out.println( v.width );
	    System.out.print( "view height =  " );
	    System.out.println( v.height );
	    // 	if( r.x > v.x || r.y > v.y || r.width < v.width || r.height < v.height )
	    // 	    setup( view );
	    System.out.println( "drawing object" );
	    graphics2d.draw( path );
	}
    }
    /**
     * Get incrementValue.
     * @return The amount to increment x by in the view between points
     */
    double increment(){
	return incrementValue;
    }
    /**
     * We substitute for variable x: so we need a variable
     */
    protected static final jscicalc.expression.Variable variable
	= new jscicalc.expression.Variable( new jscicalc.pobject.Variable( 'x' ) );
    /**
     * We use substitution to substitute values for x
     */
    protected jscicalc.Substitution substitution;
    /**
     * Limit point at which imaginary part is considered zero
     */
    protected static final double epsilon = 1e-32;
    /**
     * Limit point for x distances
     */
    protected static final double delta = 1e-8;
    /**
     * Internal class used by find() functions.
     */
    protected class FindResult {
	/**
	 * Constructor.
	 * @param point The point
	 * @param interiorPoint The value of interiorPoint
	 * @param success The value of success
	 */
	FindResult( Point point, boolean interiorPoint, boolean success ){
	    this.point = point;
	    this.interiorPoint = interiorPoint;
	    this.success = success;
	}
	/**
	 * This is where we store points
	 */
	public Point point;
	/**
	 * Set to true if point is an interior point (inside the view frame)
	 */
	public boolean interiorPoint;
	/**
	 * Set to true if find() finds a point successfully.
	 */
	public boolean success;
    }
    /**
     * The standard x increment (in view) for this locus.
     */
    private double incrementValue = 10;
    /**
     * The minimum distance between points
     */
    private double distance = 16;
    /**
     * The OObject represented by this locus.
     */
    private final OObject oobject;
    /**
     * A path representing this locus.
     */
    private java.awt.geom.Path2D.Double path;
    /**
     * Used for finding the View
     */
    private View view;
    /**
     * Set thread to null to terminate it
     */
    private Thread thread;
    /**
     * Used to lock threads
     */
    private Object lock;
    /**
     * Flag to indicate that an update should restart as soon as possible
     */
    java.util.concurrent.atomic.AtomicBoolean restart;
}
	
