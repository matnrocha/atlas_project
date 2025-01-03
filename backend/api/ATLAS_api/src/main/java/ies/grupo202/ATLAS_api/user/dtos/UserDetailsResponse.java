package ies.grupo202.ATLAS_api.user.dtos;

public class UserDetailsResponse {
    private String name;
    private String email;
    private String role; 

    public UserDetailsResponse(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}