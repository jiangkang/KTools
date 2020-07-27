package com.jiangkang.compiler;


import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.jiangkang.annotations.apt.Router;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static com.jiangkang.compiler.utils.Constants.PACKAGE_NAME;
import static com.jiangkang.compiler.utils.Constants.classContext;
import static com.jiangkang.compiler.utils.Constants.classIntent;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.jiangkang.annotations.apt.Router"})
public class RouterProcessor extends BaseProcessor {

    private Map<String, String> maps;

    private static final String METHOD_PREFIX = "start";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //初始化一些帮助类
        maps = new HashMap<>();
        msgUtils.info("----------------------init processor---------------------------");
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //遍历
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Router.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;

            msgUtils.info(" >>> " + element.toString() + " <<< ");

            //得到类信息
            TypeElement typeElement = (TypeElement) element;

            if (!maps.containsKey(typeElement.getSimpleName().toString())) {
                //存入Map
                maps.put(
                        typeElement.getSimpleName().toString(),
                        elementsUtils.getPackageOf(typeElement).getQualifiedName().toString()
                );
            }
        }

        //生成类
        TypeSpec.Builder routerHelperClass = TypeSpec
                .classBuilder("RouterHelper")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);


        for (Map.Entry<String, String> map : maps.entrySet()) {

            String activityName = map.getKey();
            String packageName = map.getValue();

            ClassName activityClass = ClassName.get(packageName, activityName);

            String statement1 = "$T intent = new $T($L,$L);";
            String statement2 = "context.startActivity(intent);";

            //生成方法
            MethodSpec method = MethodSpec
                    .methodBuilder(METHOD_PREFIX + activityName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addParameter(classContext, "context")
                    .addStatement(statement1, classIntent, classIntent, "context", activityClass + ".class")
                    .addStatement(statement2)
                    .build();

            routerHelperClass.addMethod(method);
        }

        try {
            JavaFile.builder(
                    PACKAGE_NAME,
                    routerHelperClass.build()
            ).build().writeTo(filerUtils);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
