package me.liwenkun.demo.libcompiler

import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import com.sun.source.tree.ClassTree
import com.sun.source.util.TreePathScanner
import com.sun.source.util.Trees
import me.liwenkun.demo.libannotation.Source
import java.io.File
import java.net.URI
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.*


@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("me.liwenkun.demo.libannotation.Source")
class SourceProcessor : AbstractProcessor() {

    private var messager: Messager? = null
    private var elementUtils: Elements? = null
    private var typeUtils: Types? = null
    private var filer: Filer? = null

    private lateinit var trees: Trees

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        messager = processingEnv?.messager
        elementUtils = processingEnv?.elementUtils
        typeUtils = processingEnv?.typeUtils
        filer = processingEnv?.filer
        trees = Trees.instance(processingEnv)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

        val elementsAnnotatedWith = roundEnv?.getElementsAnnotatedWith(Source::class.java)
        val variableElements = ElementFilter.fieldsIn(elementsAnnotatedWith)

        if (variableElements.isEmpty()) {
            return false
        }

        val codeSpec = CodeBlock.builder()

        for (element in variableElements) {
            val className = ClassName.get(element.enclosingElement as TypeElement)
            val topClassName = className.topLevelClassName().canonicalName()

            val sourceDir = findLayouts()
            var sourceFile = File(sourceDir, topClassName.replace('.', '/') + ".kt")
            if (!sourceFile.exists()) {
                sourceFile = File(sourceDir, topClassName.replace('.', '/') + ".java")
            }
            if (!sourceFile.exists()) {
                messager?.printMessage(Diagnostic.Kind.ERROR, "source file does not exist for class $topClassName \n")
            }

            codeSpec.addStatement(
                "\$T.\$L=\$S;",
                className,
                element.simpleName,
                sourceFile.readText()
            )
        }
        val javaFile = JavaFile.builder(
            "me.liwenkun.demo", TypeSpec.classBuilder("SourceInjector")
                .addMethod(
                    MethodSpec.methodBuilder("init")
                        .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                        .addCode(codeSpec.build())
                        .build()
                )
                .build()
        ).build()
        javaFile.writeTo(filer)
        return true
    }

    @Throws(Exception::class)
    private fun findLayouts(): File {
        val filer = processingEnv.filer
        val dummySourceFile = filer.createSourceFile("dummy" + System.currentTimeMillis())
        var dummySourceFilePath = dummySourceFile.toUri().toString()
        if (dummySourceFilePath.startsWith("file:")) {
            if (!dummySourceFilePath.startsWith("file://")) {
                dummySourceFilePath = "file://" + dummySourceFilePath.substring("file:".length)
            }
        } else {
            dummySourceFilePath = "file://$dummySourceFilePath"
        }
        val cleanURI = URI(dummySourceFilePath)
        val dummyFile = File(cleanURI)
        val projectRoot: File =
            dummyFile.parentFile.parentFile.parentFile.parentFile
                .parentFile.parentFile
        return File(projectRoot.absolutePath.toString() + "/src/main/java")
    }
}