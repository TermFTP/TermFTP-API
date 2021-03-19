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

public class SettingsParser {

    /**
     * is responsible for actual validation
     * @param json the jsonString to validate
     * @param schemaString the jsonString of the schema used for validating
     * @return whether validation was successful or not
     * @throws ValidationException thrown if the json file is not valid ACCORDING TO THE SCHEMA
     */
    private static boolean validate(String json, String schemaString) throws ValidationException{
        JSONObject jsonSchema = new JSONObject(new JSONTokener(schemaString));
        JSONObject jsonSubject = new JSONObject(new JSONTokener(json));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
        return true;
    }

    /**
     * used to validate a whole json FILE
     * @param jsonPath the path to the json file
     * @param schemaPath the path to the schema file
     * @return whether validation was successful or not
     * @throws IOException thrown if the json file cannot be parsed
     * @throws ValidationException thrown if the json file is not valid ACCORDING TO THE SCHEMA
     */
    public static boolean validationWrapper(Path jsonPath, Path schemaPath) throws IOException, ValidationException {
        if(!jsonPath.toFile().isFile()){
            return false;
        }
        if(!schemaPath.toFile().isFile()){
            return false;
        }

        JsonMapper mapper = new JsonMapper();

        String jsonStringFile = mapper.readTree(jsonPath.toFile()).toPrettyString();
        String jsonStringSchema = mapper.readTree(schemaPath.toFile()).toPrettyString();

        return validate(jsonStringFile, jsonStringSchema);
    }

    /**
     * used to get a complete json file
     * @param path the path to the json file
     * @param schemaPath the path to the schema file (if validation should be done)
     * @return the jsonString of the json file
     * @throws IOException thrown if the json file cannot be parsed
     * @throws ValidationException thrown if the json file is not valid ACCORDING TO THE SCHEMA
     */
    public static String getCompleteJsonFile(Path path, Path schemaPath) throws IOException, ValidationException {
        if(schemaPath != null){
            if(!validationWrapper(path, schemaPath)){
                return null;
            }
        }

        JsonMapper mapper = new JsonMapper();
        return mapper.readTree(path.toFile()).toPrettyString();
    }

    /**
     * used to save the settings file (json-file)
     * @param path the path to the json file
     * @param schemaPath the path to the schema file
     * @param jsonString the jsonString that should be written into the file
     * @return whether writing (to the file) was successful or not
     * @throws IOException thrown if the json file cannot be parsed
     * @throws ValidationException thrown if the json file is not valid ACCORDING TO THE SCHEMA
     */
    public static boolean saveSettings(Path path, Path schemaPath, String jsonString) throws IOException, ValidationException {
        JsonMapper mapper = new JsonMapper();
        boolean valid = validate(jsonString, mapper.readTree(schemaPath.toFile()).toPrettyString());

        if(valid){
            mapper.writeValue(path.toFile(), mapper.readTree(jsonString).toPrettyString()); // write in pretty format
            System.out.println(">> Settings file successfully written.");
        }
        return valid;
    }






}
