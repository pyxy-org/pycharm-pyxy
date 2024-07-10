import org.jetbrains.changelog.exceptions.MissingVersionException
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.com.intellij.openapi.util.io.FileUtil.exists
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  id("java")

  kotlin("jvm") version "1.9.24"

  id("org.jetbrains.intellij") version "1.17.3"
  id("org.jetbrains.changelog") version "2.2.0"
}

group = "org.pyxy"
version = properties("pluginVersion")

repositories {
  mavenCentral()
}

dependencies {}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  val platformType = properties("platformType")

  pluginName.set(properties("pluginName"))

  val platformLocalPath = project.findProperty("platformLocalPath") as? String
  if (platformLocalPath != null) {
    if (!exists(platformLocalPath)) {
      logger.error("Custom platform path not exist: $platformLocalPath")
    } else {
      logger.warn("Using custom platform path: $platformLocalPath")
    }
    //See https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html#intellij-extension-localpath
    localPath.set(platformLocalPath)
    downloadSources.set(false)
  } else {
    version.set(properties("platformVersion"))
    logger.warn("Use version: ${version.get()}")
    downloadSources.set(properties("platformDownloadSources").toBoolean())
  }

  type.set(platformType)
  updateSinceUntilBuild.set(true)

  val isPyCharm = platformType == "PC" || platformType == "PY" || platformType == "PD"
  sandboxDir.set("${project.rootDir}/.sandbox${if (isPyCharm) "_pycharm" else ""}")
  ideaDependencyCachePath.set("${project.rootDir}/.idea_distrib_cache")  // Useful for Windows due to short cmdline path

  // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
  val platformPlugins = ArrayList<String>()
  when (platformType) {
    "PC" -> platformPlugins.add("python-ce")
    "PY", "PD" -> {
      platformPlugins.add("python")
      // Workaround: https://youtrack.jetbrains.com/issue/PY-51535/PluginException-when-using-Python-Plugin-213-x-version#focus=Comments-27-5439344.0-0
      platformPlugins.add("com.intellij.platform.images")
    }

    else -> platformPlugins.add(properties("pythonPlugin"))
  }
  platformPlugins.addAll(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
  plugins.set(platformPlugins)
}

// Configure gradle-changelog-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-changelog-plugin
// Configuration: https://github.com/JetBrains/gradle-changelog-plugin#configuration
changelog {
  // Helps to organize content in CHANGLOG.md. Could generate change notes from it.

  version.set(project.version.toString())
  headerParserRegex.set("""(\d+\.\d+.(\d+|SNAPSHOT)(-\w+)?)""".toRegex())

  // Optionally generate changed commits list url.
  repositoryUrl.set("https://github.com/pyxy-org/pyxycharm")  // url to compare commits beetween previous and current release

  // default values:
  // combinePreReleases.set(true) // default; Combines pre-releases (like 1.0.0-alpha, 1.0.0-beta.2) into the final release note when patching.
  // header.set(provider { "[${version.get()}] - ${date()}" })
  // groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
  // itemPrefix.set("-") // default
  // path.set(file("CHANGELOG.md").canonicalPath)  // default value
  // keepUnreleasedSection.set(true) // default
  // unreleasedTerm.set("[Unreleased]") // default
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(properties("javaVersion")))
  }
}

tasks {
  properties("javaVersion").let {
    withType<JavaCompile> {
      sourceCompatibility = it
      targetCompatibility = it
    }
    withType<KotlinCompile> {
      kotlinOptions {
        jvmTarget = it
        freeCompilerArgs = listOf("-Xjvm-default=all")
      }
    }
  }

  patchPluginXml {
    version.set(project.version.toString())
    sinceBuild.set(properties("pluginSinceBuild"))
    untilBuild.set(properties("pluginUntilBuild"))

    // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
    pluginDescription.set(projectDir.resolve("README.md").readText().lines().run {
      val start = "<!-- Plugin description -->"
      val end = "<!-- Plugin description end -->"

      if (!containsAll(listOf(start, end))) {
        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
      }
      subList(indexOf(start) + 1, indexOf(end))
    }.joinToString("\n").run { markdownToHTML(this) })

    // Get the latest available change notes from the changelog file
    changeNotes.set(
      provider {
        with(changelog) {
          val log = try {
            getUnreleased()
          } catch (e: MissingVersionException) {
            getOrNull(version.toString()) ?: getLatest()
          }
          renderItem(
            log,
            org.jetbrains.changelog.Changelog.OutputType.HTML,
          )
        }
      },
    )
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }
}
