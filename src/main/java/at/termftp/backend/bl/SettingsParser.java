package at.termftp.backend.bl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingsParser {
    public static boolean validate(Path jsonPath, Path schemaPath) throws IOException, ValidationException {
        if(!jsonPath.toFile().isFile()){
            return false;
        }
        if(!schemaPath.toFile().isFile()){
            return false;
        }

        JsonMapper mapper = new JsonMapper();

        String jsonStringFile = mapper.readTree(jsonPath.toFile()).toPrettyString();
        String jsonStringSchema = mapper.readTree(schemaPath.toFile()).toPrettyString();


        JSONObject jsonSchema = new JSONObject(new JSONTokener(jsonStringSchema));
        JSONObject jsonSubject = new JSONObject(new JSONTokener(jsonStringFile));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
        return true;
    }




}
