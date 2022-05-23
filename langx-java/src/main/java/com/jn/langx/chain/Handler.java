package com.jn.langx.chain;


import com.jn.langx.lifecycle.Destroyable;
import com.jn.langx.lifecycle.Initializable;
import com.jn.langx.util.function.Handler3;

public interface Handler<REQ, RESP> extends Handler3<REQ, RESP, Chain<REQ, RESP>>, Initializable, Destroyable {
    void handle(REQ request, RESP response, Chain<REQ, RESP> chain);
}

