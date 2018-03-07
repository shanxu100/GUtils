package scut.luluteam.gutils.network.websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 基于Netty的websocket协议的 Client 实现。。。
 * 用于Android还有问题，目前无法使用
 *
 * @author Guan
 */
public class WebSocketClientByNetty {

    static final String URL = "ws://125.216.242.147:8080/bathProject/websocket";
    private EventLoopGroup group = new NioEventLoopGroup();
    private URI uri;
    private final int port;
    private Timer timer = new Timer();
    private static volatile boolean isConnected = false;


    private WebSocketClientByNetty() {
        try {
            uri = new URI(URL);
            String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                System.err.println("Only WS(S) is supported.");
                throw new Exception();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        port = uri.getPort() == -1 ? 80 : uri.getPort();
    }

    private static class WSClientBuilder {
        public static WebSocketClientByNetty client = new WebSocketClientByNetty();
    }

    public static WebSocketClientByNetty getInstance() {
        return WSClientBuilder.client;
    }


    public void start() {
        connect();
    }

    public void stop() {
        isConnected = false;
        if (group != null) {
            group.shutdownGracefully();
        }
        timer = null;
    }

    /**
     * 开始建立连接
     */
    public void connect() {
        if (isConnected) {
            System.out.println("连接已建立，无需重复连接");
            return;
        }
        try {
            Bootstrap b = new Bootstrap();
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri,
                    WebSocketVersion.V13, null, true,
                    new DefaultHttpHeaders());
            final WebSocketClientHandler handler = new WebSocketClientHandler(handshaker);
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpClientCodec(),
                                    new HttpObjectAggregator(8192),
                                    WebSocketClientCompressionHandler.INSTANCE,
                                    handler);
                        }
                    });

            Channel ch = b.connect(uri.getHost(), port).sync().channel();
            handler.handshakeFuture().sync();

            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = console.readLine();
                if (msg == null) {
                    break;
                } else if ("bye".equals(msg.toLowerCase())) {
                    ch.writeAndFlush(new CloseWebSocketFrame());
                    ch.closeFuture().sync();
                    break;
                } else if ("ping".equals(msg.toLowerCase())) {
                    WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
                    ch.writeAndFlush(frame);
                } else {
                    WebSocketFrame frame = new TextWebSocketFrame(msg);
                    ch.writeAndFlush(frame);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

    /**
     * 断线重连
     */
    public void reconnect() {
        if (timer == null) {
            return;
        }
        //发现断线后，1000ms后开始执行该任务。每次执行完该任务后，5000ms再次执行
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isConnected) {
                    timer.cancel();
                }
                connect();
            }
        }, 1000, 5000);
    }

    public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

        private final WebSocketClientHandshaker handshaker;
        private ChannelPromise handshakeFuture;

        public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
            this.handshaker = handshaker;
        }

        public ChannelFuture handshakeFuture() {
            return handshakeFuture;
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            handshakeFuture = ctx.newPromise();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            System.out.println("连接已经建立......" + Thread.currentThread().getName());
            handshaker.handshake(ctx.channel());
            isConnected = true;
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            System.out.println("WebSocket Client disconnected......reconnecting......"
                    + Thread.currentThread().getName());
            isConnected = false;
            reconnect();
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            Channel ch = ctx.channel();
            if (!handshaker.isHandshakeComplete()) {
                try {
                    handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                    System.out.println("WebSocket Client connected!");
                    handshakeFuture.setSuccess();
                } catch (WebSocketHandshakeException e) {
                    System.out.println("WebSocket Client failed to connect");
                    handshakeFuture.setFailure(e);
                }
                return;
            }

            if (msg instanceof FullHttpResponse) {
                FullHttpResponse response = (FullHttpResponse) msg;
                throw new IllegalStateException(
                        "Unexpected FullHttpResponse (getStatus=" + response.status() +
                                ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
            }

            WebSocketFrame frame = (WebSocketFrame) msg;
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
                System.out.println("WebSocket Client received message: " + textFrame.text());
            } else if (frame instanceof PongWebSocketFrame) {
                System.out.println("WebSocket Client received pong");
            } else if (frame instanceof CloseWebSocketFrame) {
                System.out.println("WebSocket Client received closing");
                ch.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            if (!handshakeFuture.isDone()) {
                handshakeFuture.setFailure(cause);
            }
            ctx.close();
        }
    }

    public static void main(String[] args) throws Exception {
        WebSocketClientByNetty.getInstance().start();
    }


}
