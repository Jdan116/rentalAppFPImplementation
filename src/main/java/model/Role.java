package model;


import lombok.Data;

@Data
public abstract class Role{
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
