package com.qunar.work5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description
 * @auther lx
 * @create 2020-07-06 20:39
 */
public class NIOServer {
    public static void main(String[] args) {
        try {
            openServer(8899);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void openServer(int port) throws IOException {
//        打开通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
//        设置为非阻塞
        ssChannel.configureBlocking(false);
//        绑定端口
        ssChannel.socket().bind(new InetSocketAddress(port));
//        选择器
        Selector selector = Selector.open();
//        把通道接受事件注册到选择器
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
//        当被选中的通道个数大于0时，进行业务处理
        while (selector.select() > 0) {
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                if (sk.isAcceptable()) {
//                    得到发出请求连接的通道
                    ServerSocketChannel channel = (ServerSocketChannel) sk.channel();
                    SocketChannel sChannel = channel.accept();
//                    通道设置非阻塞
                    sChannel.configureBlocking(false);
//                    把通道读事件注册到选择器
                    sChannel.register(selector, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
//                    得到发出读请求的通道
                    SocketChannel sChannel = (SocketChannel) sk.channel();
//                      定义一个缓冲区
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    StringBuilder data = new StringBuilder();

                    int len = 0;
//                    len为0时，无数据可读；len为-1时，客户端关闭了socket
                    while ((len = sChannel.read(buffer)) > 0) {
//                        更改缓冲区为读模式
//                        buffer.flip();
                        String str = new String(buffer.array(), 0, len);
                        data.append(str);
                        System.out.println(str);
//                         清空缓冲区
                        buffer.clear();
                    }

//                    写响应数据
                    int[] nums = calNumbers(data.toString());
                    buffer.put("总字符数：".getBytes());
                    buffer.put(String.valueOf(nums[0]).getBytes());
                    buffer.put("\r\n汉字数：".getBytes());
                    buffer.put(String.valueOf(nums[1]).getBytes());
                    buffer.put("\r\n英文字符数：".getBytes());
                    buffer.put(String.valueOf(nums[2]).getBytes());
                    buffer.put("\r\n标点符号数：".getBytes());
                    buffer.put(String.valueOf(nums[3]).getBytes());
                    buffer.flip();
                    sChannel.write(buffer);
                    buffer.clear();

                    if (len == -1) {
                        sChannel.close();
                    }
                }
//               取消
                it.remove();
            }
        }
        System.out.println("end");
    }

    private static int[] calNumbers(String data) {
        int[] res = new int[4];
        res[0] = data.length();//总字符数
        int res1 = 0, res2 = 0, res3 = 0;
        String replaceAll = data.replaceAll("[\\pP]", ",");
        for (int i = 0; i < replaceAll.length(); i++) {
            //判断是否为汉字
            if ((19968 <= replaceAll.charAt(i) && replaceAll.charAt(i) <= 40869))
                res1++;
                //判断是否为英文字符
            else if (replaceAll.charAt(i) >= 'a' && replaceAll.charAt(i) <= 'z'
                    || replaceAll.charAt(i) >= 'A' && replaceAll.charAt(i) <= 'Z')
                res2++;
                //判断是否为标点符号
            else if (replaceAll.charAt(i) == ',')
                res3++;
        }
        res[1] = res1;
        res[2] = res2;
        res[3] = res3;
        return res;
    }
}