# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified

# in /home/buddy/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# in /Applications/Android Studio.app/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.

#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:



# Obfuscation parameters:
#-dontobfuscate
-useuniqueclassmembernames
-keepattributes SourceFile,LineNumberTable
-allowaccessmodification

# Ignore warnings:
#-dontwarn org.mockito.**
#-dontwarn org.junit.**
#-dontwarn com.robotium.**
#-dontwarn org.joda.convert.**

# Ignore warnings: We are not using DOM model
-dontwarn com.fasterxml.jackson.databind.ext.DOMSerializer
# Ignore warnings: https://github.com/square/okhttp/wiki/FAQs
-dontwarn com.squareup.**
# Ignore warnings: https://github.com/square/okio/issues/60
-dontwarn okio.**
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

-dontwarn org.apache.http.**
-dontwarn android.net.**

# Ignore warnings: https://github.com/square/retrofit/issues/435
-dontwarn com.google.appengine.api.urlfetch.**
-keep public class android.net.http.SslError
-keep  class indwin.c3.shareapp.adapters.PlaceAutocompleteAdapter

-keep public class android.webkit.WebViewClient
-keep class org.apache.http.** { *; }
# Keep the pojos used by GSON or Jackson
-keep class indwin.c3.shareapp.models.** { *; }

# Keep GSON stuff
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

-keep class com.cloudinary.** { *; }

-keep class com.github.ParkSangGwon.** { *; }

-keep class uk.co.chrisjenx.** { *; }

-keep class com.facebook.** { *; }

-keep class com.balysv.** { *; }

-keep class de.hdodenhof.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.pixplicity.sharp.** { *; }

-keep class ch.acra.sharp.** { *; }



-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     !static !transient <fields>;
     !private <fields>;
     !private <methods>;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }
-keep class org.acra.**{*;}
# Keep Jackson stuff
-keep class org.codehaus.** { *; }
-keep class com.fasterxml.jackson.annotation.** { *; }

# Keep these for GSON and Jackson
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# Keep Retrofit
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.** *;
}
-keepclassmembers class * {
    @retrofit.** *;
}

# Keep Picasso
-keep class com.squareup.picasso.** { *; }
-keepclasseswithmembers class * {
    @com.squareup.picasso.** *;
}
-keepclassmembers class * {
    @com.squareup.picasso.** *;
}
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
  public *;
}

-keep class com.commonsware.cwac.** { *; }