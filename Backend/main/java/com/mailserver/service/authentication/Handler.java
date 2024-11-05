package com.mailserver.service.authentication;

public abstract class Handler {
    private Handler nextHandler;
    public Handler() {

    }
    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract boolean handle(String email, String password);
    protected boolean checkNext(String email, String password) {
        if(nextHandler == null)
            return true;
        return nextHandler.handle(email, password);
    }
}
