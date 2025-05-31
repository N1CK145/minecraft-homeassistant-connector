package io.github.n1ck145.redhook.utils;

import org.bukkit.entity.Player;

public class ResponseMessage {
    private final String message;
    private final boolean success;

    public ResponseMessage(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public void send(Player player){
        player.sendMessage(message);
    }
    
    public static ResponseMessage of(String message, boolean success){
        return new ResponseMessage(message, success);
    }

    public boolean isSuccess(){
        return success;
    }
}
