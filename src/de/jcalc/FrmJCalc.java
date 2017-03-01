package de.jcalc;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;



/**
 * Enthält alle Ereignisse und nötigen Funktionen zur Ereignis behandlung 
 * von einem Frame aus FrmCalc.
 * 
 * @author Aug
 * @version 0.1A
 */
public class FrmJCalc extends AbstractFrmJCalc 
{
	JCalc jcalc = new JCalc();
	
	private boolean isValue = false;
	private boolean isResult = false;
	private boolean isAction = false;
	private boolean isRight = false;
	private int clipsOpen = 0;
	private Performed tempPerf = null;
	
	//Lineare Gleichungen
	private Performed.typs tempTyp = null;
	private Performed.unknows tempUnknow = null;
	boolean isUnknowPower = false;
	int tempUnknowPower = 1;
	private String tempInput = null;
	//private boolean tempTypMultiply = false;

	public FrmJCalc()
	{
		super();
		changeCalctyp(0); //Set Simply Calculation
		
		
		//Ein paar Versuche
		System.out.println("Dec2Bin(585) = "+jcalc.Dec2Bin("585"));
		System.out.println("Bin2Dec(1001001001) = "+jcalc.Bin2Dec("1001001001"));
		System.out.println("Dec2Oct(1234) = "+jcalc.Dec2Oct("1234"));
		System.out.println("Oct2Dec(2322) = "+jcalc.Oct2Dec("2322"));
		System.out.println("Dec2Hex(1234) = "+jcalc.Dec2Hex("1234"));
		System.out.println("Hex2Dec(4D2) = "+jcalc.Hex2Dec("4D2"));
	}
	
	protected void clearCalc()
	{
		System.out.println(jcalc.langmgr.getMessage("clearCalc"));
		jcalc.clearQueue();
		txtState.setText("");
		txtDisplay.setText("0");
		tempPerf=null;
		clipsOpen = 0;
		isAction = false;
		isResult = false;
		isValue = false;
		isRight = false;
		tempInput = null;
		tempTyp = null;
		tempUnknow = null;
		tempUnknowPower = 1;
		isUnknowPower = false;
	}
	
	/* --------------------------------------- EVENTS -----------------------------------------*/
	private void eventNumber(String key)
	{
		if(tempPerf==null)
		{
			if(isResult == true)
			{
				clearCalc();
				
			}
			
			if(Double.parseDouble(txtDisplay.getText()) == 0 && txtDisplay.getText().indexOf(".") < 0 || isValue==true)
			{
				txtDisplay.setText(key);
				isValue = false;
			}
			else
			{
				txtDisplay.setText(txtDisplay.getText()+key);
			}
		}
		isAction = false;
	}
	
	private void eventReverse()
	{
		if(txtDisplay.getText().startsWith("+"))
		{
			txtDisplay.setText("-"+txtDisplay.getText().substring(1));
		}
		else if(txtDisplay.getText().startsWith("-"))
		{
			txtDisplay.setText("+"+txtDisplay.getText().substring(1));
		}
		else
		{
			txtDisplay.setText("-"+txtDisplay.getText());
		}
	}
	
	private void eventPoint()
	{
		if(txtDisplay.getText().indexOf(".") == -1 && tempPerf==null)
		{
			txtDisplay.setText(txtDisplay.getText()+".");
		}
	}
	
	private void eventAction(String key)
	{
		if(isAction == true) return;
		
		if(jcalc.calctyp == JCalc.calctyps.LINEAR_EQUATION)
		{
			equationEventAction(key);
		}
		else
		{
			if(tempPerf == null)
			{
				if(isResult == true)
				{
					txtState.setText("");
					String tempDisp = txtDisplay.getText();
					clearCalc();
					txtDisplay.setText(tempDisp); tempDisp = null;
				}
				
				jcalc.addQueue(key.toString().toCharArray()[0],txtDisplay.getText(),txtDisplay.getText());			
				
				txtState.setText(txtState.getText()+txtDisplay.getText());
			}
			else
			{
				if(key == "+") tempPerf.clipCloseTyp =Performed.typs.PLUS;
				if(key == "-") tempPerf.clipCloseTyp =Performed.typs.MINUS;
				if(key == "*") tempPerf.clipCloseTyp =Performed.typs.MULTI;
				if(key == "/") tempPerf.clipCloseTyp =Performed.typs.DIVIDE;
				
				jcalc.addQueue(tempPerf);
				tempPerf=null;
			}
			
		}

		txtState.setText(txtState.getText()+key);
		txtDisplay.setText("0");		
		isValue = false;
		isAction = true;
	}
	
