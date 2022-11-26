package com.vtest.o360.exceptionhandler;

import lombok.Getter;

@Getter
class MethodArgumentErrorDetailMessage extends ErrorDetailMessage {
    private final String fieldName;
    private final Object rejectedValue;

    public MethodArgumentErrorDetailMessage(String fieldName, Object rejectedValue, String detailMessage) {
        super(detailMessage);
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
    }
}
