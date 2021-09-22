package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
public class Invoice {

  private LocalDateTime invoiceDate;
  private Payment payment;
  private Reservation reservation;


}