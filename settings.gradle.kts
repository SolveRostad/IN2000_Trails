import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                val localProperties = Properties().apply {
                    load(project(":").projectDir.resolve("local.properties").inputStream())
                }
                username = "mapbox"
                password = localProperties["MAPBOX_SECRET_TOKEN"]?.toString() ?: error("MAPBOX_SECRET_TOKEN not found in local.properties")
            }
        }
        maven("https://jitpack.io")
    }
}

rootProject.name = "IN2000_gruppe3"
include(":app")