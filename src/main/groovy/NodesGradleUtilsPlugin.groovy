import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.dsl.ProductFlavor
import org.gradle.api.Plugin
import org.gradle.api.Project

class NodesGradleUtilsPlugin implements Plugin<Project> {
    private static final String K_API_URL = "apiUrl";
    private static final String K_NSTACK_KEY = "nstackKey";
    private static final String K_NSTACK_API = "nstackApi";
    private static final String K_CUSTOM_K1 = "customKey1";
    private static final String K_CUSTOM_K2 = "customKey2";
    private static final String K_CUSTOM_K3 = "customKey3";
    private static final String K_CUSTOM_K4 = "customKey4";
    private static final String K_CUSTOM_K5 = "customKey5";
    private static final String K_CUSTOM_K6 = "customKey6";
    private static final String K_CUSTOM_K7 = "customKey7";
    private static final String K_CUSTOM_K8 = "customKey8";
    private static final String K_CUSTOM_K9 = "customKey9";

    private boolean genMPs;
    private boolean genBCFs;
    private boolean genResVals;

    @Override
    void apply(Project project) {
        def hasApp = project.plugins.withType(AppPlugin)
        def hasLib = project.plugins.withType(LibraryPlugin)

        if (!hasApp && !hasLib) {
            throw new IllegalStateException("NODES GRADLE UTILS PLUGIN: 'android' or 'android-library' plugin required.")
        }

        project.extensions.create("nodesGradle", NodesGradleExtension)


        project.android.defaultConfig.ext.set(K_API_URL, null)
        project.android.defaultConfig.ext.set(K_NSTACK_KEY, null)
        project.android.defaultConfig.ext.set(K_NSTACK_API, null)

        project.android.defaultConfig.ext.set(K_CUSTOM_K1, null)
        project.android.defaultConfig.ext.set(K_CUSTOM_K2, null)
        project.android.defaultConfig.ext.set(K_CUSTOM_K3, null)
        project.android.defaultConfig.ext.set(K_CUSTOM_K4, null)
        project.android.defaultConfig.ext.set(K_CUSTOM_K5, null)
        project.android.defaultConfig.ext.set(K_CUSTOM_K6, null)
        project.android.defaultConfig.ext.set(K_CUSTOM_K7, null)
        project.android.defaultConfig.ext.set(K_CUSTOM_K8, null)
        project.android.defaultConfig.ext.set(K_CUSTOM_K9, null)

        //Go through each flavor and add extensions
        project.android.productFlavors.all {
            flavor ->
                flavor.ext.set("apiUrl", [:])
                flavor.ext.set("nstackKey", [:])
                flavor.ext.set("nstackApi", [:])

                flavor.ext.set(K_CUSTOM_K1, [:])
                flavor.ext.set(K_CUSTOM_K2, [:])
                flavor.ext.set(K_CUSTOM_K3, [:])
                flavor.ext.set(K_CUSTOM_K4, [:])
                flavor.ext.set(K_CUSTOM_K5, [:])
                flavor.ext.set(K_CUSTOM_K6, [:])
                flavor.ext.set(K_CUSTOM_K7, [:])
                flavor.ext.set(K_CUSTOM_K8, [:])
                flavor.ext.set(K_CUSTOM_K9, [:])
        }

        project.afterEvaluate {
            genMPs = project.nodesGradle.generateManifestPlaceholders
            genBCFs = project.nodesGradle.generateBuildConfigFields
            genResVals = project.nodesGradle.generateResValues

            project.android.applicationVariants.all { variant ->

                if (genBCFs) generateBuildConfigFromField(variant, "apiUrl", "API_URL", project.android.defaultConfig)
                if (genResVals) generateResValFromField(variant, "apiUrl", "api_url", project.android.defaultConfig)
                if (genMPs) generateManifestPlaceholderFromField(variant, "apiUrl", "apiUrl", project.android.defaultConfig)

                generateNstackField(variant, project.android.defaultConfig)

                for (int i = 1; i <= 9; i++) {
                    String fieldNameKey = "customKey" + i + "FieldName"
                    String key = "customKey" + i
                    if (project.nodesGradle[fieldNameKey] != null) {
                        String fieldName = project.nodesGradle[fieldNameKey].replace(" ", "_")

                        if (genBCFs) generateBuildConfigFromField(variant, key, fieldName.toUpperCase(), project.android.defaultConfig)
                        if (genResVals) generateResValFromField(variant, key, fieldName.toLowerCase(), project.android.defaultConfig)
                        if (genMPs) generateManifestPlaceholderFromField(variant, key, fieldName, project.android.defaultConfig)

                    }
                }
            }
        }
    }

