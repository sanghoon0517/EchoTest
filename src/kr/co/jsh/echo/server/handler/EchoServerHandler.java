package kr.co.jsh.echo.server.handler;

import java.io.ByteArrayOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author ������
 * 
 * Echo������ ������ �޽����� �����ؾ� �ϹǷ� �ιٿ�� �̺�Ʈ�� �����ϴ� �޼��尡 ���ǵ� ChannelInboundHandler�������̽��� �����ؾ� �Ѵ�.
 * EchoServerHandler�� ����Ͻ� ������ �����Ѵ�.
 *
 */

//@Sharable ������̼��� Race Condition ����, �ϳ� �Ǵ� �ټ��� ���������ο� ������ �߰��� �� �ִ�
@Sharable //ChannelHandler�� ���� ä�� ���� �����ϰ� ������ �� ������ ��Ÿ����.
public class EchoServerHandler extends ChannelInboundHandlerAdapter{
	@Override //�޽����� ���� ������ ȣ��ȴ�.
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf)msg;
		System.out.println("Server received : "+in.toString(CharsetUtil.UTF_8));
		ctx.write(in); //Ŭ���̾�Ʈ�κ��� ���� �޽����� �ٽ� Echo��Ų��.
		//�ƿ��ٿ�� �޽����� �÷������� ���� ä�� ���� �޽����� �߽��ڷ� ����Ѵ�.
		
		String ENCODING = "UTF-8";
		String response = "FIXED TEST RESPONSE DATA�� �����մϴ�.";
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(response.getBytes(ENCODING));
		
		byte[] byteArr = baos.toByteArray();
		
		ByteBuf out = Unpooled.directBuffer();
		out.writeBytes(byteArr);
		
		ctx.writeAndFlush(out); //ä�� Ȱ��ȭ �� ���� �޽��� ����
	}
	
	@Override //channelRead()�� ������ ȣ�⿡�� ���� �ϰ� ó���� ������ �޽����� ó�������� �ڵ鷯�� �뺸�Ѵ�.
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Bye");
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //������� �޽����� �÷����ϰ� ä���� ����
			.addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override //�б� �۾� �� ���ܰ� �߻��ϸ� ȣ��ȴ�.
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close(); //���ܰ� �߻����� ��, ���ܸ� ��� ä�� context�� �ݾƹ���
	}
}
