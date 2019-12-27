package com.itsallbinary.netty.chat.enhanced;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class FileMessageCodec extends ByteToMessageCodec<FileMessage> {

	/*
	 * In case of message of type FileMessage, read file & create a String message
	 * with the text inside file. Write final string to buffer.
	 * 
	 * File command format file:<file-name>
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, FileMessage msg, ByteBuf out) throws Exception {

		File file = new File(msg.getFileCommand().replace("file:", ""));
		String fileData = Files.readString(file.toPath());
		String message = "[" + msg.getClientName() + "]: \n-------------------" + file.getName()
				+ "-------------------\n" + fileData + "\n-------------------End Of File-------------------\n";
		out.writeCharSequence(message, Charset.defaultCharset());

	}

	/*
	 * For decoding convert message to String.
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		out.add(msg.toString(Charset.defaultCharset()));
	}

}
