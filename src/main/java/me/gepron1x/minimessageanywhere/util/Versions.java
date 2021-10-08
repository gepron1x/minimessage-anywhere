package me.gepron1x.minimessageanywhere.util;


import com.comphenix.protocol.utility.MinecraftProtocolVersion;
import com.comphenix.protocol.utility.MinecraftVersion;

public final class Versions {
    private static final int CAVES_AND_CLIFFS_FIRST_PART = MinecraftProtocolVersion.getVersion(MinecraftVersion.CAVES_CLIFFS_1);

    private Versions() {
        throw new UnsupportedOperationException();
    }

    public static boolean isCavesAndCliffs() {
        return MinecraftProtocolVersion.getCurrentVersion() >= CAVES_AND_CLIFFS_FIRST_PART;
    }
}
