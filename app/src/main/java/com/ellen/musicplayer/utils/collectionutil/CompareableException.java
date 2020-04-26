package com.ellen.musicplayer.utils.collectionutil;

public class CompareableException extends RuntimeException {

    private String errorCode;
    private String errorMessage;

    public CompareableException(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorCode+""+errorMessage;
    }

}
