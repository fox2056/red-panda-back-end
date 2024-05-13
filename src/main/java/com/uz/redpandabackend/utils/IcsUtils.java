package com.uz.redpandabackend.utils;

import com.uz.redpandabackend.model.Event;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import org.springframework.web.multipart.MultipartFile;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IcsUtils {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DAY_OF_WEEK_FORMAT = DateTimeFormatter.ofPattern("EEEE", new Locale("pl", "PL"));

    public static List<Event> parseICSFromFile(MultipartFile file) {
        List<Event> events = new ArrayList<>();
        try {
            String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            events = parseICSCalendar(new StringReader(fileContent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    private static List<Event> parseICSCalendar(java.io.Reader reader) throws Exception {
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(reader);
        List<Event> events = new ArrayList<>();

        for (Component component : calendar.getComponents(Component.VEVENT)) {
            String dtStart = getPropertyAsString(component.getProperty(Property.DTSTART));
            String dtEnd = getPropertyAsString(component.getProperty(Property.DTEND));
            String summary = getPropertyAsString(component.getProperty(Property.SUMMARY));
            String location = getPropertyAsString(component.getProperty(Property.LOCATION));
            String classType = extractClassType(summary);
            String startDate = getDate(dtStart);
            String dayOfWeek = getDayOfWeek(dtStart);
            String startTime = getTime(dtStart);
            String endTime = getTime(dtEnd);

            Event event = new Event(startDate, dayOfWeek, startTime, endTime, summary, classType, location);
            System.out.println(event);
            events.add(event);
        }

        return events;
    }

    private static String getPropertyAsString(Optional<Property> property) {
        return property.map(Property::getValue).orElse("");
    }

    private static String extractClassType(String summary) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(summary);
        return matcher.find() ? matcher.group(1) : "";
    }

    private static String getDayOfWeek(String dateTimeStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMAT);
        return DAY_OF_WEEK_FORMAT.format(dateTime);
    }

    private static String getTime(String dateTimeStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMAT);
        return TIME_FORMAT.format(dateTime.toLocalTime());
    }

    private static String getDate(String dateTimeStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMAT);
        return DATE_FORMAT.format(dateTime.toLocalDate());
    }
}
