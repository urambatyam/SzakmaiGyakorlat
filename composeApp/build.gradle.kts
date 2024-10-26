import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    //ROOM local database
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kotlin {
    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
    }
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            //Voyager Navigation
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            //Datastore Preference
            implementation(libs.androidx.data.store.core)
            //logging
            implementation(libs.logback)
            //ROOM local database
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            //Chart
            implementation(libs.koala.plot)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.kmp.boci.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.kmp.boci"
            packageVersion = "1.0.0"
        }
    }
}

room{
    schemaDirectory("$projectDir/schemas")
}

dependencies{
    ksp(libs.room.compiler)
}





