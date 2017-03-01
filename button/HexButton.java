/** @file
 * Copyright (C) 2005 John D Lamb (J.D.Lamb@btinternet.com)
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

package jscicalc.button;
import jscicalc.CalculatorApplet;
import jscicalc.pobject.Hex;

/**
 * Button to set Base to HEXADECIMAL.
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class HexButton extends EqualsButton {

    /**
     * Constructor. Sets PObject to Hex.
     * @param applet The <em>controller</em> object.
     */
    public HexButton( CalculatorApplet applet ){
	this.applet = applet;
	this.pobject = new Hex();
	setText();
	tooltip = pobject.tooltip();
	shortcut = pobject.shortcut();
	setTextSize();
	setToolTipText();
	changeBase = ChangeBase.HEXADECIMAL;
	addActionListener( this );
    }

    private static final long serialVersionUID = 1L;   
}
