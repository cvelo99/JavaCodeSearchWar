package cc.jcsw.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.jcsw.beans.SearchInputs;
import cc.jcsw.search.SearchAreaData;
import cc.jcsw.search.SearchOuput;
import cc.jcsw.search.Searcher;
import cc.utils.SearchUtils;

/**
 * A rest controller for drop down data, config data and for doing the search.
 * 
 * @author carcelc
 *
 */
@RestController
@RequestMapping("/data")
public class DataController {

    public static final String VERSIONS = "VERSIONS";
    public static final String BRANCHES = "BRANCHES";
    public static final String DIRECTORIES = "DIRECTORIES";
    public static final String FILE_EXTENSIONS = "FILE_EXTENSIONS";
    public static final String LAST_INDEX_DATE = "LAST_INDEX_DATE";

    @Autowired
    private SearchAreaData searchAreaData;

    @Autowired
    private Searcher searcher;

    @Autowired
    private SearchUtils searchUtils;

    @RequestMapping(value = "/versions", method = RequestMethod.GET)
    public List<Integer> version() {
        return searchAreaData.getJbossVersions();
    }

    @RequestMapping(value = "/branches", method = RequestMethod.GET)
    public List<String> branches() {
        return searchAreaData.getBranches();
    }

    @RequestMapping(value = "/directories", method = RequestMethod.GET)
    public List<String> directories() {
        return searchAreaData.getDirectories();
    }

    @RequestMapping(value = "/fileExtensions", method = RequestMethod.GET)
    public List<String> fileExtensions() {
        return searchAreaData.getFileExtensions();
    }

    @RequestMapping(value = "/lastIndexDate", method = RequestMethod.GET, produces = "text/plain")
    public String lastIndexDate() {
        return Long.valueOf(searchAreaData.getLastIndexTime().getTime()).toString();
    }

    @RequestMapping(value = "/allSearchData", method = RequestMethod.GET)
    public Map<String, List<?>> allSearchData(HttpServletRequest request) {
        Map<String, List<?>> result = new HashMap<>();
        result.put(BRANCHES, branches());
        result.put(VERSIONS, version());
        result.put(DIRECTORIES, directories());
        result.put(FILE_EXTENSIONS, fileExtensions());
        result.put(LAST_INDEX_DATE, Collections.singletonList(lastIndexDate()));
        result.put("isAdmin1", Collections.singletonList(request.isUserInRole("Admin1")));
        result.put("isAdmin2", Collections.singletonList(request.isUserInRole("Admin2")));
        return result;

    }

    /**
     * Works: http://localhost:8080/JavaCodeSearchWar/data/search/10/0/bob?folder=admin&fileExtension=java&branch=jboss5&version=5
     * 
     * @param inputs
     * @return
     * @throws SolrServerException
     */
    @RequestMapping(value = "/search/{rows}/{start}/{query}")
    public SearchOuput search(SearchInputs inputs, @RequestParam(required = false) String directory,
            @RequestParam(required = false) String fileExtension, @RequestParam(required = false) String branch,
            @RequestParam(required = false) Integer version) throws SolrServerException {
        inputs.setFolder(directory);
        inputs.setFileExtension(fileExtension);
        inputs.setBranch(branch);
        inputs.setVersion(version);
        return searcher.search(inputs);
    }

    /**
     * Load the info needed to create the svn url returning the parts before and after the file name.
     * 
     * @return
     */
    @RequestMapping(value = "/configData")
    public Map<String, String> svnInfo() {

        Map<String, String> result = new HashMap<>();
        result.put("svnPre", searchUtils.getConfigOptions().get("svn_pre"));
        result.put("svnPost", searchUtils.getConfigOptions().get("svn_post"));
        return result;
    }
}
