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

/**
 * This class is designed to create a view of a graph
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class View extends javax.swing.JPanel {
    
    /**
     * The constructor uses a copy of a model so that the view knows what to display.
     * @param model A model containing elements to be displayed
     * @param graph The graph: used to update menu sizes
     */
    public View( Model model, Graph graph ){
	this.model = model;
	this.graph = graph;
	forceUpdateBool = new java.util.concurrent.atomic.AtomicBoolean( false );
	transformation = new Transformation( this );
	
	java.awt.Font font = javax.swing.UIManager.getFont( "Label.font" );
	setFont( font.deriveFont( (float)10.0 ) );
	distanceValue = 1; // default value
	lastWidth = lastHeight = 0;
    }
    /**
     * Force an update to Locus objects when reapaint is called
     */
    public void forceUpdate(){
	forceUpdateBool.set( true );
    }
    
    /**
     * Overrides parent method.
     * @param graphics The standard parameter
     */
    public void paintComponent( java.awt.Graphics graphics ){
	if( lastWidth != getWidth() || lastHeight != getHeight()
	    || forceUpdateBool.compareAndSet( true, false ) ){
	    System.out.println( "*** changed size  or forced update *** " );
	    graph.updateMenu();
	    setCursor( new java.awt.Cursor( java.awt.Cursor.WAIT_CURSOR ) );
	    model.updatePaths();
	    lastWidth = getWidth();
	    lastHeight = getHeight();
	}
	// change this to vectorgraphics
	java.awt.Graphics2D graphics2d = (java.awt.Graphics2D)graphics;
	graphics2d.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING,
				     java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
	graphics2d.setColor( java.awt.Color.WHITE );
	graphics2d.fillRect( 0, 0, lastWidth, lastHeight );
	graphics2d.setColor( java.awt.Color.BLACK );
	model.draw( this, graphics2d );
	System.out.println( "finished updating view" );
    }

    /**
     * Get the transformation from the model to the view. This depends on the
     * view because the model can assume it can draw anywhere.
     * @return The transformation
     */
    public final Transformation getTransformation(){
	return transformation;
    }
    
    /**
     * Get a copy of the model.
     * @return The model
     */
    public Model getModel(){
	return model;
    }
    /**
     * The distance is the maximum distance between Point objects in the View. We wish
     * to draw a spline through a set of Point objects and if we cannot find two within
     * this distance, it is probably better not to join them.
     * @return The distance
     */
    double distance(){
	return distanceValue;
    }
    
    /**
     * The Graph instance.
     */
    protected Graph graph;
    /**
     * The Model instance.
     */
    protected Model model;
    /**
     * The transformation used to get the view from the model
     */
    protected Transformation transformation;
    /**
     * The distanceCalue is the maximum distance between Point objects in the view.
     */
    protected double distanceValue;
    /**
     * Use to limit searches
     */
    public static final double delta = 0.001;
    /**
     * Last recorded width
     */
    private int lastWidth;
    /**
     * Last recorded height
     */
    private int lastHeight;
    /**
     * used to force update even if size has not changed
     */
    java.util.concurrent.atomic.AtomicBoolean forceUpdateBool;

    private static final long serialVersionUID = 1L;   
}
	
