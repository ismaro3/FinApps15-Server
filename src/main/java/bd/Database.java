package bd;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import logica.Cliente;
import logica.*;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by teruyi on 6/11/15.
 */
public class Database {

    private static Connection con;

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "jdbc:mysql://";
    static final String DB_URL = "jdbc:mysql://localhost/EMP";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "wallapet";
    static final String IP = "192.168.56.101";
    static final String DB = "finapps";

    public Database() {
    }


    private static final String INSERCION_CLIENTE = "insert into Clients(dni, name, surname, date, postalCode) values (?, ?, ?, ?, ?)";
    private static final String INSERCION_ARTICLE ="insert into Article(code, name, vat, price, description, stock, Category_name) values (?, ?, ?, ?, ?,?,?)";


    public String connect() {
        try {
            String db_driver = JDBC_DRIVER + IP + ":" + 3306 + "/" + DB;
            con = (Connection) DriverManager.getConnection(db_driver, USER, PASS);
            con.setAutoCommit(false);
            System.out.println("bien");
            return "00000";
        } catch (SQLException e) {
            System.out.println("mal");
            return e.getSQLState();
        }
    }

    public String disconnect() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                return e.getSQLState();
            }
        }
        return "00000";
    }

    public Boolean insertarCliente(Cliente c) {
        try (PreparedStatement stmt = (PreparedStatement) con.prepareStatement(INSERCION_CLIENTE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, String.valueOf(c.getCode()));
            stmt.setString(2, c.getName());
            stmt.setString(3, c.getSurname());
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime;
            currentTime = sdf.format(c.getBirthDate());
            stmt.setString(4, String.valueOf(currentTime));
            stmt.setString(5, String.valueOf(c.getPostalCode()));

            stmt.executeUpdate();
            con.commit();
            return true;
        } catch (SQLException e) {
            try {
                con.rollback();
                return false;
            } catch (SQLException e2) {

            }
            return false;
        }
    }
    //
    public Boolean insertarArticulo(Article a) {
        try (PreparedStatement stmt = (PreparedStatement) con.prepareStatement(INSERCION_ARTICLE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, String.valueOf(a.getCode()));
            stmt.setString(2, a.getName());
            stmt.setString(3, String.valueOf(a.getVat()));
            stmt.setString(4, String.valueOf(a.getPrize()));
            stmt.setString(5, String.valueOf(a.getDescription()));
            stmt.setString(6, String.valueOf(a.getStock()));
            stmt.setString(7, String.valueOf(a.getCategory()));


            stmt.executeUpdate();
            con.commit();
            return false;
        } catch (SQLException e) {
            try {
                System.out.print(e.getMessage());
                con.rollback();
            } catch (SQLException e2) {
                System.out.print("insercion cliente");
            }
            return false;
        }
    }

    //
    public String insertarVenta(Cliente c) {
        return "";
    }

    //
    public String insertarDevolucion(Cliente c) {
        return "";
    }

    public boolean modificarCliente(int dni, String name, String surname, String date, int postalCode) {
        Statement stmt = null;
        try {
            stmt = (Statement)con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // (LOW PRIORITY) Actualizar cuando nadie este leyendo
        String sql_1 = "UPDATE LOW PRIORITY " + "Clients"
                + " SET dni =" + dni
                + ", name =" + name + ", surname =" + surname
                + ", date =" + date + ", postalCode=" + postalCode
                + "WHERE dni=\""
                + dni
                + "\"";

        try {
            stmt.executeQuery(sql_1);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

   public boolean borrarCliente(Cliente c){
       try {
           Statement stmt = (Statement) con.createStatement();
       String sql_1 = "DELETE FROM Clients " + " WHERE dni = '" + c.getCode() + "'";
           return true;
       } catch (SQLException e) {
           return false;
       }
   }

}
