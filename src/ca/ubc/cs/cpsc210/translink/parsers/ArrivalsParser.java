package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {

    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop             stop to which parsed arrivals are to be added
     * @param jsonResponse    the JSON response produced by Translink
     * @throws JSONException  when JSON response does not have expected format
     * @throws ArrivalsDataMissingException  when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {
        JSONArray routes = new JSONArray(jsonResponse);

        for (int index = 0; index < routes.length(); index++) {

            JSONObject route = routes.getJSONObject(index);
            parseARoute(route,stop);
        }
    }

    private static void parseARoute(JSONObject route, Stop stop) throws JSONException, ArrivalsDataMissingException {
        try {
            String routeNo = route.getString("RouteNo");
            Route theRoute = RouteManager.getInstance().getRouteWithNumber(routeNo);

            JSONArray schedules = route.getJSONArray("Schedules");
            for (int index = 0; index < schedules.length(); index++) {

                JSONObject schedule = schedules.getJSONObject(index);
                parseASchedule(schedule, theRoute, stop);
            }
            if (stop.getArrivals().isEmpty()) {
                throw new ArrivalsDataMissingException("Arrivals data missing");
            }
        }
        catch(JSONException e) {
            throw new ArrivalsDataMissingException("Route number missing");
        }
    }

    private static void parseASchedule(JSONObject route,Route theRoute, Stop stop) throws JSONException, ArrivalsDataMissingException {
        try {
            int time = route.getInt("ExpectedCountdown");
            String destination = route.getString("Destination");
            String status = route.getString("ScheduleStatus");

            Arrival newArrival = new Arrival(time, destination, theRoute);
            stop.addArrival(newArrival);
        }
        catch (JSONException e) {
            // do not add arrival
        }
    }
}
