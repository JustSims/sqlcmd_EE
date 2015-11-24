//package ua.com.juja.sqlcmd_homework.model;
//
//import java.sql.*;
//import java.util.Random;
//
///**
// * Created by Sims on 02.09.2015.
// */
//public class Main {
//    public static void main(String[] argv) throws ClassNotFoundException, SQLException {
//        Class.forName("org.postgresql.Driver");
//        Connection connection = DriverManager.getConnection(
//                "jdbc:postgresql://localhost:5432/mydb_home", "postgres",
//                "postgres");
//
//        // insert
//        Statement stmt = connection.createStatement();
//        stmt.executeUpdate("INSERT INTO public.user (name, password)" +
//                "VALUES ('John', 'pass')");
//        stmt.close();
//
//        // select
//        stmt = connection.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM public.user WHERE id > 10");
//        while (rs.next()) {
//            System.out.println("id:" + rs.getString("id"));
//            System.out.println("name:" + rs.getString("name"));
//            System.out.println("password:" + rs.getString("password"));
//            System.out.println("-----");
//        }
//        rs.close();
//        stmt.close();
//
//        // table names
//        stmt = connection.createStatement();
//        rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'");
//        while (rs.next()) {
//            System.out.println(rs.getString("table_name"));
//        }
//        rs.close();
//        stmt.close();
//
//        // delete
//        stmt = connection.createStatement();
//        stmt.executeUpdate("DELETE FROM public.user " +
//                "WHERE id > 10 AND id < 100");
//        stmt.close();
//
//        // update
//        PreparedStatement ps = connection.prepareStatement(
//                "UPDATE public.user SET password = ? WHERE id > 3");
//        String pass = "password_" + new Random().nextInt();
//        ps.setString(1, pass);
//        ps.executeUpdate();
//        ps.close();
//
//        connection.close();
//    }
//}