package com.github.walterfan.hellonetty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ByteBufTest {

    public static void printBufferIndex(ByteBuf buffer, String message) {

        log.info("# {} -> buffer: {},  readableTypes {}, writableBytes: {}, capacity: {}",
                message, buffer,  buffer.readableBytes(), buffer.writableBytes(), buffer.capacity());
    }

    public static void testByteBuf() {

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(4, 12);

        buffer.writeBytes(new byte[] { 1 , 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 ,12});
        printBufferIndex(buffer, "write 12 bytes");

        int size = buffer.readableBytes();
        byte[] output = new byte[size];
        buffer.readBytes(output);
        printBufferIndex(buffer, String.format("read %d bytes", size));

        buffer.discardReadBytes();
        printBufferIndex(buffer, "discardReadBytess");

        buffer.writeBytes(new byte[] { 1 , 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 ,12});
        printBufferIndex(buffer, "write 12 bytes");

    }

    public static void testReference() {
        log.info("----- {} -----", "testReference");
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(4, 32);
        log.info("created: refCnt={}", buffer.refCnt());
        //add_reference : retain()
        buffer.retain();
        log.info("retained: refCnt={}", buffer.refCnt());
        //release_reference: release()
        buffer.release();
        log.info("released: refCnt={}", buffer.refCnt());
        //add_reference : retain()
        buffer.release();
        log.info("released: refCnt={}", buffer.refCnt());


    }

    public static void testAllocator() {
        log.info("----- {} -----", "testAllocator");
        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(4, 32);


    }

    public static void main(String[] args) {

        testByteBuf();
        testReference();
        testAllocator();
    }
}
