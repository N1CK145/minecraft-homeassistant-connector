package io.github.n1ck145.redhook.redstoneactions;

public class RedstoneActionInstance {

    private final RedstoneAction action;
    private final TriggerCondition triggerCondition;

    public RedstoneActionInstance(RedstoneAction action, TriggerCondition triggerCondition){
        this.action = action;
        this.triggerCondition = triggerCondition;
    }

    public RedstoneAction getAction() {
        return action;
    }

    public TriggerCondition getTriggerCondition() {
        return triggerCondition;
    }
}
