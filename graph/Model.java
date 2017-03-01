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
 * This class represents the model from which any particular View of a graph is
 * constructed. We use the model/view architecture so that we can export graph pictures
 * in a variety of formats.
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class Model {
    
    /**
     * Constructor. 
     */
    public Model(){
	xAxis = new HAxis();
	yAxis = new VAxis();
	elements = new java.util.LinkedList<Element>(); 
	elements.add( xAxis );
	elements.add( yAxis );
    }
    
    /**
     * Add an element to the list.
     * @param element The element to add
     */
    public void add( Element element ){
	elements.add( element );
    }

    /**
     * Set a unique Locus in the list.
     * @param locus The element to add
     */
    public void reset( Locus locus ){
	elements.clear();
	elements.add( xAxis );
	elements.add( yAxis );
	elements.add( locus );
    }
    
    /**
     * Draw the model on the view using the graphics object supplied.
     * @param view A view
     * @param graphics2d A graphics context
     */
    public void draw( View view, java.awt.Graphics2D graphics2d ){
	for( java.util.ListIterator<Element> i = elements.listIterator(); i.hasNext(); ){
	    i.next().draw( this, view, graphics2d );
	}
    }

    /**
     * Update paths in Locus objects
     *
     */
    public void updatePaths(){
	for( java.util.ListIterator<Element> i = elements.listIterator(); i.hasNext(); ){
	    Element element = i.next();
	    if( element instanceof Locus ){
		Locus locus = (Locus)element;
		locus.updatePath();
	    }
	}
    }

    /**
     * Get the x axis
     * @return The x axis
     */
    Axis getXAxis(){
	return xAxis;
    } 
    /**
     * Get the y axis
     * @return The y axis
     */
    Axis getYAxis(){
	return yAxis;
    } 

    /**
     * An x axis
     */
    private HAxis xAxis;
    /**
     * A y axis
     */
    private VAxis yAxis;
    
    /**
     * The list of elements to be drawn.
     */
    protected java.util.LinkedList<Element> elements;
}
	
