# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\ProgramFiles\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-keep public class com.umeng.fb.ui.ThreadView { }
#-keep public class com.firstaid.relative.R$*{
#    public static final int *;
#}
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}

-dontwarn com.ut.mini.**
-dontwarn okio.**
-dontwarn com.xiaomi.**
-dontwarn com.squareup.wire.**
-dontwarn android.support.v4.**

-keepattributes *Annotation*

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

-keep class okio.** {*;}
-keep class com.squareup.wire.** {*;}

-keep class com.umeng.message.protobuffer.* {
         public <fields>;
         public <methods>;
}

-keep class com.umeng.message.* {
         public <fields>;
         public <methods>;
}

-keep class org.android.agoo.impl.* {
         public <fields>;
         public <methods>;
}

-keep class org.android.agoo.service.* {*;}

-keep class org.android.spdy.**{*;}

-keep public class com.firstaid.relative.R$*{
    public static final int *;
}
