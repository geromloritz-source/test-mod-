package com.supergems.item;

import org.joml.Vector3f;

/** Die sieben Diamant-Farben. */
public enum GemColor {
    RED("red", 0xE53935),
    BLUE("blue", 0x1E88E5),
    GREEN("green", 0x43A047),
    YELLOW("yellow", 0xFDD835),
    PURPLE("purple", 0x8E24AA),
    CYAN("cyan", 0x00ACC1),
    WHITE("white", 0xF5F5F5);

    private final String id;
    private final int rgb;

    GemColor(String id, int rgb) {
        this.id = id;
        this.rgb = rgb;
    }

    public String getId() {
        return id;
    }

    public int getRgb() {
        return rgb;
    }

    /** Registry-Name des Items, z. B. "red_diamond". */
    public String itemName() {
        return id + "_diamond";
    }

    /** Farbe als 0..1-Vektor (für Partikel). */
    public Vector3f toVector() {
        return new Vector3f(((rgb >> 16) & 0xFF) / 255f, ((rgb >> 8) & 0xFF) / 255f, (rgb & 0xFF) / 255f);
    }
}
