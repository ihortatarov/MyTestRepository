package de.jcalc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Eine Queue mit Objekten<Performed> gefüllt werden vollständig durchgerechnet<br>
 * Die Klasse unterstützt alle Zahlensysteme. Diese werten intern ins Decimalsystem
 * umgerechnet und normal nach allen Regeln der mathematik verarbeitet. Vor der Ausgabe 
 * erfolgt wieder die Umrechnung in das jeweilige Zahlensystem zurückgerechnet!
 * <br><br>
 * Es wurde versucht bei dieser Calculationsroutine alle Regeln der mathematik 
 * zu brücksichtigen!<br>
 * Folgende Regegeln sind zum Einsatz gekommen:
 * Normale Rechnungen: Klammern(inkl. Sqrt,Sin,Cos,Tan),Punkt,Strich
 * Lineare gleichungen: Werte ausgleichen,.....
 * 
 * @author Aug
 * @version 0.1.2A
 */
public class JCalc 
{
	public JCalc()
	{
		langmgr = new LangMgr();
	}
	
	public enum calctyps
	{
		SIMPLE_CALCULATION,
		BINARY_CALCULATION,
		OCT_CALCULATION,
		HEX_CALCULATION,
		LINEAR_EQUATION,
		NONLINEAR_EQUATION;
	};
	
	private Queue<Performed> queue= new LinkedList<Performed>();
	private Queue<Performed> left = new LinkedList<Performed>();
	private Queue<Performed> right= new LinkedList<Performed>();
	
	private String solutionWay = new String();
	
	public final BigDecimal PI = new BigDecimal("3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117");
	
	public calctyps calctyp;
	public LangMgr langmgr;
	
	public static void main(String args[])
	{
		FrmJCalc frm = new FrmJCalc();
		frm.setVisible(true);
	}
	
	
	public void addQueue(Performed.typs typ, String input)
	{
		queue.add(new Performed(typ,input));
	}
	public void addQueue(char typ, String input)
	{
		switch(typ)
		{
			case '+':
				this.addQueue(Performed.typs.PLUS,input);
				break;
			case '-':
				this.addQueue(Performed.typs.MINUS,input);
				break;
			case '*':
				this.addQueue(Performed.typs.MULTI,input);
				break;
			case '/':
				this.addQueue(Performed.typs.DIVIDE,input);
				break;
			
		}
	}
	public void addQueue(char typ,String NumInput, String StrInput)
	{
		switch(typ)
		{
			case '+':
				this.addQueue(Performed.typs.PLUS,NumInput,StrInput);
				break;
			case '-':
				this.addQueue(Performed.typs.MINUS,NumInput,StrInput);
				break;
			case '*':
				this.addQueue(Performed.typs.MULTI,NumInput,StrInput);
				break;
			case '/':
				this.addQueue(Performed.typs.DIVIDE,NumInput,StrInput);
				break;
			
		}
	}
	public void addQueue(Performed.typs typ,String NumInput, String StrInput)
	{
		queue.add(new Performed(typ,NumInput, StrInput));
	}
	public void addQueue(Performed perf)
	{
		this.queue.add(perf);
	}
	public void addQueue(Queue<Performed> q)
	{
		this.queue.addAll(q);
	}
	
	public void addLeft(Performed.typs typ, String input)
	{
		left.add(new Performed(typ,input));
	}
	public void addLeft(Performed.typs typ, String input, Performed.unknows unknow)
	{
		left.add(new Performed(typ,input,unknow));
	}
	public void addLeft(Performed.typs typ, String input, Performed.unknows unknow, int power)
	{
		Performed perf = new Performed();
		perf.typ = typ;
		if(input != null) perf.NumInput = new BigDecimal(input);
		perf.unknow = unknow;
		perf.unknowPower = power;
		
		addLeft(perf);
	}
	public void addLeft(Performed perf)
	{
		left.add(perf);
	}
	
	public void addRight(Performed.typs typ, String input)
	{
		right.add(new Performed(typ,input));
	}
	public void addRight(Performed.typs typ, String input, Performed.unknows unknow)
	{
		right.add(new Performed(typ,input,unknow));
	}
	public void addRight(Performed.typs typ, String input, Performed.unknows unknow, int power)
	{
		Performed perf = new Performed();
		perf.typ = typ;
		if(input != null) perf.NumInput = new BigDecimal(input);
		perf.unknow = unknow;
		perf.unknowPower = power;
		
		addRight(perf);
	}
	public void addRight(Performed perf)
	{
		right.add(perf);
	}
	
	
	public Performed getQueue()
	{
		return this.queue.peek();
	}

