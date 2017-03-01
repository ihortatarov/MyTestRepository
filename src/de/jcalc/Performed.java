package de.jcalc;
import java.math.BigDecimal;

public class Performed 
{

	public enum typs
	{
		PLUS,
		MINUS,
		DIVIDE,
		MULTI,
		RESULT,
		POWER,
		SQRT,
		SIN,
		COS,
		TAN,
		CLIP_OPEN,
		CLIP_CLOSE,
		CLIP_MORECLOSE
	};
	
	public enum unknows
	{
		X,
		Y,
		Z,
	}
	
	public typs typ = null;
	public typs clipCloseTyp = null; 
	public BigDecimal NumInput = null;
	public String StrInput = null;
	public unknows unknow = null;
	public int unknowPower = 1;
	
	
	
	
	public Performed()
	{
		/* Do nothing */
	}
	
	public Performed(Performed.typs typ, String input)
	{
		this.typ = typ;
		this.NumInput = new BigDecimal(input);
	}
	public Performed(Performed.typs typ, BigDecimal input)
	{
		this.typ = typ;
		this.NumInput = input;
	}
	
	public Performed(Performed.typs typ, String input, Performed.typs clipCloseTyp)
	{
		this.typ = typ;
		if(input != null)
			this.NumInput = new BigDecimal(input);
		this.clipCloseTyp = clipCloseTyp;
	}
	public Performed(Performed.typs typ, BigDecimal input, Performed.typs clipCloseTyp)
	{
		this.typ = typ;
		this.NumInput = input;
		this.clipCloseTyp = clipCloseTyp;
	}
	
	public Performed(Performed.typs typ, Object input, String StrInput)
	{
		this.typ = typ;
		this.StrInput = StrInput;
	}
	public Performed(Performed.typs typ, Object input, String StrInput, Performed.typs clipCloseTyp)
	{
		this.typ = typ;
		this.StrInput = StrInput;
		this.clipCloseTyp = clipCloseTyp;
	}
	public Performed(Performed.typs typ, String NumInput, String StrInput)
	{
		this.typ = typ;
		if(NumInput != null)
			this.NumInput = new BigDecimal(NumInput);
		this.StrInput = StrInput;
	}
	public Performed(Performed.typs typ, BigDecimal NumInput, String StrInput)
	{
		this.typ = typ;
		this.NumInput = NumInput;
		this.StrInput = StrInput;
	}
	
	public Performed(Performed.typs typ, String input, String StrInput, Performed.typs clipCloseTyp)
	{
		this.typ = typ;
		this.NumInput = new BigDecimal(input);
		this.StrInput = StrInput;
		this.clipCloseTyp = clipCloseTyp;
	}
	public Performed(Performed.typs typ, BigDecimal input, String StrInput, Performed.typs clipCloseTyp)
	{
		this.typ = typ;
		this.NumInput = input;
		this.StrInput = StrInput;
		this.clipCloseTyp = clipCloseTyp;
	}
	public Performed(Performed.typs typ, BigDecimal input, Performed.unknows unknow)
	{
		this.typ = typ;
		this.NumInput = input;
		this.unknow = unknow;
	}
	public Performed(typs typ, String input, unknows unknow)
	{
		this.typ = typ;
		if(input != null)
			this.NumInput = new BigDecimal(input);
		this.unknow = unknow;
	}
	
	
	
	
	/**
	 * Wandelt den Performed.typs in ein Passendes Formeldzeichen um, 
	 * gibt "" zurück falls nichts passt!
	 * 
	 * @param t
	 * @return 
	 */
	public String typ2Str(typs t)
	{
		if(t == typs.PLUS) return "+";
		if(t == typs.MINUS) return "-";
		if(t == typs.MULTI) return "*";
		if(t == typs.DIVIDE) return "/";
		if(t == typs.RESULT) return "=";
		if(t == typs.CLIP_OPEN) return "(";
		if(t == typs.CLIP_CLOSE) return ")";
		if(t == typs.POWER) return "^";
		if(t == typs.SQRT) return "sqr(";
		if(t == typs.SIN) return "sin(";
		if(t == typs.COS) return "cos(";
		if(t == typs.TAN) return "tan(";
		
		return "";
	}
	public String typ2Str()
	{
		return typ2Str(this.typ);
	}
	public static String typ2Strs(Performed p)
	{
		Performed perf = new Performed();
		return perf.typ2Str(p.typ);
	}
	public static String typ2Strs(typs typ)
	{
		Performed perf = new Performed();
		return perf.typ2Str(typ);
	}
	
}
