plugins {
    id("java")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.intellijPlatform)
}

group = "com.k.pmpstudy"
version = "1.1.0"

// Set the JVM language level used to build the project.
kotlin {
    jvmToolchain(25)
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        intellijIdea(providers.gradleProperty("platformVersion"))
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

        // Add plugin dependencies for compilation here, for example:
        // bundledPlugin("com.intellij.java")

    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
        }

        changeNotes = """
            <h3>${project.version}</h3>
            <ul>
              <li>Added support for moved / renamed files: right-clicking a moved entry now shows
                  <b>Reveal Pre-Move Location</b>, which focuses the directory the file was in
                  before the move.</li>
              <li>The action is now also available in the <b>Git</b> tool window's per-commit
                  changed files list (in addition to the Commit tool window).</li>
            </ul>
        """.trimIndent()
    }
    publishing {
        token = providers.gradleProperty("JETBRAIN_TOKEN")
    }
}

tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }
}
