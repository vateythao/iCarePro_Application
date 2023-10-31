package com.vat.icare.chats.media;

/**
 * Created by Pradeep on 10/1/2016.
 */
public interface Packable {
    ByteBuf marshal(ByteBuf out);
}
