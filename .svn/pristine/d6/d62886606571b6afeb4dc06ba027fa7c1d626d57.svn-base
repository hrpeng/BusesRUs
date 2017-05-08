package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for routes stored in a compact format in a txt file
 */
public class RouteMapParser {
    private String fileName;

    public RouteMapParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parse the route map txt file
     */
    public void parse() {
        DataProvider dataProvider = new FileDataProvider(fileName);
        try {
            String c = dataProvider.dataSourceToString();
            if (!c.equals("")) {
                int posn = 0;
                while (posn < c.length()) {
                    int endposn = c.indexOf('\n', posn);
                    String line = c.substring(posn, endposn);
                    parseOnePattern(line);
                    posn = endposn + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one route pattern, adding it to the route that is named within it
     * @param str
     */
    private void parseOnePattern(String str) {
        Pattern routeNumberPattern = Pattern.compile("(N)(\\w+)(\\-)");
        Matcher routeNumberMatcher = routeNumberPattern.matcher(str);
        routeNumberMatcher.lookingAt();
        String routeNumber = routeNumberMatcher.group(2);

        Pattern patternNamePattern = Pattern.compile("(\\-)([A-Z]\\w*\\-?\\w+)(\\;)");
        Matcher patternNameMatcher = patternNamePattern.matcher(str);
        patternNameMatcher.find();
        String patternName = patternNameMatcher.group(2);

        //Pattern locationPattern = Pattern.compile("(\\d+\\.\\d+)(\\;)(\\-\\d+\\.\\d+)(\\;)");
        Pattern locationPattern = Pattern.compile("(\\d+\\.\\d+)(\\;)(\\-?\\d+\\.\\d+)(\\;)");
        Matcher locationMatcher = locationPattern.matcher(str);
        List<LatLon> locations = new ArrayList<LatLon>();
        while(locationMatcher.find()) {
            double lat = Double.parseDouble(locationMatcher.group(1));
            double lon = Double.parseDouble(locationMatcher.group(3));
            locations.add(new LatLon(lat, lon));
        }
        storeRouteMap(routeNumber, patternName, locations);
    }

    /**
     * Store the parsed pattern into the named route
     * Your parser should call this method to insert each route pattern into the corresponding route object
     * There should be no need to change this method
     *
     * @param routeNumber       the number of the route
     * @param patternName       the name of the pattern
     * @param elements          the coordinate list of the pattern
     */
    private void storeRouteMap(String routeNumber, String patternName, List<LatLon> elements) {
        Route r = RouteManager.getInstance().getRouteWithNumber(routeNumber);
        RoutePattern rp = r.getPattern(patternName);
        rp.setPath(elements);
    }
}
