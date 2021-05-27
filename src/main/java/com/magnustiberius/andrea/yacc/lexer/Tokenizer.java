package com.magnustiberius.andrea.yacc.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	byte ch;
	int line;
	int col;
	int scanPtr;
	String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
	String num = "0123456789";
	String numdec = "0123456789.";
	String alphanum = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";
	String nonAlpha = ";:{}#@%<>.=|?*-!$&'\",.`~()+^[]";
	String ctlChar = "\r\n\t";
	
	byte[] inputData;
	String inputData2;
	
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
	
	public byte[] readFileBytes(String inputFile) throws IOException {
		Class clazz = Tokenizer.class;
		InputStream inputStream = clazz.getResourceAsStream(inputFile);
		//InputStream inputStream = new FileInputStream(inputFile);
		inputData = inputStream.readAllBytes();
		//long fileSize = new File(inputFile).length();
		//inputData = new byte[(int) fileSize];
	//	inputStream.read(inputData);
		return inputData;
	}
	
	public void resetCounter() {
	    curptr = 0;
	    line = 0;
	    col = 1;
	    scanPtr = 1;
	}
	
	public String add2(String ID, String fileName) throws IOException {
	    Class clazz = Tokenizer.class;
	    InputStream inputStream = clazz.getResourceAsStream(fileName);
	    inputData2 = readFromInputStream(inputStream);	
	    inventory.add(ID, inputData2);
	    resetCounter();
	    return ID;
	}

	public String add(String ID, String fileName) throws IOException {
	    inputData = readFileBytes(fileName);	
	    //inventory.add(ID, inputData);
	    resetCounter();
	    return ID;
	}
	
	public Token getNext() {
		Token t = new Token();
		ch = inputData[curptr];
		while(ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') {
			if (ch == '\n' ) {
				line++;
			}
			curptr++;
			ch = inputData[curptr];
		}

		int i = nonAlpha.indexOf((char)ch);
		if (i != -1) {
			scanPtr = curptr;
			scanPtr++;
			ch = inputData[scanPtr];
			i = nonAlpha.indexOf((char)ch);
			t.setBegin(curptr);
			t.setEnd(scanPtr);
			curptr = scanPtr;
			t.setType("spec");
			t.fill(inputData);
			return t;
		}		

		if (ch == '/') {
			if (inputData[curptr+1] == '/') {
				// a comment that will go until newline.
				scanPtr = curptr + 1;
				while(inputData[scanPtr] != '\n') {
					scanPtr++;
				}
				System.out.println("curptr:" + curptr + " scanPtr:" + scanPtr);
				curptr = scanPtr;
				curptr++;
				t.setBegin(curptr);
				t.setEnd(scanPtr);
				t.setType("comment");
				t.fill(inputData);
				return t;
			}
			if (inputData[curptr+1] == '*') {
				// comment block, scan until end comment block is detected
				ch = inputData[scanPtr];
				Boolean keepGoing = true;
				while (keepGoing) {
					if (ch == '*') {
						byte ch2 = inputData[scanPtr+1];
						if (ch2 == '/') {
							keepGoing = false;
							scanPtr++;
							break;
						}
					}
					scanPtr++;
					ch = inputData[scanPtr];
				}
				System.out.println("Done scanning comment >> curptr:" + curptr + " scanPtr:" + scanPtr);
				t.setBegin(curptr);
				t.setEnd(scanPtr);
				t.setType("comment");
				t.fill(inputData);
				curptr = scanPtr;
				curptr++;
				return t;
			}
			t.setBegin(curptr);
			t.setEnd(scanPtr);
			t.setType("slash");
			t.fill(inputData);
			curptr = scanPtr;
			curptr++;
			
			return t;
		}
		
		if (alpha.indexOf((char)ch) != -1) {
			scanPtr = curptr;
			i = alpha.indexOf((char)ch);
			while (i != -1)  { // alpha
				scanPtr++;
				ch = inputData[scanPtr];
				i = alpha.indexOf((char)ch);
			}
			i = alphanum.indexOf((char)ch);
			while (i != -1)  { // alpha with number
				scanPtr++;
				ch = inputData[scanPtr];
				i = alphanum.indexOf((char)ch);
			}
			t.setBegin(curptr);
			t.setEnd(scanPtr);
			t.setType("ident");
			t.fill(inputData);
			curptr = scanPtr;
			return t;
		}
		
		// number
		if (num.indexOf((char)ch) != -1) {
			scanPtr = curptr;
			i = num.indexOf((char)ch);
			while (i != -1)  { // number
				scanPtr++;
				ch = inputData[scanPtr];
				i = num.indexOf(ch);
			}
			i = numdec.indexOf((char)ch);
			while (i != -1)  { // number and dot
				scanPtr++;
				ch = inputData[scanPtr];
				i = numdec.indexOf((char)ch);
			}
			t.setBegin(curptr);
			t.setEnd(scanPtr);
			curptr = scanPtr;
			t.setType("number");
			t.fill(inputData);
			return t;
		}		
		
		return t;
	}
	
}
