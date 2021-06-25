package com.vfornazi.dscatalog.resources.exceptions;

import java.io.Serializable;

public class FieldMessage implements Serializable{


	private static final long serialVersionUID = 1L;

	private String fieldMessage;
	private String message;
	
	public FieldMessage() {
		
	}

	public FieldMessage(String fieldMString, String message) {
		this.fieldMessage = fieldMString;
		this.message = message;
	}

	public String getFieldMString() {
		return fieldMessage;
	}

	public void setFieldMString(String fieldMString) {
		this.fieldMessage = fieldMString;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
