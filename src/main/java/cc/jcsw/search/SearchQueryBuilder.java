package cc.jcsw.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.jcsw.beans.SearchInputs;

/**
 * Build the solr query for a search.
 * 
 * @author Chris Carcel
 *
 */
public class SearchQueryBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(SearchQueryBuilder.class);

    private Integer version;
    private String branch;
    private String query;
    private Integer rows;
    private Integer start;
    private String id;
    private String fileExtension;
    private String folder;
    private String fqn;

    /**
     * Add filter query.
     * 
     * @param q
     * @param version
     * @param branch
     */
    private void buildFilterQuery(SolrQuery q) {

        List<String> filters = new ArrayList<>(2);

        if (null != version) {
            String f = "jboss_version:" + version;
            filters.add(f);
        }

        if (StringUtils.isNotBlank(branch)) {
            String f = "branch:" + branch;
            filters.add(f);
        }

        if (StringUtils.isNotBlank(fileExtension)) {
            String f = "file_extension:" + fileExtension;
            filters.add(f);
        }

        if (StringUtils.isNotBlank(folder)) {
            String f = "folder:" + folder;
            filters.add(f);
        }

        String[] array = filters.toArray(new String[filters.size()]);

        q.setFilterQueries(array);
    }

    public SolrQuery buildSearch() {
        SolrQuery q = new SolrQuery();

        buildFilterQuery(q);

        if (StringUtils.isNotBlank(id)) {
            q.setQuery("id:" + escape(id));
        }

        if (StringUtils.isNotBlank(fqn)) {
            q.setQuery("fqn:\"" + fqn + "\"");
        }

        if (StringUtils.isNotBlank(query)) {

            q.setQuery("{!type=edismax qf=\"file_name2^3 full_file_path2^2 name_and_file\" q.op=AND}" + query);

            LOG.trace("Query: {}", q.getQuery());
        }

        q.setStart(this.start);
        q.setRows(this.rows);

        LOG.trace("q: {}", q);

        return q;

    }

    private String escape(String s) {
        s = s.replace("\\", "\\\\");
        s = s.replace(".", "\\.");
        return s;
    }

    public Integer getRows() {
        return rows;
    }

    public SearchQueryBuilder setRows(Integer rows) {
        this.rows = rows;
        return this;
    }

    public Integer getStart() {
        return start;
    }

    public SearchQueryBuilder setStart(Integer start) {
        this.start = start;
        return this;
    }

    public String getBranch() {
        return branch;
    }

    public String getQuery() {
        return query;
    }

    public Integer getVersion() {
        return version;
    }

    /**
     * Utility method to populate this object from the dsp.
     * 
     * @param dsp
     * @return
     */
    public SearchQueryBuilder populateFromDsp(SearchInputs dsp) {
        this.version = dsp.getVersion();
        this.branch = dsp.getBranch();
        this.query = dsp.getQuery();
        this.start = dsp.getStart();
        this.rows = dsp.getRows();
        this.folder = dsp.getFolder();
        this.fileExtension = dsp.getFileExtension();
        this.fqn = dsp.getFqn();
        this.id = dsp.getId();
        return this;
    }

    public SearchQueryBuilder setBranch(String branch) {
        this.branch = branch;
        return this;
    }

    public SearchQueryBuilder setQuery(String query) {
        this.query = query;
        return this;
    }

    public SearchQueryBuilder setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public SearchQueryBuilder setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getId() {
        return id;
    }

    public SearchQueryBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public SearchQueryBuilder setFqn(String fqn) {
        this.fqn = fqn;
        return this;
    }

    public String getFqn() {
        return fqn;
    }
}
