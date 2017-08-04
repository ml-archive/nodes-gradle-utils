# Nodes Gradle Utils Plugin
Android Gradle Utils plugin generates BuildConfig fields and/or resValues for some predefined keys like `apiUrl`, but you can also create up to 4 custom keys that will generate both BuildConfig fielad nand resValue if define.

Those keys can be defined in default config as well as product flavours. 

## Android Flavor Config


Simple projects
```groovy
android {
//...

    defaultConfig {
        apiUrl = "http://defaultUrl.com"

    }
    productFlavors {
        development {
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

This will generate a public static and final field in the BuildConfig class accessible from everywhere in your app. If the key is missing (not in flavour neither in defaultConfig) it wont be include. The predefined keys are:

|     key    |     BuildConfig field name     |         res string key         |                notes               |
|:----------:|:------------------------------:|:------------------------------:|:----------------------------------:|
|   apiUrl   |             API_URL            |                *               |    no String resource generated    |
|  nstackApi |           NSTACK_API           |           nstack_api           | requires nstackKey to be generated |
|  nstackKey |           NSTACK_KEY           |           nstack_key           | requires nstackApi to be generated |
| customKeyX | CUSTOM_KEY_X, or specified one | custom_key_x, or specified one |      X is a value from 1 - 4.      |

If you want a specific name to be used for your custom keys, just add the following to your gradle file

```groovy
nodesGradle {
    customKey1FieldName = "my custom service key"
}
```

This will change the name of the field in your BuildConfig class to the one specified for your custom key.

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

* It only let you have 4 custom keys. There is no particular reason for this, but we are still trying to figure out the best way for having custom keys with not much hassle from the consumer perspective. 

* When using dimensions, only the first two dimensions are used on each variant to determine whether or not there is a key that references the other flavour in the other dimensions.

* When using dimensions on each flavour, you either specify a single key like `customKey1 = "dsafasdjgt"` or by using `customKey1.staging = "213123hdsredsd"` but not both combined in the same flavour.
