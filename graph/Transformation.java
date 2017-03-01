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
 * This class represents a transformation of real coordinates to View coordinates.
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class Transformation {
    
    /**
     * Create a transformation.
     * @param view Used to initialise the transformation.
     */
    public Transformation( View view ){
	this.view = view;
	setScale( 10 );
	setOrigin( 0, 0 );
	setXMajorUnit( 5 );
	setXMinorUnit( 1 );
	setYMajorUnit( 5 );
	setYMinorUnit( 1 );
    }
    /**
     * Set the scale. The scale determines how the Model co&ouml;rdinates get transformed
     * to View co&ouml;rdinates: a distance of scale in the View corresponds to a distance
     * of 1 in the Model.
     * @param scale The new value of scale
     */
    public void setScale( double scale ){
	if( scale <= 0 ){
	    System.err
		.println( "Ignoring attempt to set invalid scale in Transformation." );
	    return;
	}
	xScale = yScale = scale;
    }
    /**
     * Set the vertical scale. The scale determines how the Model co&ouml;rdinates
     * get transformed
     * to View co&ouml;rdinates: a distance of scale in the View corresponds to a distance
     * of 1 in the Model.
     * @param scale The new value of scale
     */
    public void setScaleY( double scale ){
	if( scale <= 0 ){
	    System.err
		.println( "Ignoring attempt to set invalid scale in Transformation." );
	    return;
	}
	yScale = scale;
    }

    /**
     * Set the horizontal scale. The scale determines how the Model co&ouml;rdinates
     * get transformed
     * to View co&ouml;rdinates: a distance of scale in the View corresponds to a distance
     * of 1 in the Model.
     * @param scale The new value of scale
     */
    public void setScaleX( double scale ){
	if( scale <= 0 ){
	    System.err
		.println( "Ignoring attempt to set invalid scale in Transformation." );
	    return;
	}
	xScale = scale;
    }


    /**
     * Set the point in the Model that appears in the centre of the View.
     * @param x The x co&ouml;rdinate
     * @param y The x co&ouml;rdinate
     */
    public void setOrigin( double x, double y ){
	xOrigin = x;
	yOrigin = y;
    }
    /**
     * Set the x co&ouml;ordinate of the point in the Model that appears in the centre
     * of the View.
     * @param x The x co&ouml;rdinate
     */
    public void setOriginX( double x ){
	xOrigin = x;
    }
    /**
     * Set the y co&ouml;ordinate of the point in the Model that appears in the centre
     * of the View.
     * @param y The y co&ouml;rdinate
     */
    public void setOriginY( double y ){
	yOrigin = y;
    }
    /**
     * Get the origin x co&ouml;rdinate (Model)
     * @return the origin x co&ouml;rdinate
     */
    double getOriginX(){
	return xOrigin;
    }
    /**
     * Get the origin y co&ouml;rdinate (Model)
     * @return the origin y co&ouml;rdinate
     */
    double getOriginY(){
	return yOrigin;
    }

    /**
     * Convert a View x co&ouml;rdinate to a Model x co&ouml;rdinate.
     * @param x The View x co&ouml;rdinate.
     * @return The Model x co&ouml;rdinate.
     */
    public double toModelX( double x ){
	return xOrigin + (x - (double)view.getWidth() / 2) / xScale;
    }
    /**
     * Convert a View y co&ouml;rdinate to a Model y co&ouml;rdinate.
     * @param y The View y co&ouml;rdinate.
     * @return The Model y co&ouml;rdinate.
     */
    public double toModelY( double y ){
	return yOrigin + ((double)view.getHeight() / 2 - y) / yScale;
    }
    /**
     * Convert a Model x co&ouml;rdinate to a View x co&ouml;rdinate.
     * @param x The Model x co&ouml;rdinate.
     * @return The View x co&ouml;rdinate.
     */
    public double toViewX( double x ){
	return (x - xOrigin) * xScale + (double)view.getWidth() / 2;
    }
    /**
     * Convert a Model y co&ouml;rdinate to a View y co&ouml;rdinate.
     * @param y The Model y co&ouml;rdinate.
     * @return The View y co&ouml;rdinate.
     */
    public double toViewY( double y ){
	return (double)view.getHeight() / 2 + (yOrigin - y) * yScale;
    }
    /**
     * Get the major unit in the horizontal direction.
     * @return the major unit in the horizontal direction
     */
    double getXMajorUnit(){
	return xMajorUnit;
    }
    /**
     * Set the major unit in the horizontal direction.
     * @param unit The new major unit in the horizontal direction
     */
    void setXMajorUnit( double unit ){
	xMajorUnit = unit;
    }
    /**
     * Get the minor unit in the horizontal direction.
     * @return the minor unit in the horizontal direction
     */
    double getXMinorUnit(){
	return xMinorUnit;
    }
    /**
     * Set the minor unit in the horizontal direction.
     * @param unit The new minor unit in the horizontal direction
     */
    void setXMinorUnit( double unit ){
	xMinorUnit = unit;
    }
    /**
     * Get the major unit in the vertical direction.
     * @return the major unit in the vertical direction
     */
    double getYMajorUnit(){
	return yMajorUnit;
    }
    /**
     * Set the major unit in the vertical direction.
     * @param unit The new major unit in the vertical direction
     */
    void setYMajorUnit( double unit ){
	yMajorUnit = unit;
    }
    /**
     * Get the minor unit in the vertical direction.
     * @return the minor unit in the vertical direction
     */
    double getYMinorUnit(){
	return yMinorUnit;
    }
    /**
     * Set the minor unit in the vertical direction.
     * @param unit The new minor unit in the vertical direction
     */
    void setYMinorUnit( double unit ){
	yMinorUnit = unit;
    }
    /**
     * The View
     */
    protected View view;
    /**
     * Horizontal scale
     */
    protected double xScale;
    /**
     * Vertical scale
     */
    protected double yScale;
    /**
     * The x co&ouml;rdinate of the origin in the Model
     */
    protected double xOrigin;
    /**
     * The y co&ouml;rdinate of the origin in the Model
     */
    protected double yOrigin;
    /**
     * The major unit on the horizontal axis. This is used for marking the axis.
     * The major unit is defined in Model co&ouml;rdinates so that, for example, it still
     * will make sense in a logarithmic scale. A value of zero indicates that no major 
     * units should be set.
     */
    protected double xMajorUnit;
    /**
     * The minor unit on the horizontal axis.
     */
    protected double xMinorUnit;
    /**
     * The major unit on the vertical axis. This is used for marking the axis.
     * The major unit is defined in Model co&ouml;rdinates so that, for example, it stil
     * will make sense in a logarithmic scale. A value of zero indicates that no major 
     * units should be set.
     */
    protected double yMajorUnit;
    /**
     * The minor unit on the vertical axis.
     */
    protected double yMinorUnit;
}
	
