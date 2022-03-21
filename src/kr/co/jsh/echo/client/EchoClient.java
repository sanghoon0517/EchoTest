package kr.co.jsh.echo.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import kr.co.jsh.echo.client.handler.EchoClientHandler;

/**
 * @author 전상훈
 * 
 * EchoClient 과정 정리
 * 1. 클라이언트를 초기화하기 위한 Bootstrap 인스턴스를 생성한다.
 * 2. 새로운 연결을 생성하고 인바운드와 아웃바운드 데이터를 처리하는 것을 포함하는 이벤트 처리를 제어할 NioEventLoopGroup 인스턴스를 만들고 할당한다.
 * 3. 서버로 연결하기 위한 InetSocketAddress를 생성한다.
 * 4. 연결이 만들어지면 파이프라인에 EchoClientHandler 하나를 추가한다.
 * 5. 모든 준비가 완료되면 Bootstrap.connect()를 호출시켜 원격 서버로 연결한다.
 *
 */
public class EchoClient {
	public EchoClient() {}

	public static void main(String[] args) throws Exception{
		new EchoClient().start();
	}
	
	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap b = new Bootstrap(); //bootstrap 생성
			b.group(group) //클라이언트 이벤트를 처리할 EventLoopGroup을 지정
					.channel(NioSocketChannel.class) //채널 유형 NIO 지정
					.remoteAddress(new InetSocketAddress(8888)) //서버의 InetSocketAddress를 설정
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception { //채널이 생성될 때, 파이프라인에 EchoClientHandler 하나를 추가
							ch.pipeline().addLast(new EchoClientHandler());
						}
					});
			ChannelFuture f = b.connect().sync(); //원격 피어로 연결하고 연결이 완료되기를 기다림
			f.channel().closeFuture().sync(); //채널이 닫힐 때까지 블로킹함
		} finally {
			group.shutdownGracefully().sync(); //스레드 풀을 종료하고 모든 리소스를 해제함
		}
	}

}
