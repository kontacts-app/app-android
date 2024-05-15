import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    dependencies {
        classpath(libs.androidx.room.gp)
    }
}

allprojects {
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)
    configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory}/**/*.kt")
            targetExclude("bin/**/*.kt")
            ktlint(libs.versions.ktlint.get())
        }
        kotlinGradle {
            target("**/*.kts")
            targetExclude("${layout.buildDirectory}/**/*.kts")
            ktlint(libs.versions.ktlint.get())
        }
    }

    // Configure Java to use our chosen language level. Kotlin will automatically
    // pick this up
    plugins.withType<JavaBasePlugin>().configureEach {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }
}
