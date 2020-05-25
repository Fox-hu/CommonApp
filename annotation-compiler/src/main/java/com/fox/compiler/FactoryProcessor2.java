package com.fox.compiler;


import com.fox.annotation.Factory;
import com.fox.annotation.Node;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * 根据meal 自动生成mealfactory文件
 * https://www.race604.com/annotation-processing/
 * Created by fox.hu on 2018/10/18.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.fox.annotation.Factory")
public class FactoryProcessor2 extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementsUtils;
    private Filer filer;
    private Logger logger;
    private List<Node> pizzaList = new ArrayList<>();
    private TypeMirror typeMeal;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        typeUtils = processingEnvironment.getTypeUtils();
        elementsUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        logger = new Logger(processingEnv.getMessager());
        typeMeal = elementsUtils.getTypeElement(Utils.MEAL_CLASS).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> factoryNodes = roundEnvironment.getElementsAnnotatedWith(
                Factory.class);
        logger.info(">>> factoryNodes is " + factoryNodes.toString() + " <<<");
        parserFactoryNodes(factoryNodes);
        generateFactoryFile();
        pizzaList.clear();
        return true;
    }

    private void generateFactoryFile() {
        if (!pizzaList.isEmpty()) {
            String claName = Utils.genFactoryClass();
            logger.info(">>> claName is " + claName + " <<<");
            String pkg = claName.substring(0, claName.lastIndexOf("."));
            logger.info(">>> pkg is " + pkg + " <<<");
            String simpleName = claName.substring(claName.lastIndexOf(".") + 1);
            logger.info(">>> simpleName is " + simpleName + " <<<");
            MethodSpec addPizza = generateAddPizzaMethod();

            try {
                JavaFile.builder(pkg, TypeSpec.classBuilder(simpleName).addModifiers(
                        Modifier.PUBLIC).addMethod(addPizza).build()).build().writeTo(filer);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parserFactoryNodes(Set<? extends Element> factoryNodes) {
        TypeMirror mealType = elementsUtils.getTypeElement(Utils.MEAL_CLASS).asType();
        logger.info(">>> TypeMirror is " + mealType.toString());
        for (Element factoryNode : factoryNodes) {
            TypeMirror tm = factoryNode.asType();

            Factory factory = factoryNode.getAnnotation(Factory.class);
            if (typeUtils.isSubtype(tm, mealType) && !pizzaList.contains(factory.id())) {
                Node node = new Node();
                node.setId(factory.id());
                node.setRawType(factoryNode);
                pizzaList.add(node);
            }
        }
    }

    private MethodSpec generateAddPizzaMethod() {
        TypeName returnType = TypeName.get(typeMeal);

        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("getPizza").returns(
                returnType).addModifiers(Modifier.PUBLIC).addModifiers(Modifier.STATIC)
                .addParameter(String.class, "id");

        openUriMethodSpecBuilder.beginControlFlow("if (id == null)").addStatement(
                "throw new IllegalArgumentException($S)", "id is null!").endControlFlow();


        for (Node node : pizzaList) {
            openUriMethodSpecBuilder.beginControlFlow("if ($S.equals(id))", node.getId())
                    .addStatement("return new $L()", node.getRawType()).endControlFlow();
        }

        openUriMethodSpecBuilder.addStatement("throw new IllegalArgumentException($S + id)",
                "Unknown id = ");

        return openUriMethodSpecBuilder.build();
    }
}
