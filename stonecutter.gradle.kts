plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.4-fabric" /* [SC] DO NOT EDIT */

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("build")
}

stonecutter registerChiseled tasks.register("chiseledBuildAndCollect", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://mvn.devos.one/snapshots/")
        maven("https://maven.wispforest.io")
        maven("https://maven.imb11.dev/releases")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.isxander.dev/releases")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.quiltmc.org/repository/release")
        maven("https://maven.shedaniel.me/")
        maven("https://maven.terraformersmc.com/releases")
    }
}

gradle.rootProject {
    apply(plugin = "org.moddedmc.wiki.toolkit")

    (extensions.findByName("wiki") as? org.moddedmc.wiki.toolkit.WikiToolkitExtension)?.apply {
        docs {
            create("smartitemhighlight") {
                root = rootProject.file("docs/")
            }
        }

        val resolvedWikiAccessToken = "WIKI_ACCESS_TOKEN".let {
            System.getenv(it) ?: findProperty(it)?.toString()
        }

        if (!resolvedWikiAccessToken.isNullOrEmpty()) {
            wikiAccessToken = resolvedWikiAccessToken
        }
    } ?: println("Warning: `wiki` extension is not available in the root project.")
}