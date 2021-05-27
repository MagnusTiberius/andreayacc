package com.magnustiberius.andrea.yacc.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magnustiberius.andrea.yacc.dao.Token;
import com.magnustiberius.andrea.yacc.storage.Inventory;


@Service
public class Tokenizer {

	@Autowired
	Inventory inventory;
	
	int curptr;
	String inputData;
	char ch;
	int line;
	int col;
	int scanPtr;
	String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
	String num = "0123456789";
	String alphanum = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";
	String nonAlpha = ";:{}#@%<>.=|?*-!$&";
	
	public String readFromInputStream(InputStream inputStream)
	  throws IOException {
	    StringBuilder resultStringBuilder = new StringBuilder();
	    try (BufferedReader br
	      = new BufferedReader(new InputStreamReader(inputStream))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            resultStringBuilder.append(line).append("\n");
	        }
	    }
	  return resultStringBuilder.toString();
	}	
	
	public void resetCounter() {
	    curptr = 0;
	    line = 0;
	    col = 1;
	    scanPtr = 1;
	}
	
	public String add(String ID, String fileName) throws IOException {
	    Class clazz = Tokenizer.class;
	    InputStream inputStream = clazz.getResourceAsStream(fileName);
	    inputData = readFromInputStream(inputStream);	
	    inventory.add(ID, inputData);
	    resetCounter();
	    return ID;
	}
	
	public Token getNext() {
		ch = inputData.charAt(curptr);
		while(inputData.charAt(curptr) == ' ' || inputData.charAt(curptr) == '\n' || inputData.charAt(curptr) == '\r') {
			curptr++;
			ch = inputData.charAt(curptr);
		}

		int i = nonAlpha.indexOf(ch);
		if (i != -1) {
			curptr++;
			ch = inputData.charAt(curptr);
			i = nonAlpha.indexOf(ch);
			return null;
		}		
		
		if (ch == '/') {
			if (inputData.charAt(curptr+1) == '/') {
				// a comment that will go until newline.
				scanPtr = curptr + 1;
				System.out.println("curptr:" + curptr + " scanPtr:" + scanPtr);
			}
			if (inputData.charAt(curptr+1) == '*') {
				// comment block, scan until end comment block is detected
				ch = inputData.charAt(scanPtr);
				Boolean keepGoing = true;
				while (keepGoing) {
					if (ch == '*') {
						char ch2 = inputData.charAt(scanPtr+1);
						if (ch2 == '/') {
							keepGoing = false;
							scanPtr++;
							break;
						}
					}
					scanPtr++;
					ch = inputData.charAt(scanPtr);
				}
				System.out.println("Done scanning comment >> curptr:" + curptr + " scanPtr:" + scanPtr);
				curptr = scanPtr;
				curptr++;
			}
			return null;
		}
		
		ch = inputData.charAt(curptr);
		while(inputData.charAt(curptr) == ' ' || inputData.charAt(curptr) == '\n' || inputData.charAt(curptr) == '\r') {
			curptr++;
			ch = inputData.charAt(curptr);
		}

		if (alpha.indexOf(ch) > 0) {
			scanPtr = curptr;
			i = alpha.indexOf(ch);
			while (i != -1)  {
				scanPtr++;
				ch = inputData.charAt(scanPtr);
				i = alpha.indexOf(ch);
			}
			i = alphanum.indexOf(ch);
			while (i != -1)  {
				scanPtr++;
				ch = inputData.charAt(scanPtr);
				i = alphanum.indexOf(ch);
			}
			curptr = scanPtr;
			return null;
		}
		
		return null;
	}
	
}
