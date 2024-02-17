package config;


public class AppConfig {

    public static String getDbUrl() {
        return "jdbc:postgresql://localhost:5432/Movies";
    }

    public static String getDbUser() {
        return "postgres";
    }

    public static String getDbPassword() {
        return "password";
    }

}
