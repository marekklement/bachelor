package cz.cvut.fel.adaptivestructure.entity;

import java.time.LocalDateTime;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;
import cz.cvut.fel.adaptivestructure.converter.ListStringConverter;
import cz.cvut.fel.adaptivestructure.converter.LocalDateTimeConverter;

/**
 * Node represents one node in structure.
 *
 * @author Marek Klement
 */
@Entity
public class Node extends Identifiable {
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "parent")
    private int parent;

    @ColumnInfo(name = "version")
    private int version;

    @TypeConverters(ListStringConverter.class)
    private List<String> buttons;

    // node info
    @ColumnInfo(name = "state_changing", defaultValue = "0")
    private long stateChanging;
    @ColumnInfo(name = "visits", defaultValue = "1")
    private long visits;
    @ColumnInfo(name = "disgust", defaultValue = "0")
    private float disgust;
    @ColumnInfo(name = "joy", defaultValue = "0")
    private float joy;
    @ColumnInfo(name = "neutral", defaultValue = "0")
    private float neutral;
    @ColumnInfo(name = "sadness", defaultValue = "0")
    private float sadness;
    @ColumnInfo(name = "anger", defaultValue = "0")
    private float anger;
    // emotion weight
    @ColumnInfo(name = "disgust_weight", defaultValue = "0")
    private long disgustWeight;
    @ColumnInfo(name = "joy_weight", defaultValue = "0")
    private long joyWeight;
    @ColumnInfo(name = "neutral_weight", defaultValue = "0")
    private long neutralWeight;
    @ColumnInfo(name = "sadness_weight", defaultValue = "0")
    private long sadnessWeight;
    @ColumnInfo(name = "anger_weight", defaultValue = "0")
    private long angerWeight;
    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime startVisit;
    @ColumnInfo(name = "visitation_session", defaultValue = "0")
    private long visitationSession;

    public LocalDateTime getStartVisit() {
        return startVisit;
    }

    public void setStartVisit(LocalDateTime startVisit) {
        this.startVisit = startVisit;
    }

    public long getVisitationSession() {
        return visitationSession;
    }

    public void setVisitationSession(long visitationSession) {
        this.visitationSession = visitationSession;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<String> getButtons() {
        return buttons;
    }

    public void setButtons(List<String> buttons) {
        this.buttons = buttons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public long getVisits() {
        return visits;
    }

    public void setVisits(long visits) {
        this.visits = visits;
    }

    public float getDisgust() {
        return disgust;
    }

    public void setDisgust(float disgust) {
        this.disgust = disgust;
    }

    public float getJoy() {
        return joy;
    }

    public void setJoy(float joy) {
        this.joy = joy;
    }

    public float getNeutral() {
        return neutral;
    }

    public void setNeutral(float neutral) {
        this.neutral = neutral;
    }

    public float getSadness() {
        return sadness;
    }

    public void setSadness(float sadness) {
        this.sadness = sadness;
    }

    public float getAnger() {
        return anger;
    }

    public void setAnger(float anger) {
        this.anger = anger;
    }

    public long getDisgustWeight() {
        return disgustWeight;
    }

    public void setDisgustWeight(long disgustWeight) {
        this.disgustWeight = disgustWeight;
    }

    public long getJoyWeight() {
        return joyWeight;
    }

    public void setJoyWeight(long joyWeight) {
        this.joyWeight = joyWeight;
    }

    public long getNeutralWeight() {
        return neutralWeight;
    }

    public void setNeutralWeight(long neutralWeight) {
        this.neutralWeight = neutralWeight;
    }

    public long getSadnessWeight() {
        return sadnessWeight;
    }

    public void setSadnessWeight(long sadnessWeight) {
        this.sadnessWeight = sadnessWeight;
    }

    public long getAngerWeight() {
        return angerWeight;
    }

    public void setAngerWeight(long angerWeight) {
        this.angerWeight = angerWeight;
    }

    public long getStateChanging() {
        return stateChanging;
    }

    public void setStateChanging(long stateChanging) {
        this.stateChanging = stateChanging;
    }
}
