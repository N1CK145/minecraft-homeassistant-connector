package io.github.n1ck145.redhook.redstoneactions.lib;

public enum TriggerCondition {
	ON, // Triggered when redstone turns ON
	OFF, // Triggered when redstone turns OFF
	BOTH; // Triggered on both transitions

	public boolean matches(boolean isRedstoneNowOn) {
		return switch (this) {
			case ON -> isRedstoneNowOn;
			case OFF -> !isRedstoneNowOn;
			case BOTH -> true;
		};
	}
}
