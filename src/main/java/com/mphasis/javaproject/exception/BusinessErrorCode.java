package com.mphasis.javaproject.exception;

public enum BusinessErrorCode {

	
	    INSUFFICIENT_FUNDS("1001", "The account does not have enough balance."),
	   EVENT_TYPE("1002", "Event type should be CREDIT or DEBIT"),
	   INVALID_ACCOUNT_NUMBER("1000", "The provided account number does not exist."),
	   CURRENCY_TYPE("1003", "please provide valid currency"),
	   TRANSACTION_AMOUNT("1004", "please choose more than 0 as transaction amount");

	    private final String code;
	    private final String message;

	    BusinessErrorCode(String code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	    public String getCode() { return code; }
	    public String getMessage() { return message; }
	

}
