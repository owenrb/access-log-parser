package com.ef.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class InputReader {
	 
	private InputTypeEnum inputType;
	private String path;
	
	public BufferedReader createBufferedReader() throws FileNotFoundException {
		
		switch(inputType) {
		case CLASSPATH:
			return new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream(path)));
		case FILEPATH:
			return new BufferedReader(new InputStreamReader(
                    new FileInputStream(path)));
		default:
			return new BufferedReader(new InputStreamReader(System.in));
		}
		
	}

	/**
	 * @return the inputType
	 */
	public InputTypeEnum getInputType() {
		return inputType;
	}

	/**
	 * @param inputType the inputType to set
	 */
	public void setInputType(InputTypeEnum inputType) {
		this.inputType = inputType;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
