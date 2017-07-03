package cc.jcsw.controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Redirect from old struts2 actions. Should handle: <br>
 * http://localhost:8081/JavaCodeSearchWar/search_search.action <br>
 * http://localhost:8081/JavaCodeSearchWar/search_search.action?dsp.start=&dsp.rows=10&dsp.version=5&dsp.branch=jboss5&dsp.folder=&dsp.
 * fileExtension=&dsp.query=bob <br>
 * http://localhost:8081/JavaCodeSearchWar/display_display.action?dsp.id=fileName
 *
 */
@Controller
public class RedirectController {

    /**
     * Holder for "dsp" since struts used dot based params: dsp.version
     *
     */
    public static class DspHolder {

        private Dsp dsp;

        public Dsp getDsp() {
            return dsp;
        }

        public void setDsp(Dsp dsp) {
            this.dsp = dsp;
        }

    }

    /**
     * Dsp vals
     *
     */
    public static class Dsp {

        private String branch;
        private Integer version;
        private Integer rows;
        private Integer start;
        private String query;
        private String folder;
        private String fileExtension;

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

        public Integer getRows() {
            return rows;
        }

        public void setRows(Integer rows) {
            this.rows = rows;
        }

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
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

    }

    @RequestMapping("/search_search.action")
    public void search(HttpServletRequest request, HttpServletResponse response, @ModelAttribute DspHolder holder)
            throws URISyntaxException, IOException {

        URIBuilder b = new URIBuilder(request.getContextPath() + "/app/search/home");

        if (null != holder.getDsp()) {
            Dsp dsp = holder.getDsp();
            b.addParameter("version", integerToString(dsp.getVersion()));
            b.addParameter("rows", dsp.getRows() + "");
            b.addParameter("start", integerToString(dsp.getStart()));
            b.addParameter("branch", dsp.getBranch());
            b.addParameter("directory", dsp.folder);
            b.addParameter("text", dsp.query);
        }

        response.sendRedirect(b.toString());
    }

    @RequestMapping("/display_display.action")
    public void display(HttpServletRequest request, HttpServletResponse response, @RequestParam("dsp.id") String id)
            throws IOException, URISyntaxException {

        URIBuilder b = new URIBuilder(request.getContextPath() + "/app/search/display");
        b.addParameter("file", id);
        response.sendRedirect(b.toString());
    }

    private String integerToString(Integer i) {
        if (null == i) {
            return "";
        } else {
            return i.toString();
        }
    }

}
