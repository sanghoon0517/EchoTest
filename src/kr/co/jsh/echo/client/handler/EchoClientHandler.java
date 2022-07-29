package kr.co.jsh.echo.client.handler;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author 전상훈
 * 
 * Echo클라이언트 step
 * 
 * 1.서버로 연결한다.
 * 2. 메시지를 하나 이상 전송한다.
 * 3. 메시지마다 대기하고 서버로부터 동일한 메시지를 수신한다.
 * 4. 연결을 닫는다.
 *
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
	
	@Override //서버에 대한 연결이 만들어지면 호출된다.
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		String ENCODING = "UTF-8";
		String content = "FIXED TEST DATA를 전송합니다.";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(content.getBytes(ENCODING));
		
		byte[] bs = baos.toByteArray();
		
		ByteBuf buf = Unpooled.directBuffer();
		buf.writeBytes(bs);

		
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Connect()\n", CharsetUtil.UTF_8)); //채널 활성화 시 메시지 전송
		ctx.writeAndFlush(buf); //채널 활성화 시 메시지 전송
	}
	
	@Override //서버로부터 메시지를 수신하면 호출된다.
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		System.out.println("Client receive : "+msg.toString(CharsetUtil.UTF_8)); //수신한 메시지 로깅
	}
	
	@Override //처리 중에 예외가 발생하면 호출된다.
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
