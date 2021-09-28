package me.gepron1x.minimessageanywhere.config.serializer;

import com.comphenix.protocol.PacketType;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;


public class PacketTypeSerializer implements ValueSerialiser<PacketType> {
    @Override
    public Class<PacketType> getTargetClass() {
        return PacketType.class;
    }

    @Override
    public PacketType deserialise(FlexibleType flexibleType) throws BadValueException {
        String name = flexibleType.getString();
        for(PacketType type : PacketType.Play.Server.getInstance()) {
            if(type.name().equalsIgnoreCase(name)) return type;
        }
        throw flexibleType.badValueExceptionBuilder().message("no packet with name " + name + "found!").build();

    }

    @Override
    public Object serialise(PacketType value, Decomposer decomposer) {
        return value.name();
    }
}
