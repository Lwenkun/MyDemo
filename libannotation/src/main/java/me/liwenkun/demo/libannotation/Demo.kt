package me.liwenkun.demo.libannotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Demo(val category: String = "", val title: String = "")