package com.qunar.work5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Description
 * @auther lx
 * @create 2020-07-06 20:40
 */
public class NIOClient {
    public static void main(String[] args) {
        try {
            connect("127.0.0.1", 8899);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void connect(String host, int port) throws IOException {
//        打开通道
        SocketChannel sChannel = SocketChannel.open();
//        通道设置为非阻塞
        sChannel.configureBlocking(false);
//        连接
        sChannel.connect(new InetSocketAddress(host, port));
//        选择器
        Selector selector = Selector.open();
//        把通道注册到选择器，监听读事件
        sChannel.register(selector, SelectionKey.OP_READ);

//        1、对于阻塞模式下，调用connect()进行连接操作时，会一直阻塞到连接建立完成
//        （无连接异常的情况下）。所以可以不用finishConnect来确认。
//        2、但在非阻塞模式下，connect()操作是调用后直接返回结果的，有可能是true
//         (如本地连接)，也可能是false(在部分情况下是false)。所以为了确定
//         后续IO操作正常进行需等待连接的建立，这时finishConnect的作用就出来了。
//         可以阻塞到连接建立好。
        while (!sChannel.finishConnect()) ;

//        开启一个线程读数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                //            定义缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                try {
//                    轮询
                    while (selector.select() > 0) {
                        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                        while (it.hasNext()) {
                            SelectionKey sk = it.next();
//                            读事件
                            if (sk.isReadable()) {
                                SocketChannel channel = (SocketChannel) sk.channel();
                                int len = 0;
                                while ((len = channel.read(buffer)) > 0) {
//                                    channel.read(buffer);
//                                    buffer.flip();
                                    String str = new String(buffer.array(), 0, len);
                                    System.out.println(str);
                                    buffer.clear();
                                }
                            }
                        }
                        it.remove();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        主线程写数据
        Scanner sc = new Scanner(System.in);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            String str = sc.next();
            if ("quit".equals(str)) {
                break;
            }
            buffer.put(str.getBytes());
            buffer.flip();
            sChannel.write(buffer);
            buffer.clear();
        }
//        关闭通道
        sChannel.close();
    }
}