-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes Exceptions
-keepattributes Deprecated
-keepattributes SourceFile
-keepattributes LineNumberTable
-keepattributes EnclosingMethod

-renamesourcefileattribute SourceFile

-keep public class com.sherepenko.android.archivarius.logger.** {
  public protected *;
}

-keep interface com.sherepenko.android.archivarius.logger.**
