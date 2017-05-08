package ca.ubc.cs.cpsc210.translink.util;

/**
 * Compute relationships between points, lines, and rectangles represented by LatLon objects
 */
public class Geometry {
    /**
     * Return true if the point is inside of, or on the boundary of, the rectangle formed by northWest and southeast
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param point             the point in question
     * @return                  true if the point is on the boundary or inside the rectangle
     */
    public static boolean rectangleContainsPoint(LatLon northWest, LatLon southEast, LatLon point) {
        boolean isBetweenLatitude = northWest.getLatitude() >= point.getLatitude() && southEast.getLatitude() <= point.getLatitude();
        boolean isBetweenLongitude = northWest.getLongitude() <=point.getLongitude() && southEast.getLongitude() >= point.getLongitude();
        return isBetweenLatitude && isBetweenLongitude;
    }

    /**
     * Return true if the rectangle intersects the line
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param src               one end of the line in question
     * @param dst               the other end of the line in question
     * @return                  true if any point on the line is on the boundary or inside the rectangle
     */
    public static boolean rectangleIntersectsLine(LatLon northWest, LatLon southEast, LatLon src, LatLon dst) {
        if ((between(northWest.getLongitude(),southEast.getLongitude(),src.getLongitude()) &&
                between(southEast.getLatitude(), northWest.getLatitude(), src.getLatitude())) ||
                (between(northWest.getLongitude(),southEast.getLongitude(),dst.getLongitude()) &&
                        between(southEast.getLatitude(), northWest.getLatitude(), dst.getLatitude())))
            return true;
        if ((src.getLongitude() == dst.getLongitude() && !between(northWest.getLongitude(),southEast.getLongitude(), src.getLongitude())))
            return false;
        double la = (dst.getLatitude()-src.getLatitude()) / (dst.getLongitude() - src.getLongitude());
        double lb = (src.getLatitude() + dst.getLatitude() - src.getLongitude() * la - dst.getLongitude() * la) / 2;
        double x1 = (northWest.getLatitude() - lb) / la;
        double x2 = (southEast.getLatitude() - lb) / la;
        return (between(northWest.getLongitude(),southEast.getLongitude(),x1) || between(northWest.getLongitude(),southEast.getLongitude(),x2));
    }

    /**
     * A utility method that you might find helpful in implementing the two previous methods
     * Return true if x is >= lwb and <= upb
     * @param lwb      the lower boundary
     * @param upb      the upper boundary
     * @param x         the value in question
     * @return          true if x is >= lwb and <= upb
     */
    private static boolean between(double lwb, double upb, double x) {
        return lwb <= x && x <= upb;
    }
}
