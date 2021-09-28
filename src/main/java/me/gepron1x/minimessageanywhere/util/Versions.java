package me.gepron1x.minimessageanywhere.util;

import com.comphenix.protocol.utility.MinecraftProtocolVersion;
import com.comphenix.protocol.utility.MinecraftVersion;

public final class Versions {
    private Versions() {
        throw new UnsupportedOperationException("sex");
    }
    public static final int VERSION = MinecraftProtocolVersion.getCurrentVersion();
    public static final int V1_17 = MinecraftProtocolVersion.getVersion(new MinecraftVersion("1.17"));
    public static boolean isCavesAndCliffs() {
        return VERSION >= V1_17;
    }

}
