package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

public class ConditionalTest {

    @Test
    @Disabled("Don't run until JIRA #123 is resolved")
    void basicTest(){
        //exceute method and perfom asserts
        
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testForWindowsOnly(){
        //exceute method and perfom asserts
        
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testForLinuxOnly(){
        //exceute method and perfom asserts
        
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testForMacOnly(){
        //exceute method and perfom asserts
        
    }

    @Test
    @EnabledOnOs({OS.WINDOWS, OS.LINUX})
    void testForWindowsAndLinux(){
        //exceute method and perfom asserts
        
    }

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testForOnlyJava17(){
        //exceute method and perfom asserts
    }

    @Test
    @EnabledOnJre(JRE.JAVA_11)
    void testForOnlyJava11(){
        //exceute method and perfom asserts
    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_11, max=JRE.JAVA_17)
    void testOnlyForJavaRange(){
        //exceute method and perfom asserts
    }

    @Test
    @EnabledIfEnvironmentVariable(named="LUV2CODE_ENV", matches="DEV")
    void testOnlyForDevEnvironment(){
        //exceute method and perfom asserts
    }
    
    @Test
    @EnabledIfSystemProperty(named="LUV2CODE_SYS_PROP", matches="CI_CD_DEPLOY")
    void testOnlyForSystemProperty(){
        //exceute method and perfom asserts
    }
}
