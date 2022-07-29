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
 * @author ������
 * 
 * EchoŬ���̾�Ʈ step
 * 
 * 1.������ �����Ѵ�.
 * 2. �޽����� �ϳ� �̻� �����Ѵ�.
 * 3. �޽������� ����ϰ� �����κ��� ������ �޽����� �����Ѵ�.
 * 4. ������ �ݴ´�.
 *
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
	
	@Override //������ ���� ������ ��������� ȣ��ȴ�.
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		String ENCODING = "UTF-8";
		String content = "FIXED TEST DATA�� �����մϴ�.";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(content.getBytes(ENCODING));
		
		byte[] bs = baos.toByteArray();
		
		ByteBuf buf = Unpooled.directBuffer();
		buf.writeBytes(bs);

		
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Connect()\n", CharsetUtil.UTF_8)); //ä�� Ȱ��ȭ �� �޽��� ����
		ctx.writeAndFlush(buf); //ä�� Ȱ��ȭ �� �޽��� ����
	}
	
	@Override //�����κ��� �޽����� �����ϸ� ȣ��ȴ�.
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		System.out.println("Client receive : "+msg.toString(CharsetUtil.UTF_8)); //������ �޽��� �α�
	}
	
	@Override //ó�� �߿� ���ܰ� �߻��ϸ� ȣ��ȴ�.
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
