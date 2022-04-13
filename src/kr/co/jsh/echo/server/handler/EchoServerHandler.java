package kr.co.jsh.echo.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author 전상훈
 * 
 * Echo서버는 들어오는 메시지에 반응해야 하므로 인바운드 이벤트에 반응하는 메서드가 정의된 ChannelInboundHandler인터페이스를 구현해야 한다.
 * EchoServerHandler는 비즈니스 로직을 구현한다.
 *
 */
@Sharable //ChannelHandler를 여러 채널 간에 안전하게 공유할 수 있음을 나타낸다.
public class EchoServerHandler extends ChannelInboundHandlerAdapter{
	@Override //메시지가 들어올 때마다 호출된다.
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf)msg;
		System.out.println("Server received : "+in.toString(CharsetUtil.UTF_8));
		ctx.write(in); //클라이언트로부터 받은 메시지를 다시 Echo시킨다.
		//아웃바운드 메시지를 플러시하지 않은 채로 받은 메시지를 발신자로 출력한다.
	}
	
	@Override //channelRead()의 마지막 호출에서 현재 일괄 처리의 마지막 메시지를 처리했음을 핸들러에 통보한다.
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Bye");
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //대기중인 메시지를 플러시하고 채널을 닫음
			.addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override //읽기 작업 중 예외가 발생하면 호출된다.
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close(); //예외가 발생했을 시, 예외를 잡고 채널 context를 닫아버림
	}
}
