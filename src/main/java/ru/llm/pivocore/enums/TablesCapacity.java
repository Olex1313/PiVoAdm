package ru.llm.pivocore.enums;

public enum TablesCapacity {
    LARGE(8), SMALL(4);

    private final int value;

    TablesCapacity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
