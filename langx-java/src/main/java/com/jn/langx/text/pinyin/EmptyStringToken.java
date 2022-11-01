package com.jn.langx.text.pinyin;

import com.jn.langx.util.Emptys;

class EmptyStringToken extends StringToken {
    EmptyStringToken(){
        setBody(Emptys.EMPTY_STRING);
    }
}
