package com.mphasis.javaproject.exception;

public class BusinessException extends RuntimeException {
	    private final BusinessErrorCode errorCode;

	    public BusinessException(BusinessErrorCode errorCode) {
	        super(errorCode.getMessage());
	        this.errorCode = errorCode;
	    }

	    // Secondary constructor to append a dynamic detail message if needed
	    public BusinessException(BusinessErrorCode errorCode, String customDetails) {
	        super(errorCode.getMessage() + " " + customDetails);
	        this.errorCode = errorCode;
	    }

	    public BusinessErrorCode getErrorCode() {
	        return errorCode;
	    }
	


}
