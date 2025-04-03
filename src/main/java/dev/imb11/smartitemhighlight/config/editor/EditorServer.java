package dev.imb11.smartitemhighlight.config.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.config.HighlightConditionManager;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Objects;

import static spark.Spark.*;

public class EditorServer {
    public static final int PORT = 10239;
    private static final String HTML_DATA;
    private static final String SCHEMA_DATA;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static {
        String data;
        try (InputStream in = SmartItemHighlight.class.getResourceAsStream("/assets/smartitemhighlight/web/index.html")) {
            data = new String(Objects.requireNonNull(in).readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception error) {
            data = "Failed to load index.html from SmartItemHighlight's jar! The editor is therefore unavailable...\n\n" +
                    error;
            SmartItemHighlight.LOGGER.error(data);
        }

        String schemaData;
        try (InputStream in = SmartItemHighlight.class.getResourceAsStream("/assets/smartitemhighlight/web/schema.json")) {
            schemaData = new String(Objects.requireNonNull(in).readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception error) {
            schemaData = "Failed to load the schema from SmartItemHighlight's jar! The editor is therefore unavailable...\n\n" +
                    error;
            SmartItemHighlight.LOGGER.error(data);
        }

        HTML_DATA = data;
        SCHEMA_DATA = schemaData;
    }

    public record UpdateFileResponse(boolean success, @Nullable String errorMessage) {}

    public static void setup() {
        port(PORT);
        get("/", (request, response) -> {
            response.type("text/html; charset=utf-8");
            String id = request.queryParams("id");
            try {
                return HTML_DATA.replace("%%data_here%%", HighlightConditionManager.getRawFile(id));
            } catch (Exception e) {
                return "Failed to load editor: \n\n" + e;
            }
        });

        get("/schema", (request, response) -> {
            response.type("application/json");
            return SCHEMA_DATA;
        });

        post("/update_file", (request, response) -> {
            response.type("application/json");
            String id = request.queryParams("id");
            String content = request.body();

            try {
                var element = GSON.fromJson(content, JsonElement.class);
                HighlightCondition condition = HighlightCondition.CODEC.parse(JsonOps.INSTANCE, element).getOrThrow();
                condition.setFileID(id);
                if (element.getAsJsonObject().has("renderOptions")) {
                    condition.setRenderOptions(element.getAsJsonObject().getAsJsonObject("renderOptions"));
                }
                HighlightConditionManager.save(condition);
            } catch (Exception e) {
                return new UpdateFileResponse(false, e.toString());
            }

            return GSON.toJson(new UpdateFileResponse(true, null));
        });
    }
}
