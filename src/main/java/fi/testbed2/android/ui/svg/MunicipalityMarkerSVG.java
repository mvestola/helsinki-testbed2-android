package fi.testbed2.android.ui.svg;

import fi.testbed2.android.app.Logger;
import fi.testbed2.util.ColorUtil;
import lombok.Getter;

/**
 * Map marker image as SVG for municipalities.
 */
public class MunicipalityMarkerSVG {

    @Getter
    private String xmlContent;

    public MunicipalityMarkerSVG(String colorHex) {

        double opacity = ColorUtil.getOpacityFromARGB(colorHex);
        String colorWithoutAlpha = ColorUtil.getColorWithoutAlpha(colorHex);
        int strokeWidth = 20;

        Logger.debug("MapPoint opacity: " + opacity);
        Logger.debug("MapPoint color: " + colorWithoutAlpha);

        xmlContent = "<?xml version=\"1.0\"?>\n" +
                "<svg width=\"200\" height=\"200\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                " <g>\n" +
                "  <title>Layer 1</title>\n" +
                "  <g id=\"Layer_1\">\n" +
                "    <circle id=\"point_circle\" cx=\"100\" cy=\"100\" r=\"80\" stroke=\"#000000\" stroke-width=\""+strokeWidth+"\" fill=\""+colorWithoutAlpha+"\" fill-opacity=\""+opacity+"\" />\n" +
                "  </g>\n" +
                " </g>\n" +
                "</svg>";
    }

}
