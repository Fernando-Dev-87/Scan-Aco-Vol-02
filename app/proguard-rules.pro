# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep data models for metallurgy & spectrometry
-keep class com.example.model.** { *; }

# Keep Compose runtime annotations and synthetic accessors
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

