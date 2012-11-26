package fi.testbed2.android.ui.svg;

import fi.testbed2.android.app.Logging;
import fi.testbed2.util.ColorUtil;

/**
 * Map marker image as SVG for user location.
 * The SVG was copied from: http://www.clker.com/clipart-map-29.html
 */
public class LocationMarkerSVG {

    private String xmlContent;

    public LocationMarkerSVG(String colorHex) {

        double opacity = ColorUtil.getOpacityFromARGB(colorHex);
        String colorWithoutAlpha = ColorUtil.getColorWithoutAlpha(colorHex);
        int strokeWidth = 60;

        Logging.debug("LocationMarkerSVG opacity: "+opacity);
        Logging.debug("LocationMarkerSVG color: "+colorWithoutAlpha);

        xmlContent = "<?xml version=\"1.0\"?><svg width=\"2481\" height=\"2073\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                " <title>map</title>\n" +
                " <g>\n" +
                "  <title>Layer 1</title>\n" +
                "  <g externalResourcesRequired=\"false\" id=\"svg_1\">\n" +
                "   <path id=\"svg_2\" d=\"m1240.940063,1945.628906c-38.766113,-190.300781 -107.116089,-348.665039 -189.903076,-495.439697c-61.406982,-108.872314 -132.543945,-209.363281 -198.363892,-314.938232c-21.972168,-35.243896 -40.934082,-72.477051 -62.047119,-109.053955c-42.215942,-73.136963 -76.44397,-157.934814 -74.268921,-267.932007c2.124878,-107.473022 33.208008,-193.68396 78.030029,-264.171997c73.718872,-115.934937 197.200928,-210.989014 362.883911,-235.968994c135.465942,-20.42395 262.475098,14.082031 352.542969,66.748047c73.600098,43.037964 130.596069,100.526978 173.920044,168.280029c45.220093,70.715942 76.359009,154.259888 78.971069,263.231934c1.336914,55.829834 -7.804932,107.531982 -20.684082,150.417969c-13.033936,43.40918 -33.996094,79.695068 -52.645996,118.454102c-36.406006,75.658936 -82.04895,144.981934 -127.85498,214.345947c-136.437012,206.605957 -264.496094,417.309814 -320.579956,706.026855z\" stroke-miterlimit=\"10\" stroke-width=\""+strokeWidth+"\" stroke=\"#000000\" fill=\"" + colorWithoutAlpha + "\" fill-opacity=\""+opacity+"\" clip-rule=\"evenodd\" fill-rule=\"evenodd\"/>\n" +
                "   <circle id=\"svg_4\" r=\"183.332993\" cy=\"757.047\" cx=\"1239.546\" clip-rule=\"evenodd\" fill-rule=\"evenodd\"/>\n" +
                "  </g>\n" +
                " </g>\n" +
                "</svg>";
    }

    public String getXmlContent() {
        return xmlContent;
    }

}
