package com.itsallbinary.netty.chat.enhanced;

import java.io.File;
import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class EnhancedChatClient {

	static final String HOST = "127.0.0.1";
	static final int PORT = 8007;
	static String clientName;

	public static void main(String[] args) throws Exception {

		/*
		 * Get name of the user for this chat session.
		 */
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter your name: ");
		if (scanner.hasNext()) {
			clientName = scanner.nextLine();
			System.out.println("Welcome " + clientName);
		}

		/*
		 * Configure the client.
		 */

		// Since this is client, it doesn't need boss group. Create single group.
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group) // Set EventLoopGroup to handle all eventsf for client.
					.channel(NioSocketChannel.class)// Use NIO to accept new connections.
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							/*
							 * Socket/channel communication happens in byte streams. String decoder &
							 * encoder helps conversion between bytes & String.
							 */
							p.addLast(new StringDecoder());
							p.addLast(new StringEncoder());

							/*
							 * Add FileMessage codec. If message has format file:<file-name>, then this
							 * codec will read file & send content of file to other clients.
							 */
							p.addLast(new FileMessageCodec());

							// This is our custom client handler which will have logic for chat.
							p.addLast(new EnhancedChatClientHandler());

						}
					});

			// Start the client.
			ChannelFuture f = b.connect(HOST, PORT).sync();

			/*
			 * Iterate & take chat message inputs from user & then send to server.
			 */
			while (scanner.hasNext()) {
				String input = scanner.nextLine();
				Channel channel = f.sync().channel();

				if (input.startsWith("file:")) {
					channel.writeAndFlush(new FileMessage(input, clientName));
				} else {
					channel.writeAndFlush("[" + clientName + "]: " + input);
				}
				channel.flush();
			}

			// Wait until the connection is closed.
			f.channel().closeFuture().sync();
		} finally {
			// Shut down the event loop to terminate all threads.
			group.shutdownGracefully();
		}
	}
}
