package me.gepron1x.minimessageanywhere.pdc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.apache.commons.lang.SerializationException;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ComponentListDataType<C extends Collection<Component>> implements PersistentDataType<String, List<Component>> {


    @SuppressWarnings("unchecked")
    private final Class<List<Component>> complex = (Class<List<Component>>) Collections.<Component>emptyList().getClass();
    private final JsonParser jsonParser = new JsonParser();
    private final GsonComponentSerializer gson = GsonComponentSerializer.gson();

    public ComponentListDataType() {
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<List<Component>> getComplexType() {
        return complex;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull List<Component> complex, @NotNull PersistentDataAdapterContext context) {
        JsonArray array = new JsonArray();
        for(Component component : complex) array.add(gson.serializeToTree(component));
        return array.toString();
    }

    @Override
    public @NotNull List<Component> fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        JsonArray array = jsonParser.parse(primitive).getAsJsonArray();
        ArrayList<Component> components = new ArrayList<>(array.size());
        for(JsonElement element : array) components.add(gson.deserializeFromTree(element));
        return components;
    }


}
