package me.itsenderrr.destroythecore;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class EventScheduler {

    private final FileConfiguration config;

    public EventScheduler(FileConfiguration config) {
        this.config = config;
    }

    public void load() {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        data.writeUTF("events");

        Calendar now = new GregorianCalendar();
        for (String time : config.getStringList("Times")) {
            String[] split = time.split(":");

            Calendar event = new GregorianCalendar();

            event.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
            event.set(Calendar.MINUTE, Integer.parseInt(split[1]));
            event.set(Calendar.SECOND, 0);
            event.set(Calendar.MILLISECOND, 0);

            if (now.after(event)) {
                event.add(Calendar.DATE, 1); //add a day
            }

            //could server de-sync?
            Bukkit.getScheduler().runTaskTimer(
                    Main.getInstance(),
                    () -> Main.getInstance().setEventState(true),
                    TimeUnit.MILLISECONDS.toSeconds(event.getTimeInMillis() - now.getTimeInMillis()) * 20,
                    TimeUnit.DAYS.toSeconds(1) * 20
            );
        }
    }
    public long getTimeUntilNextEvent() {
        Calendar now = new GregorianCalendar();

        long timeUntilNextEvent = Long.MAX_VALUE;

        for (String time : config.getStringList("Times")) {
            String[] split = time.split(":");

            Calendar event = new GregorianCalendar();

            // reset calendar
            event.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
            event.set(Calendar.MINUTE, Integer.parseInt(split[1]));
            event.set(Calendar.SECOND, 0);
            event.set(Calendar.MILLISECOND, 0);

            if (now.after(event)) {
                event.add(Calendar.DATE, 1); // add a day
            }

            long timeUntilThisEvent = TimeUnit.MILLISECONDS.toSeconds(event.getTimeInMillis() - now.getTimeInMillis());

            if (timeUntilThisEvent < timeUntilNextEvent) {
                timeUntilNextEvent = timeUntilThisEvent;
            }
        }

        return timeUntilNextEvent; // Convert seconds to ticks
    }
}