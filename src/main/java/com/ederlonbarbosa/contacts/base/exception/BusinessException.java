package com.ederlonbarbosa.contacts.base.exception;

/**
 * @author Ederlon Barbosa
 */
public class BusinessException extends RuntimeException {

    public BusinessException(final String messageDetails) {
        super(messageDetails);
    }

}
