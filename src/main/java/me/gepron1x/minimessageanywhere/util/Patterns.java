package me.gepron1x.minimessageanywhere.util;

import com.google.common.collect.ImmutableSet;

public final class Patterns {
    public static final ImmutableSet<Character> METACHARACTERS =
            ImmutableSet.of('.', '(', '[', '|', '{', '*', '+', '?', '^', '$', '/', '-');
    private static final char BACKSLASH = '\\';
    private Patterns() {
        throw new UnsupportedOperationException("sex");
    }
    public static String adaptUserInput(String input) {
        StringBuilder sb = new StringBuilder();
        for(char c : input.toCharArray()) {
            if(METACHARACTERS.contains(c)) {
                sb.append(BACKSLASH);
            }
            sb.append(c);
        }
        return sb.toString();
    }

}
