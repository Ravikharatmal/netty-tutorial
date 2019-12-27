package com.itsallbinary.netty.chat.enhanced;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EnhancedChatClientHandler extends SimpleChannelInboundHandler<String> {

	/*
	 * Print chat message received from server.
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println("Message: " + msg);

	}

}
