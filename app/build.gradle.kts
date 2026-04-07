import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.gestaofinanceiraapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.gestaofinanceiraapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Exportação de schemas do Room para versionamento e migrações
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }

    }

    buildFeatures {
        // Ativa a geração de classe BuildConfig, onde será injetada a chave de API do local.properties
        buildConfig = true
        // Recurso que facilita a interação com os elementos de layout do código. Gera automaticamente uma classe de vinculação para cada arquivo XML do projeto
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Carregando o properties da raiz do projeto
    val properties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(FileInputStream(localPropertiesFile))
    }
    // Acessando a chave GOOGLE_CLIENT_ID dentro do properties e injetando ela no BuildConfig
    val googleClientId = properties.getProperty("GOOGLE_CLIENT_ID") ?: ""
    defaultConfig.buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$googleClientId\"")

    /*
    * Após sincronizar e rodar o app, o Android Studio vai ler o local.properties e gerar automaticamente uma calsse java chamada BuildConfig
    * (app/build/generated/source). Agora não será necessário inserir o código do Google Sign-in no código, apenas chama-lo.
    * */
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Arquitetura e Ciclo de Vida
    implementation(libs.bundles.lifecycle)
    // Room Database ("JPA")
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    // Segurança
    implementation(libs.androidx.security.crypto)
    // Autenticação Google Sign-in
    implementation(libs.google.play.services.auth)
    // Bibliotecas do Credential Manager e Google Sign-In
    implementation("androidx.credentials:credentials:1.2.2")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")
}