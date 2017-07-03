package cc.jcsw.search;

import java.util.List;
import java.util.Map;

import cc.jcsw.beans.SearchInputs;

/**
 * Output to a search done in {@link Searcher} .
 * 
 * @author Chris Carcel
 *
 */
public class SearchOuput {

    private String queryResultTime;
    private long numberResultsFound;
    /**
     * The solr document as a map.
     * 
     * @see Searcher#search(SearchInputs)
     */
    private List<Map<String, Object>> results;

    public String getQueryResultTime() {
        return queryResultTime;
    }

    public long getNumberResultsFound() {
        return numberResultsFound;
    }

    /**
     * The solr document as a map.
     * 
     * @see Searcher#search(SearchInputs)
     * @return
     */
    public List<Map<String, Object>> getResults() {
        return results;
    }

    public void setQueryResultTime(String queryResultTime) {
        this.queryResultTime = queryResultTime;
    }

    public void setNumberResultsFound(long numberResultsFound) {
        this.numberResultsFound = numberResultsFound;
    }

    /**
     * The solr document as a map.
     * 
     * @see Searcher#search(SearchInputs)
     * @param results
     */
    public void setResults(List<Map<String, Object>> results) {
        this.results = results;
    }

}
