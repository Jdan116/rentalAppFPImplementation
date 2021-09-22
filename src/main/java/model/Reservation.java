package model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
public class Reservation {


  private Integer id;

  private Guest guest;

  private Accommodation accommodation;

  private LocalDateTime checkInDate;

  private LocalDateTime checkOutDate;

  private BigDecimal grossPrice;

  private Boolean available = null;
  private Integer rating;

  private Double managmentCost;

  private String comments;
}