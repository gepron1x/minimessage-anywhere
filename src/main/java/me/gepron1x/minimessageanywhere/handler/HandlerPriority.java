package me.gepron1x.minimessageanywhere.handler;

/**
 * Handler priority for GlobalComponentHandler
 * handlers with lower priorities will be executed first.
 */
public enum HandlerPriority {
    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4);

    private final int priority;

    HandlerPriority(int priority) {

        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

}
