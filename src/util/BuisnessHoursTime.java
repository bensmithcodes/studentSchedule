/**
 * A utility class that generates a list of LocalTime objects representing hourly business hours.
 */
package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import entity.DisplayAlertMessage;

public class BuisnessHoursTime {
    /**
     * A list of business hours
     */
    private ObservableList<LocalTime> listAppointmentTime = FXCollections.observableArrayList();

    /**
     * Generates an observable list of LocalTime objects representing hourly business hours.
     *
     * @return An observable list of LocalTime objects representing hourly business hours.
     */
    public ObservableList<LocalTime> generateTimeList() {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59));

        while (startTime.isBefore(endTime)) {
            LocalTime timeSlot = startTime.toLocalTime();
            listAppointmentTime.add(timeSlot);
            startTime = startTime.plusMinutes(15);
        }


        return listAppointmentTime;


    }

    /**
     * Determines if date or time are correct
     *
     * @param selectedDate The selected date.
     * @param startTime    The start time of the appointment.
     * @param endTime      The end time of the appointment.
     * @param localZoneId  The local zone ID.
     * @return True if the appointment is outside of business hours, false otherwise.
     */
    public static boolean validateBuisnessHours(LocalDate selectedDate, LocalTime startTime, LocalTime endTime,
                                                ZoneId localZoneId) {
        ZonedDateTime sysZoneSTime = ZonedDateTime.of(LocalDateTime.of(selectedDate, startTime), localZoneId);
        ZonedDateTime sysZoneETime = ZonedDateTime.of(LocalDateTime.of(selectedDate, endTime), localZoneId);

        ZonedDateTime estZoneSTime = ZonedDateTime.ofInstant(sysZoneSTime.toInstant(), ZoneId.of("America/New_York"));
        ZonedDateTime estZoneETime = ZonedDateTime.ofInstant(sysZoneETime.toInstant(), ZoneId.of("America/New_York"));

        if (estZoneSTime.toLocalDateTime().toLocalTime().isBefore(LocalTime.of(8, 0))
                || estZoneSTime.toLocalDateTime().toLocalTime().isAfter(LocalTime.of(22, 0))) {

            DisplayAlertMessage.errorAlert("Outside Business Hours", "Business hours are 8am - 10pm eastern",
                    "Please select a start time within business hours");
            return true;
        } else if (estZoneETime.toLocalDateTime().toLocalTime().isBefore(LocalTime.of(8, 0))
                || estZoneETime.toLocalDateTime().toLocalTime().isAfter(LocalTime.of(22, 0))) {

            DisplayAlertMessage.errorAlert("Outside Business Hours", "Business hours are 8am - 10pm eastern",
                    "Please select an end time within business hours");
            return true;
        }
        return false;
    }
}
