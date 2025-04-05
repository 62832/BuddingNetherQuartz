plugins {
    id("net.neoforged.moddev")
}

val modId = "buddingnetherquartz"

base.archivesName = modId
version = if (System.getenv("GITHUB_REF_TYPE") == "tag") System.getenv("GITHUB_REF_NAME") else "0.0"
group = "gripe.90"

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

dependencies {
    implementation("org.appliedenergistics:appliedenergistics2:19.2.7")
}

sourceSets {
    main {
        resources.srcDir(file("src/generated/resources"))
    }

    create("data") {
        val main = main.get()
        compileClasspath += main.compileClasspath + main.output
        runtimeClasspath += main.runtimeClasspath + main.output
    }
}

val mc = "1.21.1"

neoForge {
    val maj = mc.substringAfter('.')
    version = "${maj + (if (!maj.contains('.')) ".0" else "")}.113"

    parchment {
        minecraftVersion = mc
        mappingsVersion = "2024.11.17"
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("data"))
        }
    }

    runs {
        configureEach {
            gameDirectory = file("run")
        }

        create("client") {
            client()
        }

        create("server") {
            server()
            gameDirectory = file("run/server")
        }

        create("data") {
            data()
            programArguments.addAll(
                "--mod", modId,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
            sourceSet = sourceSets.getByName("data")
        }
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    processResources {
        exclude("**/.cache")

        val props = mapOf("version" to version, "mcVersion" to mc)
        inputs.properties(props)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(props)
        }
    }
}
