package cc.jcsw.controllers;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.Validate;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.jcsw.beans.SearchInputs;
import cc.jcsw.search.ClassResolver;
import cc.jcsw.search.SearchOuput;
import cc.jcsw.search.Searcher;

/**
 * Display a file, resolve a clicked on file.
 * 
 * @author carcelc
 *
 */
@RestController
@RequestMapping("/display")
public class DisplayController {

    @Autowired
    private ClassResolver classResolver;

    @Autowired
    private Searcher searcher;

    /**
     * When we are displaying a file and click on another, this resolves the solr id of the file name we clicked on.
     * 
     * @param filePath
     * @param elementClickedOn
     * @return
     */
    @RequestMapping("/findFile")
    public Map<String, Object> goToFile(@RequestParam String filePath, @RequestParam String elementClickedOn) {
        return Collections.singletonMap("file", classResolver.resolve(filePath, elementClickedOn));
    }

    /**
     * Display a file.
     * 
     * @param filePath
     * @return
     * @throws SolrServerException
     */
    @RequestMapping("/file")
    public Map<String, Object> display(@RequestParam("file") String filePath) {
        SearchInputs inputs = new SearchInputs();
        inputs.setId(filePath);
        inputs.setAllFields(true);
        SearchOuput search = searcher.search(inputs);
        long numberResultsFound = search.getNumberResultsFound();
        Validate.isTrue(1L == numberResultsFound, "Number of results should be 1 but was %d", numberResultsFound);
        Map<String, Object> result = search.getResults().iterator().next();
        if (MapUtils.isNotEmpty(result) && result.containsKey("java_source")) {
            result.put("java_source", StringEscapeUtils.escapeHtml4((String) result.get("java_source")));
        }
        return result;

    }

}
