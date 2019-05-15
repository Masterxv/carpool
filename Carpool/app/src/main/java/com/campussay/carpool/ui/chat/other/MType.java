package com.campussay.carpool.ui.chat.other;

import java.util.EnumSet;
import java.util.Iterator;

public enum MType {
    TYPE_LEFT_TEXT(1001),  TYPE_RIGHT_TEXT(1002),   //文字
    TYPE_LEFT_VOICE(1003), TYPE_RIGHT_VOICE(1004),  //语音
    TYPE_TIME(1005),                                //时间
    TYPE_REFRESH(1006),                             //刷新
    NULL(-1);

    public final int intValue;

    MType(int v) {
        this.intValue = v;
    }

    public static MType get(int value) {
        Iterator i = EnumSet.allOf(MType.class).iterator();

        while (i.hasNext()) {
            MType c = (MType) i.next();
            if (c.intValue == value) return c;
        }
        return NULL;
    }
}