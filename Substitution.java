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

package jscicalc;

/**
 * This class allows us to set up a substitution so that we can substitute variables
 * in an OObject with arbitrary values. The arbitrary values are defined by another
 * OObject.
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class Substitution {
    /**
     * The constructor. The default does nothing because it is possible to make no
     * substitutions.
     */
    public Substitution(){
	substitutions = new java.util.LinkedList<Pair>();
    }
    /**
     * Add a substitution. This maps a Variable to an OObject. We regard any variable with
     * matching name as equivalent to variable. If oobject is null, we take the addition
     * as a request not to substiute (equivalent to but more effiecient than substituting
     * the Variable with itself.
     * @param variable The Variable
     * @param oobject Its replacement
     */
    public void add( jscicalc.expression.Variable variable, OObject oobject ){
	if( variable == null ) return; // nothing to do
	for( java.util.Iterator<Pair> i = substitutions.iterator(); i.hasNext(); ){
	    Pair pair = i.next();
	    if( pair.variable.name().equals( variable.name() ) ){
		// found match
		if( oobject == null ){
		    i.remove();
		} else {
		    pair.oobject = oobject;
		}
		return; // Changed or deleted Pair
	    }
	}
	// We have to add a Pair
	Pair pair = new Pair();
	pair.variable = variable;
	pair.oobject = oobject;
	substitutions.add( pair );
    }

    /**
     * Get substitutions.
     * @return list of substitutions
     */
    public final java.util.LinkedList<Pair> getSubstitutions(){
	return substitutions;
    }
    /**
     * The map containing all the substitutions.
     */
    private java.util.LinkedList<Pair> substitutions;
    /**
     * Inner class used for substitution pairs.
     */
    public class Pair {
	/**
	 * A variable
	 */
	public jscicalc.expression.Variable variable;
	/**
	 * An OObject
	 */
	public OObject oobject;
    }
}
