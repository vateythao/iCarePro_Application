package com.vat.icare.chats.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
