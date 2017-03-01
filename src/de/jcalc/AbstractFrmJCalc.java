package de.jcalc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;



/**
 * Enthält alle Visuell relevanten Objekte und Funktionen
 * @author Aug
 * @version 0.1A
 */
public abstract class AbstractFrmJCalc 
{	
	private JFrame frame = null; 
	private GridBagLayout layout = null;

	private JComboBox cmbCalctyp = null;
	protected JTextField txtDisplay = null;
	protected JTextField txtState = null;
	protected TextArea txtOutput = null;
	
	private JMenuBar menuBar = null;
	private JMenu menuFile = null;
	private JMenuItem mFile[] = new JMenuItem[5];
	private JMenu menuParse = null;
	private JMenuItem mParse[] = new JMenuItem[5];
	private JMenu menuHelp = null;
	private JMenuItem[] mHelp = new JMenuItem[2];
	
	private JButton[] butNum = new JButton[16];
	private JButton[] butAct = new JButton[20];
	private JButton[] butValue=new JButton[4];
	
	private LangMgr lang = new LangMgr(); 	


	public AbstractFrmJCalc()
	{
		this.frame = this.getFrame();
		this.getCmbCalctyp();
		this.getTxtDisplay();
		this.txtDisplay.setText("0");
		this.getTxtState();
		this.getTxtOutput();
		
		//NumButtons
		frame.add(butNum[0]=this.makeButton("0","number",0,7,1,1));
		frame.add(butNum[1]=this.makeButton("1","number",0,4,1,1));
		frame.add(butNum[2]=this.makeButton("2","number",1,4,1,1));
		frame.add(butNum[3]=this.makeButton("3","number",2,4,1,1));
		frame.add(butNum[4]=this.makeButton("4","number",0,5,1,1));
		frame.add(butNum[5]=this.makeButton("5","number",1,5,1,1));
		frame.add(butNum[6]=this.makeButton("6","number",2,5,1,1));
		frame.add(butNum[7]=this.makeButton("7","number",0,6,1,1));
		frame.add(butNum[8]=this.makeButton("8","number",1,6,1,1));
		frame.add(butNum[9]=this.makeButton("9","number",2,6,1,1));
		
		frame.add(butNum[10]=this.makeButton("A","number",0,8,1,1));
		frame.add(butNum[11]=this.makeButton("B","number",1,8,1,1));
		frame.add(butNum[12]=this.makeButton("C","number",2,8,1,1));
		frame.add(butNum[13]=this.makeButton("D","number",3,8,1,1));
		frame.add(butNum[14]=this.makeButton("E","number",4,8,1,1));
		frame.add(butNum[15]=this.makeButton("F","number",5,8,1,1));
		
		//ActButtons
		frame.add(butAct[0]=this.makeButton("+","plus",3,4,1,1));
		frame.add(butAct[1]=this.makeButton("-","minus",3,5,1,1));
		frame.add(butAct[2]=this.makeButton("*","multi",3,6,1,1));
		frame.add(butAct[3]=this.makeButton("/","divid",3,7,1,1));
		frame.add(butAct[4]=this.makeButton(".","point",1,7,1,1));
		frame.add(butAct[5]=this.makeButton("+/-","reverse",2,7,1,1));
		
		frame.add(butAct[6]=this.makeButton("=","result",4,7,1,1));
		frame.add(butAct[7]=this.makeButton("(","clipopen",4,5,1,1));
		frame.add(butAct[8]=this.makeButton(")","clipclose",4,6,1,1));
		frame.add(butAct[9]=this.makeButton("clr","clear",4,4,1,1));
		frame.add(butAct[10]=this.makeButton("<-","back",5,4,1,1));
		frame.add(butAct[11]=this.makeButton("sin","sin",5,5,1,1));
		frame.add(butAct[12]=this.makeButton("cos","cos",5,6,1,1));
		frame.add(butAct[13]=this.makeButton("tan","tan",5,7,1,1));
		frame.add(butAct[14]=this.makeButton("x^2","power2",6,4,1,1));
		frame.add(butAct[15]=this.makeButton("x^y","power",6,5,1,1));
		frame.add(butAct[16]=this.makeButton("sqrt","sqrt",6,6,1,1));
		
		frame.add(butValue[0]=this.makeButton("PI","PI",6,7,1,1));
		frame.add(butValue[1]=this.makeButton("x","x",7,4,1,1));
		frame.add(butValue[2]=this.makeButton("y","y",7,5,1,1));
		frame.add(butValue[3]=this.makeButton("z","z",7,6,1,1));
		
		
		this.frame.setJMenuBar(getMenuBar());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setFocusableWindowState(true);
		this.txtDisplay.requestFocus();
	}
	
