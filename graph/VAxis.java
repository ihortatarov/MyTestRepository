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
 * This object represents a horizontal axis.
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class VAxis extends Axis {
    /**
     * Constructor. Defines Y axis to be zero
     */
    public VAxis(){
	setX( 0 );
    }

    /**
     * Set the value of x.
     * @param x The new value
     */
    public void setX( double x ){
	this.x = x;
    }

    /**
     * Draw the element on the view using the graphics object supplied.
     * @param model A Model
     * @param view A View of the Model
     * @param graphics2d A graphics context
     */
    public void draw( Model model, View view, java.awt.Graphics2D graphics2d ){
	// Draw the axis. First find bottom and top of view in model.
	double top = view.getTransformation().toModelY( 0 ); 
	double bottom = view.getTransformation().toModelY( view.getHeight() ); 
	double x = view.getTransformation().toViewX( this.x );
	// Draw a line
	java.awt.geom.Line2D.Double line
	    = new java.awt.geom.Line2D.Double( x, 0, x, view.getHeight() );
	graphics2d.draw( line );

	if( getShowMajorUnit() ){
	    // Add major unit elements
	    double unit = view.getTransformation().getYMajorUnit();
	    double origin = view.getTransformation().getOriginY();
	    for( int i = (int)Math.ceil( bottom / unit ) - 2;
		 i < (int)Math.floor( top / unit ) + 1; ++i ){
		// Draw a tick
		double tick = view.getTransformation().toViewY( i * unit );
		line = new java.awt.geom.Line2D.Double( x, tick,
							x - getMajorUnitTickLength(),
							tick );
		graphics2d.draw( line );
		
		// Put a number underneath
		if( i * unit == origin ) continue; // no zero
		String s = convertDouble( i * unit );
		graphics2d.setFont( view.getFont() );
		java.awt.font.TextLayout textLayout
		    = new java.awt.font.TextLayout( s, graphics2d.getFont(),
						    graphics2d.getFontRenderContext() );
		float width = textLayout.getAdvance();
		float height = textLayout.getAscent();
		graphics2d.drawString( s, (float)x - width - getMajorUnitTickLength() - 2,
				       (float)tick + (float)0.5 * height );
	    }
	}
	
	if( getShowMinorUnit() ){
	    // Add minor unit elements
	    double unit = view.getTransformation().getYMinorUnit();
	    for( int i = (int)Math.ceil( bottom / unit ) - 2;
		 i < (int)Math.floor( top / unit ) + 1; ++i ){
		// Draw a tick
		double tick = view.getTransformation().toViewY( i * unit );
		line = new java.awt.geom.Line2D.Double( x, tick,
							x - getMinorUnitTickLength(),
							tick );
		graphics2d.draw( line );
	    }
	}
    }

    /**
     * The position of the axis.
     */
    protected double x;
}
