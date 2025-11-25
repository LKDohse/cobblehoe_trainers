package net.electricbudgie.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum NPCVariant {
    LASS(0),
    YOUNGSTER(1),
    HIKER(2);

    private final int id;
    private static final NPCVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(NPCVariant::getId)).toArray(NPCVariant[]::new);

    NPCVariant(int id) {this.id = id;}

    public int getId(){return this.id;}

    public static NPCVariant byId(int id) {return BY_ID[id % BY_ID.length];}
}
