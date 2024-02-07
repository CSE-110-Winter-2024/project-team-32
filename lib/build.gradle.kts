plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation("androidx.annotation:annotation:1.7.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.testng:testng:6.9.6")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}