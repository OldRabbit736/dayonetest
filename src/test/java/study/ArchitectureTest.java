package study;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchitectureTest {

    JavaClasses javaClasses;

    @BeforeEach
    public void beforeEach() {
        javaClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("study");
    }

    @Test
    @DisplayName("controller 패키지 안에 있는 클래스들의 이름은 'Api'로 끝나야 한다")
    public void controllerTest() {
        ArchRule rule = classes()
                .that().resideInAnyPackage("..controller")
                .should().haveSimpleNameEndingWith("Api");

        ArchRule annotationRule = classes()
                .that().resideInAnyPackage("..controller")
                .should().beAnnotatedWith(RestController.class)
                .orShould().beAnnotatedWith(Controller.class);

        rule.check(javaClasses);
        annotationRule.check(javaClasses);
    }

    @Test
    @DisplayName("request 패키지 안에 있는 클래스들의 이름은 'Request'로 끝나야 한다.")
    public void requestTest() {
        ArchRule rule = classes()
                .that().resideInAnyPackage("..request..")
                .should().haveSimpleNameEndingWith("Request");

        rule.check(javaClasses);
    }

    @Test
    @DisplayName("response 패키지 안에 있는 클래스들의 이름은 'Response'로 끝나야 한다.")
    public void responseTest() {
        ArchRule rule = classes()
                .that().resideInAnyPackage("..response..")
                .should().haveSimpleNameEndingWith("Response");

        rule.check(javaClasses);
    }

    @Test
    @DisplayName("repository 패키지 안에 있는 클래스들은 interface여야 하며 이들의 이름은 'Repository'로 끝나야 한다.")
    public void repositoryTest() {
        ArchRule rule = classes()
                .that().resideInAnyPackage("..repository..")
                .should().haveSimpleNameEndingWith("Repository")
                .andShould().beInterfaces();

        rule.check(javaClasses);
    }

    @Test
    @DisplayName("service 패키지 안에 있는 클래스들은 @Service 애노테이션이 붙어야 하며 이들의 이름은 'Service'로 끝나야 한다.")
    public void serviceTest() {
        ArchRule rule = classes()
                .that().resideInAnyPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .andShould().beAnnotatedWith(Service.class);

        rule.check(javaClasses);
    }

    @Test
    @DisplayName("config 패키지 안에 있는 클래스들은 @Configuration 애노테이션이 붙어야 하며 이들의 이름은 'Config'로 끝나야 한다.")
    public void configTest() {
        ArchRule rule = classes()
                .that().resideInAnyPackage("..config..")
                .should().haveSimpleNameEndingWith("Config")
                .andShould().beAnnotatedWith(Configuration.class);

        rule.check(javaClasses);
    }
}
