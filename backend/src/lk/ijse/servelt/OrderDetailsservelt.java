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

@WebServlet(urlPatterns = "/orderdetails")
public class OrderDetailsservelt extends HttpServlet {

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
            String orderId = customer.getString("orderId");
            String itemCode = customer.getString("itemCode");
            Integer qty = Integer.parseInt(customer.getString("qty"));
            String  unitPrice= customer.getString("unitPrice");
            connection = ds.getConnection();
            System.out.println(itemCode);

//            Statement stm = connection.createStatement();
//            stm.executeUpdate(
//                    "UPDATE item SET qty =" +qty1+
//                            "WHERE code =" +code
//            );

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Itemdetail VALUES (?,?,?,?)");
            pstm.setObject(1, orderId);
            pstm.setObject(2, itemCode);
            pstm.setObject(3, qty);
            pstm.setObject(4, unitPrice);
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