	public void clearQueue()
	{
		this.queue.clear();
		this.left.clear();
		this.right.clear();
		this.solutionWay = new String();
		System.gc();
	}
		
	
	public String setSolutionWay(String s)
	{
		this.solutionWay += s;
		System.out.print(s);
		return s;
	}
	public BigDecimal setSolutionWay(BigDecimal s)
	{
		if(calctyp ==  JCalc.calctyps.SIMPLE_CALCULATION)
			this.setSolutionWay(s.toString());
		else if(calctyp == JCalc.calctyps.BINARY_CALCULATION)
			this.setSolutionWay(Dec2Bin(s));
		else if(calctyp == JCalc.calctyps.OCT_CALCULATION)
			this.setSolutionWay(Dec2Oct(s));
		else if(calctyp == JCalc.calctyps.HEX_CALCULATION)
			this.setSolutionWay(Dec2Hex(s));
		else this.setSolutionWay(s.toString());
		return s;
	}
	public Performed setSolutionWay(Performed s)
	{
		if(s == null) return s;
		
		s = absPerf(s);
		if(s.typ != null && (s.NumInput != null || s.unknow != null)) setSolutionWay(s.typ2Str());
		if(s.NumInput != null && s.unknow == null) setSolutionWay(s.NumInput);
		else if(s.NumInput != null && s.NumInput.compareTo(new BigDecimal("1")) > 0) setSolutionWay(s.NumInput.toString());
		if(s.unknow != null) setSolutionWay(s.unknow.toString());
		if(s.unknowPower > 1) setSolutionWay("^"+s.unknowPower+" ");
		
		return s;
	}
	private String tempSolutionWay(Performed p) throws Exception
	{
		String s = "";
		if(p == null) throw new Exception("p should not NULL");
		p = absPerf(p);
		if(p.typ != null) s += p.typ2Str();
		if(p.NumInput != null && p.unknow == null) s +=p.NumInput;
		else if(p.NumInput != null && p.NumInput.compareTo(new BigDecimal("1")) >0) s+=p.NumInput.toString();
		if(p.unknow != null) s+=p.unknow.toString();
		if(p.unknowPower >= 1) s+="^"+p.unknowPower+" ";
		
		return s;
	}
	
	public String getSolutionWay()
	{
		return this.solutionWay.toString();
	}
	
	
	/**
	 * Startet die Berechnung der Queue
	 * 
	 * @return result(BigDecimal) 
	 * @throws Exception
	 */
	public String calc() throws Exception
	{
		BigDecimal result=new BigDecimal("0");
		
		if(calctyp == JCalc.calctyps.SIMPLE_CALCULATION)
		{
			//Do Nothing by simple Calculations!
		}
		else if(calctyp == JCalc.calctyps.BINARY_CALCULATION)
		{
			Queue<Performed> tempQueue = new LinkedList<Performed>();
			Performed perf = null;
			tempQueue.addAll(queue);
			queue.clear();
			
			while(!tempQueue.isEmpty())
			{
				perf = tempQueue.poll();
				perf.NumInput = new BigDecimal(Bin2Dec(perf.StrInput));
				addQueue(perf);
			}
		}
		else if(calctyp == JCalc.calctyps.OCT_CALCULATION)
		{
			Queue<Performed> tempQueue = new LinkedList<Performed>();
			Performed perf = null;
			tempQueue.addAll(queue);
			queue.clear();
			
			while(!tempQueue.isEmpty())
			{
				perf = tempQueue.poll();
				perf.NumInput = new BigDecimal(Oct2Dec(perf.StrInput));
				addQueue(perf);
			}
		}
		else if(calctyp == JCalc.calctyps.HEX_CALCULATION)
		{
			Queue<Performed> tempQueue = new LinkedList<Performed>();
			Performed perf = null;
			tempQueue.addAll(queue);
			queue.clear();
			
			while(!tempQueue.isEmpty())
			{
				perf = tempQueue.poll();
				perf.NumInput = new BigDecimal(Hex2Dec(perf.StrInput));
				addQueue(perf);
			}
		}
		else if(calctyp == JCalc.calctyps.LINEAR_EQUATION)
		{
			setSolutionWay(" --> V\n");
			equationClipCalc(left); //Klammern auflösen
			setSolutionWay(" = ");
			equationClipCalc(right);
			setSolutionWay("   --> V\n");
			return equationCalc(); //Vereinfachen
		}
		else
		{
			System.err.println("CalcTyp = "+calctyp.toString());
			throw new Exception(langmgr.getError("noCalcTyp"));
		}
		
		setSolutionWay("\n");
		while(clipCalc()) 
		{
			this.setSolutionWay("\n"); 
			//hier werden die Klammern aufgelöst
		}
		result = primCalc(this.queue);
		this.setSolutionWay(result);
		System.out.println();
		
		if(calctyp ==  JCalc.calctyps.SIMPLE_CALCULATION)
			return(result.toString());
		else if(calctyp == JCalc.calctyps.BINARY_CALCULATION)
			return(Dec2Bin(result));
		else if(calctyp == JCalc.calctyps.OCT_CALCULATION)
			return(Dec2Oct(result));
		else if(calctyp == JCalc.calctyps.HEX_CALCULATION)
			return(Dec2Hex(result));
		else return result.toString();
	}
	
	
	
	
	private boolean clipCalc() throws Exception
	{
		boolean indexSpecClips = false;
		Performed.typs specTyp = null;
		boolean indexClips = false;
		boolean clipOpen = false;
		BigDecimal res1 = null;

		Queue<Performed> clip = new LinkedList<Performed>();
		Queue<Performed> tempQueue = new LinkedList<Performed>();
		Queue<Performed> perfQ = new LinkedList<Performed>();
		Performed perf = null;
		
		perfQ.addAll(this.queue);
		this.queue.clear();
		
		while(!perfQ.isEmpty())
		{
			perf = perfQ.poll();
			

			if(	isSpecClip(perf.typ) == true ||
				perf.typ == Performed.typs.CLIP_OPEN )
			{
				if(clipOpen == true)
				{
					Performed[] print = (Performed[]) tempQueue.toArray(new Performed[0]);
					this.setSolutionWay(print[0].typ2Str());
					for(int i=1; i < print.length; i++)
					{
						this.setSolutionWay(print[i].NumInput);
						this.setSolutionWay(print[i].typ2Str());
					}
					addQueue(tempQueue);
					tempQueue.clear();
					clip.clear();
				}
				
				if(indexSpecClips == true && isSpecClip(perf.typ) == true)
				{
					throw new Exception (langmgr.getError("specClipTwo"));
				}
				indexClips = true;
				if(isSpecClip(perf.typ)==true)
				{
					indexSpecClips = true;
					specTyp = perf.typ;
				}
				clipOpen = true;
				tempQueue.add(perf);
				clip.clear();
			}
			else if(perf.typ == Performed.typs.CLIP_CLOSE && clipOpen == true)
			{
				//Normale Klammern
				if(indexSpecClips == false)
				{
					clipOpen = false;
					clip.add(new Performed(Performed.typs.RESULT,perf.NumInput));
					this.setSolutionWay("(");
					this.setSolutionWay(res1 = primCalc(clip));

					/*
					 * Wenn zwei CLIP_CLOSE nacheinander kommen, ein Perf weitermachen!
					 * 
					 * Probleme, wenn mehrere Klammern mit interner Klammer aufgelöst werden sollte,
					 * sollte so behoben werden!
					 */
					if(perf.clipCloseTyp == Performed.typs.CLIP_MORECLOSE)
					{
						perf = perfQ.poll();
						this.setSolutionWay(")");
						addQueue(new Performed(perf.typ, res1.toString(),perf.clipCloseTyp));
					}
					else
					{
						addQueue(perf.clipCloseTyp, res1.toString());
					}
					

					this.setSolutionWay(")"+perf.typ2Str(perf.clipCloseTyp));
					clip.clear();
					tempQueue.clear();
				}
				//Spezielle Klammern
				else
				{
					clipOpen = false;
					indexSpecClips = false;
					clip.add(new Performed(Performed.typs.RESULT,perf.NumInput));
					
					double res2 = 0d;
					if(specTyp == Performed.typs.SQRT)
					{
						res2 = Double.parseDouble(primCalc(clip).toString());
						res2 = Math.sqrt(res2);
					}
					else if(specTyp == Performed.typs.SIN)
					{
						res2 = Math.toRadians(Double.parseDouble(primCalc(clip).toString()));
						res2 = Math.sin(res2);
					}
					else if(specTyp == Performed.typs.COS)
					{
						res2 = Math.toRadians(Double.parseDouble(primCalc(clip).toString()));
						res2 = Math.cos(res2);
					}
					else if(specTyp == Performed.typs.TAN)
					{
						res2 = Math.toRadians(Double.parseDouble(primCalc(clip).toString()));
						res2 = Math.tan(res2);
					}
					
					addQueue((Performed.typs)perf.clipCloseTyp, Double.toString(res2));
					clip.clear();
				}
			}
			else
			{
				if(clipOpen == true)
				{
					clip.add(perf);
					tempQueue.add(perf);
				}
				else
				{
					addQueue(perf);
					if(perf.typ == Performed.typs.CLIP_CLOSE && perf.NumInput.equals(new BigDecimal("0")))
						this.setSolutionWay(perf.typ2Str()+perf.typ2Str(perf.clipCloseTyp));
					else
					{
						this.setSolutionWay(perf.NumInput);
						this.setSolutionWay(perf.typ2Str()+perf.typ2Str(perf.clipCloseTyp));
					}
				}
			}
		}
		return indexClips;
	}
	
	
	
	
	
