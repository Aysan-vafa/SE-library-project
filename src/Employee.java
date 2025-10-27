public class Employee {
    private String name;
    private String username;
    private String password;

    public Employee(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return "Name: " + name + " | Username: " + username;
    }

    public String toFileString() {
        return name + "," + username + "," + password;
    }

    public static Employee fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) return null;
        return new Employee(parts[0], parts[1], parts[2]);
    }
}

