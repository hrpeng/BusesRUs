package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {

    private String filename;

    public StopParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse stop data from the file and add all stops to stop manager.
     *
     */
    public void parse() throws IOException, StopDataMissingException, JSONException {
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }
    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when JSON data does not have expected format
     * @throws StopDataMissingException when
     * <ul>
     *  <li> JSON data is not an array </li>
     *  <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop</li>
     * </ul>
     */

    public void parseStops(String jsonResponse)
            throws JSONException, StopDataMissingException {
        JSONArray stops = new JSONArray(jsonResponse);

        for (int index = 0; index < stops.length(); index++) {
            JSONObject stop = stops.getJSONObject(index);
            parseAStop(stop);
        }
    }

    public void parseAStop(JSONObject stop)
            throws JSONException, StopDataMissingException {
        try {
            String name = stop.getString("Name");
            int stopNumber = stop.getInt("StopNo");
            double latitude = stop.getDouble("Latitude");
            double longitude = stop.getDouble("Longitude");


            Stop newStop = StopManager.getInstance().getStopWithId(stopNumber, name, new LatLon(latitude, longitude));

            String routesString = stop.getString("Routes");
            for (String routeNumber : (Arrays.asList(routesString.split(",")))) {
                Route route = RouteManager.getInstance().getRouteWithNumber(routeNumber);
                route.addStop(newStop);
                newStop.addRoute(route);
            }
        }
        catch(JSONException e) {
            throw new StopDataMissingException("Stop data missing");
        }
    }
}
