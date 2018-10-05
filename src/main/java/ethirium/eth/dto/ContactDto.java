package ethirium.eth.dto;

public class ContactDto {
    private String name;
    private String email;
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("");
        sb.append("\""+name+"\"").append(',');
        sb.append("\""+email+"\"").append(',');
        sb.append("\""+role+"\"").append('\n');
        return sb.toString();
    }
}
