plugins {
    id("java")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.intellijPlatform)
}

group = "com.k.pmpstudy"
version = "1.2.0"

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
        bundledModule("intellij.platform.vcs.impl")

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
              <li>Added <b>Show Deleted Files</b> and <b>Show Moved Files (Pre-Move Location)</b>
                  toggles in the Project view tool window's options menu (the ⋮ three-dot button,
                  under <b>Reveal Deleted Files</b>). Locally deleted files appear with strikethrough
                  text at their original location; moved / renamed files appear with italic text at
                  their pre-move location. The two toggles are independent and both ON by default.</li>
              <li>Phantom entries can be opened (Enter or double-click) to view the deleted /
                  pre-move file content as a read-only editor.</li>
              <li>If the original parent directory no longer exists, phantom entries are attached
                  to the nearest existing ancestor and the original sub-path is shown.</li>
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
