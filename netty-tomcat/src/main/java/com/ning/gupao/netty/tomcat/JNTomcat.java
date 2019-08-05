package com.ning.gupao.netty.tomcat;

import com.ning.gupao.netty.tomcat.http.JNServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import javax.swing.plaf.FontUIResource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @Author JAY
 * @Date 2019/8/5 19:30
 * @Description TODO
 **/
//Netty就是一个同时支持多协议的网络通信框架
public class JNTomcat {

    private int port = 8080;
    private Map<String, JNServlet> servletMapping = new HashMap<>();
    private Properties webXml = new Properties();

    private void init() {
        try {
            //读取配置文件
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/" + "web.properties");
            webXml.load(resourceAsStream);
            //读取servlet放入容器
            Set<Object> keySet = webXml.keySet();
            for (Object k : keySet) {
                String key = k.toString();
                if (key.endsWith("url")){
                    String servletName = key.replaceAll("\\.url$", "");
                    String url = webXml.getProperty(key);
                    String className = webXml.getProperty(servletName + ".className");
                    JNServlet obj = (JNServlet)Class.forName(className).newInstance();
                    servletMapping.put(url, obj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JNTomcat tomcat = new JNTomcat();
        tomcat.start();
    }

    private void start() {
        init();
        //开始使用netty API建立服务器连接

        //Netty封装了NIO，Reactor模型，Boss，worker
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // Netty服务
            //ServerBootstrap   ServerSocketChannel
            ServerBootstrap server = new ServerBootstrap();
            //链路式编程
            server.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 客户端初始化处理
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {
                            // 无锁化串行编程
                            //Netty对HTTP协议的封装，顺序有要求
                            // HttpResponseEncoder 编码器
                            client.pipeline().addLast(new HttpResponseEncoder());
                            // HttpRequestDecoder 解码器
                            client.pipeline().addLast(new HttpRequestDecoder());
                            // 业务逻辑处理
                            client.pipeline().addLast(new JNTomcatHandler(servletMapping));
                        }
                    })
                    // 针对主线程的配置 分配线程最大数量 128
                    .option(ChannelOption.SO_BACKLOG,128)
                    // 针对子线程的配置 保持长连接
                    .option(ChannelOption.SO_KEEPALIVE, true);
            // 启动服务器
            ChannelFuture future = server.bind(port).sync();
            System.out.println("JN Tomcat 已启动，监听的端口是：" + port);
            future.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
