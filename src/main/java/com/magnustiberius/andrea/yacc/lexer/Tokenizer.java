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
	String nonAlpha = ";:{}#@%<>.=|?*-!$&'\",.`~()+^[]\\";
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
		resetCounter();
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
	
	public byte nextChar() {
		return inputData[curptr];
	}

	public byte scanChar() {
		byte b = inputData[scanPtr];
		scanPtr++;
		return b;
	}
	
	public void setCurrentPointerFromScan() {
		curptr = scanPtr;
	}
	
	public Token getNext() {
		Token t = new Token();
		ch = nextChar();
		while(ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') {
			if (ch == '\n' ) {
				line++;
			}
			curptr++;
			ch = nextChar();
		}

		int i = nonAlpha.indexOf((char)ch);
		if (i != -1) {
			scanPtr = curptr;
			scanPtr++;
			t.setBegin(curptr);
			t.setEnd(scanPtr);
			t.setType("spec");
			t.setLine(line);
			t.fill(inputData);
			setCurrentPointerFromScan();
			return t;
		}		

		if (ch == '/') {
			if (inputData[curptr+1] == '/') {
				// a comment that will go until newline.
				scanPtr = curptr + 1;
				while(scanChar() != '\n') {
				}
				System.out.println("curptr:" + curptr + " scanPtr:" + scanPtr);
				t.setBegin(curptr);
				t.setEnd(scanPtr-2);
				t.setType("comment");
				t.setLine(line);
				t.fill(inputData);
				setCurrentPointerFromScan();
				return t;
			}
			if (inputData[curptr+1] == '*') {
				// comment block, scan until end comment block is detected
				ch = scanChar();
				Boolean keepGoing = true;
				while (keepGoing) {
					if (ch == '\n') {
						line++;
					}
					char c1 = (char)ch;
					if (ch == '*') {
						byte ch2 = inputData[scanPtr];
						char c2 = (char)ch2;
						if (ch2 == '/') {
							keepGoing = false;
							break;
						}
					}
					ch = scanChar();
				}
				System.out.println("Done scanning comment >> curptr:" + curptr + " scanPtr:" + scanPtr);
				t.setBegin(curptr);
				t.setEnd(scanPtr);
				t.setType("comment");
				t.setLine(line);
				t.fill(inputData);
				setCurrentPointerFromScan();
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
				ch = scanChar();
				i = alpha.indexOf((char)ch);
			}
			i = alphanum.indexOf((char)ch);
			while (i != -1)  { // alpha with number
				ch = scanChar();
				i = alphanum.indexOf((char)ch);
			}
			t.setBegin(curptr);
			t.setEnd(scanPtr-1);
			t.setType("ident");
			t.setLine(line);
			t.fill(inputData);
			setCurrentPointerFromScan();
			return t;
		}
		
		// number
		if (num.indexOf((char)ch) != -1) {
			scanPtr = curptr;
			i = num.indexOf((char)ch);
			while (i != -1)  { // number
				ch = scanChar();
				i = num.indexOf(ch);
			}
			i = numdec.indexOf((char)ch);
			while (i != -1)  { // number and dot
				ch = scanChar();
				i = numdec.indexOf((char)ch);
			}
			t.setBegin(curptr);
			t.setEnd(scanPtr);
			setCurrentPointerFromScan();
			t.setType("number");
			t.setLine(line);
			t.fill(inputData);
			return t;
		}		
		
		return t;
	}
	
}