	/**
	 * This method initializes frame	
	 * 	
	 * @return javax.swing.JFrame	
	 */
	private JFrame getFrame() 
	{
		if (frame == null) 
		{
			System.out.println(lang.getMessage("iniFrame"));
			frame = new JFrame("JCalc");
			frame.setSize(new Dimension(500, 400));
			layout = new GridBagLayout();
			frame.setLayout(layout);
		}
		return frame;
	}

	private JComboBox getCmbCalctyp()
	{
		if(cmbCalctyp == null)
		{
			cmbCalctyp = new JComboBox();
			cmbCalctyp.addItem(lang.getGuiText("simpleCalculation")); //Index 0
			cmbCalctyp.addItem(lang.getGuiText("binaryCalculation")); //Index 1
			cmbCalctyp.addItem(lang.getGuiText("octalCalculation")); //Index 2
			cmbCalctyp.addItem(lang.getGuiText("hexCalculation")); //Index 3
			cmbCalctyp.addItem(lang.getGuiText("equationCalculation")); // Index 4
			cmbCalctyp.addItemListener( 
					new ItemListener() 
					{
						public void itemStateChanged( ItemEvent e ) 
						{
							JComboBox e1 = (JComboBox)e.getSource();
							changeCalctyp(e1.getSelectedIndex());
							txtDisplay.requestFocus();
						}
		            });
		          
			GridBagConstraints gbc = makegbc(0,0,10,1);
			gbc.weightx = 99;
			gbc.weighty = 99;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			layout.setConstraints(cmbCalctyp,gbc);
			frame.add(cmbCalctyp);
			
		}
		
		return cmbCalctyp;
	}
	
	private JTextField getTxtDisplay()
	{
		if(txtDisplay == null)
		{
			txtDisplay = new JTextField();
			txtDisplay.setHorizontalAlignment(JTextField.RIGHT);
			txtDisplay.setEditable(false);
			txtDisplay.setBackground(new Color(255,255,255));

			txtDisplay.addKeyListener(
					new KeyListener ()
					{
						public void keyTyped(KeyEvent key) 
						{
							keyEventTyped(key);
						}

						public void keyPressed(KeyEvent key) 
						{
							keyEventPressed(key);
						}

						public void keyReleased(KeyEvent key) 
						{
							// TODO Auto-generated method stub
						}
						
					});
			GridBagConstraints gbc = null;
			gbc = makegbc(0,2,10,1);
			gbc.weightx = 99;
			gbc.weighty = 99;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			layout.setConstraints(txtDisplay,gbc);
			frame.add(txtDisplay);
		}
		return txtDisplay;
	}
	
	private JTextField getTxtState()
	{
		if(txtState == null)
		{
			txtState = new JTextField();
			txtState.setHorizontalAlignment(JTextField.RIGHT);
			txtState.setEditable(false);
			txtState.setFocusable(false);
			txtState.setBackground(new Color(255,255,255));
			GridBagConstraints gbc = makegbc(0,3,10,1);
			gbc.weightx = 99;
			gbc.weighty = 99;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			layout.setConstraints(txtState,gbc);
			frame.add(txtState);
		}
		return txtState;
	}
	
	private TextArea getTxtOutput()
	{
		if(txtOutput == null)
		{
			txtOutput = new TextArea();
			txtOutput.setEditable(false);
			txtOutput.setBackground(new Color(255,255,255));
			GridBagConstraints gbc = makegbc(0,9,10,1);
			gbc.weightx = 99;
			gbc.weighty = 99;
			gbc.fill = GridBagConstraints.BOTH;
			layout.setConstraints(txtOutput,gbc);
			frame.add(txtOutput);
		}
		return txtOutput;
	}
	
	private JMenuBar getMenuBar()
	{
		if(menuBar == null)
		{
			menuBar = new JMenuBar();
			menuBar.add(getMenuFile());
			menuBar.add(getMenuParse());
			menuBar.add(getMenuHelp());
			
		}
		
		return menuBar;
	}
	
	private JMenu getMenuFile()
	{
		if(menuFile == null)
		{
			menuFile = new JMenu(lang.getGuiText("menuFile"));
			menuFile.add(getMFileClear());
			menuFile.add(getMFileSave());
			menuFile.addSeparator();
			menuFile.add(getMFileClose());
		}
		return menuFile;
	}
	