    private void generateNstackField(ApplicationVariant variant, ProductFlavor defautlConfig) {
        String nstackKey = getValueForField(variant, "nstackKey", defautlConfig);
        String nstackApi = getValueForField(variant, "nstackApi", defautlConfig);

        if (nstackApi != null && nstackKey != null) {
            if (genBCFs) {
                variant.buildConfigField "String", "NSTACK_KEY", "\"" + nstackKey + "\"";
                variant.buildConfigField "String", "NSTACK_API", "\"" + nstackApi + "\"";
            }

            if (genResVals) {
                variant.resValue "string", "nstack_key", nstackKey;
                variant.resValue "string", "nstack_api", nstackApi;
            }

            if (genMPs) {
                variant.getMergedFlavor().getManifestPlaceholders().put("NSTACK_KEY", value);
                variant.getMergedFlavor().getManifestPlaceholders().put("NSTACK_API", value);
            }
        }
    }

    static String getValueForField(ApplicationVariant variant, String fieldName) {
        return getValueForField(variant, fieldName, null);
    }

    static String getValueForField(ApplicationVariant variant, String fieldName, ProductFlavor defaultConfig) {
        String value = null;

        if (defaultConfig != null && defaultConfig.ext.has(fieldName)) {
            value = defaultConfig.ext[fieldName]
        }

        if (!variant.productFlavors || variant.productFlavors.size() == 0)
            return value;



        if (variant.productFlavors.size() == 2) {
            //apps with dimensions like riide
            if (variant.productFlavors[0].ext[fieldName] instanceof Map && variant.productFlavors[0].ext[fieldName][variant.productFlavors[1].name] != null) {
                //When environment keys specified
                value = variant.productFlavors[0].ext.apiUrl[variant.productFlavors[1].name];
            } else if (variant.productFlavors[1].ext[fieldName] instanceof Map && variant.productFlavors[1].ext[fieldName][variant.productFlavors[0].name] != null) {
                //When environment keys specified
                value = variant.productFlavors[1].ext[fieldName][variant.productFlavors[0].name];
            } else if (variant.productFlavors[0].ext[fieldName] instanceof String) {
                //When only an unique key has been specified
                value = variant.productFlavors[0].ext[fieldName];
            } else if (variant.productFlavors[1].ext[fieldName] instanceof String) {
                //When only an unique key has been specified
                value = variant.productFlavors[1].ext[fieldName];
            }
        } else if (variant.productFlavors[0].ext[fieldName] instanceof String) {
            //normal apps
            value = variant.productFlavors[0].ext[fieldName];
        }

        return value;
    }

    static void generateBuildConfigFromField(ApplicationVariant variant, String fieldName, String buildConfigFieldName, ProductFlavor defaultConfig) {
        String value = getValueForField(variant, fieldName, defaultConfig);

        if (value != null)
            variant.buildConfigField "String", buildConfigFieldName, "\"" + value + "\"";

    }

    static void generateBuildConfigFromField(ApplicationVariant variant, String fieldName, String buildConfigFieldName) {
        generateBuildConfigFromField(variant, fieldName, buildConfigFieldName, null);
    }

    static void generateResValFromField(ApplicationVariant variant, String fieldName, String resValName, ProductFlavor defaultConfig) {
        String value = getValueForField(variant, fieldName, defaultConfig);

        if (value != null)
            variant.resValue "string", resValName, value;
    }

    static void generateResValFromField(ApplicationVariant variant, String fieldName, String resValName) {
        generateResValFromField(variant, fieldName, resValName, null);
    }

    static void generateManifestPlaceholderFromField(ApplicationVariant variant, String fieldName, String manifestKey) {
        generateManifestPlaceholderFromField(variant, fieldName, manifestKey, null);
    }

    static void generateManifestPlaceholderFromField(ApplicationVariant variant, String fieldName, String manifestKey, ProductFlavor defaultConfig) {
        String value = getValueForField(variant, fieldName, defaultConfig);

        if (value != null)
            variant.getMergedFlavor().getManifestPlaceholders().put(manifestKey, value);

    }
}
