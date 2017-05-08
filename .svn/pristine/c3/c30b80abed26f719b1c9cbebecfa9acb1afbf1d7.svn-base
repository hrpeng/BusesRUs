package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;

    public RouteParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException {
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }
    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when JSON data does not have expected format
     * @throws RouteDataMissingException when
     * <ul>
     *  <li> JSON data is not an array </li>
     *  <li> JSON data is missing Name, StopNo, Routes or location elements for any stop</li>
     * </ul>
     */

    public void parseRoutes(String jsonResponse)
            throws JSONException, RouteDataMissingException {
        JSONArray routes = new JSONArray(jsonResponse);

        for (int index = 0; index < routes.length(); index++) {

            JSONObject route = routes.getJSONObject(index);
            parseARoute(route);
        }
    }

    public void parseARoute(JSONObject route)
            throws JSONException, RouteDataMissingException {
        try {
            String name = route.getString("Name");
            String routeNo = route.getString("RouteNo");
            //Route newRoute = new Route(routeNo,name);
            //parseRoutePatterns(route.getJSONArray("Patterns"), newRoute);

            try {
                JSONArray patterns = route.getJSONArray("Patterns");
                parseRoutePatterns(patterns, RouteManager.getInstance().getRouteWithNumber(routeNo, name));
            } catch (JSONException e) {
                throw new RouteDataMissingException("Pattern missing or not an array");

            }
        }
        catch(JSONException e) {
            throw new RouteDataMissingException("Route data missing");
        }
    }

    public void parseRoutePatterns(JSONArray patterns, Route newRoute)
            throws JSONException, RouteDataMissingException {

        for (int index = 0; index < patterns.length(); index++) {

            JSONObject pattern = patterns.getJSONObject(index);
            parseARoutePattern(pattern, newRoute);
        }
    }

    public void parseARoutePattern(JSONObject pattern, Route newRoute)
            throws JSONException, RouteDataMissingException {
        try {

            String destination = pattern.getString("Destination");
            String direction = pattern.getString("Direction");
            String patternNo = pattern.getString("PatternNo");

            new RoutePattern(patternNo, destination, direction, newRoute);
        }
        catch(JSONException e) {
            throw new RouteDataMissingException("Route pattern data missing");
        }
    }
}

