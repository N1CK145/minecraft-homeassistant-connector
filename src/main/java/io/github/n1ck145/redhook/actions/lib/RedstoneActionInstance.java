package io.github.n1ck145.redhook.actions.lib;

public class RedstoneActionInstance {

	private final RedstoneAction action;
	private final TriggerCondition triggerCondition;

	public RedstoneActionInstance(RedstoneAction action, TriggerCondition triggerCondition) {
		this.action = action;
		this.triggerCondition = triggerCondition;
	}

	public RedstoneAction getAction() {
		return action;
	}

	public TriggerCondition getTriggerCondition() {
		return triggerCondition;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RedstoneActionInstance) {
			RedstoneActionInstance other = (RedstoneActionInstance) obj;
			return action.equals(other.action) && triggerCondition.equals(other.triggerCondition);
		}
		return false;
	}
}
