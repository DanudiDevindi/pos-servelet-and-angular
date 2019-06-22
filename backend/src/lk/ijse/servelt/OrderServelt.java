package lk.ijse.servelt;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(urlPatterns = "/orders")
public class OrderServelt extends HttpServlet {


    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource ds;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        Connection connection = null;

        try {
            JsonObject customer = reader.readObject();
            String oid = customer.getString("id");
            String date = customer.getString("date");
            String customerId = customer.getString("customerId");
            connection = ds.getConnection();
            System.out.println(oid);
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Orders VALUES (?,?,?)");
            pstm.setObject(1, oid);
            pstm.setObject(2, date);
            pstm.setObject(3, customerId);
            boolean result = pstm.executeUpdate() > 0;

            if (result) {
                out.println("true");
            } else {
                out.println("false");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            out.println("false");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            out.close();
        }
    }
}
