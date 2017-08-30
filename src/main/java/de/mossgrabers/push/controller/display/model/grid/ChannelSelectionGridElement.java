// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.push.controller.display.model.grid;

import de.mossgrabers.push.PushConfiguration;
import de.mossgrabers.push.controller.display.model.ChannelType;
import de.mossgrabers.push.controller.display.model.LayoutSettings;
import de.mossgrabers.push.controller.display.model.ResourceHandler;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.api.GraphicsOutput;
import com.bitwig.extension.api.Image;

import java.util.EnumMap;


/**
 * An element in the grid which contains a menu and a channels' icon, name and color.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class ChannelSelectionGridElement extends AbstractGridElement
{
    private static final EnumMap<ChannelType, String> ICONS            = new EnumMap<> (ChannelType.class);

    protected static final int                        TRACK_ROW_HEIGHT = (int) (1.6 * UNIT);

    static
    {
        ICONS.put (ChannelType.AUDIO, "track/audio_track.svg");
        ICONS.put (ChannelType.INSTRUMENT, "track/instrument_track.svg");
        ICONS.put (ChannelType.GROUP, "track/group_track.svg");
        ICONS.put (ChannelType.EFFECT, "track/return_track.svg");
        ICONS.put (ChannelType.HYBRID, "track/hybrid_track.svg");
        ICONS.put (ChannelType.MASTER, "track/master_track.svg");
        ICONS.put (ChannelType.LAYER, "track/multi_layer.svg");
    }

    private final ChannelType type;


    /**
     * Constructor.
     *
     * @param menuName The text for the menu
     * @param isMenuSelected True if the menu is selected
     * @param name The of the grid element (track name, parameter name, etc.)
     * @param color The color to use for the header, may be null
     * @param isSelected True if the grid element is selected
     * @param type The type of the track
     */
    public ChannelSelectionGridElement (final String menuName, final boolean isMenuSelected, final String name, final Color color, final boolean isSelected, final ChannelType type)
    {
        super (menuName, isMenuSelected, null, name, color, isSelected);
        this.type = type;
    }


    /**
     * Get the type of the channel.
     *
     * @return The type
     */
    public ChannelType getType ()
    {
        return this.type;
    }


    /** {@inheritDoc} */
    @Override
    public String getIcon ()
    {
        return ICONS.get (this.type);
    }


    /** {@inheritDoc} */
    @Override
    public void draw (final GraphicsOutput gc, final double left, final double width, final double height, final LayoutSettings layoutSettings, PushConfiguration configuration)
    {
        this.drawMenu (gc, left, width, layoutSettings);

        final String name = this.getName ();
        // Element is off if the name is empty
        if (name == null || name.length () == 0)
            return;

        final double trackRowTop = height - TRACK_ROW_HEIGHT - UNIT - SEPARATOR_SIZE;
        this.drawTrackInfo (gc, left, width, height, trackRowTop, name, layoutSettings, configuration);
    }


    /**
     * Draws the tracks info, like icon, color and name.
     *
     * @param gc The graphics context
     * @param left The left bound of the drawing area
     * @param width The width of the drawing area
     * @param height The height of the drawing area
     * @param trackRowTop The top of the drawing area
     * @param name The name of the track
     * @param layoutSettings The layout settings
     */
    protected void drawTrackInfo (final GraphicsOutput gc, final double left, final double width, final double height, final double trackRowTop, final String name, final LayoutSettings layoutSettings, final PushConfiguration configuration)
    {
        // Draw the background
        final Color backgroundColor = configuration.getColorBackground ();
        gc.setColor (this.isSelected () ? ColorEx.brighter (backgroundColor) : backgroundColor);
        gc.rectangle (left, trackRowTop + 1, width, height - UNIT - 1);
        gc.fill ();

        // The tracks icon and name
        final String iconName = this.getIcon ();
        if (iconName != null)
        {
            final Color textColor = layoutSettings.getTextColor ();
            final Image icon = ResourceHandler.getSVGImage (iconName);

            // TODO find a solution to draw icons
            // gc.setColor (textColor);
            // gc.setOperator (Operator.IN);
            // gc.rectangle (left + (DOUBLE_UNIT - icon.getWidth ()) / 2, height - TRACK_ROW_HEIGHT
            // - UNIT + (TRACK_ROW_HEIGHT - icon.getHeight ()) / 2, icon.getWidth (), icon.getHeight
            // ());
            // gc.fill ();
            // gc.setOperator (Operator.SOURCE);
            gc.drawImage (icon, left + (DOUBLE_UNIT - icon.getWidth ()) / 2, height - TRACK_ROW_HEIGHT - UNIT + (TRACK_ROW_HEIGHT - icon.getHeight ()) / 2);

            gc.setFontSize (1.2 * UNIT);
            drawTextInBounds (gc, name, left + DOUBLE_UNIT, height - TRACK_ROW_HEIGHT - UNIT + (TRACK_ROW_HEIGHT - gc.getFontExtents ().getHeight ()) / 2, width, TRACK_ROW_HEIGHT, Align.LEFT, textColor);
        }

        // The track color section
        gc.setColor (this.getColor ());
        gc.rectangle (left, height - UNIT, width, UNIT);
        gc.fill ();
    }
}
