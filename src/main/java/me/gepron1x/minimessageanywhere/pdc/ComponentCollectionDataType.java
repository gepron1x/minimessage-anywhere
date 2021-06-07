package me.gepron1x.minimessageanywhere.pdc;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Supplier;

public class ComponentCollectionDataType<C extends Collection<Component>> implements PersistentDataType<String, C> {
    @SuppressWarnings("unchecked")
    private final Class<C> complex = (Class<C>) new TypeToken<C>(){}.getRawType();
    private final Supplier<C> collectionFactory;
    private final JsonParser jsonParser = new JsonParser();
    private final GsonComponentSerializer gson = GsonComponentSerializer.gson();

    public ComponentCollectionDataType(Supplier<C> collectionFactory) {
        this.collectionFactory = collectionFactory;
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<C> getComplexType() {
        return complex;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull C complex, @NotNull PersistentDataAdapterContext context) {
        final JsonArray array = new JsonArray();
        complex.stream().map(gson::serializeToTree).forEach(array::add);
        return array.toString();
    }

    @Override
    public @NotNull C fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        final C collection = collectionFactory.get();
        jsonParser.parse(primitive).getAsJsonArray()
                .forEach(element -> collection.add(gson.deserializeFromTree(element)));
        return collection;
    }


}
