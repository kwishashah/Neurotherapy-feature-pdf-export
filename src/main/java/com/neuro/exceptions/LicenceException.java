package com.neuro.exceptions;

import com.neuro.license.LicenseManager;

/**
 * This exception is thrown when a license key is invalid.
 */
public class LicenceException extends Exception {

    public LicenceException(String message) {
        super(message);
    }
    public LicenceException (String message,Throwable e ) {
        super(message,e);
    }

}