	private void equationEventAction(String key) 
	{
		//if(tempTyp == null) tempTyp = Performed.typs.PLUS;
		
		if(isValue==false)
		{
			tempInput = txtDisplay.getText();
		}
		else if(isUnknowPower == true)
		{
			tempUnknowPower = Integer.parseInt(txtDisplay.getText().toString());
		}
		else
		{
			System.out.println("isValue==True");
			if(txtDisplay.getText().substring(txtDisplay.getText().length() -1).equalsIgnoreCase("X"))
				tempUnknow = Performed.unknows.X;
			else if(txtDisplay.getText().substring(txtDisplay.getText().length() -1).equalsIgnoreCase("Y"))
				tempUnknow = Performed.unknows.Y;
			else if(txtDisplay.getText().substring(txtDisplay.getText().length() -1).equalsIgnoreCase("Z"))
				tempUnknow = Performed.unknows.Z;
		}
		
		if(isRight == false) jcalc.addLeft(tempTyp,tempInput,tempUnknow, tempUnknowPower);
		else jcalc.addRight(tempTyp, tempInput, tempUnknow,tempUnknowPower);
		
		tempTyp = null;
		tempInput = null;
		tempUnknow = null;
		tempUnknowPower = 1;
		isUnknowPower = false;
		try {if(Double.parseDouble(txtDisplay.getText().toString()) != 0.0d) txtState.setText(txtState.getText()+txtDisplay.getText());}
		catch(NumberFormatException e) {  txtState.setText(txtState.getText()+txtDisplay.getText()); }
		txtDisplay.setText("0");
		
		
		if(key == "+") tempTyp =Performed.typs.PLUS;
		if(key == "-") tempTyp =Performed.typs.MINUS;
		if(key == "*") tempTyp =Performed.typs.MULTI;
		if(key == "/") tempTyp =Performed.typs.DIVIDE;

	}

	private void eventClipOpen()
	{
		if(jcalc.calctyp == JCalc.calctyps.LINEAR_EQUATION)
		{
			Performed temp = new Performed();
			temp.typ = Performed.typs.CLIP_OPEN;
			temp.clipCloseTyp = tempTyp;
			if(isRight == false)
			{
				jcalc.addLeft(temp);
			}
			else
			{
				jcalc.addRight(temp);
			}
			txtState.setText(txtState.getText()+"(");
			isAction = true;
			tempTyp = null;
		}
		else
		{
			if(txtState.getText().endsWith("+") ||
			   txtState.getText().endsWith("-") ||
			   txtState.getText().endsWith("*") ||
			   txtState.getText().endsWith("/") ||
			   txtState.getText().endsWith("^") ||
			   txtState.getText().length() < 1  && 
			   isResult == false &&
			   tempPerf == null )
			{
				this.clipsOpen++;
				jcalc.addQueue(Performed.typs.CLIP_OPEN,"0");
				txtState.setText(txtState.getText()+"(");
			}
					
					isAction =false;
		}
	}
	
