package cc.jcsw.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.MapUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import cc.jcsw.beans.SearchInputs;
import cc.utils.SearchUtils;

/**
 * Given an ID (full file path) of a file and the name of a class referenced within it, find the referenced class.
 * 
 * @author Chris Carcel
 *
 */
@Component
public class ClassResolver {

    private static final Logger LOG = LoggerFactory.getLogger(ClassResolver.class);

    private static final Pattern IMPORT_PATTERN = Pattern.compile("^import\\s*([^;/]*)");
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("^package\\s*([^;/]*)");

    @Autowired
    private Searcher searcher;

    @Autowired
    private SearchUtils searchUtils;

    /**
     * Returns the solr id (the full path to the file) of <code>elementClickedOn</code> within the file <code>filePath</code>
     * 
     * @param filePath
     * @param elementClickedOn
     * @return
     */
    public String resolve(String filePath, String elementClickedOn) {

        if (LOG.isTraceEnabled()) {
            LOG.trace("Resolve element " + elementClickedOn + " in class " + filePath);
        }

        Map<String, Object> document = findDocument(filePath);
        String fullClass = resolveFromDocument(document, elementClickedOn);
        return fullClass;
    }

    private String resolveFromDocument(Map<String, Object> document, String elementClickedOn) {
        String branch = MapUtils.getString(document, "branch");
        Integer version = MapUtils.getInteger(document, "jboss_version");
        String source = MapUtils.getString(document, "java_source");
        String folder = MapUtils.getString(document, "folder");
        String[] lines = source.split("\n");
        List<String> imports = extractImportLines(lines);

        String id = null;
        for (String importLine : imports) {
            if (importLine.endsWith('.' + elementClickedOn)) {
                String fqn = importLine;
                id = findForFqn(fqn, branch, version, folder);
                if (null != id) {
                    break;
                }
            }
        }

        if (null == id) {
            String classNameInThisPackage = classNameInThisPackage(lines, elementClickedOn);
            id = findForFqn(classNameInThisPackage, branch, version, folder);
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("Resolved class to " + id);
        }

        return id;

    }

    private String findForFqn(String className, String branch, Integer version, String folder) {

        List<String> folders = Lists.newArrayList(folder, "shared", "admin");

        for (String folderToCheck : folders) {

            SearchQueryBuilder b = new SearchQueryBuilder();
            b.setBranch(branch);
            b.setVersion(version);
            b.setFqn(className);
            b.setFolder(folderToCheck);

            SolrQuery query = b.buildSearch();

            HttpSolrServer server = searchUtils.getServer();
            QueryResponse response;
            try {
                response = server.query(query);
            } catch (SolrServerException e) {
                throw new IllegalStateException(e);
            }
            SolrDocumentList results = response.getResults();
            long numFound = results.getNumFound();

            if (LOG.isTraceEnabled()) {
                LOG.trace("Found {} results in folder {}", numFound, folderToCheck);
            }

            if (1L == numFound) {
                SolrDocument document = results.get(0);
                String id = (String) document.getFieldValue("id");
                return id;
            }

        }

        return null;
    }

    private String classNameInThisPackage(String[] lines, String elementClickedOn) {
        String packageName = null;
        for (String line : lines) {
            Matcher m = PACKAGE_PATTERN.matcher(line);
            if (m.find()) {
                packageName = m.group(1);
                break;
            }
        }

        String result;
        if (null == packageName) {
            result = null;
        } else {
            result = packageName + "." + elementClickedOn;

        }

        return result;

    }

    /**
     * Returns the import lines in a java source file.
     * 
     * @param lines
     * @return
     */
    private List<String> extractImportLines(String[] lines) {
        List<String> result = new ArrayList<>();

        for (String line : lines) {
            Matcher m = IMPORT_PATTERN.matcher(line);
            if (m.find()) {
                result.add(m.group(1));
            }
        }

        return result;
    }

    /**
     * Find the source based on the file path. Return null if not found.
     * 
     * @param filePath
     * @return
     */
    private Map<String, Object> findDocument(String filePath) {
        SearchInputs inputs = new SearchInputs();
        inputs.setId(filePath);
        inputs.setAllFields(true);
        SearchOuput search = searcher.search(inputs);
        long numberResultsFound = search.getNumberResultsFound();
        if (1L == numberResultsFound) {
            return search.getResults().stream().findFirst().get();
        } else {
            return null;
        }
    }

}
