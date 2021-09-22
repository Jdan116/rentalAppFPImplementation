package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class User {

  private String firstName;

  private String lastName;

  private String idNumber;

  private Integer age;

  private String phoneNumber;

  private String address;

  private String nationality;

  private String username;
  private String password;

  private List<Role> roles = new ArrayList<Role>();

  public String getName(){
    return this.firstName+" "+this.lastName;
  }


  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof User))
      return false;
    User user = (User) o;
    return Objects.equals(this.username, user.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.username, this.firstName+this.lastName);
  }

  @Override
  public String toString() {
    return "Employee{" + "id=" + this.username + ", name='" + this.getName() + '\'' ;
  }

}