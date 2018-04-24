/**
 * $RCSfile: StringUtils.java,v $
 * $Revision: 1.11.2.2 $
 * $Date: 2001/01/16 06:06:07 $
 *
 * Copyright (C) 2000 CoolServlets.com. All rights reserved.
 *
 * ===================================================================
 * The Apache Software License, Version 1.1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by
 *        CoolServlets.com (http://www.coolservlets.com)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Jive" and "CoolServlets.com" must not be used to
 *    endorse or promote products derived from this software without
 *    prior written permission. For written permission, please
 *    contact webmaster@coolservlets.com.
 *
 * 5. Products derived from this software may not be called "Jive",
 *    nor may "Jive" appear in their name, without prior written
 *    permission of CoolServlets.com.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL COOLSERVLETS.COM OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of CoolServlets.com. For more information
 * on CoolServlets.com, please see <http://www.coolservlets.com>.
 */

package com.zhaisoft.lib.updater.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Utility class to peform common String manipulation algorithms.
 */
public class StringUtils {

	public final static String NULL = "null";

	private static final String TIMEFORMAT = "yyyy-MM-dd HH:mm";
	private static final String[] TM = new String[48];
	public static final String lineSeparator = System
			.getProperty("line.separator");

	/**
	 * Initialization lock for the whole class. Init's only happen once per
	 * class load so this shouldn't be a bottleneck.
	 */
	private static Object initLock = new Object();

	/**
	 * Replaces all instances of oldString with newString in line.
	 * 
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 * 
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static final String replace(String line, String oldString,
			String newString) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	public static String replaceSubstring(String s__Text, String s__Src,
			String s__Dest) {
		try {
			if (s__Text == null || s__Src == null || s__Dest == null)
				return null;
			int i = 0;
			int i_SrcLength = s__Src.length();
			int i_DestLength = s__Dest.length();
			do {
				int j = s__Text.indexOf(s__Src, i);
				if (-1 == j)
					break;
				s__Text = s__Text.substring(0, j).concat(s__Dest)
						.concat(s__Text.substring(j + i_SrcLength));
				i = j + i_DestLength;
			} while (true);
		} catch (Exception e) {

		}
		return s__Text;
	}

	/**
	 * Replaces all instances of oldString with newString in line with the added
	 * feature that matches of newString in oldString ignore case.
	 * 
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 * 
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	/**
	 * Replaces all instances of oldString with newString in line. The count
	 * Integer is updated with number of replaces.
	 * 
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 * 
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static final String replace(String line, String oldString,
			String newString, int[] count) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			int counter = 0;
			counter++;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}


	public static final String escapeHTMLTags(String input) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (input == null || input.length() == 0) {
			return input;
		}
		// Use a StringBuffer in lieu of String concatenation -- it is
		// much more efficient this way.
		StringBuffer buf = new StringBuffer(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * Used by the hash method.
	 */
	private static MessageDigest digest = null;