	private JMenuItem getMFileClear()
	{
		if(mFile[0] == null)
		{
			mFile[0] = new JMenuItem(lang.getGuiText("clear"));
			mFile[0].addActionListener
			(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent action)
						{
							clearCalc();
						}
					}
			);
		}
		
		return mFile[0];
	}
	private JMenuItem getMFileSave()
	{
		if(mFile[1]==null)
		{
			mFile[1] = new JMenuItem(lang.getGuiText("save"));
			mFile[1].addActionListener
			(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent action)
						{
							outputSave();
						}

					}
			);
		}
		
		return mFile[1];
	}
	private JMenuItem getMFileClose()
	{
		if(mFile[2] == null)
		{
			mFile[2] = new JMenuItem(lang.getGuiText("close"));
			mFile[2].addActionListener
			(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent action)
						{
							System.exit(0);
						}
					}
			);
		}
		
		return mFile[2];
	}
	
	private JMenu getMenuParse()
	{
		if(menuParse == null)
		{
			menuParse = new JMenu(lang.getGuiText("menuParse"));
			menuParse.add(getMParseCalculation());
			menuParse.add(getMParseEquation());
		}
		
		return menuParse;
	}
	private JMenuItem getMParseCalculation()
	{
		if(mParse[0] == null)
		{
			mParse[0] = new JMenuItem(lang.getGuiText("simpleCalculation"));
			mParse[0].addActionListener
			(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent action)
						{
							parseSimCalculation();
						}
					}
			);
		}
		
		return mParse[0];
	}
	private JMenuItem getMParseEquation()
	{
		if(mParse[1] == null)
		{
			mParse[1] = new JMenuItem(lang.getGuiText("equationCalculation"));
			mParse[1].addActionListener
			(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent action)
						{
							parseEquation();
						}
					}
			);
		}
		
		return mParse[1];
	}
		
	private JMenu getMenuHelp()
	{
		if(menuHelp == null)
		{
			menuHelp = new JMenu(lang.getGuiText("help"));
			menuHelp.add(getMHelpAbout());
			menuHelp.add(getMHelpHelp());
		}
		
		return menuHelp;
	}
	private JMenuItem getMHelpAbout()
	{
		if(mHelp[0] == null)
		{
			mHelp[0] = new JMenuItem(lang.getGuiText("about"));
			mHelp[0].addActionListener
			(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent action)
						{
							showAbout();
						}
					}
			);
		}
		
		return mHelp[0];
	}
	private JMenuItem getMHelpHelp()
	{
		if(mHelp[1] == null)
		{
			mHelp[1] = new JMenuItem(lang.getGuiText("help"));
			mHelp[1].addActionListener
			(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent action)
						{
							showHelp();
						}
					}
			);
		}
		
		return mHelp[1];
	}
	
	private GridBagConstraints makegbc( int x, int y, int width, int height)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(1, 1, 1, 1);
		return gbc;
	}
	
	private JButton makeButton(String text,String name, int posX, int posY, int weight, int height)
	{
		JButton button = new JButton(text);
		button.setName(name);
		button.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent action)
					{
						butEvent(action);
					}
				}
		);
		GridBagConstraints gbc = makegbc(posX,posY,weight,height);
		gbc.weightx = 50;
		gbc.weighty = 50;
		gbc.fill = GridBagConstraints.BOTH;
		layout.setConstraints(button,gbc);
		
		return button;
	}
	
	public boolean setVisible(boolean set)
	{
		boolean oldVis = this.frame.isVisible();
		this.frame.setVisible(set);
		return oldVis;
	}
	
	/**
	 * 
	 * @param index
	 * 		  0 -> SIMPLE_CALCULATION
	 * 		  1 -> BINARY_CALCULATION
	 * 	      2 -> OCT_CALCULATION
	 * 		  3 -> HEX_CALCULATION
	 * 		  4 -> LINEAR_EQUATION
	 * 		  5 -> NONLINEAR_EQUATION
	 */
	protected void changeCalctyp(int index)
	{
		switch(index)
		{
// <--------    Simple Calculation ---------------- >
			case 0:
				//NumButtons 
				butNum[0].setEnabled(true); // 0
				butNum[1].setEnabled(true); // 1
				butNum[2].setEnabled(true); // 2
				butNum[3].setEnabled(true); // 3
				butNum[4].setEnabled(true); // 4
				butNum[5].setEnabled(true); // 5
				butNum[6].setEnabled(true); // 6
				butNum[7].setEnabled(true); // 7
				butNum[8].setEnabled(true); // 8
				butNum[9].setEnabled(true); // 9
				butNum[10].setEnabled(false); // A
				butNum[11].setEnabled(false); // B
				butNum[12].setEnabled(false); // C
				butNum[13].setEnabled(false); // D
				butNum[14].setEnabled(false); // E
				butNum[15].setEnabled(false); // F
				
				//ActButtons
				butAct[0].setEnabled(true); // +
				butAct[1].setEnabled(true); // -
				butAct[2].setEnabled(true); // *
				butAct[3].setEnabled(true); // /
				butAct[4].setEnabled(true); // .
				butAct[5].setEnabled(true); // +/-
				butAct[6].setEnabled(true); // =
				butAct[7].setEnabled(true); // (
				butAct[8].setEnabled(true); // )
				butAct[9].setEnabled(true); // clr
				butAct[10].setEnabled(true); // <-
				butAct[11].setEnabled(true); // sin
				butAct[12].setEnabled(true); // cos
				butAct[13].setEnabled(true); // tan
				butAct[14].setEnabled(true); // x^2
				butAct[15].setEnabled(true); // x^y
				butAct[16].setEnabled(true); // sqrt
				
				//ValueButton
				butValue[0].setEnabled(true); // PI
				butValue[1].setEnabled(false); // x
				butValue[2].setEnabled(false); // y
				butValue[3].setEnabled(false); // z
				break;
				
// 		<---------- Binary Calculation -------------------->
			case 1:
//				NumButtons 
				butNum[0].setEnabled(true); // 0
				butNum[1].setEnabled(true); // 1
				butNum[2].setEnabled(false); // 2
				butNum[3].setEnabled(false); // 3
				butNum[4].setEnabled(false); // 4
				butNum[5].setEnabled(false); // 5
				butNum[6].setEnabled(false); // 6
				butNum[7].setEnabled(false); // 7
				butNum[8].setEnabled(false); // 8
				butNum[9].setEnabled(false); // 9
				butNum[10].setEnabled(false); // A
				butNum[11].setEnabled(false); // B
				butNum[12].setEnabled(false); // C
				butNum[13].setEnabled(false); // D
				butNum[14].setEnabled(false); // E
				butNum[15].setEnabled(false); // F
				
				//ActButtons
				butAct[0].setEnabled(true); // +
				butAct[1].setEnabled(true); // -
				butAct[2].setEnabled(true); // *
				butAct[3].setEnabled(true); // /
				butAct[4].setEnabled(false); // .
				butAct[5].setEnabled(false); // +/-
				butAct[6].setEnabled(true); // =
				butAct[7].setEnabled(true); // (
				butAct[8].setEnabled(true); // )
				butAct[9].setEnabled(true); // clr
				butAct[10].setEnabled(true); // <-
				butAct[11].setEnabled(true); // sin
				butAct[12].setEnabled(true); // cos
				butAct[13].setEnabled(true); // tan
				butAct[14].setEnabled(true); // x^2
				butAct[15].setEnabled(true); // x^y
				butAct[16].setEnabled(true); // sqrt
				
				//ValueButton
				butValue[0].setEnabled(false); // PI
				butValue[1].setEnabled(false); // x
				butValue[2].setEnabled(false); // y
				butValue[3].setEnabled(false); // z
				break;
				
//		 		<---------- Octal Calculation -------------------->
			case 2:
//				NumButtons 
				butNum[0].setEnabled(true); // 0
				butNum[1].setEnabled(true); // 1
				butNum[2].setEnabled(true); // 2
				butNum[3].setEnabled(true); // 3
				butNum[4].setEnabled(true); // 4
				butNum[5].setEnabled(true); // 5
				butNum[6].setEnabled(true); // 6
				butNum[7].setEnabled(true); // 7
				butNum[8].setEnabled(false); // 8
				butNum[9].setEnabled(false); // 9
				butNum[10].setEnabled(false); // A
				butNum[11].setEnabled(false); // B
				butNum[12].setEnabled(false); // C
				butNum[13].setEnabled(false); // D
				butNum[14].setEnabled(false); // E
				butNum[15].setEnabled(false); // F
				
				//ActButtons
				butAct[0].setEnabled(true); // +
				butAct[1].setEnabled(true); // -
				butAct[2].setEnabled(true); // *
				butAct[3].setEnabled(true); // /
				butAct[4].setEnabled(false); // .
				butAct[5].setEnabled(false); // +/-
				butAct[6].setEnabled(true); // =
				butAct[7].setEnabled(true); // (
				butAct[8].setEnabled(true); // )
				butAct[9].setEnabled(true); // clr
				butAct[10].setEnabled(true); // <-
				butAct[11].setEnabled(true); // sin
				butAct[12].setEnabled(true); // cos
				butAct[13].setEnabled(true); // tan
				butAct[14].setEnabled(true); // x^2
				butAct[15].setEnabled(true); // x^y
				butAct[16].setEnabled(true); // sqrt
				
				//ValueButton
				butValue[0].setEnabled(false); // PI
				butValue[1].setEnabled(false); // x
				butValue[2].setEnabled(false); // y
				butValue[3].setEnabled(false); // z
				break;
				
				
//		<-------------- Hex Calculation -------------------->
			case 3:
//				NumButtons 
				butNum[0].setEnabled(true); // 0
				butNum[1].setEnabled(true); // 1
				butNum[2].setEnabled(true); // 2
				butNum[3].setEnabled(true); // 3
				butNum[4].setEnabled(true); // 4
				butNum[5].setEnabled(true); // 5
				butNum[6].setEnabled(true); // 6
				butNum[7].setEnabled(true); // 7
				butNum[8].setEnabled(true); // 8
				butNum[9].setEnabled(true); // 9
				butNum[10].setEnabled(true); // A
				butNum[11].setEnabled(true); // B
				butNum[12].setEnabled(true); // C
				butNum[13].setEnabled(true); // D
				butNum[14].setEnabled(true); // E
				butNum[15].setEnabled(true); // F
				
				//ActButtons
				butAct[0].setEnabled(true); // +
				butAct[1].setEnabled(true); // -
				butAct[2].setEnabled(true); // *
				butAct[3].setEnabled(true); // /
				butAct[4].setEnabled(false); // .
				butAct[5].setEnabled(false); // +/-
				butAct[6].setEnabled(true); // =
				butAct[7].setEnabled(true); // (
				butAct[8].setEnabled(true); // )
				butAct[9].setEnabled(true); // clr
				butAct[10].setEnabled(true); // <-
				butAct[11].setEnabled(true); // sin
				butAct[12].setEnabled(true); // cos
				butAct[13].setEnabled(true); // tan
				butAct[14].setEnabled(true); // x^2
				butAct[15].setEnabled(true); // x^y
				butAct[16].setEnabled(true); // sqrt
				
				//ValueButton
				butValue[0].setEnabled(false); // PI
				butValue[1].setEnabled(false); // x
				butValue[2].setEnabled(false); // y
				butValue[3].setEnabled(false); // z
				break;
				
				
//	<---------------    LINEAR_EQUATION ---------------- >
			case 4:
				//NumButtons 
				butNum[0].setEnabled(true); // 0
				butNum[1].setEnabled(true); // 1
				butNum[2].setEnabled(true); // 2
				butNum[3].setEnabled(true); // 3
				butNum[4].setEnabled(true); // 4
				butNum[5].setEnabled(true); // 5
				butNum[6].setEnabled(true); // 6
				butNum[7].setEnabled(true); // 7
				butNum[8].setEnabled(true); // 8
				butNum[9].setEnabled(true); // 9
				butNum[10].setEnabled(false); // A
				butNum[11].setEnabled(false); // B
				butNum[12].setEnabled(false); // C
				butNum[13].setEnabled(false); // D
				butNum[14].setEnabled(false); // E
				butNum[15].setEnabled(false); // F
				
				//ActButtons
				butAct[0].setEnabled(true); // +
				butAct[1].setEnabled(true); // -
				butAct[2].setEnabled(true); // *
				butAct[3].setEnabled(true); // /
				butAct[4].setEnabled(true); // .
				butAct[5].setEnabled(true); // +/-
				butAct[6].setEnabled(true); // =
				butAct[7].setEnabled(true); // (
				butAct[8].setEnabled(true); // )
				butAct[9].setEnabled(true); // clr
				butAct[10].setEnabled(true); // <-
				butAct[11].setEnabled(false); // sin
				butAct[12].setEnabled(false); // cos
				butAct[13].setEnabled(false); // tan
				butAct[14].setEnabled(true); // x^2
				butAct[15].setEnabled(true); // x^y
				butAct[16].setEnabled(false); // sqrt
				
				//ValueButton
				butValue[0].setEnabled(true); // PI
				butValue[1].setEnabled(true); // x
				butValue[2].setEnabled(false); // y
				butValue[3].setEnabled(false); // z
				break;
				
				
			default:
				JOptionPane.showMessageDialog(null,lang.getError("typImplemented"),"Error(changeCalcTyp)",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	//Methoden die von der nächsten Instanz überschrieben werden müssen!
	protected abstract void keyEventPressed(KeyEvent key);
	protected abstract void keyEventTyped(KeyEvent key);
	protected abstract void butEvent(ActionEvent action);
	protected abstract void clearCalc();
	protected abstract void outputSave();
	protected abstract void showHelp();
	protected abstract void showAbout();
	protected abstract boolean parseEquation();
	protected abstract boolean parseSimCalculation();
	
}
