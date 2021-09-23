package util;

import model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface FunctionUtil {

    Function<List<User>, List<Role>> getRoles = users -> users.stream()
            .flatMap(u -> u.getRoles().stream())
            .collect(Collectors.toList());

    Function<List<User>, List<Guest>> getGuests = users -> getRoles.apply(users).stream()
            .filter(u -> u instanceof Guest)
            .map(g -> (Guest) g)
            .collect(Collectors.toList());

    Function<List<User>, List<Host>> getHosts = users -> getRoles.apply(users).stream()
            .filter(u -> u instanceof Guest)
            .map(h -> (Host) h)
            .collect(Collectors.toList());


    BiFunction<LocalDateTime, LocalDateTime, Long> diff = (cIn, cOut) ->
            ChronoUnit.DAYS.between(cIn, cOut);

    /* Functional Implementation to get yearly gross of an accommodation */
    BiFunction<Accommodation, Integer, BigDecimal> getYearlyGrossOfAccommodation = (accommodation, year) ->
            accommodation.getReservations().stream().filter(reservation -> reservation.getCheckOutDate().getYear() == year).map(reservation -> reservation.getGrossPrice())
                    .reduce(BigDecimal.valueOf(0.0), (a, b) -> BigDecimal.valueOf(a.doubleValue() + b.doubleValue()));


    /* Functional Implementation to get yearly avarage rating of an accommodation*/
    BiFunction<Accommodation, Integer, Double> getYearlyTopKAvarageRatingOfAccommodation = (accommodation, year) ->
            accommodation.getReservations().stream().filter(reservation -> reservation.getCheckOutDate().getYear() == year)
                    .map(reservation -> (Integer) reservation.getRating()).filter(rating -> rating != null)
                    .collect(Collectors.averagingInt(rating -> (int) rating));

    /* Functional Implementation to get gross of an accommodation starting from a given year */
    BiFunction<Accommodation, Integer, BigDecimal> getGrossOfAccommodationFromYear = (accommodation, year) ->
            accommodation.getReservations().stream().filter(reservation -> reservation.getCheckOutDate().getYear() >= year)
                    .map(reservation -> reservation.getGrossPrice())
                    .reduce(BigDecimal.valueOf(0.0), (a, b) -> BigDecimal.valueOf(a.doubleValue() + b.doubleValue()));


    TriFunction<User, Integer, Integer, List<Accommodation>> getTopKHigestEarningAccommodationOfYear = (user, year, k) ->
            user.getRoles().stream().filter(role -> role instanceof Host).map(role -> (Host) role)
                    .flatMap(host -> host.getAccommodations().stream())
                    .map(accommodation -> new Pair<>(accommodation, getYearlyGrossOfAccommodation.apply(accommodation, year)))
                    .sorted((p1, p2) -> (int) Math.ceil(p1.getValue().doubleValue() - p2.getValue().doubleValue()))
                    .limit(k).map(pair -> pair.getKey())
                    .collect(Collectors.toList());

    TriFunction<User, Integer, Integer, List<Accommodation>> getTopKHighlyRatedAccommodationOfYear = (user, year, k) ->
            Optional.ofNullable(user.getRoles()).orElseGet(Collections::emptyList).stream().filter(role -> role instanceof Host).map(role -> (Host) role)
                    .flatMap(host -> host.getAccommodations().stream())
                    .map(accommodation -> new Pair<>(accommodation, getYearlyTopKAvarageRatingOfAccommodation.apply(accommodation, year)))
                    .sorted((p1, p2) -> (int) Math.ceil(p1.getValue() - p2.getValue())).limit(k)
                    .map(pair -> pair.getKey()).collect(Collectors.toList());


    TriFunction<List<Reservation>, Integer, String, Boolean> isAccommodationXReservedInAGivenYear = (reservation, year, houseNumber) -> reservation.stream()
            .filter(r -> r.getAccommodation().getHouseNumber().equals(houseNumber))
            .filter(r -> r.getCheckInDate().getYear() == year).count() > 0;


    /**
     * A list of Guests who reserved at host X in a give year
     */
    TriFunction<List<User>, String, Integer, List<String>> getLastNameOfGuestsWhoReservedAtXInYearK =
            (users, k, houseNumber) -> getGuests.apply(users).stream()
                    .filter(g -> isAccommodationXReservedInAGivenYear.apply(g.getReservations(), houseNumber, k))
                    .map(g -> g.getUser().getLastName())
                    .collect(Collectors.toList());

    QuadFunction<List<Reservation>, String, Integer, Long, Boolean> isAccommodationXReservedNTimesInYear =
            (reservations, houseNumber, year, k) -> reservations.stream()
                    .filter(r -> r.getCheckInDate().getYear() == year &&
                            r.getAccommodation().getHouseNumber().equals(houseNumber))
                    .map(r -> diff.apply(r.getCheckInDate(), r.getCheckOutDate()))
                    .reduce(0l, Long::sum) >= k;

    /**
     * A list of guest lastname who reserved at X at least K nights
     */
    QuadFunction<List<User>, String, Integer, Long, List<String>> getGuestNameWhoStayedAtAccXLeastKNights = (users, houseNumber, year, k) -> getGuests.apply(users).stream()
            .filter(g -> isAccommodationXReservedNTimesInYear.apply(g.getReservations(), houseNumber, year, k))
            .map(g -> g.getUser().getFirstName())
            .collect(Collectors.toList());

    /**
     * get cheapest accommodation
     */
    Function<List<User>, List<Accommodation>> getTheCheapestAccommodation = users -> getHosts.apply(users).stream()
            .flatMap(h -> h.getAccommodations().stream())
            .sorted((a1, a2) -> (a1.getPrice().subtract(a1.getPrice())).intValue())
            .limit(1)
            .collect(Collectors.toList());

}