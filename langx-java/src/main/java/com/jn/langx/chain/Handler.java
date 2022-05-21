package com.jn.langx.chain;


import com.jn.langx.lifecycle.Destroyable;
import com.jn.langx.lifecycle.Initializable;

public interface Handler<REQ, RESP> extends Initializable, Destroyable {
    void handle(REQ request, RESP response, Chain<REQ, RESP> chain);
}