	private void eventClipClose()
	{
		if(jcalc.calctyp == JCalc.calctyps.LINEAR_EQUATION)
		{
			if(isValue==false)
			{
				tempInput = txtDisplay.getText();
			}
			else
			{
				System.out.println("isValue==True");
				if(txtDisplay.getText().substring(txtDisplay.getText().length() -1).equalsIgnoreCase("X"))
					tempUnknow = Performed.unknows.X;
				else if(txtDisplay.getText().substring(txtDisplay.getText().length() -1).equalsIgnoreCase("Y"))
					tempUnknow = Performed.unknows.Y;
				else if(txtDisplay.getText().substring(txtDisplay.getText().length() -1).equalsIgnoreCase("Z"))
					tempUnknow = Performed.unknows.Z;
			}
			
			if(isRight == false) jcalc.addLeft(tempTyp,tempInput,tempUnknow);
			else jcalc.addRight(tempTyp, tempInput, tempUnknow);
			
			tempTyp = null;
			tempInput = null;
			tempUnknow = null;
			tempUnknowPower = 1;
			isUnknowPower = false;
			txtState.setText(txtState.getText()+txtDisplay.getText());
			txtDisplay.setText("");
			
			if(isRight == false)
			{
				jcalc.addLeft(new Performed(Performed.typs.CLIP_CLOSE,"0"));
			}
			else
			{
				jcalc.addRight(new Performed(Performed.typs.CLIP_CLOSE,"0"));
			}
			txtState.setText(txtState.getText()+")");
			txtDisplay.setText("0");
			isAction = false;
		}
		else
		{
			if(txtState.getText().endsWith("+") == false ||
			   txtState.getText().endsWith("-") == false ||
			   txtState.getText().endsWith("*") == false ||
			   txtState.getText().endsWith("/") == false &&
			   isResult == false)
			{
				this.clipsOpen--;
				if(tempPerf != null)
				{
					tempPerf.clipCloseTyp = Performed.typs.CLIP_MORECLOSE;
					jcalc.addQueue(tempPerf);
				}
				
				tempPerf = new Performed();
				tempPerf.NumInput = new BigDecimal(txtDisplay.getText());
				tempPerf.StrInput = txtDisplay.getText();
				tempPerf.typ = Performed.typs.CLIP_CLOSE;
				if(txtState.getText().endsWith(")") == false)
				{
					txtState.setText(txtState.getText()+txtDisplay.getText()+")");
				}
				else
				{
					txtState.setText(txtState.getText()+")");
				}
				txtDisplay.setText("0");
				
			}
		}
	}
	
	private void eventResult()
	{
		if(jcalc.calctyp == JCalc.calctyps.LINEAR_EQUATION)
		{
			if(isValue == false) tempInput = txtDisplay.getText();
			
			if(tempInput != null) txtState.setText(txtState.getText()+tempInput);
			if(tempUnknow !=null) txtState.setText(txtState.getText()+tempUnknow.toString());
			
			if(isRight == false)
			{
				jcalc.addLeft(tempTyp,tempInput,tempUnknow);
				txtState.setText(txtState.getText()+"=");
				txtDisplay.setText("0");
				isRight = true;
				tempTyp = null; 
				tempInput = null;
				tempUnknow = null;
			}
			else
			{
				jcalc.addRight(tempTyp,tempInput,tempUnknow);
				txtState.setText(txtState.getText()+ "  => ");
				getResult();
			}
		}
		else
		{
			if(txtState.getText().toString().endsWith("=") == false && isResult == false)
			{
				if(clipsOpen > 0)
				{
					while(clipsOpen > 0)
					{
						eventClipClose();
					}
				}
				
				if(txtState.getText().endsWith(")"))
				{
					txtDisplay.setText("");
				}
				txtState.setText(txtState.getText()+txtDisplay.getText()+"=");
				if(tempPerf==null)
				{
					jcalc.addQueue(Performed.typs.RESULT,txtDisplay.getText().toString(),txtDisplay.getText().toString());
				}
				else
				{
					tempPerf.clipCloseTyp =Performed.typs.RESULT;
					jcalc.addQueue(tempPerf);
					tempPerf=null;
					isValue = false;
				}
				
				getResult();
			}
			isAction = false;
		}
	}
	
	private void eventBack()
	{ 
		txtDisplay.setText(txtDisplay.getText().substring(0,txtDisplay.getText().length()-1));
		if(txtDisplay.getText().length() == 0) txtDisplay.setText("0");
	}
	
	private void eventSqrt()
	{
		String temp = null;
		if(isResult == true)
		{
			temp = txtDisplay.getText();
			clearCalc();
		}
		
		if(txtState.getText().endsWith("+") ||
		   txtState.getText().endsWith("-") ||
		   txtState.getText().endsWith("*") ||
		   txtState.getText().endsWith("/") ||
		   txtState.getText().length() == 0 && 
		   tempPerf == null )
		{
			jcalc.addQueue(Performed.typs.SQRT,"0");
			txtState.setText(txtState.getText()+"sqrt(");
		}
		
		if(temp != null) 
		{
			txtDisplay.setText(temp);
			isValue = true;
		}
	}
	
