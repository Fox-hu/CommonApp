package com.fox.compiler;

import com.fox.annotation.view.BindView;
import com.fox.annotation.view.Unbinder;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import javax.lang.model.util.Elements;

/**
 * @Author fox
 * @Date 2020/5/24 22:54
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.fox.annotation.view.BindView")
public class ViewProcessor extends AbstractProcessor {
    private Logger logger;
    private Filer filer;
    private Elements elementsUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        logger = new Logger(processingEnv.getMessager());
        filer = processingEnvironment.getFiler();
        elementsUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logger.info(">>> ViewProcessor process <<<");
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindView.class);
        //activity->list<element>
        Map<Element, List<Element>> elementsMap = new LinkedHashMap<>();
        //        element is view，enclosingElement is activity
        for (Element element : elements) {
            //            Element enclosingElement = element.getEnclosingElement();
            //            logger.info(">>> ViewProcessor  <<<" + element.getSimpleName().toString() +
            //                        enclosingElement.getSimpleName().toString());
            Element enclosingElement = element.getEnclosingElement();
            List<Element> viewBindElements = elementsMap.get(enclosingElement);
            if (viewBindElements == null) {
                viewBindElements = new ArrayList<>();
                elementsMap.put(enclosingElement, viewBindElements);
            }
            viewBindElements.add(element);
        }

        //generate code by map
        for (Map.Entry<Element, List<Element>> entry : elementsMap.entrySet()) {
            Element activityElement = entry.getKey();
            List<Element> viewBindElements = entry.getValue();
            //generate class declaration

            String activityClassStr = activityElement.getSimpleName().toString();
            ClassName activityClassName = ClassName.bestGuess(activityClassStr);
            ClassName superInterfaceName = ClassName.get(Unbinder.class);
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(activityClassName + "_ViewBinding")
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                    .addSuperinterface(superInterfaceName)
                    .addField(activityClassName,"target",Modifier.PRIVATE);

            //generate constructor method
            MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                    .addParameter(activityClassName,"target");
            constructorBuilder.addStatement("this.target=target");

            //generate unbind method
            ClassName callSuperClassName = ClassName.get("androidx.annotation","CallSuper");
            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("unbind")
                    .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                    .addAnnotation(callSuperClassName);
            methodSpecBuilder.addStatement("$T target = this.target",activityClassName);
            methodSpecBuilder.addStatement("if(target == null) throw new IllegalStateException(\"Bindings already cleared.\");");

            for (Element viewBindElement : viewBindElements) {
                String fileName = viewBindElement.getSimpleName().toString();
                ClassName utilsClassName = ClassName.get("com.fox.toutiao.util","Utils");
                int resId = viewBindElement.getAnnotation(BindView.class).value();
                constructorBuilder.addStatement("target.set$L($T.findViewById(target,$L))",upperFirstCase(fileName),utilsClassName,resId);

                methodSpecBuilder.addStatement("target.set$L(null)",upperFirstCase(fileName));
            }

            classBuilder.addMethod(constructorBuilder.build());
            classBuilder.addMethod(methodSpecBuilder.build());

            try {
                String packageName = elementsUtils.getPackageOf(activityElement).getQualifiedName()
                        .toString();
                JavaFile.builder(packageName, classBuilder.build()).addFileComment(
                        "auto generate,do not modify").build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } return false;
    }

    //because kotlin use method instead property,for example getDrawerLayout() instead drawerLayout
    private String upperFirstCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
