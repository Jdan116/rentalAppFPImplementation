package util;

import model.Accommodation;
import model.Guest;
import model.Host;
import model.User;

import java.math.BigDecimal;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface FunctionUtil {

    Function<List<User>, List<Guest>> getGuests = users -> users.stream()
            .flatMap(u -> u.getRoles().stream())
            .filter(u -> u instanceof Guest)
            .map(g -> (Guest)g)
            .collect(Collectors.toList());

    Function<List<User>, List<Host>> getHosts = users -> users.stream()
            .flatMap(u -> u.getRoles().stream())
            .filter(u -> u instanceof Guest)
            .map(h -> (Host)h)
            .collect(Collectors.toList());

    /* Functional Implementation to get yearly gross of an accommodation */
    BiFunction<Accommodation, Integer, BigDecimal> getYearlyGrossOfAccommodation = (accommodation, year) ->
            accommodation.getReservations().stream().filter(reservation -> reservation.getCheckOutDate().getYear() == year).map(reservation -> reservation.getGrossPrice())
                    .reduce(BigDecimal.valueOf(0.0), (a, b)-> BigDecimal.valueOf(a.doubleValue() + b.doubleValue()));


    /* Functional Implementation to get yearly avarage rating of an accommodation*/
    BiFunction<Accommodation, Integer, Double> getYearlyTopKAvarageRatingOfAccommodation = (accommodation, year) ->
            accommodation.getReservations().stream().filter(reservation -> reservation.getCheckOutDate().getYear() == year)
                    .map(reservation -> (Integer) reservation.getRating()).filter(rating -> rating != null)
                    .collect(Collectors.averagingInt(rating ->(int) rating));

    /* Functional Implementation to get gross of an accommodation starting from a given year */
    BiFunction<Accommodation, Integer, BigDecimal> getGrossOfAccommodationFromYear = (accommodation, year) ->
            accommodation.getReservations().stream().filter(reservation -> reservation.getCheckOutDate().getYear() >= year)
                    .map(reservation -> reservation.getGrossPrice())
                    .reduce(BigDecimal.valueOf(0.0), (a, b)-> BigDecimal.valueOf(a.doubleValue() + b.doubleValue()));



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
                .map(accommodation -> new Pair<>(accommodation, getYearlyTopKAvarageRatingOfAccommodation.apply(accommodation,year)))
                .sorted((p1, p2)-> (int) Math.ceil(p1.getValue()- p2.getValue())).limit(k)
                .map(pair -> pair.getKey()).collect(Collectors.toList());


}
