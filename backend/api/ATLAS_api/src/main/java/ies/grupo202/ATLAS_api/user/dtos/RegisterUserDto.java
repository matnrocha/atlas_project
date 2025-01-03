package ies.grupo202.ATLAS_api.user.dtos;

public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;
    private String preferredRole;

    public String getEmail() {
        return email;
    }

    public RegisterUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public RegisterUserDto setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getPreferredRole() {
        return preferredRole;
    }

    public RegisterUserDto setPreferredRole(String preferredRole) { 
        this.preferredRole = preferredRole;
        return this;
    }

    @Override
    public String toString() {
        return "RegisterUserDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", preferredRole='" + preferredRole + '\'' +
                '}';
    }
}
