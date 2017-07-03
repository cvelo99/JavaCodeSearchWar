package cc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.Validate;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Search utilities.
 * 
 * @author Chris Carcel
 *
 */
@Component
public class SearchUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SearchUtils.class);

    @Autowired
    private ServletContext servletContext;

    private static final String CONFIG_PROPS_FILE_NAME = "configOptions.properties";
    private Map<String, String> cachedConfig;

    private String solrRoot;

    private HttpSolrServer solrServer;

    /**
     * Get config options, reloading at most every {@link #MINUTES_TO_WAIT_BEFORE_CHECKING_FILE} minutes .
     * 
     * @return
     */
    public Map<String, String> getConfigOptions() {
        return this.cachedConfig;
    }

    public HttpSolrServer getServer() {
        return this.solrServer;
    }

    @PostConstruct
    private void setupServer() {
        if ("true".equals(System.getProperty("cc.home"))) {
            solrRoot = "http://localhost:8081/solr/javasource"; // home
        } else {
            solrRoot = "http://192.168.0.99/solr/javasource"; // work
        }

        this.solrServer = new HttpSolrServer(solrRoot);

    }

    @PostConstruct
    private void loadConfig() {
        try {
            Properties p = new Properties();
            try (InputStream in = servletContext.getResourceAsStream("WEB-INF/classes/" + CONFIG_PROPS_FILE_NAME);) {
                p.load(in);
            } catch (NullPointerException ne) {
                // ignore, this happens locally using jrebel
            }

            if (p.isEmpty()) {
                // local, jrebel
                String path = servletContext.getRealPath("/");
                File dir = new File(path);
                Path start = dir.getParentFile().toPath();
                LOG.trace("start is {}", start);
                File configFile = start.getParent().resolve(Paths.get("main", "resources", CONFIG_PROPS_FILE_NAME)).toRealPath().toFile();
                try (FileInputStream fis = new FileInputStream(configFile);
                        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);) {
                    p.load(isr);
                }
            }

            Validate.isTrue(!p.isEmpty(), "properties is empty");

            Map<String, String> map = new HashMap<>();
            p.entrySet().stream().forEach(e -> map.put(e.getKey().toString(), e.getValue().toString()));

            this.cachedConfig = Collections.unmodifiableMap(map);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
