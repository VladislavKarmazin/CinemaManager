package config;

public class AppConfig {

    public static String getDbUrl() {
        return "jdbc:postgresql://localhost:5432/postgres";
    }

    public static String getDbUser() {
        return "postgres";
    }

    public static String getDbPassword() {
        return "password";
    }

}
