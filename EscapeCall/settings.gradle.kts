pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://github.com/jitsi/jitsi-meet-release-notes/raw/master/android-sdk") }
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "EscapeCall"
include(":app")