	/**
	 * Löst den eine Queue mit simplen(primitiven) Rechnungen 
	 * (Addition,Substraktion,Multiplikation,Division) und Potenzen auf<br>
	 * <br>
	 * Es wurde versucht alle Mathematischen Gesetze zu berücksichtigen
	 * Vorgangsweise:<br>
	 *  1.Ermitteln der Potenzen
	 *  2.Alle Divisonen und Multiplikationen(Punkt vor Strich)
	 *  3.Restlichen Rechnungen (Plus,Minus)
	 * @param prim Queue<Performed><bR> Enthält alle Rechenschritte
	 * 
	 * @return result BigDecimal Ergebnis der Rechnungen
	 * 
	 * @throws Exception
	 */
	private BigDecimal primCalc(Queue<Performed> prim) throws Exception
	{
		BigDecimal result = null;
		Performed clc = null;
		Queue<Performed> tempQueue = new LinkedList<Performed>();
		
		BigDecimal temp = null;
		Performed.typs tempTyp = null;
		
			
			//Potenzen
			while(!prim.isEmpty())
			{
				clc = prim.poll();
				if(temp != null)
				{
					temp = temp.pow(Integer.parseInt(clc.NumInput.toString()));
				}
				
				if(clc.typ == Performed.typs.POWER )
				{
					if(temp == null) temp = clc.NumInput;
					tempTyp = clc.typ;
				}
				else
				{
					if(temp == null)
					{
						tempQueue.add(clc);
					}
					else
					{
						tempQueue.add(new Performed(clc.typ,temp,clc.clipCloseTyp));
						temp = null;
						tempTyp = null;
					}
				}
			}
			clc = null;
			//Potenzen ENDE
			
			prim.addAll(tempQueue);
			tempQueue.clear();
		
			//Punkt vor Strich
			while(!prim.isEmpty())
			{
				clc = prim.poll();
				
				//Verarbeite den Vorherigen Satz
				if(temp != null && tempTyp != null)
				{
					if(tempTyp == Performed.typs.DIVIDE)
					{
						try
						{
							temp = temp.divide(clc.NumInput);
						}
						catch(ArithmeticException ae0)
						{
							temp = temp.divide(clc.NumInput,100,BigDecimal.ROUND_HALF_UP);
						}
					}
					else if(tempTyp == Performed.typs.MULTI)
					{
						temp = temp.multiply(clc.NumInput);
					}
				}
				//Verarbeite den Vorherigen Satz ENDE
				
				
				//Verarbeite aktuellen Datensatz
				if(clc.typ == Performed.typs.DIVIDE || clc.typ == Performed.typs.MULTI)
				{
					if(temp == null) temp = clc.NumInput;
					tempTyp = clc.typ;
				}
				else
				{
					if(temp == null)
					{
						tempQueue.add(clc);
					}
					else
					{
						tempQueue.add(new Performed(clc.typ,temp,clc.clipCloseTyp));
						temp = null;
						tempTyp = null;
					}
				}
				//	Verarbeite aktuellen Datensatz ENDE
			}
			clc = null;
			//Punkt vor Strick ENDE
			
			prim.addAll(tempQueue);
			tempQueue.clear();
		
		//Strichrechnungen 
		try
		{
			while(!prim.isEmpty())
			{
				clc = prim.poll();
				
				if(result == null)
				{
					result = clc.NumInput;
					tempTyp = clc.typ;
				}
				else
				{
					if(tempTyp == Performed.typs.PLUS)
					{
						result = result.add(clc.NumInput);
					}
					else if(tempTyp == Performed.typs.MINUS)
					{
						result = result.subtract(clc.NumInput);
					}
				}
				
				tempTyp = clc.typ;
			}
			
			if(result == null)
			{
				throw new Exception(langmgr.getError("resultNull"));
			}
		}
		
		catch(ArithmeticException e1)
		{
			e1.printStackTrace();
			throw new Exception(langmgr.getError("calcError"));
		}

		//Strichrechnung ENDE
		
		return result;
	}
	
	
	private void equationClipCalc(Queue<Performed> source) throws Exception
	{
		Queue<Performed> tempQueue = new LinkedList<Performed>();
		Performed perf = null;
		Performed tempPerf = null;
		Performed.typs tempTyp = null;
		boolean isClipOpen = false;
		
		//Wenn 1 dann braucht man nicht weitermachen!
		if(source.size() <= 1) 
		{
			equationOutput(source);
			return;
		}
		
		//Überprüfe ob zwei Klammern sind!
		tempQueue.addAll(source);
		int clipsOpen = 0;
		boolean isClipClose = false;
		boolean multiClip2Clip = false;
		while(!tempQueue.isEmpty())
		{
			perf = tempQueue.poll();
			if(perf.typ == Performed.typs.CLIP_OPEN)
			{
				if(isClipOpen == true) throw new Exception(langmgr.getError("clipInClip"));
				
				if(isClipClose == true)
				{
					if(perf.clipCloseTyp == null || perf.clipCloseTyp == Performed.typs.MULTI)
						multiClip2Clip = true;
				}
				
				clipsOpen++;
				isClipOpen = true;
				isClipClose = false;

			}
			else if(perf.typ == Performed.typs.CLIP_CLOSE)
			{
				if(isClipOpen == false) throw new Exception(langmgr.getError("noClipOpen"));
				
				isClipOpen = false;
				isClipClose = true;
			}
			else if(perf.typ == null && perf.NumInput == null && perf.unknow == null)
			{
				//Do nothing
			}
			else
			{
				isClipClose = false;
			}
		}

		//ENDE Überprüfe ob zwei Klammern sind
		
		//Klammer mal Klammer
		if(multiClip2Clip == true) 
			throw new Exception("multiClip2Clip : "+langmgr.getError("notImplemented"));
		//ENDE Klammer mal Klammer
		
		//Klammer auflösen
		tempQueue.addAll(source);
		source.clear();
		while(!tempQueue.isEmpty())
		{
			perf = tempQueue.poll();
			if(perf.typ == Performed.typs.CLIP_OPEN)
			{
				tempTyp = perf.clipCloseTyp;
				isClipOpen = true;

			}
			else if(perf.typ == Performed.typs.CLIP_CLOSE)
			{
				tempTyp = null;
				isClipOpen = false;
				tempPerf = null;
				perf=null;
			}
			else
			{
				if(isClipOpen == true)
				{
					if(tempPerf != null)
					{
						if(tempPerf.typ != null && tempTyp == Performed.typs.MULTI)
						{
							if(tempPerf.typ == Performed.typs.MINUS) tempPerf.NumInput = tempPerf.NumInput.multiply(new BigDecimal("-1"));
							tempPerf.typ = null;
						}
						else if(tempTyp != Performed.typs.MULTI)
						{
							source.add(tempPerf);
							setSolutionWay(tempPerf);
							tempPerf = null;
						}
						else if(tempTyp == Performed.typs.MULTI)
						{
							//Do Nothing!
						}
						else
						{
							throw new Exception(langmgr.getError("unknowError"));
						}
					}
					
					if(perf.NumInput == null) perf.NumInput = new BigDecimal("1");
					
					if(tempTyp == Performed.typs.PLUS) { } //Do Nothing ->Klammer weglassen
					else if(tempTyp == Performed.typs.MINUS) perf.typ = reverseTyp(perf.typ);
					else if(tempTyp == Performed.typs.MULTI) perf.NumInput = tempPerf.NumInput.multiply(perf.NumInput);
					else if(tempTyp == Performed.typs.DIVIDE)perf.NumInput = tempPerf.NumInput.divide(perf.NumInput);
					
					source.add(perf);
					setSolutionWay(perf);
				}
				else
				{
					if(tempPerf != null)
					{
						source.add(tempPerf);
						setSolutionWay(tempPerf);
						tempPerf = null;
					}
					tempPerf = perf;
				}
				
			}
			perf = null;
		}
		
		if(tempPerf != null)
		{
			source.add(tempPerf);
			setSolutionWay(tempPerf);
			tempPerf = null;
		}
		tempQueue.clear();
		perf = null;
		tempTyp = null;
	}
	
