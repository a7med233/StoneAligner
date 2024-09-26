package swapstones.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * Stopwatch model that track the elapsed time.
 */
public class Stopwatch {

    private final LongProperty seconds;
    private final StringProperty hhmmss;
    private final Timeline timeline;

    /**
     * Creates a{@code Stopwatch} object.
     */
    public Stopwatch() {
        seconds = new SimpleLongProperty();
        hhmmss = new SimpleStringProperty();
        timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> seconds.set(seconds.get() + 1)),
                new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        hhmmss.bind(Bindings.createStringBinding(() -> DurationFormatUtils.formatDuration(seconds.get() * 1000, "HH:mm:ss"), seconds));
    }

    /**
     * @return a property to access the number of seconds
     */
    public LongProperty secondsProperty() {
        return seconds;
    }

    /**
     * @return a property to access the formatted elapsed time
     */

    public StringProperty hhmmssProperty() {
        return hhmmss;
    }

    /**
     * Starts the stopwatch.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Stops the stopwatch.
     */
    public void stop() {
        timeline.pause();
    }

    /**
     * Resets the stopwatch. This method can only be called when the stopwatch is paused.
     *
     * @throws IllegalStateException if the stopwatch is not paused when reset is called
     */
    public void reset() {
        if (timeline.getStatus() != Animation.Status.PAUSED) {
            throw new IllegalStateException();
        }
        seconds.set(0);
    }

    /**
     * Gets the current status of the stopwatch.
     *
     * @return the status of the stopwatch
     */
    public Animation.Status getStatus() {
        return timeline.getStatus();
    }

}
