package kr.co.jsh.echo.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import kr.co.jsh.echo.server.handler.EchoServerHandler;

/**
 * @author ������
 * 
 * main�޼��带 ���� ������ ��Ʈ��Ʈ���Ѵ�.
 * 
 * ��Ʈ��Ʈ�� step
 * 1. ������ ��Ʈ��Ʈ���ϰ� ���ε��ϴ� �� �̿��� ServerBootstrap �ν��Ͻ��� �����Ѵ�.
 * 2. ���ο� ���� ���� �� ������ �б�/����� ���� �̺�Ʈ ó���� ������ NioEventLoopGroup �ν��Ͻ��� �����ϰ� �Ҵ��Ѵ�.
 * 3. ������ ���ε��ϴ� ���� InetSocketAddress�� �����Ѵ�.
 * 4. EchoServerHandler �ν��Ͻ��� �̿��� ���ο� �� Channel�� �ʱ�ȭ�Ѵ�.
 * 5. ServerBootstrap.bind()�� ȣ���� ������ ���ε��Ѵ�.
 *
 */
public class EchoServer {
	private final int port;
	public EchoServer() {
		this.port = 8888;
	}
	public static void main(String[] args) throws Exception{
		new EchoServer().start();
	}
	
	private void start() throws Exception {
		final EchoServerHandler serverHandler = new EchoServerHandler();
		EventLoopGroup group = new NioEventLoopGroup(); // EventLoogGroup ����
		ServerBootstrap b = new ServerBootstrap(); // ServerBootstrap ����
		try {
			b.group(group)
			.channel(NioServerSocketChannel.class) //NIO ����ä���� �̿��ϵ��� ����
			.localAddress(new InetSocketAddress(port)) // ������ ��Ʈ�� ���� �ּ� ����
			.childHandler(new ChannelInitializer<SocketChannel>() { // EchoServerHandler �ϳ��� ä���� ChannelPipeline���� �߰�
				protected void initChannel(SocketChannel ch) throws Exception { //EchoServerHandler
					ch.pipeline().addLast(serverHandler); //EchoServerHandler�� @Sharable�̹Ƿ� ������ �׸��� �̿��� �� �ִ�.
				};
			});
			ChannelFuture f = b.bind().sync(); //������ �񵿱������ ���ε��ϰڴ�. sync()�� ���ε��� �Ϸ�Ǳ⸦ ����ϰڴٴ� �ǹ��̴�.
			f.channel().closeFuture().sync(); // ä���� closeFutre�� ��� �Ϸ�� ������ ���� �����带 ���ŷ
		} finally {
			group.shutdownGracefully().sync(); //EventGroup�� �����ϰ� ��� ���ҽ� ����
		} 
	}
}