	private String equationCalc() throws Exception
	{
		Queue<Performed> tempQueue = new LinkedList<Performed>();
		Performed perf = null;
		String tempSolutionWay = "";
		
		if(left.size() >1) equationSimplify(left);
		else equationOutput(left);
		setSolutionWay(" = ");
		if(right.size() > 1) equationSimplify(right);
		else equationOutput(right);
		
		if(left.size() > 1) //Alles rechenbare mal nach Rechts schieben
		{
			tempQueue.addAll(left);
			left.clear();
			setSolutionWay("  --> ");
			while(!tempQueue.isEmpty())
			{
				perf = tempQueue.poll();
				if(perf == null)
				{
					//Drop
				}
				else if(perf.NumInput != null && perf.unknow == null)
				{
					perf.typ = reverseTyp(perf.typ);
					right.add(perf);
					setSolutionWay(perf.typ2Str()+perf.NumInput+", ");
				}
				else
				{
					left.add(perf);
				}
			}
			tempSolutionWay = "";
			tempQueue.clear();
			perf = null;
			setSolutionWay("\n");
			equationOutput(left);
			setSolutionWay(" = ");
			if(right.size() >1 ) equationSimplify(right);
			else equationOutput(right);
			
			if(right.size() > 1) //Wenn alle normalen Zahlen nach rechtskommen, dann alle unbekannten nach Links
			{
				tempQueue.addAll(right);
				right.clear();
				setSolutionWay("   --> ");
				while(!tempQueue.isEmpty())
				{
					perf = tempQueue.poll();
					if(perf.unknow != null)
					{
						perf.typ = reverseTyp(perf.typ);
						left.add(perf);
						setSolutionWay(perf);
						setSolutionWay(", ");
					}
					else
					{
						right.add(perf);
					}
					perf = null;
				}
				tempQueue.clear();
				perf = null;
				setSolutionWay("\n");
				if(left.size() > 1) equationSimplify(left);
				else equationOutput(left);
				setSolutionWay(" = ");
				equationOutput(right);
				setSolutionWay(" \n");
			}
			
		} //Nach Rechts schieben ENDE
		
		
		else if(right.size() > 1) //Alles rechenbare nach Links
		{
			tempQueue.addAll(right);
			right.clear();
			setSolutionWay("  --> ");
			while(!tempQueue.isEmpty())
			{
				perf = tempQueue.poll();
				if(perf.NumInput != null && perf.unknow == null)
				{
					perf.typ = reverseTyp(perf.typ);
					left.add(perf);
					setSolutionWay(perf.typ2Str()+perf.NumInput+", ");
				}
				else
				{
					right.add(perf);
				}
			}
			tempSolutionWay = "";
			tempQueue.clear();
			perf = null;
			setSolutionWay("\n");
			
			if(left.size() >1 ) equationSimplify(left);
			else equationOutput(left);
			setSolutionWay(" = ");
			equationOutput(right);
			
			if(left.size() > 1) //Wenn alle normalen Zahlen nach links kommen, dann alle unbekannten nach rechts
			{
				tempQueue.addAll(left);
				left.clear();
				setSolutionWay("   --> ");
				while(!tempQueue.isEmpty())
				{
					perf = tempQueue.poll();
					if(perf.unknow != null)
					{
						perf.typ = reverseTyp(perf.typ);
						right.add(perf);
						setSolutionWay(perf);
						setSolutionWay(", ");
					}
					else
					{
						left.add(perf);
					}
					perf = null;
				}
				tempQueue.clear();
				perf = null;
				setSolutionWay("\n");
				equationOutput(left);
				setSolutionWay(" = ");
				if(right.size() > 1) equationSimplify(right);
				else equationOutput(right);
				
				setSolutionWay(" \n");
			}

		}
		//Alles rechenbare nach Links ENDE
		
		/*
		 * Wenn beide Seiten nur noch ein Objekt enthalten wird geschaut,
		 * ob es ein vielfaches von Unknow gibt zum auflösen
		 */
		
		setSolutionWay("\n");equationSimplify(left); setSolutionWay(" = "); equationSimplify(right);
		if(left.size() == 1 && right.size() == 1)
		{
			
			perf = left.poll();
			if(perf.NumInput != null && perf.unknow != null && perf.NumInput.compareTo(new BigDecimal("1")) > 0)
			{
				setSolutionWay("   !--> "+"/"+perf.NumInput+"\n");
				BigDecimal numInput = perf.NumInput;
				perf.NumInput = null;
				left.add(perf);
				perf = null;
				perf = right.poll();
				if(perf.NumInput != null) 
				{
					if(numInput.compareTo(new BigDecimal("0")) == 0) numInput = new BigDecimal("1");
					try
					{
						perf.NumInput = perf.NumInput.divide(numInput);
					}
					catch(ArithmeticException e0)
					{
						perf.NumInput = perf.NumInput.divide(numInput,100,BigDecimal.ROUND_HALF_UP);
					}
					perf = absPerf(perf);
					right.add(perf);
					perf=null;
				}
				else throw new Exception (langmgr.getError("calcError"));
				
			}
			else if(perf.NumInput != null && perf.unknow != null && perf.NumInput.compareTo(new BigDecimal("0")) == 0)
			{
				perf.unknow = null;
				left.add(perf);
				perf = null;
			}
			
			else
			{
				left.add(perf);
				perf = null;
				perf = right.poll();
				if(perf.NumInput != null && perf.unknow != null && perf.NumInput.compareTo(new BigDecimal("1")) > 0)
				{
					setSolutionWay("   !--> "+"/"+perf.NumInput+"\n");
					BigDecimal numInput = perf.NumInput;
					perf.NumInput = null;
					right.add(perf);
					perf = null;
					perf = left.poll();
					if(perf.NumInput != null) 
					{
						if(numInput.compareTo(new BigDecimal("0")) == 0) numInput = new BigDecimal("1");
						try
						{
							perf.NumInput = perf.NumInput.divide(numInput);
						}
						catch(ArithmeticException e0)
						{
							perf.NumInput = perf.NumInput.divide(numInput,100,BigDecimal.ROUND_HALF_UP);
						}
						perf = absPerf(perf);
						left.add(perf);
						perf=null;
					}
					else throw new Exception (langmgr.getError("calcError"));
				}
				else
				{
					right.add(perf);
					perf = null;
				}
			}
			
			if(perf != null) throw new Exception(langmgr.getError("notNull"));
			
			//Negativierung
			perf = left.poll();
			if(perf.unknow != null && perf.typ == Performed.typs.MINUS)
			{
				perf.typ = reverseTyp(perf.typ);
				perf = absPerf(perf);
				left.add(perf);
				perf = right.poll();
				setSolutionWay("   &--> *-1\n");
				perf.typ = reverseTyp(perf.typ);
				perf = absPerf(perf);
				right.add(perf);
			}
			else
			{
				left.add(perf);
				perf = right.poll();
				if(perf.unknow != null && perf.typ == Performed.typs.MINUS)
				{
					tempSolutionWay = tempSolutionWay(perf);
					perf.typ = reverseTyp(perf.typ);
					perf = absPerf(perf);
					right.add(perf);
					perf = left.poll();
					setSolutionWay(perf);
					setSolutionWay(" = "+tempSolutionWay+"   /-->*-1\n");
					perf.typ = reverseTyp(perf.typ);
					perf = absPerf(perf);
					left.add(perf);
				}
				else
				{
					right.add(perf);
				}
			}
		}
		else
		{
			this.equationDebug();
		}

		return (equationOutput(left)+setSolutionWay(" = ")+equationOutput(right)).toString();
		
	}
	
	
	private void equationSimplify(Queue<Performed> source) throws Exception
	{
		Queue<Performed> calcQueue = new LinkedList<Performed>();
		Queue<Performed> tempQueue = new LinkedList<Performed>();
		Performed tempPerf = null;
		Performed perf = null;
		
		//Wenn nur ein Feld da ist, braucht man nichts vereinfachen!
		if(source.size() <= 1) return;
		
		//Vereinfache normale Zahlen
		tempQueue.addAll(source);
		source.clear();
		while(!tempQueue.isEmpty())
		{
			perf = tempQueue.poll();
			if(perf.NumInput != null && perf.unknow == null)
			{
				if(perf.typ == null) perf.typ = Performed.typs.PLUS;
				
				if(tempPerf == null) 
				{
					tempPerf = new Performed();
					tempPerf.NumInput = new BigDecimal("0");
					tempPerf.typ = perf.typ;
					calcQueue.add(tempPerf);
					tempPerf = new Performed();
					tempPerf.NumInput = perf.NumInput;
				}
				else
				{
					tempPerf.typ = perf.typ;
					calcQueue.add(tempPerf);
					tempPerf = new Performed();
					tempPerf.NumInput = perf.NumInput;
				}
			}
			else if(perf.typ != null && perf.NumInput == null && perf.unknow == null)
			{
				//Verwerfen
			}
			else
			{
				source.add(perf);
			}
		}
		if(tempPerf != null)
		{
			tempPerf.typ = Performed.typs.RESULT;
			calcQueue.add(tempPerf);

			tempPerf = new Performed();
			tempPerf.typ = Performed.typs.PLUS;
			tempPerf.NumInput = primCalc(calcQueue);
			tempPerf = absPerf(tempPerf);
			source.add(tempPerf);
			calcQueue.clear();
			tempQueue.clear();
			tempPerf = null;
			perf = null;
		}
		//Ende Normale Zahlen
		
		
		//Vereinfache unbekannte
		if(source.size() > 1)
		{
			tempQueue.addAll(source);
			source.clear();
			while(!tempQueue.isEmpty())
			{
				perf = tempQueue.poll();
				if(perf.typ == null) perf.typ = Performed.typs.PLUS;
				if(perf.unknow != null)
				{
					if(tempPerf == null)
					{
						tempPerf = perf;
						if(tempPerf.unknowPower == 0) tempPerf.unknowPower = 1;
						if(tempPerf.NumInput == null) tempPerf.NumInput = new BigDecimal("1");
					}
					else
					{
						if(tempPerf.unknow == perf.unknow)
						{
							if(perf.NumInput == null) perf.NumInput = new BigDecimal("1");
							if(perf.unknowPower == 0) perf.unknowPower = 1;
							
							if(perf.typ == Performed.typs.PLUS)
							{
								if(tempPerf.unknowPower == perf.unknowPower)
									tempPerf.NumInput = tempPerf.NumInput.add(perf.NumInput);
								else
									source.add(perf);
							}
							else if(perf.typ == Performed.typs.MINUS)
							{
								if(tempPerf.unknowPower == perf.unknowPower)
									tempPerf.NumInput = tempPerf.NumInput.subtract(perf.NumInput);
								else
									source.add(perf);
							}
							else if(perf.typ == Performed.typs.MULTI)
							{
								tempPerf.unknowPower = (tempPerf.unknowPower+perf.unknowPower);
								tempPerf.NumInput = tempPerf.NumInput.multiply(perf.NumInput);
							}
							else if(perf.typ == Performed.typs.DIVIDE)
							{
								tempPerf.unknowPower = tempPerf.unknowPower - perf.unknowPower;
								tempPerf.NumInput = tempPerf.NumInput.divide(perf.NumInput);
							}
							
							if(tempPerf.NumInput.compareTo(new BigDecimal("0")) == 0 && tempPerf.unknow != null) tempPerf = null;
						}
						else
						{
							if(tempPerf.NumInput.compareTo(new BigDecimal("1")) == 0) tempPerf.NumInput = null;
							if(tempPerf.NumInput.compareTo(new BigDecimal("0")) == 0) tempPerf.unknow = null;
							if(tempPerf.unknow == null && tempPerf.NumInput == null) tempPerf = null;
							else
							{
								source.add(tempPerf);
								setSolutionWay(tempPerf);
							}
							tempPerf = perf;
						}
					}
				
				}
				else if(perf.typ == null && perf.NumInput == null && perf.unknow == null)
				{
					//Verwerfen
				}
				else
				{
					source.add(perf);
					setSolutionWay(perf);
				}
			}
			source.add(tempPerf);
			setSolutionWay(tempPerf);
		}
	}
	
