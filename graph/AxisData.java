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
 * This class represents data for an Axis
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class AxisData {
    
    /**
     * The constructor.
     */
    public AxisData(){
	minimum = -10;
	maximum = 10;
	majorUnit = 5;
	minorUnit = 5;
	majorVisible = true;
	minorVisible = true;
    }
    /**
     * Copy to this
     */
    public void assign( final AxisData axisData ){
	minimum = axisData.minimum;
	maximum = axisData.maximum;
	majorUnit = axisData.majorUnit;
	minorUnit = axisData.minorUnit;
	majorVisible = axisData.majorVisible;
	minorVisible = axisData.minorVisible;
    }

    /**
     * Partial equality operator: return <em>true</em> if and only axisData is identical
     * to this for minimum and maximum values. This is used so that we can tell whether
     * we need to update locus elements in response to a change in an Axis.
     * @param axisData Another object of this class
     * @return <em>true</em> or <em>false</em> according as minimum and maximum match or
     * fail to match
     */
    boolean minMaxMatches( AxisData axisData ){
	if( axisData.minimum != minimum ) return false;
	if( axisData.maximum != maximum ) return false;
	return true;
    }
    /**
     * Equality operator: return <em>true</em> if and only axisData is identical
     * to this
     * @param axisData Another object of this class
     * @return <em>true</em> or <em>false</em> according as every element of this matches
     * or not
     */
    boolean equals( AxisData axisData ){
	if( axisData.minimum != minimum ) return false;
	if( axisData.maximum != maximum ) return false;
	if( axisData.majorUnit != majorUnit ) return false;
	if( axisData.minorUnit != minorUnit ) return false;
	if( axisData.majorVisible != majorVisible ) return false;
	if( axisData.minorVisible != minorVisible ) return false;
	return true;
    }
    
    /**
     * The minimum
     */
    public double minimum;
    /**
     * The maximum
     */
    public double maximum;
    /**
     * The major unit
     */
    public double majorUnit;
    /**
     * The minor unit
     */
    public double minorUnit;
    /**
     * Is major unit visible?
     */
    public boolean majorVisible;
    /**
     * Is minor unit visible?
     */
    public boolean minorVisible;
}
