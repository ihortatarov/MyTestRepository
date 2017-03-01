package jscicalc.graph;

public class Test {
    public static void main( String[] args ){
	final double distance = 5;
	final double step = 1;
	final double h = 20;
	final double w = 30;
	final double delta = 1e-32;
	java.util.LinkedList<Point> list = new java.util.LinkedList<Point>();

	// initialise list
	for( double x = -w; x < w + step; x += step ){
	    list.add( new Point( x, f( x ) ) );
	}
	System.out.println( "Initial list is:" );
	for( java.util.ListIterator<Point> i = list.listIterator(); i.hasNext(); ){
	    Point p = i.next();
	    System.out.print( p.getX() );
	    System.out.print( ", " );
	    System.out.println( p.getY() );
	}
	System.out.println( "---------------------" );
	
	// Now try dealing with cases where we have infinite values or large distances
	for( java.util.ListIterator<Point> i = list.listIterator(); i.hasNext(); ){
	    Point p = i.next();
	    if( !i.hasNext() ) break;
	    Point q = i.next();
	    i.previous(); // Go back to p
	    double xp = p.getX();
	    //System.out.print( xp );
	    //System.out.print( ": " );
	    double xq = q.getX();
	    double d = xq - xp;
	    double yp = p.getY();
	    double yq = q.getY();
	    // first case p in range, q infinite
	    if( yp < h && yp > -h && (Double.isInfinite( yq ) || Double.isNaN( yq )) ){
		if( d > delta ) i.add( new Point( xp + d / 2, f( xp + d / 2 ) ) );
		i.previous();
		i.previous();
	    } else if( yq < h && yq > -h && // p infinite, q in range
		       (Double.isInfinite( yp ) || Double.isNaN( yp )) ){
		if( d > delta ) i.add( new Point( xp + d / 2, f( xp + d / 2 ) ) );
		i.previous();
		i.previous();
	    } else if(  !(Double.isInfinite( yp ) || Double.isNaN( yp ) ||
			  Double.isInfinite( yq ) || Double.isNaN( yq )) &&
			((yp < h && yp > -h) || (yq < h && yq > -h)) ){
		//System.out.print( p.distance( q ) );
		if( p.distance( q ) > distance ){
		    //System.out.print( "  Adding point" );
		    if( d > delta ) i.add( new Point( xp + d / 2, f( xp + d / 2 ) ) );
		    i.previous();
		    i.previous();
		}
	    }	
	    //System.out.println();
	}
	System.out.println( "First revision list is:" );
	for( java.util.ListIterator<Point> i = list.listIterator(); i.hasNext(); ){
	    Point p = i.next();
	    System.out.print( p.getX() );
	    System.out.print( ", " );
	    System.out.println( p.getY() );
	}
	System.out.println( "---------------------" );
	
	java.util.Vector<PointList> pl = createList( list, -h, h );
	for( java.util.ListIterator<PointList> j = pl.listIterator(); j.hasNext(); ){
	    PointList l = j.next();
	    System.out.println( "list is:" );
	    for( java.util.ListIterator<Point> i = l.listIterator(); i.hasNext(); ){
		Point p = i.next();
		System.out.print( p.getX() );
		System.out.print( ", " );
		System.out.println( p.getY() );
	    }
	    System.out.println( "---------------------" );
	}
    }

    public static java.util.Vector<PointList>
	createList( java.util.LinkedList<Point> list, double lb, double ub ){
	java.util.Vector<PointList> vector = new java.util.Vector<PointList>();
	PointList pointList = new PointList();
	for( java.util.ListIterator<Point> i = list.listIterator(); i.hasNext(); ){
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
		 * (ii) pointList not empty, but then add to vector and start again
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
		    vector.add( pointList );
		    pointList = new PointList();
		}
	    }
	} // finished
	if( !pointList.isEmpty() )
	    vector.add( pointList );
	return vector;
    }
    
    public static double f( double x ){
	return 1 / (x * x);
    }
}