	private String equationOutput(Queue<Performed> source)
	{
		Queue<Performed> tempQueue = new LinkedList<Performed>();
		Performed perf = null;
		String output = "";
		
		tempQueue.addAll(source);
		while(!tempQueue.isEmpty())
		{
			perf = tempQueue.poll();
			setSolutionWay(perf);
			if(perf.typ != null) output+=perf.typ2Str();
			if(perf.NumInput != null) output += perf.NumInput;
			if(perf.unknow != null) output += perf.unknow;
			if(perf.unknowPower > 1) output+= "^"+perf.unknowPower;
		}
		return output;
	}
	
	private void equationDebug()
	{
		Queue<Performed> tempQueue = new LinkedList<Performed>();
		Performed perf = null;
		
		System.out.print("  leftsize="+left.size()+" & rightsize="+right.size());
		System.out.println(" ? ");
		tempQueue.addAll(left); tempQueue.add(new Performed(Performed.typs.RESULT,"0")); tempQueue.addAll(right);
		while(!tempQueue.isEmpty())
		{
			perf = tempQueue.poll();
			System.out.print(""+perf.typ+perf.NumInput+perf.unknow+"^"+perf.unknowPower+perf.clipCloseTyp+" || ");
		}
	}

	
/* ------------------------------------ Hilfsfunktionen ---------------------------- */
	
