package com.github.walterfan.hellotest;



import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.IObjectFactory;
import org.testng.annotations.Test;


import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.testng.Assert.assertEquals;

/**
 * Created by yafan on 24/1/2018.
 */
//@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtils.class)
public class FileUtilsTest extends PowerMockTestCase {

    public  int howManyFiles(String path, FileFilter filter) {
        System.out.println("-----------");
        List<String> files = FileUtils.listFiles(new File(path), filter);
        files.forEach(System.out::println);
        return files.size();
    }


    @Test
    public void testHowManyFiles() {

        List<String> fileNames = Arrays.asList("a.java", "b.java", "c.java");
        PowerMockito.mockStatic(FileUtils.class);
        PowerMockito.when(FileUtils.listFiles(Mockito.any(), Mockito.any())).thenReturn(fileNames);

        int count = howManyFiles(".", FileUtils.javaFileFilter);
        assertEquals(count, 3);
    }
}
