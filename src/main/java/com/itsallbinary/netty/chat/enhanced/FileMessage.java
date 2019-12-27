package com.itsallbinary.netty.chat.enhanced;

public class FileMessage {

	// Command format = file:<file-name>
	private String fileCommand;

	private String clientName;

	public FileMessage(String fileCommand, String clientName) {
		this.fileCommand = fileCommand;
		this.clientName = clientName;
	}

	String getClientName() {
		return clientName;
	}

	String getFileCommand() {
		return fileCommand;
	}

}
