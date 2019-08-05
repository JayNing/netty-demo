package com.ning.gupao.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author JAY
 * @Date 2019/8/4 12:35
 * @Description TODO
 **/
public class NIOServerDemo {

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private Selector selector;
    private int port;

    public static void main(String[] args) {
        new NIOServerDemo(8080).listen();
    }

    private void listen() {
        System.out.println("listen on " + this.port + ".");
        try {
            //轮询主线程
            while (true){
                //selector叫号
                selector.select();
                //每次都拿到所有的号子进行循环遍历，然后根据状态进行处理
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                //不断地迭代，就叫轮询
                //同步体现在这里，因为每次只能拿一个key，每次只能处理一种状态
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    //处理完之后，移除当前key
                    iterator.remove();
                    //每一个key代表一种状态
                    //没一个号对应一个业务
                    //数据就绪、数据可读、数据可写 等等等等
                    processKey(selectionKey);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processKey(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()){
            //可接收连接的，之后进入可读状态
            //建立通道连接
            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
            SocketChannel channel = server.accept();
            channel.configureBlocking(false);
            //当准备就绪，数据进入可读阶段
            selectionKey = channel.register(selector,SelectionKey.OP_READ);
        }else if (selectionKey.isReadable()){
            //是可读的
            //key.channel 从多路复用器中拿到客户端的引用
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            int len = channel.read(byteBuffer);
            if (len > 0){
                //将索引下标指向有数据的区域
                byteBuffer.flip();

                String content = new String(byteBuffer.array(),0, len);
                selectionKey = channel.register(selector, SelectionKey.OP_WRITE);
                //在key上携带一个附件，一会再写出去
                selectionKey.attach(content);
                System.out.println("读取到的内容：" + content);
            }

        }else if (selectionKey.isWritable()){
            //可写的
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            String content = (String) selectionKey.attachment();
            channel.write(ByteBuffer.wrap(("写入的内容：" + content).getBytes()));
            channel.close();
        }
    }

    public NIOServerDemo(int port) {
        try {
            this.port = port;
            //建立通道连接
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(this.port));
            //BIO 升级版本 NIO，为了兼容BIO，NIO模型默认是采用阻塞式
            serverSocketChannel.configureBlocking(false);

            //准备大堂经理就绪
            selector = Selector.open();
            //通道开始进入接收状态
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
