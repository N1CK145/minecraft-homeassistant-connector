package io.github.n1ck145.redhook.utils.webclient;

import io.github.n1ck145.redhook.dto.webhook.WebhookSettings;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class WebhookBlocks {
    private static WebhookBlocks instance;
    private ArrayList<Block> blocks;

    private WebhookBlocks() {
        this.blocks = new ArrayList<>();
    }

    public static WebhookBlocks getInstance(){
        if(instance == null)
            instance = new WebhookBlocks();

        return instance;
    }

    public ArrayList<Block> getBlocks(){
        return blocks;
    }

    public Block addBlock(org.bukkit.block.Block block, WebhookSettings webhook) {
        Block webBlock = new Block(block, webhook);

        this.blocks.add(webBlock);
        return webBlock;
    }

    public void removeBlock(org.bukkit.block.Block block){
        for(int i = 0; i < blocks.size(); i++){
            Block b = blocks.get(i);

            if(b.block == block){
                blocks.remove(b);
                break;
            }
        }
    }

    public void removeBlock(int posX, int posY, int posZ){
        for(int i = 0; i < blocks.size(); i++){
            Block b = blocks.get(i);
            Location loc = b.block.getLocation();

            if(loc.getBlockX() == posX && loc.getBlockY() == posY && loc.getBlockZ() == posZ){
                blocks.remove(b);
                break;
            }
        }
    }

    public Block getBlock(int posX, int posY, int posZ){
        for(int i = 0; i < blocks.size(); i++){
            Block b = blocks.get(i);
            Location loc = b.block.getLocation();

            if(loc.getBlockX() == posX && loc.getBlockY() == posY && loc.getBlockZ() == posZ){
                return b;
            }
        }
        return null;
    }

    public Block getBlock(org.bukkit.block.Block block){
        for(int i = 0; i < blocks.size(); i++){
            Block b = blocks.get(i);

            if(b.block == block){
                return b;
            }
        }
        return null;
    }

    public class Block {
        private org.bukkit.block.Block block;
        private WebhookSettings webhook;
        private Boolean triggeredOnPowerOn = true;
        private Boolean triggeredOnPowerOff = true;

        public Block(org.bukkit.block.Block block, WebhookSettings webhook){
            this.block = block;
            this.webhook = webhook;
        }

        public WebhookSettings getWebhook(){
            return webhook;
        }

        public org.bukkit.block.Block getBlock() {
            return block;
        }

        public Boolean triggerOnPowerOn() {
            return triggeredOnPowerOn;
        }

        public Boolean triggerOnPowerOff() {
            return triggeredOnPowerOff;
        }

        public void setTriggeredOnPowerOff(Boolean triggeredOnPowerOff) {
            this.triggeredOnPowerOff = triggeredOnPowerOff;
        }

        public void setTriggeredOnPowerOn(Boolean triggeredOnPowerOn) {
            this.triggeredOnPowerOn = triggeredOnPowerOn;
        }
    }
}
