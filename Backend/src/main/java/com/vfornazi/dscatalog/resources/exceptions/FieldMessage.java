package com.vfornazi.dscatalog.resources.exceptions;

import java.io.Serializable;

public class FieldMessage implements Serializable{


	private static final long serialVersionUID = 1L;

	private String fieldMString;
	private String message;
	
	public FieldMessage() {
		
	}

	public FieldMessage(String fieldMString, String message) {
		this.fieldMString = fieldMString;
		this.message = message;
	}

	public String getFieldMString() {
		return fieldMString;
	}

	public void setFieldMString(String fieldMString) {
		this.fieldMString = fieldMString;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