	private void eventSin()
	{
		String temp = null;
		if(isResult == true)
		{
			temp = txtDisplay.getText();
			clearCalc();
		}
		
		if(txtState.getText().endsWith("+") ||
		   txtState.getText().endsWith("-") ||
		   txtState.getText().endsWith("*") ||
		   txtState.getText().endsWith("/") ||
		   txtState.getText().length() == 0 && 
		   tempPerf == null )
		{
			jcalc.addQueue(Performed.typs.SIN,"0");
			txtState.setText(txtState.getText()+"sin(");
		}
		
		if(temp != null) 
		{
			txtDisplay.setText(temp);
			isValue = true;
		}
	}
	
	private void eventTan()
	{
		String temp = null;
		if(isResult == true)
		{
			temp = txtDisplay.getText();
			clearCalc();
		}
		
		if(txtState.getText().endsWith("+") ||
		   txtState.getText().endsWith("-") ||
		   txtState.getText().endsWith("*") ||
		   txtState.getText().endsWith("/") ||
		   txtState.getText().length() == 0&& 
		   tempPerf == null )
		{
			jcalc.addQueue(Performed.typs.TAN,"0");
			txtState.setText(txtState.getText()+"tan(");
		}
		
		if(temp != null) 
		{
			txtDisplay.setText(temp);
			isValue = true;
		}
	}
	
	private void eventCos()
	{
		String temp = null;
		if(isResult == true)
		{
			temp = txtDisplay.getText();
			clearCalc();
		}
		
		if(txtState.getText().endsWith("+") ||
		   txtState.getText().endsWith("-") ||
		   txtState.getText().endsWith("*") ||
		   txtState.getText().endsWith("/") ||
		   txtState.getText().length() == 0 && 
		   tempPerf == null )
		{
			jcalc.addQueue(Performed.typs.COS,"0");
			txtState.setText(txtState.getText()+"cos(");
		}
		
		if(temp != null) 
		{
			txtDisplay.setText(temp);
			isValue = true;
		}
	}
	
	private void eventPI()
	{
		if(isResult == true)
		{
			clearCalc();
		}
		
		this.isValue = true;
		txtDisplay.setText(jcalc.PI.toString());
	}
	
	private void eventPower2()
	{
		if(jcalc.calctyp == JCalc.calctyps.LINEAR_EQUATION)
		{
			isUnknowPower = true;
			isValue = true;
			txtState.setText(txtState.getText()+txtDisplay.getText()+"^");
			txtDisplay.setText("2");
		}
		else
		{
			if(isResult == true)
			{
				String temp = txtDisplay.getText();
				clearCalc();
				txtDisplay.setText(temp);
				temp = null;
				isResult = false;
			}
			jcalc.addQueue(Performed.typs.POWER,txtDisplay.getText(),txtDisplay.getText());
			txtState.setText(txtState.getText()+txtDisplay.getText()+"^");
			txtDisplay.setText("2");
			isValue=true;
		}
	}
	
	private void eventPower()
	{
		if(jcalc.calctyp == JCalc.calctyps.LINEAR_EQUATION)
		{
			isUnknowPower = true;
			txtState.setText(txtState.getText()+txtDisplay.getText()+"^");
			txtDisplay.setText("0");

		}
		else
		{
			if(isResult == true)
			{
				String temp = txtDisplay.getText();
				clearCalc();
				txtDisplay.setText(temp);
				temp = null;
				isResult=false;
			}
			jcalc.addQueue(Performed.typs.POWER,txtDisplay.getText(),txtDisplay.getText());
			txtState.setText(txtState.getText()+txtDisplay.getText()+"^");
			txtDisplay.setText("0");
		}
	}
	
