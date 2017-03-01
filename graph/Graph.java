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
 * This class is designed to create component to display a graph
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class Graph extends javax.swing.JFrame {
    
    /**
     * The constructor needs to know about the calculator
     * @param applet The calculator applet
     */
    public Graph( jscicalc.ReadOnlyCalculatorApplet applet ){
	super( "Scientific Calculator Graph" );
	model = new Model();
	view = new View( model, this );
	menu = new Menu( applet, view, model );
	setJMenuBar( menu );
	int h = applet.graphHeight();
	setSize( (3 * h) / 2, h ); // default value
	setDefaultCloseOperation( javax.swing.JFrame.HIDE_ON_CLOSE );
	setContentPane( view );
	setVisible( true );
    }

    /**
     * Set a locus from an OObject
     * @param oobject The OObject
     */
    public void setLocus( OObject oobject ){
	if( oobject != null && !(oobject instanceof jscicalc.Error) ){
	    //jscicalc.pobject.Variable x = new jscicalc.pobject.Variable( 'x' );
	    //jscicalc.expression.Variable v = new jscicalc.expression.Variable( x );
	    //oobject = new jscicalc.expression.Tan( v, jscicalc.AngleType.RADIANS );
	    Locus locus = new Locus( oobject, view );
	    model.reset( locus );
	    view.repaint();
	}
    }

    /**
     * Update the menu. Called by view wehn size of frame changes.
     */
    public void updateMenu(){
	menu.updateSizes();
    }

    /**
     * The menu.
     */
    private Menu menu;

    /**
     * The Model
     */
    private Model model;
    /**
     * The View
     */
    private View view;
    private static final long serialVersionUID = 1L;   
}
