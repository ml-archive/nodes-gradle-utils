import java.util.HashMap;
import java.util.Map;

/**
 * Created by enrique on 03/08/2017.
 */

class NodesGradleExtension {
    String customKey1FieldName = "CUSTOM_KEY_1";
    String customKey2FieldName = "CUSTOM_KEY_2";
    String customKey3FieldName = "CUSTOM_KEY_3";
    String customKey4FieldName = "CUSTOM_KEY_4";
    String customKey5FieldName = "CUSTOM_KEY_5";
    String customKey6FieldName = "CUSTOM_KEY_6";
    String customKey7FieldName = "CUSTOM_KEY_7";
    String customKey8FieldName = "CUSTOM_KEY_8";
    String customKey9FieldName = "CUSTOM_KEY_9";

    boolean generateManifestPlaceholders = true;
    boolean generateBuildConfigFields = true;
    boolean generateResValues = true;


    public NodesGradleExtension() {
    }

    public String getCustomKey1FieldName() {
        return customKey1FieldName;
    }

    public void setCustomKey1FieldName(String customKey1FieldName) {
        this.customKey1FieldName = customKey1FieldName;
    }

    public String getCustomKey2FieldName() {
        return customKey2FieldName;
    }

    public void setCustomKey2FieldName(String customKey2FieldName) {
        this.customKey2FieldName = customKey2FieldName;
    }

    public String getCustomKey3FieldName() {
        return customKey3FieldName;
    }

    public void setCustomKey3FieldName(String customKey3FieldName) {
        this.customKey3FieldName = customKey3FieldName;
    }

    public String getCustomKey4FieldName() {
        return customKey4FieldName;
    }

    public void setCustomKey4FieldName(String customKey4FieldName) {
        this.customKey4FieldName = customKey4FieldName;
    }


    public String getCustomKey5FieldName() {
        return customKey5FieldName;
    }

    public void setCustomKey5FieldName(String customKey5FieldName) {
        this.customKey5FieldName = customKey5FieldName;
    }

    public String getCustomKey6FieldName() {
        return customKey6FieldName;
    }

    public void setCustomKey6FieldName(String customKey6FieldName) {
        this.customKey6FieldName = customKey6FieldName;
    }

    public String getCustomKey7FieldName() {
        return customKey7FieldName;
    }

    public void setCustomKey7FieldName(String customKey7FieldName) {
        this.customKey7FieldName = customKey7FieldName;
    }

    public String getCustomKey8FieldName() {
        return customKey8FieldName;
    }

    public void setCustomKey8FieldName(String customKey8FieldName) {
        this.customKey8FieldName = customKey8FieldName;
    }

    public String getCustomKey9FieldName() {
        return customKey9FieldName;
    }

    public void setCustomKey9FieldName(String customKey9FieldName) {
        this.customKey9FieldName = customKey9FieldName;
    }

    public boolean isGenerateManifestPlaceholders() {
        return generateManifestPlaceholders;
    }

    public void setGenerateManifestPlaceholders(boolean generateManifestPlaceholders) {
        this.generateManifestPlaceholders = generateManifestPlaceholders;
    }

    public boolean isGenerateBuildConfigFields() {
        return generateBuildConfigFields;
    }

    public void setGenerateBuildConfigFields(boolean generateBuildConfigFields) {
        this.generateBuildConfigFields = generateBuildConfigFields;
    }

    public boolean isGenerateResValues() {
        return generateResValues;
    }

    public void setGenerateResValues(boolean generateResValues) {
        this.generateResValues = generateResValues;
    }
}
