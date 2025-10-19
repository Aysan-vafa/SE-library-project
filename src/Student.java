
public class Student {
    private String name;
    private String studentId;
    private String username;
    private String password;
    private boolean active;

    public Student(String name, String studentId, String username, String password) {
        this(name, studentId, username, password, true);
    }

    public Student(String name, String studentId, String username, String password, boolean active) {
        this.name = name;
        this.studentId = studentId;
        this.username = username;
        this.password = password;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                " | Student ID: " + studentId +
                " | Username: " + username +
                " | Active: " + (active ? "Yes" : "No");
    }


    public String toFileString() {
        return name + "," + studentId + "," + username + "," + password + "," + active;
    }

    public static Student fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 5) {
            return new Student(parts[0], parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]));
        } else if (parts.length == 4) {

            return new Student(parts[0], parts[1], parts[2], parts[3], true);
        } else {
            return null;
        }
    }
}
