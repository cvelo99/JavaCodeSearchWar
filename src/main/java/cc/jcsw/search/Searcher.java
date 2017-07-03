package cc.jcsw.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.jcsw.beans.SearchInputs;
import cc.utils.SearchUtils;

import com.google.common.collect.Lists;

/**
 * Search the solr instance.
 * 
 * @author Chris Carcel
 *
 */
@Component
public class Searcher {

    @Autowired
    private SearchUtils searchUtils;

    private static final Integer START_DEFAULT = 0;

    /**
     * Perform the search.
     * 
     * @param inputs
     *            the inputs to the search.
     * @return if {@link SearchInputs#isAllFields()} is true, return all the fields in the document as a map. Otherwise return
     *         <ul>
     *         <li>id</li>
     *         <li>full_file_path</li>
     *         <li>file_name</li>
     *         <li>branch</li>
     *         <li>jboss_version</li>
     *         </ul>
     */
    public SearchOuput search(SearchInputs inputs) {

        if (null == inputs.getStart()) {
            inputs.setStart(START_DEFAULT);
        }

        SolrQuery b = new SearchQueryBuilder().populateFromDsp(inputs).buildSearch();

        HttpSolrServer server = searchUtils.getServer();
        QueryResponse response;
        try {
            response = server.query(b);
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        }

        SearchOuput o = new SearchOuput();
        o.setQueryResultTime(DurationFormatUtils.formatDuration(response.getElapsedTime(), "S"));
        o.setNumberResultsFound(response.getResults().getNumFound());

        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

        if (inputs.isAllFields()) {

            for (SolrDocument doc : response.getResults()) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                doc.entrySet().stream().forEach(t -> map.put(t.getKey(), t.getValue()));
                results.add(map);
            }

        } else {
            ArrayList<String> useKeys = Lists.newArrayList("id", "full_file_path", "file_name", "branch", "jboss_version");

            for (SolrDocument doc : response.getResults()) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                doc.entrySet().stream().filter(t -> useKeys.contains(t.getKey())).forEach(t -> map.put(t.getKey(), t.getValue()));
                results.add(map);
            }
        }

        o.setResults(results);
        return o;
    }
}
