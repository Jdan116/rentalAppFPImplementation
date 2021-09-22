package model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Payment {
  private  Invoice invoice;
  private Currency currency;
  private String description;
  private Double ammount;
  private LocalDateTime paymentDate;
  private Host host;
}