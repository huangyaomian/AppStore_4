package com.hym.appstore.common.exception;

import com.hym.appstore.ui.activity.BaseActivity;

public class ApiException extends BaseException {

    public ApiException(int code, String displayMessage) {
        super(code, displayMessage);
    }
}
