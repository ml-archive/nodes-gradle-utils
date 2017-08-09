# Nodes Gradle Utils Plugin for Android
Android Gradle Utils plugin generates BuildConfig, resValues and ManifestPlaceholders for some predefined keys like `apiUrl`, but also gives you the choice of using up to 9 custom keys.

Those keys can be defined in the `defaultConfig` block as well as in the `prodcutFlavors` block for each flavour. 

## Usage

The use is simple, you just type as many of the predefined key as you want and it would generate BuildConfig fiedls, resValues and ManifestPlaceholders for you evaluating each variant. So if a key hasn't been defined, it won't be generated.


```groovy
android {
//...

    defaultConfig {
        apiUrl = "http://defaultUrl.com"
        customKey1 = "myCustomKey"
    }
    productFlavors {
        development {
            customKey1 = "myCustomKeyForDevelopment" 
            customKey2 = "myOtherCustomKeyForDevelopment"
        }
        staging {
            apiUrl = "http://staging.com"
        }
        production {
            apiUrl = "http://production.com"
        }
    }
}
```

Notes
____

**BuildConfig** fields. BuildConfig class is a constants class generated for each build variant accessible from anywhere in your app. We only generate String constants.
**ResValues** are short for resource values. When you add a String to your string file you are doing the same. We only generate string resource values.
**ManifestPlaceholders** is a nice way of generating dynamic keys in the manifest. <https://developer.android.com/studio/build/manifest-build-variables.html>

|       key      |    BuildConfig field name   |          ResVal key          |    ManifestPlaceholder key   |                                       notes                                       |
|:--------------:|:---------------------------:|:----------------------------:|:----------------------------:|:---------------------------------------------------------------------------------:|
|     apiUrl     |           API_URL           |            api_url           |           ${apiUrl}          |                            no String resource generated                           |
|   nstackAPIKey |        NSTACK_API_KEY       |         nstack_api_key       |        ${NSTACK_API_KEY}     |                       requires nstackAPIKey to be generated                       |
|   nstackAppId  |        NSTACK_APP_ID        |         nstack_app_id        |        ${NSTACK_APP_ID}      |                       requires nstackAppId to be generated                        |
| customKey**X** | CUSTOM_KEY_**X** or custom* | custom_key_**x** or custom * |${CUSTOM_KEY_**X**}, or custom| **X** has to be a value from 1 - 9.  *You can also specify the a custom field/key |

If you want a specific name to be used for your custom keys, just add the following to your gradle file

```groovy
nodesGradle {
    customKey1FieldName = "my custom service key"
    customKey2FieldName = "google api"
}
```

This will change the name of the field in your BuildConfig class to the one specified for your custom key. It would make it upper case for BuildConfig, lower case for ResValues and in all cases it will replace the spaces for `_`. 

By default BuildConfig fields, string resource and ManifestPlaceholders will be generated, but if you want to opt out for any of those, it can be specified in the nodesGradle block:

```groovy
nodesGradle {
    generateBuildConfigFields = false
    generateManifestPlaceholders = false
    generateResValues = false
}
```



## Advanced use

There are situations where using the same codebase you want to have different customizations of your app as having different different environments to point at. Some people solve this by using a combination of buildTypes and productFlavors, but it makes more sense to use dimensions instead as build types shold only define how thed build is gonna be made (signed, debuggable, proguard). This is where the plugin comes very handy.
Let's say you are using a library that requires an specific key, and that key is gonna be different for just skins as well as for each environment on each skin. Then you can do something like the following:


```groovy
android {
//...

    flavorDimensions "skin", "environment"

    productFlavors {
        firstSkin {
            dimension = "skin"
            apiUrl = "http://firstSkinBaseUrl.com"
            customKey1.staging = "adsfasdfapd3432"
            customKey1.production = "adsfasdfapd3432"
        }
        secondSkin {
            dimension = "skin"
            apiUrl = "http://secondSkinBaseUrl.com"
            customKey1.staging = "45gwdagadsfasdfapd3432"
            customKey1.production = "436geradsfasdfapd3432"
        }
        thirdSkin {
            dimension = "skin"
            apiUrl = "http://thirdSkinBaseUrl.com"
            customKey1.staging = "alkbvdskjbdsvdsfasdfapd3432"
            customKey1.production = "iu4vdso43tadsfasdfapd3432"
        }

        development {
            dimension = "environment"
            apiUrl = "http://developmentUrl.com"
        }
        staging {
            dimension = "environment"
            apiUrl = "http://staginngUrl.com"
        }
        production {
            dimension = "environment"
            apiUrl = "http://productionUrl.com"
        }
    }
}
```

When creating the variants, this is gonna check if there is an specific key for that case or only just an unique one. 

 
 
## Instalation

Clone the project, and then from a terminal:

```
./gradlew installArchives
```

In the project where you want to use the plugin, in the root build.gradle make sure you use maven local then add the classpath to the project.
 
 ```groovy
buildscript {
    repositories {
        //...
        mavenLocal();
    }
    dependencies {
        //...
        classpath 'dk.nodes:gradle-utils:0.0.1-snapshot'
    }
}
 ```
 
 Then just apply the plugin in the build.gradle file that belongs to the app module of your project
 ```groovy
 apply plugin: 'dk.nodes.gradle-utils'
 ```



## Limitations

* It only let you have 9 custom keys. There is no particular reason for this, but we are still trying to figure out the best way for having custom keys with not much hassle from the consumer perspective. 

* When using dimensions, only the first two dimensions are used on each variant to determine whether or not there is a key that references the other flavour in the other dimensions.

* When using dimensions on each flavour, you either specify a single key like `customKey1 = "dsafasdjgt"` or by using `customKey1.staging = "213123hdsredsd"` but not both combined in the same flavour.
