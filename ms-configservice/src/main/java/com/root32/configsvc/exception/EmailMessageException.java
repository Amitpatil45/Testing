package com.root32.configsvc.exception;

import javax.mail.MessagingException;

public class EmailMessageException extends RuntimeException {

	public EmailMessageException(MessagingException e) {
		super(e);
	}

	private static final long serialVersionUID = 1L;

}
