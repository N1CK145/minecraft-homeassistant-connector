package io.github.n1ck145.redhook.utils;

import org.bukkit.entity.Player;

import io.github.n1ck145.redhook.RedhookPlugin;

public class ResponseMessage {
	private final String message;
	private final boolean success;

	public ResponseMessage(String message, boolean success) {
		this.message = message;
		this.success = success;
	}

	public void send(Player player) {
		player.sendMessage(RedhookPlugin.getPrefix() + message);
	}

	public static ResponseMessage of(String message, boolean success) {
		return new ResponseMessage(message, success);
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}
}
