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

package jscicalc.button;
import jscicalc.CalculatorApplet;

/**
 * Button to make graph appear.
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class GraphButton extends CalculatorButton {

    /**
     * Constructor. Sets PObject to Off.
     * @param applet The <em>controller</em> object.
     */
    public GraphButton( CalculatorApplet applet ){
	this.applet = applet;
	this.pobject = new jscicalc.pobject.Graph();
	setText();
	setTextSize();
	tooltip = "use to display a graph";
	shortcut = 'G';
	setToolTipText();
	addActionListener( this );
    }

    /**
     * Ask CalculatorApplet to display a graph.
     * @param actionEvent The event that generated this method call: usually a button
     * press or called when CalculatorApplet responded to the key associated with
     * this button
     */
    public void actionPerformed( java.awt.event.ActionEvent actionEvent ){
	applet.displayGraph();
	getApplet().setShift( false );
	getApplet().updateDisplay( false, true );
    }

    private static final long serialVersionUID = 1L;   
}