	private void eventUnknowX()
	{
		if(isResult == true) clearCalc();
		
		if(Integer.parseInt(txtDisplay.getText()) == 0)
		{
			tempUnknow = Performed.unknows.X;
			txtDisplay.setText("x");
		}
		else
		{
			tempInput = txtDisplay.getText();
			tempUnknow = Performed.unknows.X;
			txtDisplay.setText(txtDisplay.getText()+"x");
		}
		isValue = true;
		isAction = false;
	}
	
	
	
	private void getResult()
	{
		try
		{
			jcalc.setSolutionWay(txtState.getText());
			txtDisplay.setText(jcalc.calc().toString());
			txtOutput.setText(jcalc.getSolutionWay());
		}
		catch(Exception e1)
		{
			txtDisplay.setText(jcalc.langmgr.getError("error"));
			e1.printStackTrace();
		}
		
		txtState.setText(txtState.getText()+txtDisplay.getText());
		isResult = true;
	}
	
	/* ---------------------------------- Menu-Events --------------------------------------- */
	
	protected boolean parseSimCalculation()
	{
		String parse = JOptionPane.showInputDialog(jcalc.langmgr.getGuiText("term"));
		txtState.setText(jcalc.parseString(parse));
		getResult();
		return true;
	}
	
	protected void outputSave()
	{
		JFileChooser fc = new JFileChooser();
	    FileWriter out = null;
	    
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setSelectedFile(new File("calculation"));
	    
	    int returnVal = fc.showSaveDialog( null );
	    
	    if ( returnVal == JFileChooser.APPROVE_OPTION )
	    {
	      File file = fc.getSelectedFile();
	      System.out.println("Save to " + file.getAbsolutePath() );
	      
	      try 
	      {
			out = new FileWriter(file);
	      } 
	      catch (IOException e) 
	      {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,jcalc.langmgr.getError("makeFile"),"Save...",JOptionPane.ERROR_MESSAGE);
			return;
	      }
	      
	      try 
	      {
			out.write(txtOutput.getText());
			out.flush();
	      } 
	      catch (IOException e) 
	      {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,jcalc.langmgr.getError("writeFile"),"Save...",JOptionPane.ERROR_MESSAGE);
			return;
	      }
	      
