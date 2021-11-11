import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Scanner;
import java.util.stream.StreamSupport;

public class Main {

    static Connection connection = null;

    public static void main(String[] args){

        ConexionJDBC();

        ModificarRegistro();


    }


    public static void ConexionJDBC(){

        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/examen","root","");
            System.out.println("Conectado a la base de datos");


            Statement st = connection.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS juegos(" +
                    "id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "titulo varchar(255) NOT NULL," +
                    "genero varchar(255) NOT NULL)" +
                    "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");



        }catch(SQLException | ClassNotFoundException sqlE ){
            System.out.println("No se pudo conectar a la base de datos");
            sqlE.printStackTrace();
        }
    }

    public static void ConsultaConcreta(){
        String sentenciaSql = "SELECT titulo, genero FROM juegos WHERE genero = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            ps = connection.prepareStatement(sentenciaSql);
            Scanner sc = new Scanner(System.in);
            System.out.println("Que genero quieres buscar?");
            String genero = sc.next();
            ps.setString(1, genero );


            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Titulo: " + rs.getString("titulo"));
                System.out.println("Genero; " + rs.getString("genero"));
                System.out.println("------------------------");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void EjecutarConsulta(){
        String sentenciaSql = "SELECT titulo, genero FROM juegos";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            ps = connection.prepareStatement(sentenciaSql);
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Titulo: " + rs.getString("titulo"));
                System.out.println("Genero; " + rs.getString("genero"));
                System.out.println("------------------------");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void EliminarRegistro(){

       String sentenciaSql = "DELETE FROM juegos WHERE id = ?";
       PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(sentenciaSql);
            System.out.println("Que id quieres borrar");
            Scanner sc = new Scanner(System.in);
            int num = sc.nextInt();
            ps.setInt(1, num);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void InsertarRegistro(){

        String sentenciaSql = "INSERT INTO juegos(titulo, genero) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {

            PreparedStatement misentencia = connection.prepareStatement("SELECT * FROM juegos WHERE titulo = ?");
            misentencia.setString(1, "Ghost & Goblins");
            ResultSet rs2 = misentencia.executeQuery();
            if (rs2.next()) {
                System.out.println("Ese juego ya existe");
            } else {
                ps = connection.prepareStatement(sentenciaSql);

                ps.setString(1, "Ghost & Goblins");
                ps.setString(2, "Arcade");
                ps.executeUpdate();

            }

        }catch(SQLException sqle){
            sqle.printStackTrace();
        }

    }

    public static void ModificarRegistro(){
        PreparedStatement ps = null;
        String sentenciaSql = "UPDATE juegos SET titulo = ?, genero = ? WHERE id = ?";


        try {

            PreparedStatement misentencia = connection.prepareStatement("SELECT * FROM juegos WHERE id = ?");


            Scanner sc = new Scanner(System.in);
            System.out.println("Que ID quieres modificar");
            int id = sc.nextInt();
            misentencia.setInt(1, id);
            ResultSet rs = misentencia.executeQuery();

            if (rs.next()){

                System.out.println("Introduce el juego");
                String juego = sc.next();
                System.out.println("Introduce el genero");
                String genero = sc.next();
                ps = connection.prepareStatement(sentenciaSql);
                ps.setString(1, juego);
                ps.setString(2, genero);
                ps.setInt(3, id);
                ps.executeUpdate();

            } else System.out.println("EL ID NO HA SIDO ENCONTRADO");


        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }


    }
}