	/**
	 * Hashes a String using the Md5 algorithm and returns the result as a
	 * String of hexadecimal numbers. This method is synchronized to avoid
	 * excessive MessageDigest object creation. If calling this method becomes a
	 * bottleneck in your code, you may wish to maintain a pool of MessageDigest
	 * objects instead of using this method.
	 * <p>
	 * A hash is a one-way function -- that is, given an input, an output is
	 * easily computed. However, given the output, the input is almost
	 * impossible to compute. This is useful for passwords since we can store
	 * the hash and a hacker will then have a very hard time determining the
	 * original password.
	 * <p>
	 * In Jive, every time a user logs in, we simply take their plain text
	 * password, compute the hash, and compare the generated hash to the stored
	 * hash. Since it is almost impossible that two passwords will generate the
	 * same hash, we know if the user gave us the correct password or not. The
	 * only negative to this system is that password recovery is basically
	 * impossible. Therefore, a reset password method is used instead.
	 * 
	 * @param data
	 *            the String to compute the hash of.
	 * @return a hashed version of the passed-in String
	 */
	public synchronized static final String hash(String data) {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				System.err.println("Failed to load the MD5 MessageDigest. "
						+ "Jive will be unable to function normally.");
				nsae.printStackTrace();
			}
		}
		// Now, compute hash.
		digest.update(data.getBytes());
		return toHex(digest.digest());
	}

	/**
	 * Turns an array of bytes into a String representing each byte as an
	 * unsigned hex number.
	 * <p>
	 * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	 * Distributed under LGPL.
	 * 
	 * @param hash
	 *            an rray of bytes to convert to a hex-string
	 * @return generated hex string
	 */
	public static final String toHex(byte hash[]) {
		StringBuffer buf = new StringBuffer(hash.length * 2);
		int i;

		for (i = 0; i < hash.length; i++) {
			if (((int) hash[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) hash[i] & 0xff, 16));
		}
		return buf.toString();
	}

	/**
	 * Converts a line of text into an array of lower case words. Words are
	 * delimited by the following characters: , .\r\n:/\+
	 * <p>
	 * In the future, this method should be changed to use a
	 * BreakIterator.wordInstance(). That class offers much more fexibility.
	 * 
	 * @param text
	 *            a String of text to convert into an array of words
	 * @return text broken up into an array of words.
	 */
	public static final String[] toLowerCaseWordArray(String text) {
		if (text == null || text.length() == 0) {
			return new String[0];
		}
		StringTokenizer tokens = new StringTokenizer(text, " ,\r\n.:/\\+");
		String[] words = new String[tokens.countTokens()];
		for (int i = 0; i < words.length; i++) {
			words[i] = tokens.nextToken().toLowerCase();
		}
		return words;
	}

	/**
	 * A list of some of the most common words. For searching and indexing, we
	 * often want to filter out these words since they just confuse searches.
	 * The list was not created scientifically so may be incomplete :)
	 */
	private static final String[] commonWords = new String[] { "a", "and",
			"as", "at", "be", "do", "i", "if", "in", "is", "it", "so", "the",
			"to" };
	private static Map commonWordsMap = null;

	/**
	 * Returns a new String array with some of the most common English words
	 * removed. The specific words removed are: a, and, as, at, be, do, i, if,
	 * in, is, it, so, the, to
	 */
	public static final String[] removeCommonWords(String[] words) {
		// See if common words map has been initialized. We don't statically
		// initialize it to save some memory. Even though this a small savings,
		// it adds up with hundreds of classes being loaded.
		if (commonWordsMap == null) {
			synchronized (initLock) {
				if (commonWordsMap == null) {
					commonWordsMap = new HashMap();
					for (int i = 0; i < commonWords.length; i++) {
						commonWordsMap.put(commonWords[i], commonWords[i]);
					}
				}
			}
		}
		// Now, add all words that aren't in the common map to results
		ArrayList results = new ArrayList(words.length);
		for (int i = 0; i < words.length; i++) {
			if (!commonWordsMap.containsKey(words[i])) {
				results.add(words[i]);
			}
		}
		return (String[]) results.toArray(new String[results.size()]);
	}

	/**
	 * Pseudo-random number generator object for use with randomString(). The
	 * Random class is not considered to be cryptographically secure, so only
	 * use these random Strings for low to medium security applications.
	 */
	private static Random randGen = null;

	/**
	 * Array of numbers and letters of mixed case. Numbers appear in the list
	 * twice so that there is a more equal chance that a number will be picked.
	 * We can use the array to get a random number or letter by picking a random
	 * array index.
	 */
	private static char[] numbersAndLetters = null;

	/**
	 * Returns a random String of numbers and letters of the specified length.
	 * The method uses the Random class that is built-in to Java which is
	 * suitable for low to medium grade security uses. This means that the
	 * output is only pseudo random, i.e., each number is mathematically
	 * generated so is not truly random.
	 * <p>
	 * 
	 * For every character in the returned String, there is an equal chance that
	 * it will be a letter or number. If a letter, there is an equal chance that
	 * it will be lower or upper case.
	 * <p>
	 * 
	 * The specified length must be at least one. If not, the method will return
	 * null.
	 * 
	 * @param length
	 *            the desired length of the random String to return.
	 * @return a random String of numbers and letters of the specified length.
	 */
	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		// Init of pseudo random number generator.
		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();
					// Also initialize the numbersAndLetters array
					numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
							+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
							.toCharArray();
				}
			}
		}
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * Intelligently chops a String at a word boundary (whitespace) that occurs
	 * at the specified index in the argument or before. However, if there is a
	 * newline character before <code>length</code>, the String will be chopped
	 * there. If no newline or whitespace is found in <code>string</code> up to
	 * the index <code>length</code>, the String will chopped at
	 * <code>length</code>.
	 * <p>
	 * For example, chopAtWord("This is a nice String", 10) will return
	 * "This is a" which is the first word boundary less than or equal to 10
	 * characters into the original String.
	 * 
	 * @param string
	 *            the String to chop.
	 * @param length
	 *            the index in <code>string</code> to start looking for a
	 *            whitespace boundary at.
	 * @return a substring of <code>string</code> whose length is less than or
	 *         equal to <code>length</code>, and that is chopped at whitespace.
	 */
	public static final String chopAtWord(String string, int length) {
		if (string == null) {
			return string;
		}

		char[] charArray = string.toCharArray();
		int sLength = string.length();
		if (length < sLength) {
			sLength = length;
		}

		// First check if there is a newline character before length; if so,
		// chop word there.
		for (int i = 0; i < sLength - 1; i++) {
			// Windows
			if (charArray[i] == '\r' && charArray[i + 1] == '\n') {
				return string.substring(0, i);
			}
			// Unix
			else if (charArray[i] == '\n') {
				return string.substring(0, i);
			}
		}
		// Also check boundary case of Unix newline
		if (charArray[sLength - 1] == '\n') {
			return string.substring(0, sLength - 1);
		}

		// Done checking for newline, now see if the total string is less than
		// the specified chop point.
		if (string.length() < length) {
			return string;
		}

		// No newline, so chop at the first whitespace.
		for (int i = length - 1; i > 0; i--) {
			if (charArray[i] == ' ') {
				return string.substring(0, i).trim();
			}
		}

		// Did not find word boundary so return original String chopped at
		// specified length.
		return string.substring(0, length);
	}

	/**
	 * Highlights words in a string. Words matching ignores case. The actual
	 * higlighting method is specified with the start and end higlight tags.
	 * Those might be beginning and ending HTML bold tags, or anything else.
	 * 
	 * @param string
	 *            the String to highlight words in.
	 * @param words
	 *            an array of words that should be highlighted in the string.
	 * @param startHighlight
	 *            the tag that should be inserted to start highlighting.
	 * @param endHighlight
	 *            the tag that should be inserted to end highlighting.
	 * @return a new String with the specified words highlighted.
	 */
	public static final String highlightWords(String string, String[] words,
			String startHighlight, String endHighlight) {
		if (string == null || words == null || startHighlight == null
				|| endHighlight == null) {
			return null;
		}

		// Iterate through each word.
		for (int x = 0; x < words.length; x++) {
			// we want to ignore case.
			String lcString = string.toLowerCase();
			// using a char [] is more efficient
			char[] string2 = string.toCharArray();
			String word = words[x].toLowerCase();

			// perform specialized replace logic
			int i = 0;
			if ((i = lcString.indexOf(word, i)) >= 0) {
				int oLength = word.length();
				StringBuffer buf = new StringBuffer(string2.length);

				// we only want to highlight distinct words and not parts of
				// larger words. The method used below mostly solves this. There
				// are a few cases where it doesn't, but it's close enough.
				boolean startSpace = false;
				char startChar = ' ';
				if (i - 1 > 0) {
					startChar = string2[i - 1];
					if (!Character.isLetter(startChar)) {
						startSpace = true;
					}
				}
				boolean endSpace = false;
				char endChar = ' ';
				if (i + oLength < string2.length) {
					endChar = string2[i + oLength];
					if (!Character.isLetter(endChar)) {
						endSpace = true;
					}
				}
				if ((startSpace && endSpace) || (i == 0 && endSpace)) {
					buf.append(string2, 0, i);
					if (startSpace && startChar == ' ') {
						buf.append(startChar);
					}
					buf.append(startHighlight);
					buf.append(string2, i, oLength).append(endHighlight);
					if (endSpace && endChar == ' ') {
						buf.append(endChar);
					}
				} else {
					buf.append(string2, 0, i);
					buf.append(string2, i, oLength);
				}

				i += oLength;
				int j = i;
				while ((i = lcString.indexOf(word, i)) > 0) {
					startSpace = false;
					startChar = string2[i - 1];
					if (!Character.isLetter(startChar)) {
						startSpace = true;
					}

					endSpace = false;
					if (i + oLength < string2.length) {
						endChar = string2[i + oLength];
						if (!Character.isLetter(endChar)) {
							endSpace = true;
						}
					}
					if ((startSpace && endSpace)
							|| i + oLength == string2.length) {
						buf.append(string2, j, i - j);
						if (startSpace && startChar == ' ') {
							buf.append(startChar);
						}
						buf.append(startHighlight);
						buf.append(string2, i, oLength).append(endHighlight);
						if (endSpace && endChar == ' ') {
							buf.append(endChar);
						}
					} else {
						buf.append(string2, j, i - j);
						buf.append(string2, i, oLength);
					}
					i += oLength;
					j = i;
				}
				buf.append(string2, j, string2.length - j);
				string = buf.toString();
			}
		}
		return string;
	}

	/**
	 * Escapes all necessary characters in the String so that it can be used in
	 * an XML doc.
	 * 
	 * @param string
	 *            the string to escape.
	 * @return the string with appropriate characters escaped.
	 */
	public static final String escapeForXML(String string) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (string == null || string.length() == 0) {
			return string;
		}
		char[] sArray = string.toCharArray();
		StringBuffer buf = new StringBuffer(sArray.length);
		char ch;
		for (int i = 0; i < sArray.length; i++) {
			ch = sArray[i];
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else if (ch == '&') {
				buf.append("&amp;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	public static final String deleteWhiteSpace(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}
		char[] sArray = string.toCharArray();
		StringBuffer buf = new StringBuffer(sArray.length);
		char ch;
		for (int i = 0; i < sArray.length; i++) {
			ch = sArray[i];
			if ((ch != '\u0020') && (ch != '\u0009')) {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	public static final String getChnString_ms(String string) {
		try {
			byte[] temp_t = string.getBytes("ISO8859-1");
			String temp = new String(temp_t);
			return temp;
		} catch (Exception e) {
			return "null";
		}
	}

	public static final String dumpNextLine(String string, int chNum) {
		String chStr = string;
		if (string == null || chNum == 0) {
			return "";
		}
		StringTokenizer sTk = new StringTokenizer(chStr, "<br>");
		String endStr = "";
		String tStr = "";
		while (sTk.hasMoreTokens()) {
			tStr = sTk.nextToken();
			if (tStr.length() < chNum) {
				endStr = endStr + tStr + "<br>";
			}
			if (tStr.length() > chNum) {
				endStr = endStr + tStr.substring(0, chNum) + "<br>";
			}
			int k = 0;
			int tK = 1;
			for (k = 0; k < tStr.length(); k++) {
				if (k % chNum == 0 && k != 0 && k + chNum <= tStr.length()) {
					endStr = endStr + tStr.substring(k, k + chNum) + "<br>";
					tK++;
				}
			}
			if (tStr.length() - tK * chNum > 0) {
				endStr = endStr + tStr.substring(tK * chNum);
			}
		}
		return endStr;
	}

	public static final String great_summary_3(int charNum, String detail) {
		StringBuffer detail_temp = new StringBuffer();
		int len = 0;
		int pos = 0;
		int pos2 = 0;
		int pos3 = 0;
		int num = 0;
		int j = 0;
		char ch;
		String tagStr = "";
		String tagStr2 = "";
		String tmp = "";
		String tabStr1 = "<table";
		String tabStr2 = "</table";
		String tdStr1 = "<td";
		String tdStr2 = "</td";
		String trStr1 = "<tr";
		String trStr2 = "</tr";
		String thStr1 = "<th";
		String thStr2 = "<th";
		if (detail != null) {
			detail = detail + " ";
			len = detail.length();
			for (int i = 0; i < len; i++) {
				ch = detail.charAt(i);
				while (ch == '<') {
					pos = detail.indexOf('>', i);
					if (pos > i) {
						tagStr = detail.substring(i, pos + 1);
						if (tagStr.trim().indexOf(tabStr1) == -1
								&& tagStr.trim().indexOf(tabStr2) == -1
								&& tagStr.trim().indexOf(tdStr1) == -1
								&& tagStr.trim().indexOf(tdStr2) == -1
								&& tagStr.trim().indexOf(trStr1) == -1
								&& tagStr.trim().indexOf(trStr2) == -1
								&& tagStr.trim().indexOf(thStr1) == -1
								&& tagStr.trim().indexOf(thStr2) == -1) {
							detail_temp.append(tagStr);
						}
						if (pos < len - 1) {
							i = pos + 1;
							ch = detail.charAt(i);

							if (j < charNum) {
								pos2 = detail.indexOf("</", i); /*
																 * 锟斤拷锟斤拷锟斤拷锟狡ワ拷锟侥斤拷锟斤拷Tag
																 * , 锟斤�?/FONT>
																 */
								if (pos2 > i) /* 锟斤拷锟斤拷匹锟斤拷慕锟斤拷锟絋ag */
								{
									pos3 = detail.indexOf('>', pos2);
									if (pos3 > pos2) {
										tagStr2 = detail.substring(pos2,
												pos3 + 1);

									}

									if (pos2 - pos > charNum - j) /*
																 * 锟斤拷锟斤拷剩锟铰碉拷锟街凤拷锟斤拷锟斤
																 * �?
																 */
									{
										num = pos2 - pos - charNum + j;
										tmp = detail.substring(i, i + num + 1);
										detail_temp.append(tmp);
										if (tagStr2.trim().indexOf(tabStr1) == -1
												&& tagStr2.trim().indexOf(
														tabStr2) == -1
												&& tagStr2.trim().indexOf(
														tdStr1) == -1
												&& tagStr2.trim().indexOf(
														tdStr2) == -1
												&& tagStr2.trim().indexOf(
														trStr1) == -1
												&& tagStr2.trim().indexOf(
														trStr2) == -1
												&& tagStr2.trim().indexOf(
														thStr1) == -1
												&& tagStr2.trim().indexOf(
														thStr2) == -1) {
											detail_temp.append(tagStr2);
										}
										return detail_temp.toString();
									}
								}

							}
						}

					} else
					/* pos <= i */
					{
						break;
					}
				}
				/* end while */

				j++;
				detail_temp.append(ch);
				if (j <= charNum)
					continue;
				detail_temp.append("\u2026\u2026");
				break;
			}

			return detail_temp.toString();
		} else {
			return "\u65E0";
		}
	}

	public static final boolean eval_IntNumber(String param) { // 校锟斤拷锟角凤拷为锟斤拷锟斤�?
		boolean isIntNum = true;
		char[] nums = new char[10];
		for (int j = 0; j < 9; j++) {
			nums[j] = (String.valueOf(j)).charAt(0);
		}
		for (int i = 0; i < param.length(); i++) {
			for (int k = 0; k < 9; k++) {
				if (param.charAt(i) == nums[k])
					break;
				if (k == 8)
					return false;
			}
		}
		return isIntNum;
	}

	public static final boolean eval_Real(String param) { // 校锟斤拷锟角凤拷为实锟斤�?
		boolean isReal = true;
		if (param.length() <= 0)
			return false;
		for (int i = 0; i < param.length(); i++) {
			int intHash = param.substring(i, i + 1).hashCode();
			if (intHash < 48 || intHash > 57) {
				isReal = false;
				if (param.substring(i, i + 1).equals(".")) {
					isReal = true;
				}
				if (!isReal)
					break;
			}

		}
		return isReal;
	}

	public static String floadToint(double input) {
		int dInt = 0;
		int dDec = 0;
		String doubleStr = null;
		Double db = Double.valueOf(input + 0.05);
		dInt = db.intValue();
		dDec = ((Double.valueOf((input + 0.05) * 10)).intValue()) % 10;
		doubleStr = String.valueOf(dInt) + "." + String.valueOf(dDec);
		return doubleStr;
	}

	public static boolean isImage(String filename) {
		StringTokenizer token = new StringTokenizer(filename);
		String name = "";
		String end_pref = "";
		int index = 0;
		String[] filetype = { "jpg", "gif", "bmp", "tif", "png", "ico" };
		boolean is = false;
		while (token.hasMoreTokens()) {
			name = token.nextToken();
		}
		index = name.indexOf(".");
		end_pref = name.substring(index + 1);
		for (int i = 0; i < filetype.length; i++) {
			if (end_pref.equalsIgnoreCase(filetype[i])) {
				is = true;
				break;
			}
		}
		return is;
	}

	public static String SimpleDateFormat(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat todayDateFormatter = new SimpleDateFormat(TIMEFORMAT);
		return todayDateFormatter.format(date);
	}

	public static String SimpleDateFormat(Date date, String dateformat) {
		if (date == null)
			return null;
		if (dateformat == null) {
			dateformat = TIMEFORMAT;
		}
		SimpleDateFormat todayDateFormatter = new SimpleDateFormat(dateformat);
		return todayDateFormatter.format(date);
	}

	public static Date handleDate(String data) {
		return handleDate(data, TIMEFORMAT);

	}

	public static Date handleDate(String data, String dateformat) {
		if (dateformat == null) {
			dateformat = TIMEFORMAT;
		}
		if (data == null || data.length() == 0)
			return null;
		try {
			DateFormat df = new SimpleDateFormat(dateformat);
			Date d_date = df.parse(data);
			return d_date;
		} catch (Exception e) {
			return null;
		}
	}

	public static String[] getTimes() {
		TM[0] = "00:00";
		TM[1] = "00:30";
		TM[2] = "01:00";
		TM[3] = "01:30";
		TM[4] = "02:00";
		TM[5] = "02:30";
		TM[6] = "03:00";
		TM[7] = "03:30";
		TM[8] = "04:00";
		TM[9] = "04:30";
		TM[10] = "05:00";
		TM[11] = "05:30";
		TM[12] = "06:00";
		TM[13] = "06:30";
		TM[14] = "07:00";
		TM[15] = "07:30";
		TM[16] = "08:00";
		TM[17] = "08:30";
		TM[18] = "09:00";
		TM[19] = "09:30";
		TM[20] = "10:00";
		TM[21] = "10:30";
		TM[22] = "11:00";
		TM[23] = "11:30";
		TM[24] = "12:00";
		TM[25] = "12:30";
		TM[26] = "13:00";
		TM[27] = "13:30";
		TM[28] = "14:00";
		TM[29] = "14:30";
		TM[30] = "15:00";
		TM[31] = "15:30";
		TM[32] = "16:00";
		TM[33] = "16:30";
		TM[34] = "17:00";
		TM[35] = "17:30";
		TM[36] = "18:00";
		TM[37] = "18:30";
		TM[38] = "19:00";
		TM[39] = "19:30";
		TM[40] = "20:00";
		TM[41] = "20:30";
		TM[42] = "21:00";
		TM[43] = "21:30";
		TM[44] = "22:00";
		TM[45] = "22:30";
		TM[46] = "23:00";
		TM[47] = "23:30";
		return TM;
	}

	public static String convertNull(String input) {
		String NULL_TAG = " ";
		String result = StringUtils.replace(input, "\r\n", NULL_TAG);
		result = StringUtils.replace(result, "	", NULL_TAG);
		return StringUtils.replace(result, "\n", NULL_TAG);
	}

	public static String convertLine(String input) {
		String BR_TAG = "<br>";
		String result = StringUtils.replace(input, "\r\n", BR_TAG);
		return StringUtils.replace(result, "\n", BR_TAG);
	}

	public static String dumpNew(String input, int lineNum) {
		String input1 = convertLine(input);
		int[] poses = new int[input1.length()];
		int pos1 = 0;
		int pos2 = 0;
		int j = 0;
		StringBuffer strTemp = new StringBuffer();
		for (int i = 0; i < input1.length(); i++) {
			pos1 = input1.indexOf("<br>", i);
			System.out.print("pos1=" + pos1);
			if (pos1 - i > lineNum) {
				strTemp.append(input1.substring(i, i + lineNum) + "<br>");
				i = i + lineNum - 1;
			} else if (pos1 >= 0 && pos1 - i <= lineNum) {
				strTemp.append(input1.substring(i, pos1 + 4));
				i = pos1 + 3;
			} else if (pos1 == -1 && input1.length() - i > lineNum) {
				strTemp.append(input1.substring(i, i + lineNum) + "<br>");
				i = i + lineNum - 1;
			} else if (pos1 == -1 && input1.length() - i <= lineNum) {
				strTemp.append(input1.substring(i));
				break;
			}
		}
		return strTemp.toString();

	}

	public int[] parseIP(String IP) {
		int invalida[] = { -1, -1, -1, -1 };
		int ia[] = { -1, -1, -1, -1 };
		if (IP == null)
			return invalida;
		int len = IP.length();
		int i = 0;
		int b = 0;
		int dot = 0;
		while (i < len) {
			char c = IP.charAt(i++);
			if (c >= '0' && c <= '9')
				b = (b * 10 + c) - 48;
			else if (c == '.') {
				ia[dot] = b;
				if (++dot >= 4)
					return invalida;
				b = 0;
			} else {
				return invalida;
			}
		}
		if (dot == 3) {
			ia[dot] = b;
			return ia;
		} else {
			return invalida;
		}
	}

	public static String packageOf(Class aClass) {
		if (aClass == null) {
			throw new IllegalArgumentException(
					"StringUtils: Argument \"aClass\" cannot be null.");
		}
		String result = "";
		int index = aClass.getName().lastIndexOf(".");
		if (index >= 0) {
			result = aClass.getName().substring(0, index);
		}
		return result;
	}

	public static String nameOf(Class aClass) {
		if (aClass == null) {
			throw new IllegalArgumentException(
					"StringUtils: Argument \"aClass\" cannot be null.");
		}
		String className = aClass.getName();
		int index = className.lastIndexOf(".");
		if (index >= 0) {
			className = className.substring(index + 1);
		}
		return className;
	}

	public static final byte[] decodeHex(String hex) {
		char[] chars = hex.toCharArray();
		// byte[] bytes = new byte[chars.length/2];
		byte[] bytes = new byte[hex.length() / 2];
		System.out.println("hex.length()/2=" + (hex.length() / 2));
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2) {
			byte newByte = 0x00;
			newByte |= hexCharToByte(chars[i]);
			newByte <<= 4;
			newByte |= hexCharToByte(chars[i + 1]);
			bytes[byteCount] = newByte;
			byteCount++;
		}
		return bytes;
	}

	private static final byte hexCharToByte(char ch) {
		switch (ch) {
		case '0':
			return 0x00;
		case '1':
			return 0x01;
		case '2':
			return 0x02;
		case '3':
			return 0x03;
		case '4':
			return 0x04;
		case '5':
			return 0x05;
		case '6':
			return 0x06;
		case '7':
			return 0x07;
		case '8':
			return 0x08;
		case '9':
			return 0x09;
		case 'a':
			return 0x0A;
		case 'b':
			return 0x0B;
		case 'c':
			return 0x0C;
		case 'd':
			return 0x0D;
		case 'e':
			return 0x0E;
		case 'f':
			return 0x0F;
		case 'A':
			return 0x0A;
		case 'B':
			return 0x0B;
		case 'C':
			return 0x0C;
		case 'D':
			return 0x0D;
		case 'E':
			return 0x0E;
		case 'F':
			return 0x0F;

		}
		return 0x00;
	}

	public static String toDB(String in) {
		if (in == null)
			return null;
		String out = "";
		int i = 0;
		int len = in.length();
		try {
			while (i < len) {
				char c;
				switch (c = in.charAt(i)) {
				case 34: // '"'
					out = out.concat("&quot;");
					break;

				case 39: // '\''
					out = out.concat("&apos;");
					break;

				case 63: // '?'
					out = out.concat("&qst;");
					break;

				case 38: // '&'
					out = out.concat("&amp;");
					break;

				case 60: // '<'
					out = out.concat("&lt;");
					break;

				case 62: // '>'
					out = out.concat("&gt;");
					break;

				case 36: // '$'
					out = out.concat("$$");
					break;

				default:
					if (c >= '~') {
						out = out.concat("&#" + (int) c);
						out = out.concat(";");
						break;
					}
					if (c < ' ') {
						out = out.concat("&#" + (int) c);
						out = out.concat(";");
					} else {
						out = out.concat(Character.toString(c));
					}
					break;
				}
				i++;
			}
		} catch (Exception e) {

		}
		return out;
	}

	public static String fromDB(String in) {
		if (in == null)
			return null;
		String out = in;
		out = replaceSubstring(out, "&amp;", "&");
		out = replaceSubstring(out, "&apos;", "'");
		out = replaceSubstring(out, "&quot;", "\"");
		out = replaceSubstring(out, "&qst;", "?");
		out = replaceSubstring(out, "&lt;", "<");
		out = replaceSubstring(out, "&gt;", ">");
		out = replaceSubstring(out, "$$", "$");
		int i = 0;
		do {
			i = 0;
			int j = out.indexOf("&#", i);
			int k = out.indexOf(";", j);
			if (-1 != j && -1 != k) {
				String number = out.substring(j + 2, k);
				int i_number = (Integer.valueOf(number));
				char c = (char) i_number;
				String dest = Character.toString(c);
				out = out.substring(0, j).concat(dest)
						.concat(out.substring(k + 1));
			} else {
				return out;
			}
		} while (true);
	}

	public static String toGBK(String s) {
		if (s == null || s.length() == 0)
			return s;

		byte[] b;

		try {
			b = s.getBytes("ISO8859_1");
			for (int i = 0; i < b.length; i++)
				if (b[i] + 0 < 0)
					return new String(b, "GBK");
			b = s.getBytes("GBK");
			for (int i = 0; i < b.length; i++)
				if (b[i] + 0 < 0)
					return new String(b, "GBK");
		} catch (Exception e) {
		}

		return s;
	}

	public static void newLine(StringBuffer buffer, int indent) {
		buffer.append(StringUtils.lineSeparator);
		indent(buffer, indent);
	}

	public static void indent(StringBuffer buffer, int indent) {
		for (int i = 0; i < indent; i++)
			buffer.append(' ');
	}

	public static boolean isNull(String str) {
		boolean b = false;
		if (str == null || str.trim().length() == 0)
			b = true;

		return b;
	}

	public static boolean isNULL(String str) {
		boolean b = false;
		if (str == null)
			b = true;

		return b;
	}

	public static boolean isNull(String str, boolean bValidNullString) {
		boolean b = false;
		if (str == null || str.trim().length() == 0)
			b = true;
		if (!b && bValidNullString) {
			if (str != null && str.equalsIgnoreCase("null"))
				b = true;
		}
		return b;
	}

	public static boolean isUrl(String str) {
		if (isNull(str))
			return false;
		return str
				.matches("^http://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$");
	}

	public static boolean str2Boolean(String s, boolean defaultV) {
		if (StringUtils.isNull(s))
			return defaultV;
		if (s != null && s.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}

	public static int str2Int(String s, int defaultV) {
		if (s != null && !s.equals("")) {
			int num = defaultV;
			try {
				num = Integer.parseInt(s);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultV;
		}
	}

	public static long str2Long(String s, long defaultV) {
		if (s != null && !s.equals("")) {
			long num = defaultV;
			try {
				num = Long.parseLong(s);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultV;
		}
	}

	public static double str2Double(String s, double defaultV) {
		if (s != null && !s.equals("")) {
			double num = defaultV;
			try {
				num = Double.parseDouble(s);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultV;
		}
	}

	public static float str2Float(String s, float defaultV) {
		if (s != null && !s.equals("")) {
			float num = defaultV;
			try {
				num = Float.parseFloat(s);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultV;
		}
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName) {
		if (fileName == null) {
			return null;
		}
		fileName = fileName.trim();
		if (fileName.length() == 0) {
			return null;
		}
		int index = fileName.lastIndexOf(".");

		if (index < 0 || (index + 2) > fileName.length()) {
			return null;
		}

		return fileName.substring(index + 1);
	}

	public static boolean isBlank(String url) {
		url = url == null ? "" : url.trim();
		if (url.length() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * get a domain for a url
	 * 
	 * @param url
	 * @return
	 */
	public static String getDomain(String url) {
		if (StringUtils.isBlank(url))
			return null;
		String domain;
		int pos = url.indexOf("//");
		domain = url.substring(pos + 2);
		int endpos = domain.indexOf("/");

		if (url.indexOf("http") > -1) {
			domain = "http://" + domain.substring(0, endpos);
		} else {
			domain = "https://" + domain.substring(0, endpos);
		}
		Log.i("domain: ", "domain: " + domain);
		return domain;
	}
}
