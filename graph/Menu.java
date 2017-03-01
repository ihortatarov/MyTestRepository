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
 * This class handles the changes made to a graph.
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class Menu extends javax.swing.JMenuBar {
    
    /**
     * The constructor needs to know about the calculator
     * @param applet The calculator applet
     * @param view The graph View
     * @param model The model
     */
    public Menu( jscicalc.ReadOnlyCalculatorApplet applet, View view,
		 Model model ){
	this.applet = applet;
	this.view = view;
	this.model = model;
	editMenu = new javax.swing.JMenu( "Edit" );
	editMenu.setMnemonic( 'E' );
	javax.swing.JMenuItem xAxisItem = new javax.swing.JMenuItem( "x-axis" );
	xAxisDialog = new AxisDialog( applet, view, model.getXAxis(), true, "x axis" );
	xAxisItem.addActionListener( xAxisDialog );
	xAxisItem.setMnemonic( 'x' );
	editMenu.add( xAxisItem );
	javax.swing.JMenuItem yAxisItem = new javax.swing.JMenuItem( "y-axis" );
	yAxisDialog = new AxisDialog( applet, view, model.getYAxis(), false, "y axis" );
	yAxisItem.addActionListener( yAxisDialog );
	yAxisItem.setMnemonic( 'y' );
	editMenu.add( yAxisItem );
	add( editMenu );

	// attach actions : Doesn't work?
	EditMenuAction editMenuAction = new EditMenuAction();
	getInputMap().put( javax.swing.KeyStroke
			   .getKeyStroke( java.awt.event.KeyEvent.VK_E, 0 ),
			   editMenuAction.toString() );
	getActionMap().put( editMenuAction.toString(), editMenuAction );
    }

    /**
     * Update sizes of axes and fonts in response to changes in View or applet.
     */
    void updateSizes(){
	if( xAxisDialog != null )
	    xAxisDialog.setBounds();
	if( yAxisDialog != null )
	    yAxisDialog.setBounds();
    }

    /**
     * This class should activate the editMenu.
     */
    public class EditMenuAction extends javax.swing.AbstractAction {
	/**
	 * Make the action even activate the edit menu.
	 * @param actionEvent The ActionEvent
	 */
	public void actionPerformed( java.awt.event.ActionEvent actionEvent ){
	    System.out.println( "Ouch! That hurt." );
	    editMenu.doClick( 20 ); // 20 milliseconds?
	}
	private static final long serialVersionUID = 1L;   
    }

    /**
     * The Edit menu
     */
    private javax.swing.JMenu editMenu;
    /**
     * The x axis dialog.
     */
    private AxisDialog xAxisDialog;
    /**
     * The y axis dialog.
     */
    private AxisDialog yAxisDialog;
    
    /**
     * keep a copy of the applet
     */
    private jscicalc.ReadOnlyCalculatorApplet applet;
    /**
     * The model
     */
    private Model model;
    /**
     * keep a copy of the View
     */
    private View view;
    
    private static final long serialVersionUID = 1L;   
}
	
