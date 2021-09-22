package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class Host extends Role{

    private List<Accommodation> accommodations;
    public List<Accommodation> getAccommodations() {
        return accommodations;
    }
}
