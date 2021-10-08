package me.gepron1x.minimessageanywhere.config.serializer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketTypeEnum;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;


public class PacketTypeSerializer implements ValueSerialiser<PacketType> {
    private final PacketTypeEnum packetTypes;

    public PacketTypeSerializer(PacketTypeEnum packetTypes) {

        this.packetTypes = packetTypes;
    }

    @Override
    public Class<PacketType> getTargetClass() {
        return PacketType.class;
    }

    @Override
    public PacketType deserialise(FlexibleType flexibleType) throws BadValueException {
        String name = flexibleType.getString();
        for (PacketType type : packetTypes) {
            if (type.name().equalsIgnoreCase(name)) return type;
        }
        throw flexibleType.badValueExceptionBuilder().message("no packet with name " + name + "found!").build();

    }

    @Override
    public Object serialise(PacketType value, Decomposer decomposer) {
        return value.name();
    }
}
