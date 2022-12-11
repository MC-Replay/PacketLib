package mc.replay.packetlib.network.packet.identifier;

import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class PacketIdentifierLoader {

    private static final String FOLDER = "packetidentifiers";
    private static final String FILE_NAME = "packetidentifiers_%s.json";

    private static final String CLIENTBOUND_KEY = "CLIENTBOUND";
    private static final String SERVERBOUND_KEY = "SERVERBOUND";

    private final Map<ClientboundPacketIdentifier, Integer> clientboundIdentifiers = new HashMap<>();
    private final Map<ServerboundPacketIdentifier, Integer> serverboundIdentifiers = new HashMap<>();

    public PacketIdentifierLoader() {
        int protocol = ProtocolVersion.getServerVersion().getNumber();
        InputStream inputStream = this.getResource(protocol);

        this.readResource(inputStream);
    }

    public Map<ClientboundPacketIdentifier, Integer> getClientboundIdentifiers() {
        return this.clientboundIdentifiers;
    }

    public Map<ServerboundPacketIdentifier, Integer> getServerboundIdentifiers() {
        return this.serverboundIdentifiers;
    }

    private @NotNull InputStream getResource(int protocol) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(FOLDER + File.separator + FILE_NAME.formatted(protocol));
        if (inputStream == null) {
            throw new IllegalStateException("Couldn't read protocol packet identifiers");
        }

        return inputStream;
    }

    @SuppressWarnings("unchecked")
    private void readResource(@NotNull InputStream inputStream) {
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            JSONParser jsonParser = new JSONParser();
            JSONObject object = (JSONObject) jsonParser.parse(reader);

            JSONObject clientbound = (JSONObject) object.getOrDefault(CLIENTBOUND_KEY, null);
            JSONObject serverbound = (JSONObject) object.getOrDefault(SERVERBOUND_KEY, null);
            if (clientbound == null) throw new IOException("Clientbound key not found");
            if (serverbound == null) throw new IOException("Serverbound key not found");

            this.readBound(clientbound, ClientboundPacketIdentifier.class, this.clientboundIdentifiers::put);
            this.readBound(serverbound, ServerboundPacketIdentifier.class, this.serverboundIdentifiers::put);
        } catch (IOException | ParseException exception) {
            throw new RuntimeException(exception);
        }
    }

    private <E extends Enum<E>> void readBound(@NotNull JSONObject object, Class<E> clazz, BiConsumer<E, Integer> consumer) {
        for (Object objectKey : object.keySet()) {
            if (!(objectKey instanceof String key)) continue;

            try {
                Object value = object.get(key);
                if (!(value instanceof String packetIdString)) continue;

                E identifier = Enum.valueOf(clazz, key);
                int packetId = Byte.decode(packetIdString);

                consumer.accept(identifier, packetId);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}