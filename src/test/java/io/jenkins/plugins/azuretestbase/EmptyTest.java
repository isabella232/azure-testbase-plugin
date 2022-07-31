package io.jenkins.plugins.azuretestbase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.ToString;

public class EmptyTest {
    @Test
    public void testSomething() throws IOException, InterruptedException, URISyntaxException {
        System.out.println(java.util.Arrays.toString("".split("/", -1)));
        System.out.println("".split("/", -1).length);
        System.out.println("".split("/").length);
        System.out.println(java.util.Arrays.toString("/".split("/", -1)));
        System.out.println(java.util.Arrays.toString("a".split("/", -1)));
        System.out.println(java.util.Arrays.toString("a/".split("/", -1)));
        System.out.println(java.util.Arrays.toString("a/b".split("/", -1)));
        System.out.println(java.util.Arrays.toString("a/b/".split("/", -1)));
        System.out.println(java.util.Arrays.toString("./a/./b".split("/", -1)));
        System.out.println(java.util.Arrays.toString("./".split("/", -1)));
        // System.out.println("   ".isBlank());
        // System.out.println("   ".isBlank());
    }

    @Test
    public void testJson() throws Exception {
        String[] jsonStrs = {
            "{\"name\":\"zhb\",\"age\":18}",
            "{\"age\":18}",
            "{\"age\":18, \"class\": \"3\"}",
            "{\"name\":\"\"}",
            "{\"name\":\"zhb\",\"age\":18,\"good\":true}",
        };
        ObjectMapper mapper = new ObjectMapper();

        for(String jsonStr : jsonStrs) {
            System.out.println(mapper.readValue(jsonStr, DataStructure.class));
            System.out.println(mapper.writeValueAsString(mapper.readValue(jsonStr, DataStructure.class)));
        }
    }

    @Test
    public void testArrayListNull() throws Exception {
        // List<String> l = new ArrayList<>(null);
        // System.out.println(l);
        Path p = Paths.get("a/b/c/d/e.txt");
        p = Paths.get("./a/b/c/d/e.txt");
        p = Paths.get("\\a\\b\\c\\d\\e.txt");
        p = Paths.get("./a/b/c/d/e.txt");
        System.out.println(p.toAbsolutePath());
        Spliterator<Path> sp = p.spliterator();
        System.out.println(sp.estimateSize());
        sp = sp.trySplit();
        List<String> l = new ArrayList<>();
        sp.forEachRemaining(val -> {
            System.out.println(val);
            System.out.println(val.toString());
            l.add(val.toString());
        });
        System.out.println(l);
        System.out.println(sp.estimateSize());
        System.out.println(sp.toString());
        System.out.println(p.getFileName().toUri());
        System.out.println(p.getFileName().spliterator());
        System.out.println(p.getFileName().toString());

        Path p1 = Paths.get("abc/def.json");
        System.out.println(p1.getFileName().endsWith(".json"));
        System.out.println(p1.getFileName().endsWith("json"));
    }


    @Test
    public void testPath() {
        Path path = Paths.get("a/b/c.txt");
        System.out.println(path.getFileName());
        path = Paths.get("a\\b\\c.txt");
        System.out.println(path.getFileName());
        path = Paths.get("c.txt");
        System.out.println(path.getFileName());
    }
}

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class DataStructure {
    @JsonInclude(value=Include.NON_NULL)
    public String name;
    @JsonInclude(value=Include.NON_DEFAULT, content=Include.NON_NULL)
    public int age;
    @JsonInclude(value=Include.NON_NULL)
    public Boolean good;
    @JsonInclude(value=Include.NON_EMPTY)
    public List<String> l1;
    @JsonInclude(value=Include.NON_NULL)
    public Integer grade;
}
