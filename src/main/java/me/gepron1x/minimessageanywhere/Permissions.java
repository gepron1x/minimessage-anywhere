package me.gepron1x.minimessageanywhere;

import org.bukkit.permissions.Permission;

public final class Permissions {
    private static final String PREFIX = "mmanywhere";
    private static final char SEPARATOR = '.';
    private static String permission(String... parts) {

        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX).append(SEPARATOR);
        int lastIndex = parts.length - 1;
        for(int i = 0; i < lastIndex; i++) sb.append(parts[i]).append(SEPARATOR);
        sb.append(parts[lastIndex]);
        return sb.toString();
    }

    public static final String IGNORE_FILTER = permission("filter", "ignore");
    public static final String INFO = permission("info");
    public static final String RELOAD = permission("reload");
}