	private boolean isSpecClip(Performed.typs typ)
	{
		if(	typ == Performed.typs.SQRT ||
			typ == Performed.typs.SIN ||
			typ == Performed.typs.COS ||
			typ == Performed.typs.TAN) return true;
		
		return false;
	}
	
	
	public String parseString(String parse)
	{
		System.out.println(langmgr.getMessage("stringParsing"));
		char[] temp;
		String num = "", opt=""; 
		
		//Ergebnis ist letzter zeichen
		if(parse.endsWith("=") == false) parse+="=";
		
		temp = parse.toCharArray();
		for(int x=0; x < temp.length; x++)
		{
			switch(temp[x])
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
					if(opt != "")
					{
						if(opt.equalsIgnoreCase("cos")) 
						{
							addQueue(new Performed(Performed.typs.COS,"0"));
							opt = "";
						}
						else if(opt.equalsIgnoreCase("sin")) 
						{
							addQueue(new Performed(Performed.typs.SIN,"0"));
							opt = "";
						}
						else if(opt.equalsIgnoreCase("tan")) 
						{
							addQueue(new Performed(Performed.typs.TAN,"0"));
							opt = "";
						}
						else if(opt.equalsIgnoreCase("sqr") || opt.equalsIgnoreCase("sqrt")) 
						{
							addQueue(new Performed(Performed.typs.SQRT,"0"));
							opt = "";
						}
						else
						{
							throw new IllegalArgumentException("ParseError with Operator "+opt);
						}
					}
					
					num+=temp[x];
										
					break;
					
				case '+':
					if(num.length() < 1) throw new IllegalArgumentException("ParseError by '+'");
					addQueue(new Performed(Performed.typs.PLUS,num));
					num = "";
					break;
					
				case '-':
					if(num.length() < 1) throw new IllegalArgumentException("ParseError by '-'");
					addQueue(new Performed(Performed.typs.MINUS,num));
					num = "";
					break;
					
				case '*':
					if(num.length() < 1) throw new IllegalArgumentException("ParseError by '*'");
					addQueue(new Performed(Performed.typs.MULTI,num));
					num = "";
					break;
				case '/':
					if(num.length() < 1) throw new IllegalArgumentException("ParseError by '/'");
					addQueue(new Performed(Performed.typs.DIVIDE,num));
					num = "";
					break;
				case '(':
					if(opt != "") break;
					addQueue(new Performed(Performed.typs.CLIP_OPEN,"0"));
					break;
				case ')':
					addQueue(new Performed(Performed.typs.CLIP_CLOSE,num));
					break;
				case '=':
					if(num.length() < 1) throw new IllegalArgumentException("ParseError by '='");
					addQueue(new Performed(Performed.typs.RESULT,num));
					num = "";
					break;
				case 'a':
				case 'c':
				case 'i':
				case 'n':
				case 'o':
				case 'q':
				case 'r':
				case 's':
				case 't':
				case 'A':
				case 'C':
				case 'I':
				case 'N':
				case 'O':
				case 'Q':
				case 'R':
				case 'S':
				case 'T':
					opt+=temp[x];
					break;
				case '^':
					if(num.length() < 1) throw new IllegalArgumentException("ParseError by '^'");
					addQueue(new Performed(Performed.typs.POWER,num));
					num = "";
					break;
				default:
					throw new IllegalArgumentException("Errorchar: "+temp[x]);
			}
		}
		
