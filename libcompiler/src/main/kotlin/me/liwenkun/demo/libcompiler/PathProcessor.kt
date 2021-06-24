package me.liwenkun.demo.libcompiler

import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import com.squareup.javapoet.ClassName
import me.liwenkun.demo.libannotation.Demo
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic


@AutoService(Processor::class)
open class PathProcessor : AbstractProcessor() {

    private var messager: Messager? = null
    private var elementUtils: Elements? = null
    private var typeUtils: Types? = null
    private var filer: Filer? = null

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        messager = processingEnv?.messager
        elementUtils = processingEnv?.elementUtils
        typeUtils = processingEnv?.typeUtils
        filer = processingEnv?.filer
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        val triples: MutableList<Triple<TypeElement, String, String>> = ArrayList()
        val elementsAnnotatedWith = roundEnv?.getElementsAnnotatedWith(Demo::class.java)
        for (element in ElementFilter.typesIn(elementsAnnotatedWith)) {
            if (!instanceOf(element, "android.app.Activity")
                    && !instanceOf(element, "android.app.Fragment")
                    && !instanceOf(element, "androidx.fragment.app.Fragment")) {
                messager?.printMessage(Diagnostic.Kind.ERROR, "@Demo 注解只能用于 Activity 或 Fragment")
            }
            val annotation = element.getAnnotation(Demo::class.java)
            triples.add(Triple(element, annotation.category, annotation.title))
        }
        processRegister(triples)
        return true
    }

    private fun instanceOf(element: TypeElement, className: String): Boolean {
        return typeUtils?.isAssignable(
                element.asType(),
                elementUtils?.getTypeElement(className)?.asType()) == true
    }

    private fun processRegister(triples: List<Triple<TypeElement, String, String>>) {
        if (triples.isEmpty()) {
            return
        }
        val codeSpec = CodeBlock.builder()
        for (triple in triples) {
            codeSpec.addStatement(
                "\$T.add(\$S, \$S, \$T.class)",
                ClassName.get("me.liwenkun.demo.demoframework", "DemoBook"),
                triple.second,
                triple.third,
                ClassName.get(triple.first)
            )
        }
        val javaFile = JavaFile.builder(
                "me.liwenkun.demo", TypeSpec.classBuilder("DemoRegister")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(
                        MethodSpec.methodBuilder("init")
                                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                                .addCode(codeSpec.build())
                                .build()
                )
                .build()
        ).build()
        javaFile.writeTo(filer)
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.RELEASE_8

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(Demo::class.java.name)
}