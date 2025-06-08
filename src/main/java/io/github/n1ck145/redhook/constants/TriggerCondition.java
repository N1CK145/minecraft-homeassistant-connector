package io.github.n1ck145.redhook.constants;

public enum TriggerCondition {
	ON("§2"), OFF("§4"), BOTH("§9");

	public boolean matches(boolean isRedstoneNowOn) {
		return switch (this) {
			case ON -> isRedstoneNowOn;
			case OFF -> !isRedstoneNowOn;
			case BOTH -> true;
		};
	}


	private final String colorCode;

	TriggerCondition(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorCode() {
		return colorCode;
	}
}