		System.out.println(langmgr.getMessage("endParsing"));
		return parse;
	}
	
	
	private Performed.typs reverseTyp(Performed.typs typ)
	{
		if(typ == null) typ = Performed.typs.MINUS;
		else if(typ == Performed.typs.PLUS) typ = Performed.typs.MINUS;
		else if (typ == Performed.typs.MINUS) typ = Performed.typs.PLUS;
		else if (typ == Performed.typs.MULTI) typ = Performed.typs.DIVIDE;
		else if (typ == Performed.typs.DIVIDE) typ= Performed.typs.MULTI;
		
		return typ;
	}
	
	private Performed absPerf(Performed perf)
	{
		if(perf == null) return perf;
		if(perf.NumInput == null) return perf;
		
		if(perf.NumInput.compareTo(new BigDecimal("0")) < 0 && perf.typ == Performed.typs.PLUS)
		{
			perf.typ = Performed.typs.MINUS;
			perf.NumInput = perf.NumInput.abs();
			
		}
		else if(perf.NumInput.compareTo(new BigDecimal("0")) < 0 && perf.typ == Performed.typs.MINUS)
		{
			perf.typ = Performed.typs.PLUS;
			perf.NumInput = perf.NumInput.abs();
		}
		
		return perf;
	}
	
	public BigInteger Bin2Dec(String strBin)
	{
		return other2Dec(strBin,2);
	}
	public String Dec2Bin(BigDecimal dec)
	{
		return Dec2other(dec,2);
	}
	public String Dec2Bin(String dec)
	{
		return Dec2other(new BigDecimal(dec),2);
	}
	
	
	public BigInteger Oct2Dec(String strOct)
	{
		return other2Dec(strOct,8);
	}
	public String Dec2Oct(BigDecimal dec)
	{
		return Dec2other(dec,8);
	}
	public String Dec2Oct(String dec)
	{
		return Dec2other(new BigDecimal(dec),8);
	}
	
	
	public BigInteger Hex2Dec(String strHex)
	{
		return other2Dec(strHex,16);
	}
	public String Dec2Hex(BigDecimal dec)
	{
		return Dec2other(dec,16);
	}
	public String Dec2Hex(String dec)
	{
		return Dec2other(new BigDecimal(dec),16);
	}
	
	
	
	private String Dec2other(BigDecimal dec1, int other)
	{
		BigInteger dec = dec1.toBigInteger();
		String res = "";
		int modulo;
		BigInteger temp;
		
		while(dec.compareTo(new BigInteger("0")) > 0)
		{
			temp = dec.divide(new BigInteger(""+other));
			modulo = dec.mod(new BigInteger(""+other)).intValue();
			if(modulo < 10)
			{
				res = modulo + res;
			}
			else if(modulo == 10)
			{
				res = "A"+res;
			}
			else if(modulo == 11)
			{
				res = "B"+res;
			}
			else if(modulo == 12)
			{
				res = "C"+res;
			}
			else if(modulo == 13)
			{
				res = "D"+res;
			}
			else if(modulo == 14)
			{
				res = "E"+res;
			}
			else if(modulo == 15)
			{
				res = "F"+res;
			}
			
			dec = temp; 
		}
		
		/*
		 * Bugfix(10)
		 * Wenn Dec 0 ist, wird ein Leeres Ergebnis zurückgeben und Führt in
		 * der Maske zu einer NumberFormatException!
		 */
		if(res.length() < 1) res = "0"+res;
		
		return res;
	}
	
	private BigInteger other2Dec(String strOther, int sysPow)
	{
		BigInteger dec = new BigInteger("0");
		int pow = 0;
		int temp = 0;
		char[] other = strOther.toCharArray();
		
		for(int x = other.length-1; x >= 0; x--)
		{
			if(other[x] == 'A') temp = 10;
			else if(other[x] == 'B') temp = 11;
			else if(other[x] == 'C') temp = 12;
			else if(other[x] == 'D') temp = 13;
			else if(other[x] == 'E') temp = 14;
			else if(other[x] == 'F') temp = 15;
			else temp = Integer.parseInt(""+other[x]);
			
			dec = dec.add(new BigInteger(""+(temp* (int)Math.pow((double)sysPow,(double)pow))));
			pow++;
		}
		return dec;
	}

}