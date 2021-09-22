package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accommodation {

  private String houseNumber;
  private String address;
  private Boolean available;
  private String type;
  private BigDecimal price;
  private String image;
  private Host host;

  private List<Reservation> reservations = new ArrayList<>();




}