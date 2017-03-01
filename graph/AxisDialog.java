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
 * This class shows a dialog for changing an axis.
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public class AxisDialog extends javax.swing.JFrame
    implements java.awt.event.ActionListener {
    
    /**
     * The constructor needs to know about the calculator
     * It also needs a name for its display
     * @param applet The calculator applet
     * @param view The graph View
     * @param axis The axis
     * @param x Set to <em>true</em> for a x axis, <em>false</em> otherwise
     * @param title The title for the title bar
     */
    public AxisDialog( jscicalc.ReadOnlyCalculatorApplet applet,
		       View view, Axis axis, boolean x, String title ){
	super( title );
	this.applet = applet;
	this.view = view;
	this.axis = axis;
	this.x = x;
	stack = new java.util.ArrayDeque<AxisData>();
	minimumLabel = new javax.swing.JLabel( "Minimum:" );
	minimumLabel.setHorizontalAlignment( javax.swing.JLabel.RIGHT );
	maximumLabel = new javax.swing.JLabel( "Maximum:" );
	maximumLabel.setHorizontalAlignment( javax.swing.JLabel.RIGHT );
	majorUnitLabel = new javax.swing.JLabel( "Major unit:" );
	majorUnitLabel.setHorizontalAlignment( javax.swing.JLabel.RIGHT );
	minorUnitLabel = new javax.swing.JLabel( "Minor unit:" );
	minorUnitLabel.setHorizontalAlignment( javax.swing.JLabel.RIGHT );

	minimumTextBox = new javax.swing.JTextField();
	maximumTextBox = new javax.swing.JTextField();
	majorUnitTextBox = new javax.swing.JTextField();
	minorUnitTextBox = new javax.swing.JTextField();

	minimumLabel.setLabelFor( minimumTextBox );
	minimumLabel.setDisplayedMnemonic( 'n' );
	minimumLabel.setToolTipText( "Minimum value shown on chart" );
	maximumLabel.setLabelFor( maximumTextBox );
	maximumLabel.setDisplayedMnemonic( 'x' );
	maximumLabel.setToolTipText( "Maximum value shown on chart" );
	majorUnitLabel.setLabelFor( majorUnitTextBox );
	majorUnitLabel.setDisplayedMnemonic( 'j' );
	majorUnitLabel
	    .setToolTipText( "Spacing between larger ticks with numerical scale" );
	minorUnitLabel.setLabelFor( minorUnitTextBox );
	minorUnitLabel.setDisplayedMnemonic( 'i' );
	minorUnitLabel.setToolTipText( "Spacing between smalle ticks" );

	majorCheckBox = new javax.swing.JCheckBox( "visible", true );
	minorCheckBox = new javax.swing.JCheckBox( "visible", true );

	applyButton = new javax.swing.JButton( "Apply" );
	applyButton.setMnemonic( 'A' );
	applyButton.setToolTipText( "Apply changes to graph" );
	undoButton = new javax.swing.JButton( "Undo" );
	undoButton.setMnemonic( 'U' );
	undoButton.setToolTipText( "Undo most recently applied change" );
	okButton = new javax.swing.JButton( "OK" );
	okButton.setMnemonic( 'O' );
	okButton.setToolTipText( "Apply changes to graph and quit" );
	cancelButton = new javax.swing.JButton( "Cancel" );
	cancelButton.setMnemonic( 'C' );
	cancelButton.setToolTipText( "Undo all changes and quit" );

	undoButton.setEnabled( false );

	majorCheckBox.setMnemonic( 's' );
	minorCheckBox.setMnemonic( 'v' );

	// focus listeners
	minimumTextBox
	    .addFocusListener( new FieldFocusListener( minimumTextBox, false ) );
	maximumTextBox
	    .addFocusListener( new FieldFocusListener( maximumTextBox, false ) );
	majorUnitTextBox
	    .addFocusListener( new FieldFocusListener( majorUnitTextBox, true ) );
	minorUnitTextBox
	    .addFocusListener( new FieldFocusListener( minorUnitTextBox, true ) );

	// action listeners
	applyButton.addActionListener( new ApplyButtonListener() );
	okButton.addActionListener( new OKButtonListener() );
	undoButton.addActionListener( new UndoButtonListener() );
	cancelButton.addActionListener( new CancelButtonListener() );

	springLayout = new javax.swing.SpringLayout();
	setSizes();

	setDefaultCloseOperation( javax.swing.JFrame.HIDE_ON_CLOSE );
	setResizable( false );

	java.awt.Container contentPane = getContentPane();
	contentPane.setLayout( springLayout );
	
	// objects
	contentPane.add( minimumLabel );
	contentPane.add( maximumLabel );
	contentPane.add( majorUnitLabel );
	contentPane.add( minorUnitLabel );

	contentPane.add( minimumTextBox );
	contentPane.add( maximumTextBox );
	contentPane.add( majorUnitTextBox );
	contentPane.add( minorUnitTextBox );

	contentPane.add( majorCheckBox );
	contentPane.add( minorCheckBox );

	contentPane.add( applyButton );
	contentPane.add( undoButton );
	contentPane.add( okButton );
	contentPane.add( cancelButton );

	layOut(); // set up components

	setVisible( false );
    }

    /**
     * Layout this container.
     */
    private void layOut(){
	// springs
	textBoxWidth = javax.swing.Spring.scale( buttonWidth, (float)1 );
	labelWidth = javax.swing.Spring.scale( buttonWidth, (float)1 );
	visibleWidth = javax.swing.Spring.scale( buttonWidth, (float)1 );
	// objects
	javax.swing.SpringLayout.Constraints constraints 
	    = springLayout.getConstraints( minimumLabel );
	constraints.setWidth( labelWidth );
	constraints.setHeight( buttonHeight );
	constraints = springLayout.getConstraints( maximumLabel );
	constraints.setWidth( labelWidth );
	constraints.setHeight( buttonHeight );
	constraints = springLayout.getConstraints( majorUnitLabel );
	constraints.setWidth( labelWidth );
	constraints.setHeight( buttonHeight );
	constraints = springLayout.getConstraints( minorUnitLabel );
	constraints.setWidth( labelWidth );
	constraints.setHeight( buttonHeight );

	constraints = springLayout.getConstraints( minimumTextBox );
	constraints.setWidth( textBoxWidth );
	constraints.setHeight( buttonHeight );
	constraints = springLayout.getConstraints( maximumTextBox );
	constraints.setWidth( textBoxWidth );
	constraints.setHeight( buttonHeight );
	constraints = springLayout.getConstraints( majorUnitTextBox );
	constraints.setWidth( textBoxWidth );
	constraints.setHeight( buttonHeight );
	constraints = springLayout.getConstraints( minorUnitTextBox );
	constraints.setWidth( textBoxWidth );
	constraints.setHeight( buttonHeight );

	constraints = springLayout.getConstraints( majorCheckBox );
	constraints.setWidth( visibleWidth );
	constraints.setHeight( buttonHeight );
	constraints = springLayout.getConstraints( minorCheckBox );
	constraints.setWidth( visibleWidth );
	constraints.setHeight( buttonHeight );

	constraints = springLayout.getConstraints( applyButton );
	constraints.setWidth( buttonWidth );
	constraints.setHeight( buttonHeight );
	constraints = springLayout.getConstraints( undoButton );
	constraints.setWidth( buttonWidth );
	constraints.setHeight( buttonHeight );
	constraints = springLayout.getConstraints( okButton );
	constraints.setWidth( buttonWidth );
	constraints.setHeight( buttonHeight );
	constraints = springLayout.getConstraints( cancelButton );
	constraints.setWidth( buttonWidth );
	constraints.setHeight( buttonHeight );

	java.awt.Container contentPane = getContentPane();
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, minimumLabel,
				    smallGap,
				    javax.swing.SpringLayout.NORTH, contentPane );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, minimumLabel,
				    smallGap,
				    javax.swing.SpringLayout.WEST, contentPane );
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, maximumLabel,
				    smallGap,
				    javax.swing.SpringLayout.SOUTH, minimumLabel );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, maximumLabel,
				    smallGap,
				    javax.swing.SpringLayout.WEST, contentPane );
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, majorUnitLabel,
				    gap,
				    javax.swing.SpringLayout.SOUTH, maximumLabel );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, majorUnitLabel,
				    smallGap,
				    javax.swing.SpringLayout.WEST, contentPane );
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, minorUnitLabel,
				    smallGap,
				    javax.swing.SpringLayout.SOUTH, majorUnitLabel );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, minorUnitLabel,
				    smallGap,
				    javax.swing.SpringLayout.WEST, contentPane );
	
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, minimumTextBox,
				    smallGap,
				    javax.swing.SpringLayout.NORTH, contentPane );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, minimumTextBox,
				    smallGap,
				    javax.swing.SpringLayout.EAST, minimumLabel );
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, maximumTextBox,
				    smallGap,
				    javax.swing.SpringLayout.SOUTH, minimumTextBox );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, maximumTextBox,
				    smallGap,
				    javax.swing.SpringLayout.EAST, maximumLabel );
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, majorUnitTextBox,
				    gap,
				    javax.swing.SpringLayout.SOUTH, maximumTextBox );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, majorUnitTextBox,
				    smallGap,
				    javax.swing.SpringLayout.EAST, majorUnitLabel );
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, minorUnitTextBox,
				    smallGap,
				    javax.swing.SpringLayout.SOUTH, majorUnitTextBox );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, minorUnitTextBox,
				    smallGap,
				    javax.swing.SpringLayout.EAST, minorUnitLabel );

	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, majorCheckBox,
				    gap,
				    javax.swing.SpringLayout.SOUTH, maximumTextBox );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, majorCheckBox,
				    smallGap,
				    javax.swing.SpringLayout.EAST, majorUnitTextBox );
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, minorCheckBox,
				    smallGap,
				    javax.swing.SpringLayout.SOUTH, majorCheckBox );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, minorCheckBox,
				    smallGap,
				    javax.swing.SpringLayout.EAST, minorUnitTextBox );

	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, applyButton,
				    gap,
				    javax.swing.SpringLayout.SOUTH, minorUnitLabel );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, applyButton,
				    smallGap,
				    javax.swing.SpringLayout.WEST, contentPane );
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, undoButton,
				    gap,
				    javax.swing.SpringLayout.SOUTH, minorUnitLabel );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, undoButton,
				    smallGap,
				    javax.swing.SpringLayout.EAST, applyButton );
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, okButton,
				    gap,
				    javax.swing.SpringLayout.SOUTH, minorUnitLabel );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, okButton,
				    smallGap,
				    javax.swing.SpringLayout.EAST, undoButton );
	springLayout.putConstraint( javax.swing.SpringLayout.NORTH, cancelButton,
				    gap,
				    javax.swing.SpringLayout.SOUTH, minorUnitLabel );
	springLayout.putConstraint( javax.swing.SpringLayout.WEST, cancelButton,
				    smallGap,
				    javax.swing.SpringLayout.EAST, okButton );
    }

    /**
     * Implement the ActionListener interface
     * @param event The event
     */
    public void actionPerformed( java.awt.event.ActionEvent event ){
	setBounds();
	setVisible( true );
    }

    /**
     * Validate data from dialog.
     * @return <em>true</em> if the data is valid, <em>false</em> otherwise
     */
    private boolean validateFields(){
	// check the values first
	return true;
    }

    /**
     * Set values for bounds
     */
    public void setBounds(){
	if( minimumLabel.getFont().getSize() != applet.buttonTextSize() ){
	    if( setSizes() )
		layOut(); // may also have to reset positions of controls
	}
	java.text.NumberFormat f = java.text.NumberFormat.getNumberInstance();
	if( f instanceof java.text.DecimalFormat ){
	    java.text.DecimalFormat d = (java.text.DecimalFormat)f;
	    d.setNegativePrefix( "\u2212" );
	}
	AxisData axisData = stack.peekFirst();
	double oldWidth = 0;
	if( axisData == null )
	    axisData = new AxisData();
	else 
	    oldWidth = axisData.maximum - axisData.minimum;
	// set the minimum and the maximum
	if( x ){
	    axisData.minimum = view.getTransformation().toModelX( 0 );
	    axisData.maximum = view.getTransformation().toModelX( view.getWidth() );
	} else {
	    axisData.minimum = view.getTransformation().toModelY( view.getHeight() );
	    axisData.maximum = view.getTransformation().toModelY( 0 );
	}
	minimumTextBox.setText( f.format( axisData.minimum ) );
	maximumTextBox.setText( f.format( axisData.maximum ) );
	if( stack.size() == 0 ){ // if new axis unit created
	    // set the major/minor units
	    if( x ){
		axisData.majorUnit = view.getTransformation().getXMajorUnit();
		axisData.minorUnit = view.getTransformation().getXMinorUnit();
	    } else {
		axisData.minorUnit = view.getTransformation().getYMajorUnit();
		axisData.minorUnit = view.getTransformation().getYMinorUnit();
	    }
	    majorUnitTextBox.setText( Double.toString( axisData.majorUnit ) );
	    minorUnitTextBox.setText( Double.toString( axisData.minorUnit ) );
	    majorCheckBox.setSelected( axisData.majorVisible );
	    minorCheckBox.setSelected( axisData.minorVisible );
	    stack.addFirst( axisData );
	} else {
	    final double newWidth = axisData.maximum - axisData.minimum;
	    final double change = (newWidth - oldWidth) / 2;
	    // rescale all units
	    java.util.Iterator<AxisData> i = stack.iterator();
	    if( i.hasNext() ) // should always be true
		for( i.next(); i.hasNext(); ){ // ignore first element
		    AxisData a = i.next();
		    a.maximum += change;
		    a.minimum -= change;
		}
	    else
		System.err.println( "AxisDialog.setBounds(): expected to find element"
				    + " on stack." );
	}
    }
    
    /**
     * Set the sizes for this component
     * @return <em>true</em> or <em>false</em> according as a change to Spring sizes
     * was made or not
     */
    public boolean setSizes(){
	// frame
	setSize( applet.minSize() * 5
		 + (applet.buttonWidth() * ( 1 + 1 + 1 + 1 ))
		 + applet.getFrameInsets().left + applet.getFrameInsets().right,
		 applet.minSize() * 4 + applet.strutSize() * 2
		 + applet.buttonHeight() * 5
		 + applet.getFrameInsets().top + applet.getFrameInsets().bottom );
	// springs
	boolean result = minimumLabel.getFont().getSize() != applet.buttonTextSize();
	if( result ){
	    smallGap = javax.swing.Spring.constant( applet.minSize() );
	    gap = javax.swing.Spring.constant( applet.strutSize() );
	    buttonWidth = javax.swing.Spring.constant( applet.buttonWidth() );
	    buttonHeight = javax.swing.Spring.constant( applet.buttonHeight() );
	    // objects
	    minimumLabel.setFont( minimumLabel.getFont()
				  .deriveFont( applet.buttonTextSize() ) );
	    maximumLabel.setFont( maximumLabel.getFont()
				  .deriveFont( applet.buttonTextSize() ) );
	    majorUnitLabel.setFont( majorUnitLabel.getFont()
				    .deriveFont( applet.buttonTextSize() ) );
	    minorUnitLabel.setFont( minorUnitLabel.getFont()
				    .deriveFont( applet.buttonTextSize() ) );
	    minimumTextBox.setFont( minimumTextBox.getFont()
				    .deriveFont( applet.buttonTextSize() ) );
	    maximumTextBox.setFont( maximumTextBox.getFont()
				    .deriveFont( applet.buttonTextSize() ) );
	    majorUnitTextBox.setFont( majorUnitTextBox.getFont()
				      .deriveFont( applet.buttonTextSize() ) );
	    minorUnitTextBox.setFont( minorUnitTextBox.getFont()
				      .deriveFont( applet.buttonTextSize() ) );
	    majorCheckBox.setFont( majorCheckBox.getFont()
				   .deriveFont( applet.buttonTextSize() ) );
	    minorCheckBox.setFont( minorCheckBox.getFont()
				   .deriveFont( applet.buttonTextSize() ) );
	    applyButton.setFont( applyButton.getFont()
				 .deriveFont( applet.buttonTextSize() ) );
	    undoButton.setFont( undoButton.getFont()
				.deriveFont( applet.buttonTextSize() ) );
	    okButton.setFont( okButton.getFont()
			      .deriveFont( applet.buttonTextSize() ) );
	    cancelButton.setFont( cancelButton.getFont()
				  .deriveFont( applet.buttonTextSize() ) );
	}
	return result;
    }
    /**
     * Focus listener for numerical text fields.
     */
    public class FieldFocusListener implements java.awt.event.FocusListener {
	/**
	 * the constructor needs two pieced of information. It needs to know the
	 * text field that that is attached to this so that it can return the focus. It
	 * also needs to know if the field can be nonpositive. Major and minor units are
	 * required to be positive.
	 * @param textField The text field
	 * @param positive Set to <em>true</em> to restrict values to positive integers.
	 */
	FieldFocusListener( javax.swing.JTextField textField, boolean positive ){
	    this.textField = textField;
	    this.positive = positive;
	}
	/**
	 * The focus was gained. Do nothing. This is required by the interface.
	 * @param focusEvent The event
	 */
	public void focusGained( java.awt.event.FocusEvent focusEvent ){
	}
	/**
	 * The focus was lost. Check the input and return focus if it is not valid.
	 * @param focusEvent The event
	 */
	public void focusLost( java.awt.event.FocusEvent focusEvent ){
	    String s = textField.getText();
	    s = s.replace( "\u2212", "-" ); // allow a true minus sign
	    double value = 0;
	    try {
		value = Double.parseDouble( s );
	    } catch( NumberFormatException e ) {
		if( positive )
		    textField.setText( "1" );
		else
		    textField.setText( "0" );
		textField.requestFocusInWindow();
		return;
	    }
	    if( positive && value <= 0 ){
		textField.setText( "1" );
		textField.requestFocusInWindow();
		return;
	    }
	    // now replace value with true minus sign
	    s = s.replace( "-", "\u2212" ); // allow a true minus sign
	    textField.setText( s );
	}
	/**
	 * The text field.
	 */
	private javax.swing.JTextField textField;
	/**
	 * This value should be <em>true</em> if the number must be positive,
	 * <em>false</em> otherwise.
	 */
	private boolean positive;
    }

    /**
     * Apply changes. This requires us to (1) check the validity of the input values,
     * (2) copy the input values to an AxisData object, (3) place the object on the
     * stack so that it can be undone, (4) apply the data to the graph.
     * @return <em>true</em> or <em>false</em> according as the data was valid or
     * not
     */
    private boolean apply(){
	// Create an AxisData object to hold the values we need
	AxisData axisData = new AxisData();
	// first we try to validate the values in input
	String s = minimumTextBox.getText();
	s = s.replace( "\u2212", "-" ); // allow a true minus sign
	try {
	    axisData.minimum = Double.parseDouble( s );
	} catch( NumberFormatException e ) {
	    // This should have been caught earlier
	}
	s = maximumTextBox.getText();
	s = s.replace( "\u2212", "-" ); // allow a true minus sign
	try {
	    axisData.maximum = Double.parseDouble( s );
	} catch( NumberFormatException e ) {
	    // This should have been caught earlier
	}
	if( axisData.maximum <= axisData.minimum ){
	    javax.swing.JOptionPane
		.showMessageDialog( this,
				    "The minimum must be less than the maximum.",
				    "Java Scientific Calculator",
				    javax.swing.JOptionPane.ERROR_MESSAGE );
	    return false;
	    }
	// now check units
	s = majorUnitTextBox.getText();
	s = s.replace( "\u2212", "-" ); // allow a true minus sign
	try {
	    axisData.majorUnit = Double.parseDouble( s );
	} catch( NumberFormatException e ) {
		// This should have been caught earlier
	}
	s = minorUnitTextBox.getText();
	s = s.replace( "\u2212", "-" ); // allow a true minus sign
	try {
	    axisData.minorUnit = Double.parseDouble( s );
	} catch( NumberFormatException e ) {
	    // This should have been caught earlier
	}
	if( axisData.majorUnit < axisData.minorUnit ){
	    javax.swing.JOptionPane
		.showMessageDialog( this,
				    "The major unit must be no less than the minor"
				    + " unit.",
				    "Java Scientific Calculator",
				    javax.swing.JOptionPane.ERROR_MESSAGE );
	    return false;
	}
	final double ratio = axisData.majorUnit / axisData.minorUnit;
	if( ratio != Math.floor( ratio ) ){
	    javax.swing.JOptionPane
		.showMessageDialog( this,
				    "The major unit must be an integer multiple the minor"
				    + " unit.",
				    "Java Scientific Calculator",
				    javax.swing.JOptionPane.ERROR_MESSAGE );
	    return false;
	}
	// Now the checkBoxes
	axisData.majorVisible = majorCheckBox.isSelected();
	axisData.minorVisible = minorCheckBox.isSelected();
	// check that there are some changes
	if( axisData.equals( stack.peekFirst() ) ) return true;
	boolean forceUpdate = !axisData.minMaxMatches( stack.peekFirst() );
	// then we copy to the end of the stack
	stack.addFirst( axisData );
	undoButton.setEnabled( true );
	// then we update the graph
	double origin = (axisData.maximum + axisData.minimum ) / 2;
	double width = axisData.maximum - axisData.minimum;
	if( x ){
	    view.getTransformation().setOriginX( origin );
	    view.getTransformation().setScaleX( view.getWidth() / width );
	    view.getTransformation().setXMajorUnit( axisData.majorUnit );
	    view.getTransformation().setXMinorUnit( axisData.minorUnit );
	} else {
	    view.getTransformation().setOriginY( origin );
	    view.getTransformation().setScaleY( view.getHeight() / width );
	    view.getTransformation().setYMajorUnit( axisData.majorUnit );
	    view.getTransformation().setYMinorUnit( axisData.minorUnit );
	}
	axis.setShowMajorUnit( axisData.majorVisible );
	axis.setShowMinorUnit( axisData.minorVisible );
	if( forceUpdate ) view.forceUpdate();
	view.repaint();
	return true;
    }
    /**
     * Undo changes. This requires us to (1) check there is a previous value.
     * (2) Dump current AxisData object from stack, (3) copy previous values
     * from stack to dialog, (4) apply the data to the graph.
     * @return <em>true</em> or <em>false</em> according as the undo was valid or
     * not
     */
    private boolean undo(){
	if( stack.size() <= 1 ) return false;
	AxisData oldData = stack.remove(); // dump first element;
	if( stack.size() <= 1 )
	    undoButton.setEnabled( false );
	// copy values to dialog
	java.text.NumberFormat f = java.text.NumberFormat.getNumberInstance();
	if( f instanceof java.text.DecimalFormat ){
	    java.text.DecimalFormat d = (java.text.DecimalFormat)f;
	    d.setNegativePrefix( "\u2212" );
	}
	AxisData axisData = stack.peekFirst();
	minimumTextBox.setText( f.format( axisData.minimum ) );
	maximumTextBox.setText( f.format( axisData.maximum ) );
	majorUnitTextBox.setText( Double.toString( axisData.majorUnit ) );
	minorUnitTextBox.setText( Double.toString( axisData.minorUnit ) );
	majorCheckBox.setSelected( axisData.majorVisible );
	minorCheckBox.setSelected( axisData.minorVisible );
	// then we update the graph
	double origin = (axisData.maximum + axisData.minimum ) / 2;
	double width = axisData.maximum - axisData.minimum;
	if( x ){
	    view.getTransformation().setOriginX( origin );
	    view.getTransformation().setScaleX( view.getWidth() / width );
	    view.getTransformation().setXMajorUnit( axisData.majorUnit );
	    view.getTransformation().setXMinorUnit( axisData.minorUnit );
	} else {
	    view.getTransformation().setOriginY( origin );
	    view.getTransformation().setScaleY( view.getHeight() / width );
	    view.getTransformation().setYMajorUnit( axisData.majorUnit );
	    view.getTransformation().setYMinorUnit( axisData.minorUnit );
	}
	axis.setShowMajorUnit( axisData.majorVisible );
	axis.setShowMinorUnit( axisData.minorVisible );
	if( !oldData.minMaxMatches( stack.peekFirst() ) )
	    view.forceUpdate();
	view.repaint();
	return true;
    }
    /**
     * Cancel changes. This requires us to (1) copy data from last value on stack
     * to graph.
     */
    private void cancel(){
	// then we update the graph
	AxisData axisData = stack.removeLast();
	double origin = (axisData.maximum + axisData.minimum ) / 2;
	double width = axisData.maximum - axisData.minimum;
	if( x ){
	    view.getTransformation().setOriginX( origin );
	    view.getTransformation().setScaleX( view.getWidth() / width );
	    view.getTransformation().setXMajorUnit( axisData.majorUnit );
	    view.getTransformation().setXMinorUnit( axisData.minorUnit );
	} else {
	    view.getTransformation().setOriginY( origin );
	    view.getTransformation().setScaleY( view.getHeight() / width );
	    view.getTransformation().setYMajorUnit( axisData.majorUnit );
	    view.getTransformation().setYMinorUnit( axisData.minorUnit );
	}
	axis.setShowMajorUnit( axisData.majorVisible );
	axis.setShowMinorUnit( axisData.minorVisible );
	view.forceUpdate();
	view.repaint();
    }
    
    /**
     * Class to handle apply button.
     */
    public class ApplyButtonListener implements java.awt.event.ActionListener {
	/**
	 * Implement the ActionListener interface
	 * @param event The event
	 */
	public void actionPerformed( java.awt.event.ActionEvent event ){
	    apply(); // ignore return value here
	}
    }
    /**
     * Class to handle undo button.
     */
    public class UndoButtonListener implements java.awt.event.ActionListener {
	/**
	 * Implement the ActionListener interface
	 * @param event The event
	 */
	public void actionPerformed( java.awt.event.ActionEvent event ){
	    undo();
	}
    }
    /**
     * Class to handle OK button.
     */
    public class OKButtonListener implements java.awt.event.ActionListener {
	/**
	 * Implement the ActionListener interface
	 * @param event The event
	 */
	public void actionPerformed( java.awt.event.ActionEvent event ){
	    if( !apply() ) return;
	    setVisible( false ); // assumes value OK
	    // clear stack
	    stack.clear();
	}
    }
    /**
     * Class to handle Cancel button.
     */
    public class CancelButtonListener implements java.awt.event.ActionListener {
	/**
	 * Implement the ActionListener interface
	 * @param event The event
	 */
	public void actionPerformed( java.awt.event.ActionEvent event ){
	    cancel();
	    setVisible( false );
	    // clear stack
	    stack.clear();
	}
    }

    /**
     * Label: Minimum
     */
    private javax.swing.JLabel minimumLabel;
    /**
     * Label: Maximum
     */
    private javax.swing.JLabel maximumLabel;
    /**
     * Label: Major Unit
     */
    private javax.swing.JLabel majorUnitLabel;
    /**
     * Label: Minor unit
     */
    private javax.swing.JLabel minorUnitLabel;
    /**
     * TextField: Minimum
     */
    private javax.swing.JTextField minimumTextBox;
    /**
     * TextField: Maximum
     */
    private javax.swing.JTextField maximumTextBox;
    /**
     * TextField: MajorUnit
     */
    private javax.swing.JTextField majorUnitTextBox;
    /**
     * TextField: MinorUnit
     */
    private javax.swing.JTextField minorUnitTextBox;
    /**
     * Label: visible
     */
    private javax.swing.JLabel majorVisible;
    /**
     * Label: visible
     */
    private javax.swing.JLabel minorVisible;
    /**
     * Checkbox: visible
     */
    private javax.swing.JCheckBox majorCheckBox;
    /**
     * Checkbox: visible
     */
    private javax.swing.JCheckBox minorCheckBox;
    /**
     * Button: apply
     */
    private javax.swing.JButton applyButton;
    /**
     * Button: undo
     */
    private javax.swing.JButton undoButton;
    /**
     * Button: ok
     */
    private javax.swing.JButton okButton;
    /**
     * Button: cancel
     */
    private javax.swing.JButton cancelButton;
    /**
     * The smallest gap between objects.
     */
    private javax.swing.Spring smallGap;
    /**
     * The larger gap between objects.
     */
    private javax.swing.Spring gap;
    /**
     * The button width
     */
    private javax.swing.Spring buttonWidth;
    /**
     * The button height
     */
    private javax.swing.Spring buttonHeight;
    /**
     * The text box width
     */
    private javax.swing.Spring textBoxWidth;
    /**
     * The label width
     */
    private javax.swing.Spring labelWidth;
    /**
     * The label width for labels titled &lsquo;visible&rsquo;.
     */
    private javax.swing.Spring visibleWidth;
    /**
     * The applet: used to get sizes.
     */
    private jscicalc.ReadOnlyCalculatorApplet applet;
    /**
     * The View: used to get sizes.
     */
    private View view;
    /**
     * The Axis: used to get sizes.
     */
    private Axis axis;
    /**
     * The stack of changes made so far.
     */
    private java.util.ArrayDeque<AxisData> stack;
    /**
     * This value is <em>true</em> for an x axis, <em>false</em> for a y axis
     */
    private final boolean x;
    /**
     * The layout.
     */
    javax.swing.SpringLayout springLayout;
    
    private static final long serialVersionUID = 1L;   
}
