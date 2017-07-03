package cc.jcsw.search;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ClassResolverTest {

    private ClassResolver cr;
    private String[] linesArray;

    @Before
    public void before() throws URISyntaxException, IOException {
        this.cr = new ClassResolver();

        Path path = Paths.get(this.getClass().getResource("AbstractListMultimap.java.txt").toURI());
        List<String> lines = Files.readAllLines(path);
        this.linesArray = lines.toArray(new String[lines.size()]);
    }

    @Test
    public void extractImportLinesTest() throws URISyntaxException, IOException {

        Object[] args = new Object[] { linesArray };
        List<String> imports = ReflectionTestUtils.invokeMethod(cr, "extractImportLines", args);
        Assert.assertThat(imports, IsCollectionWithSize.hasSize(6));
        Assert.assertThat(imports,
                IsIterableContainingInOrder.contains("com.google.common.annotations.GwtCompatible",
                        "com.google.errorprone.annotations.CanIgnoreReturnValue", "java.util.Collection", "java.util.List", "java.util.Map",
                        "javax.annotation.Nullable"));

    }

    @Test
    public void classNameInThisPackageTest() throws URISyntaxException, IOException {

        Path path = Paths.get(this.getClass().getResource("AbstractListMultimap.java.txt").toURI());
        List<String> lines = Files.readAllLines(path);
        String[] linesArray = lines.toArray(new String[lines.size()]);
        Object[] args = new Object[] { linesArray, "Bob" };
        String result = ReflectionTestUtils.invokeMethod(cr, "classNameInThisPackage", args);
        Assert.assertThat(result, Is.is("com.google.common.collect.Bob"));

    }

}
