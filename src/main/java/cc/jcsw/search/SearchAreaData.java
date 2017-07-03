package cc.jcsw.search;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.response.TermsResponse.Term;
import org.apache.solr.common.params.TermsParams;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cc.utils.SearchUtils;

import com.google.common.collect.Lists;

/**
 * Background data on the search page: drop downs and last modified time of the index.
 * 
 * @author Chris Carcel
 *
 */
@Component
@Scope("prototype")
public class SearchAreaData {

    private static final Logger LOG = LoggerFactory.getLogger(SearchAreaData.class);

    @Resource
    private SearchUtils searchUtils;

    private List<Integer> versions;

    private List<String> branches;

    private List<String> directories;

    private List<String> fileExtensions;

    private Date lastIndexTime;

    private boolean loaded = false;

    public List<Integer> getJbossVersions() {
        loadData();
        return this.versions;
    }

    public List<String> getBranches() {
        loadData();
        return branches;
    }

    public Date getLastIndexTime() {
        loadData();
        return lastIndexTime;
    }

    public List<String> getFileExtensions() {
        loadData();
        return fileExtensions;
    }

    public List<String> getDirectories() {
        loadData();
        return directories;
    }

    private void loadData() {

        if (loaded) {
            return;
        }

        try {

            List<Integer> versions = getOptionsList(this.searchUtils.getServer(), Integer.class, "jboss_version");
            Collections.sort(versions);
            this.versions = versions;

            List<String> branches = getOptionsList(this.searchUtils.getServer(), String.class, "branch");
            Collections.sort(branches);
            this.branches = branches;

            List<String> fileExtensions = getOptionsList(this.searchUtils.getServer(), String.class, "file_extension");
            Collections.sort(fileExtensions);
            this.fileExtensions = fileExtensions;

            List<String> dirs = getOptionsList(this.searchUtils.getServer(), String.class, "folder");
            Collections.sort(dirs);
            this.directories = dirs;

            Date lastIndexTime = loadLastIndexTime(this.searchUtils.getServer());
            this.lastIndexTime = lastIndexTime;

            loaded = true;

        } catch (Throwable e) {
            LOG.error("Error loading data", e);
            throw new IllegalStateException(e);
        }

    }

    /**
     * Load the last modified date of the index.
     * 
     * @param server
     * @return
     * @throws SolrServerException
     * @throws ParseException
     * @throws XPathExpressionException
     */

    @SuppressWarnings("rawtypes")
    private Date loadLastIndexTime(HttpSolrServer server) throws SolrServerException, ParseException, XPathExpressionException {
        SolrQuery query = new SolrQuery();
        query.set("qt", "/admin/luke");
        query.set("wt", "xml");
        query.set("show", "index");
        query.set("umTerms", "0");
        query.set("_", "1370851203426");
        QueryResponse rsp = server.query(query, METHOD.GET);

        SimpleOrderedMap map = (SimpleOrderedMap) rsp.getResponse().get("index");
        Date lm = (Date) map.get("lastModified");
        return lm;

    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> convertOptionsListToMap(Class<K> clazzK, Class<V> clazzV, List<K> values) {
        TreeMap<K, V> result = new TreeMap<>();
        for (K k : values) {
            result.put(k, (V) k);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <K> List<K> getOptionsList(HttpSolrServer server, Class<K> clazz, String fld) throws SolrServerException {

        List<K> result = Lists.newArrayList();

        SolrQuery q = new SolrQuery();

        q.setRequestHandler("/terms");
        q.setTerms(true);
        q.setTermsLimit(Integer.MAX_VALUE);
        q.setRows(Integer.MAX_VALUE);
        q.setParam(TermsParams.TERMS_FIELD, fld);
        QueryResponse queryResult = server.query(q);
        TermsResponse termsResponse = queryResult.getTermsResponse();
        List<Term> terms = termsResponse.getTerms(fld);
        if (CollectionUtils.isNotEmpty(terms)) {
            for (Term term : terms) {
                String termString = term.getTerm();
                if (String.class.equals(clazz)) {
                    result.add((K) termString);
                } else if (Integer.class.equals(clazz)) {
                    result.add((K) Integer.valueOf(termString));
                } else {
                    throw new IllegalStateException("No code to handle class " + clazz);
                }
            }
        }

        return result;

    }

}
