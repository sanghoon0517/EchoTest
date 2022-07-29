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
 * @author 전상훈
 * 
 * main메서드를 통해 서버를 부트스트랩한다.
 * 
 * 부트스트랩 step
 * 1. 서버를 부트스트랩하고 바인딩하는 데 이용할 ServerBootstrap 인스턴스를 생성한다.
 * 2. 새로운 연결 수락 및 데이터 읽기/쓰기와 같은 이벤트 처리를 수행할 NioEventLoopGroup 인스턴스를 생성하고 할당한다.
 * 3. 서버가 바인딩하는 로컬 InetSocketAddress를 지정한다.
 * 4. EchoServerHandler 인스턴스를 이용해 새로운 각 Channel을 초기화한다.
 * 5. ServerBootstrap.bind()를 호출해 서버를 바인딩한다.
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
		EventLoopGroup group = new NioEventLoopGroup(); // EventLoogGroup 생성
		ServerBootstrap b = new ServerBootstrap(); // ServerBootstrap 생성
		try {
			b.group(group)
			.channel(NioServerSocketChannel.class) //NIO 전송채널을 이용하도록 지정
			.localAddress(new InetSocketAddress(port)) // 지정된 포트로 소켓 주소 설정
			.childHandler(new ChannelInitializer<SocketChannel>() { // EchoServerHandler 하나를 채널의 ChannelPipeline으로 추가
				protected void initChannel(SocketChannel ch) throws Exception { //EchoServerHandler
					ch.pipeline().addLast(serverHandler); //EchoServerHandler는 @Sharable이므로 동일한 항목을 이용할 수 있다.
				};
			});
			ChannelFuture f = b.bind().sync(); //서버를 비동기식으로 바인딩하겠다. sync()는 바인딩이 완료되기를 대기하겠다는 의미이다.
			f.channel().closeFuture().sync(); // 채널의 closeFutre를 얻고 완료될 때까지 현재 스레드를 블로킹
		} finally {
			group.shutdownGracefully().sync(); //EventGroup을 종료하고 모든 리소스 해제
		} 
	}
}
