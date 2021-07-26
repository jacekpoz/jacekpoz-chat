package com.github.jacekpoz.common.exceptions;

import com.github.jacekpoz.common.database.queries.Query;
import com.github.jacekpoz.common.exceptions.UnknownSendableException;

public class UnknownQueryException extends UnknownSendableException {

    private static final long serialVersionUID = 6003384360314407592L;

    public UnknownQueryException() {
        super();
    }

    public UnknownQueryException(Query q) {
        super(q);
    }
}
