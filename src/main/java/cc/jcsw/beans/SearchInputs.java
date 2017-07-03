package cc.jcsw.beans;

import cc.jcsw.search.Searcher;

/**
 * POJO, inputs to query solr java source code.
 * 
 * @author Chris Carcel
 *
 */
public class SearchInputs {

    private String query;
    private Integer rows;
    private Integer start;
    private String folder;
    private String fileExtension;
    private String branch;
    private Integer version;
    private String fqn;
    private String id;
    /**
     * If true, return all fields from solr.
     * 
     * @see Searcher#search(SearchInputs)
     */
    private boolean allFields;

    /**
     * If true, return all fields from solr.
     * 
     * @see Searcher#search(SearchInputs)
     * @return
     */
    public boolean isAllFields() {
        return allFields;
    }

    /**
     * If true, return all fields from solr.
     * 
     * @see Searcher#search(SearchInputs)
     * @param allFields
     */
    public void setAllFields(boolean allFields) {
        this.allFields = allFields;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFqn() {
        return fqn;
    }

    public Integer getRows() {
        return this.rows;
    }

    public Integer getStart() {
        return this.start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setFqn(String fqn) {
        this.fqn = fqn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