	      JOptionPane.showMessageDialog(null,jcalc.langmgr.getMessage("saveFile"),"Save...",JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
	protected void showAbout()
	{
		//DoTo: Richtige Maske machen :P
		JOptionPane.showMessageDialog(null,"Programming:\nLucifer002\nLucifer002@jcalc.at\n\nQualitymanagement:\nGeisteskranker\nGeisteskranker@jcalc.at","About",JOptionPane.INFORMATION_MESSAGE);
	}
	
	protected void showHelp()
	{
		JOptionPane.showMessageDialog(null,jcalc.langmgr.getError("notImplemented")+"\nWe have no HelpMenu :(","Help", JOptionPane.ERROR_MESSAGE);
	}
	
	protected boolean parseEquation()
	{
		JOptionPane.showMessageDialog(null,jcalc.langmgr.getError("notImplemented"),"Equation", JOptionPane.ERROR_MESSAGE);
		return false;
	}
	
	protected void butEvent(ActionEvent action)
	{
		System.out.println("Button pressed --> "+action.getActionCommand());
		
		if
		(
				action.getActionCommand() == "0" ||
				action.getActionCommand() == "1" ||
				action.getActionCommand() == "2" ||
				action.getActionCommand() == "3" ||
				action.getActionCommand() == "4" ||
				action.getActionCommand() == "5" ||
				action.getActionCommand() == "6" ||
				action.getActionCommand() == "7" ||
				action.getActionCommand() == "8" ||
				action.getActionCommand() == "9" 
				
		){
			eventNumber(action.getActionCommand());
		}
		
		else if(action.getActionCommand() == "+/-" )
		{
			eventReverse();
		}
		
		else if(action.getActionCommand() == "." && isResult==false)
		{
			eventPoint();
		}
		
		else if
			   (
					   action.getActionCommand() == "+" ||
					   action.getActionCommand() == "-" ||
					   action.getActionCommand() == "/" ||
					   action.getActionCommand() == "*" 
			   )
		{
			eventAction(action.getActionCommand());
		}
		
		else if(action.getActionCommand()=="(" )
		{
			eventClipOpen();
		}
		else if(action.getActionCommand()==")")
		{
			eventClipClose();
		}
		
		else if(action.getActionCommand()=="=")
		{
			eventResult();
		}
		else if(action.getActionCommand()=="clr")
		{
			clearCalc();
		}
		
		else if(action.getActionCommand() == "<-")
		{
			eventBack();
		}
		
		else if(action.getActionCommand() == "sqrt")
		{
			eventSqrt();
		}
		else if(action.getActionCommand() == "sin")
		{
			eventSin();
		}
		else if(action.getActionCommand() == "tan")
		{
			eventTan();
		}
		else if(action.getActionCommand() == "cos" )
		{
			eventCos();
		}
		
		else if(action.getActionCommand() == "PI")
		{
			eventPI();
		}
		
		else if(action.getActionCommand() == "x^2")
		{
			eventPower2();
		}
		
		else if(action.getActionCommand() == "x^y")
		{
			eventPower();
		}
		else if(action.getActionCommand() == "x")
		{
			eventUnknowX();
		}
		
		//Focus immer auf txtDisplay zurücksetzen
		txtDisplay.requestFocus();
	}
	
	protected void keyEventPressed(KeyEvent key)
	{
		if(key.getKeyCode() == KeyEvent.VK_SHIFT) return;
		if(key.getKeyCode() == KeyEvent.VK_COMMA) eventPoint();
		else if(key.getKeyCode() == KeyEvent.VK_ENTER) eventResult();
		else if(key.getKeyCode() == KeyEvent.VK_BACK_SPACE) eventBack();
		else if(key.getKeyCode() == KeyEvent.VK_DECIMAL) eventPoint();
		else if(key.getKeyCode() == KeyEvent.VK_ESCAPE) clearCalc();
		else if(key.getKeyCode() == 46) eventPoint();
		
	}
	
	protected void keyEventTyped(KeyEvent key)
	{
		switch(key.getKeyChar())
		{
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				eventNumber(""+key.getKeyChar());
				break;
			case '+':
			case '-':
			case '*':
			case '/':
				eventAction(""+key.getKeyChar());
				break;
			case '(':
				eventClipOpen();
				break;
			case ')':
				eventClipClose();
				break;
			case '=':
				eventResult();
				break;
			default:
				System.out.println(key.toString());
				break;
			
		}
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
		clearCalc();
		
		if(jcalc.calctyp == JCalc.calctyps.BINARY_CALCULATION)
			txtDisplay.setText(jcalc.Bin2Dec(txtDisplay.getText()).toString());
		else if(jcalc.calctyp == JCalc.calctyps.OCT_CALCULATION)
			txtDisplay.setText(jcalc.Oct2Dec(txtDisplay.getText()).toString());
		else if(jcalc.calctyp == JCalc.calctyps.HEX_CALCULATION)
			txtDisplay.setText(jcalc.Hex2Dec(txtDisplay.getText()).toString());
		
		switch(index)
		{
			case 0:
				jcalc.calctyp = JCalc.calctyps.SIMPLE_CALCULATION;
				break;
			case 1:
				txtDisplay.setText(jcalc.Dec2Bin(txtDisplay.getText()));
				jcalc.calctyp = JCalc.calctyps.BINARY_CALCULATION;
				break;
			case 2:
				txtDisplay.setText(jcalc.Dec2Oct(txtDisplay.getText()));
				jcalc.calctyp = JCalc.calctyps.OCT_CALCULATION;
				break;
			case 3:
				txtDisplay.setText(jcalc.Dec2Hex(txtDisplay.getText()));
				jcalc.calctyp = JCalc.calctyps.HEX_CALCULATION;
				break;
			case 4:
				jcalc.calctyp = JCalc.calctyps.LINEAR_EQUATION;
				break;
			case 5:
				jcalc.calctyp = JCalc.calctyps.NONLINEAR_EQUATION;
				break;
			default:
				JOptionPane.showMessageDialog(null,jcalc.langmgr.getError("typImplemented"),"Error(changeCalcTyp)",JOptionPane.ERROR_MESSAGE);
		}
		super.changeCalctyp(index);
	}
	
}
