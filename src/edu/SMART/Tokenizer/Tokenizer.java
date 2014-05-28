package edu.SMART.Tokenizer;


import java.util.ArrayList;

/**
 * @author Michael A. Covington (www.covingtoninnovations.com) 2013
 * Released into the public domain as a programming example.  No warranties.
 *  
 * Simple tokenizer; breaks strings into tokens (words).
 */
public class Tokenizer {

	/**
	 * Tokenizer - breaks a string into words (tokens).
	 * 
	 * A token is either a single special character, or a sequence of
	 * token characters (letters, digits, and a few others) ending with 
	 * (and not including) a special character,
	 * whitespace, or end of input string.
     *
     * Hyphens are allowed to occur within tokens, but dashes are not.
     * On input, "--" and "---" are converted to Unicode dashes for
     * processing; all dashes are then output as the token "--" for
     * ASCII compatibility.
     * 
     * FEATURE THAT COULD BE ADDED:
     * Make separation of contractions optional.
     *
	 * @param s String to be tokenized.
	 * @return ArrayList<String> of tokens.
	 */
	public static ArrayList<String> Tokenize(String s)
	{	
		// Unicode dash as character and as string
		final char CDASH = '\u2014';
		final String SDASH = "\u2014";
		
		// Uppercase the input,
		// convert -- and --- to Unicode dashes,
		// and convert some non-ASCII chars to ASCII
		char[] c = s.toUpperCase()
				    .replaceAll("---",SDASH)
				    .replaceAll("--",SDASH)
				    .toCharArray();
		
		for (int i = 0; i < c.length; i++)
		{
			// Single quotation mark or apostrophe
			if (c[i] == '\u2018' || c[i] == '\u2019') { c[i] = '\''; }
			
			// Double quotation mark
			if (c[i] == '\u201C' || c[i] == '\u201D') { c[i] = '"'; }
			
			// Unicode hyphens
			if (c[i] == '\u2010' || c[i] == '\u2011') { c[i] = '-'; }
			
			// Other Unicode dashes
			if (c[i] == '\u2012' || c[i] == '\u2013' ||
				c[i] == '\u2212' ||
				c[i] == '\u2E3A' || c[i] == '\u2E3B') { c[i] = CDASH; }
		}
		

		// Extract the tokens
		int n = c.length;
		ArrayList<String> result = new ArrayList<String>();

		int i = 0;
		int j;
		while (i < n)
		{
			// Language-independent: Get the next token
			
			// Skip whitespace
			while (i < n && isSpace(c, i)) { i++; }
			if (i == n) { break; }

			// Got a special character?  Return it by itself.
			if (!isTokenCharacter(c, i))
			{
				if (c[i] == CDASH)
				{
					result.add("--");  // dash is output as ASCII "--"
				}
				else
				{
					result.add(new String(c, i, 1));  // other special chars are output as themselves
				}
				i++;
				continue;
			}
			
			// Got a token character.  Finish the token.
			j = i + 1;
			while (j < n && isTokenCharacter(c, j)) { j++; }
			String token = new String(c, i, j - i);
			i = j;
			
			// English-specific: See if token ends in a contraction;
			// if so, split it off and add two tokens to the result, not just one.
			// (Does not presently handle the rare case "he'd've" or the like.)
			
			if (token.endsWith("N'T") || token.endsWith("'LL") || token.endsWith("'VE"))
			{
				// Three-character contractions
				result.add(token.substring(0,token.length()-3));
				result.add(token.substring(token.length()-3));
			}
			else if (token.endsWith("'S") || token.endsWith("'D"))
			{
				// Two-character contractions
				result.add(token.substring(0,token.length()-2));
				result.add(token.substring(token.length()-2));
			}
			else
			{
				// No contraction
				result.add(token);
			}
			
		}
		
		return result;
	}

	
	/**
	 * True if c[pos] is a whitespace character.  That includes all the
	 * characters classified by Java as whitespace, and also the Unicode
	 * byte order mark that is present at the beginning of some Unicode files.
	 */
	static boolean isSpace(char[] c, int pos)
	{
		if (Character.isWhitespace(c[pos])) { return true; }
		if (c[pos] == '\uFEFF') { return true; }  // Unicode BOM
		return false;
	}
	
	
	/**
	 * True if c[pos] is a token character (a character that should be included
	 * in a token with adjacent token characters rather than standing by itself).
	 * 
	 * Letters and digits are always token characters.  Periods, commas,
	 * hyphens, apostrophes, etc., are token characters in some circumstances.
	 * 
	 * @param c Array of characters to look in.
	 * @param pos Position in that array of the character to examine.
	 * @return True or false.
	 */
	static boolean isTokenCharacter(char[] c, int pos)
	{
		if (Character.isLetterOrDigit(c[pos])) { return true; }
		if (c[pos] == ',' && isFlankedByDigits(c, pos)) { return true; }
		if (c[pos] == '.' && isFlankedByLettersOrDigits(c, pos)) { return true; }
		if (c[pos] == '/' && isFlankedByLettersOrDigits(c, pos)) { return true; }
		if (c[pos] == '-' && isFlankedByLettersOrDigits(c, pos)) { return true; }
		if (c[pos] == '\'' && isFlankedByLettersOrDigits(c, pos)) { return true; }
		return false;
	}
	
	// The following 3 methods are simple and quick but a bit repetitious.
	// A more satisfying solution would pass the appropriate test as a parameter,
	// but that is not as easy in Java as it would be in C#.
	
	/**
	 * True if c[pos] is flanked by digits.
	 */
	static boolean isFlankedByDigits(char[] c, int pos)
	{
		if (pos == 0 || pos == c.length - 1) { return false; } 
		
		if (Character.isDigit(c[pos-1]) &&
			Character.isDigit(c[pos+1])) { return true; } 
		
		return false;
	}

	
	/**
	 * True if c[pos] is flanked by alphanumerics.
	 */
	static boolean isFlankedByLettersOrDigits(char[] c, int pos)
	{
		if (pos == 0 || pos == c.length - 1) { return false; }
		
		if (Character.isLetterOrDigit(c[pos-1]) &&
			Character.isLetterOrDigit(c[pos+1])) { return true; }
		
		return false;
	}

}
