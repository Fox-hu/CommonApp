package com.fox.compiler;

import com.fox.annotation.view.BindView;
import com.fox.annotation.view.Unbinder;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

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

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        logger = new Logger(processingEnv.getMessager());
        filer = processingEnvironment.getFiler();
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

            String activityClassName = activityElement.getSimpleName().toString();
            ClassName className = ClassName.get(Unbinder.class);
            TypeSpec.Builder builder = TypeSpec.classBuilder(activityClassName + "_ViewBinding")
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC).addSuperinterface(className);

//            try {
//                JavaFile.builder("com.fox.gen", builder.build()).addFileComment("auto generate,do not modify").build()
//                        .writeTo(filer);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
        return false;
    }
}
