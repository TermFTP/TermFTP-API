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
import java.util.Arrays;
import java.util.Optional;

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

    public static String getCompleteJsonFile(Path path, Path schemaPath) throws IOException, ValidationException {
        if(schemaPath != null){
            if(!validate(path, schemaPath)){
                return null;
            }
        }

        JsonMapper mapper = new JsonMapper();
        return mapper.readTree(path.toFile()).toPrettyString();
    }

    public static String getAllProfiles(Path path) throws IOException {
        JsonMapper mapper = new JsonMapper();
        JsonNode node = mapper.readTree(path.toFile());
        JsonNode profiles = node.get("profiles");
        return profiles.toPrettyString();
    }

    /**
     * this method can get any property over a given property path
     * @param path ... path to the json file
     * @param propertyPath ... list of properties separated by '.' eg: "profiles.0.profileId"
     * @param isObject ... whether the method should return a json-tree or just a simple string
     * @param schema ... if given (not null), the json file will be validated
     * @return either the json-tree for the given property or a simple string
     * @throws IOException
     * @throws ValidationException
     */
    public static Object getProperty(Path path, String propertyPath, boolean isObject, Path schema) throws IOException, ValidationException {
        if(schema != null){
            if(!validate(path, schema)){
                return null;
            }
        }

        JsonMapper mapper = new JsonMapper();
        JsonNode node = mapper.readTree(path.toFile());

        String[] properties = propertyPath.split("\\.");

        for(String p : properties){
            if(p.matches("^[0-9]+$")){
                int i = Integer.parseInt(p);
                if(node.has(i)){
                    node = node.get(i);
                }
            }else{
                if(node.has(p)){
                    node = node.get(p);
                }else{
                    return null;
                }
            }

        }

        return isObject ? node : node.asText();
    }




}
