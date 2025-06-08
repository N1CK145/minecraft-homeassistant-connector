package io.github.n1ck145.redhook.utils;

public enum StateColor {
	ON("§2"), OFF("§4"), BOTH("§9");

	private final String colorCode;

	StateColor(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorCode() {
		return colorCode;
	}
}