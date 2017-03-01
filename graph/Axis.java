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
 * This object represents an axis.
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public abstract class Axis extends Element {
    /**
     * Constructor.
     */
    public Axis(){
	numberFormat = java.text.NumberFormat.getNumberInstance();
	if( numberFormat instanceof java.text.DecimalFormat ){ // as it should be
	    java.text.DecimalFormat df = (java.text.DecimalFormat)numberFormat;
	    df.setNegativePrefix( "\u2212" ); // make sure it's a genuine minus sign
	}
	majorUnitTick = 5;
	minorUnitTick = 2;
	setShowMajorUnit( true );
	setShowMinorUnit( true );
    }

    /**
     * Set whether (<em>true</em>) or not (<em>false</em>) we should show the major
     * units on the axis.
     * @param value Set <em>true</em> to show major unit, <em>false</em> not to show it
     */
    public void setShowMajorUnit( boolean value ){
	showMajorUnit = value;
    }
    /**
     * Set whether (<em>true</em>) or not (<em>false</em>) we should show the minor
     * units on the axis.
     * @param value Set <em>true</em> to show minor unit, <em>false</em> not to show it
     */
    public void setShowMinorUnit( boolean value ){
	showMinorUnit = value;
    }
    /**
     * Get the value of showMajorUnit
     * @return <em>true</em> if we should show major units; <em>false</em> otherwise
     */
    public boolean getShowMajorUnit(){
	return showMajorUnit;
    }
    /**
     * Get the value of showMinorUnit
     * @return <em>true</em> if we should show minor units; <em>false</em> otherwise
     */
    public boolean getShowMinorUnit(){
	return showMinorUnit;
    }
    
    /**
     * Get the length of tick used for major unit.
     * @return the length of tick used for major unit
     */
    public int getMajorUnitTickLength(){
	return majorUnitTick;
    }
    /**
     * Get the length of tick used for major unit.
     * @return the length of tick used for major unit
     */
    public int getMinorUnitTickLength(){
	return minorUnitTick;
    }
    /**
     * Convert a number to a string.
     */
    public String convertDouble( double d ){
	return numberFormat.format( d );
    }

    /**
     * Whether the major unit is shown
     */
    protected boolean showMajorUnit;
    /**
     * Whether the minor unit is shown
     */
    protected boolean showMinorUnit;

    /**
     * The length of major unit ticks in View units
     */
    protected int majorUnitTick ;
    /**
     * The length of major unit ticks in View units
     */
    protected int minorUnitTick ;
    /**
     * Used to convert numbers to strings
     */ 
    java.text.NumberFormat numberFormat;
}
