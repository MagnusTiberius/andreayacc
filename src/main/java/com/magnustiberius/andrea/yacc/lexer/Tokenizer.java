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
		while(inputData.charAt(curptr) == ' ') {
			curptr++;
		}
		ch = inputData.charAt(curptr);
		if (ch == '/') {
			if (inputData.charAt(curptr+1) == '/') {
				// a comment that will go until newline.
				scanPtr = curptr + 1;
				while (inputData.charAt(scanPtr) != '\r' || inputData.charAt(scanPtr) != '\n') {
					scanPtr++;
				}
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
							break;
						}
					}
					scanPtr++;
					ch = inputData.charAt(scanPtr);
				}
				System.out.println("Done scanning comment >> curptr:" + curptr + " scanPtr:" + scanPtr);
			}
		}
		return null;
	}
	
}
