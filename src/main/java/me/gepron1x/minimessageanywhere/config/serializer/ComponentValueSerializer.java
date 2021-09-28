package me.gepron1x.minimessageanywhere.config.serializer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

public class ComponentValueSerializer implements ValueSerialiser<Component> {
    private final ComponentSerializer<Component, ? extends Component, String> componentSerializer;

    public ComponentValueSerializer(ComponentSerializer<Component, ? extends Component, String> componentSerializer) {
        this.componentSerializer = componentSerializer;
    }

    @Override
    public Class<Component> getTargetClass() {
        return Component.class;
    }

    @Override
    public Component deserialise(FlexibleType flexibleType) throws BadValueException {
        return componentSerializer.deserialize(flexibleType.getString());
    }

    @Override
    public Object serialise(Component value, Decomposer decomposer) {
        return componentSerializer.serialize(value);
    }
}
