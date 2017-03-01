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
public class HAxis extends Axis {
    /**
     * Constructor. Defines Y axis to be zero
     */
    public HAxis(){
	setY( 0 );
    }

    /**
     * Set the value of y.
     * @param y The new value
     */
    public void setY( double y ){
	this.y = y;
    }

    /**
     * Draw the element on the view using the graphics object supplied.
     * @param model A Model
     * @param view A View of the Model
     * @param graphics2d A graphics context
     */
    public void draw( Model model, View view, java.awt.Graphics2D graphics2d ){
	// Draw the axis. First find bottom and top of view in model.
	double left = view.getTransformation().toModelX( 0 ); 
	double right = view.getTransformation().toModelX( view.getWidth() ); 
	double y = view.getTransformation().toViewY( this.y );
	// Draw a line
	java.awt.geom.Line2D.Double line
	    = new java.awt.geom.Line2D.Double( 0, y, view.getWidth(), y);
	graphics2d.draw( line );

	if( getShowMajorUnit() ){
	    // Add major unit elements
	    double unit = view.getTransformation().getXMajorUnit();
	    double origin = view.getTransformation().getOriginY();
	    for( int i = (int)Math.ceil( left / unit ) - 1;
		 i < (int)Math.floor( right / unit ) + 2; ++i ){
		// Draw a tick
		double tick = view.getTransformation().toViewX( i * unit );
		line = new java.awt.geom.Line2D.Double( tick, y,
							tick,
							y + getMajorUnitTickLength() );
		graphics2d.draw( line );
		// Put a number underneath
		String s = convertDouble( i * unit );
		graphics2d.setFont( view.getFont() );
		java.awt.font.TextLayout textLayout
		    = new java.awt.font.TextLayout( s, graphics2d.getFont(),
						    graphics2d.getFontRenderContext() );
		float width = textLayout.getVisibleAdvance();
		float height = textLayout.getAscent() + textLayout.getLeading();
		if( i * unit != origin ) 
		    graphics2d.drawString( s, (float)tick - (float)0.5 * width,
					   (float)y + (float)getMajorUnitTickLength()
					   + height );
		else
		    graphics2d.drawString( s, (float)tick - width
					   - getMajorUnitTickLength() - 2,
					   (float)y + (float)getMajorUnitTickLength()
					   + height );
	    }
	}

	if( getShowMinorUnit() ){
	    // Add minor unit elements
	    double unit = view.getTransformation().getXMinorUnit();
	    for( int i = (int)Math.ceil( left / unit ) - 1;
		 i < (int)Math.floor( right / unit ) + 2; ++i ){
		// Draw a tick
		double tick = view.getTransformation().toViewX( i * unit );
		line = new java.awt.geom.Line2D.Double( tick, y,
							tick, y
							+ getMinorUnitTickLength() );
		graphics2d.draw( line );
	    }
	}
    }

    /**
     * The position of the axis.
     */
    protected double y;
}
