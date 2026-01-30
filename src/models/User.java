package models;

public class User {
    private int id;
    private String email, pseudo, password, role;

    public User(int id, String email, String pseudo, String password, String role) {
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        this.password = password;
        this.role = role;
    }
    public int getId() { return id; }
    public String getEmail() { return email;}
    public String getPseudo() {return pseudo; }
    public String getRole() {return role; }
    public String getPassword() { return password; }

    public void setPseudo(String pseudo) { this.pseudo = pseudo;}
    public void setPassword(String password) { this.password = password;}
    public void setRole(String role) { this.role = role;}

    @Override
    public String toString() {
        return "ID: " + id + " | " + pseudo + " (" + role + ")";
    }
}
