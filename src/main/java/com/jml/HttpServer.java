package com.jml;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

/**
 * @Author: jml
 * @Date: 18-9-24
 * @Description:
 */
public class HttpServer {

    private static final int port = 8080;

    public HttpServer() {
    }

    public static void main(String[] args) throws InterruptedException {
        new HttpServer().start();
    }

    public void start() throws InterruptedException {
        ServerBootstrap b = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        System.out.println("initChannel ch:" + socketChannel);
                        socketChannel.pipeline()
                                .addLast("decoder", new HttpRequestDecoder())
                                .addLast("encoder", new HttpResponseEncoder())
                                .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                                .addLast("handler", new HttpHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        b.bind(port).sync();
    }
}
