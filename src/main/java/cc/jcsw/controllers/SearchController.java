package cc.jcsw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for the search page and related fragments.
 * 
 * @author Chris Carcel
 *
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    /**
     * The /home mapping is the primary. The /display is used when bookmarking a display page.
     * 
     * @return
     */
    @RequestMapping(value = { "/home", "/display" }, method = RequestMethod.GET)
    public String app() {
        return "/home";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search() {
        return "/fragments/search";
    }

    @RequestMapping(value = "/displayPage", method = RequestMethod.GET)
    public String display() {
        return "/fragments/display";
    }

}
