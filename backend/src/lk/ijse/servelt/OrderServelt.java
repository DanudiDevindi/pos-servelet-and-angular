package lk.ijse.servelt;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(urlPatterns = "/orders")
public class OrderServelt extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();



        Connection connection = null;
        try {

            connection .setAutoCommit(false);
            JsonObject orders = reader.readObject();


            String id = orders.getString("id");
            String date = orders.getString("date");
            String customerId = orders.getString("customerId");

            JsonObject itemdetail = reader.readObject();
            String orderId = itemdetail.getString("orderId");
            String itemCode = itemdetail.getString("itemCode");
            Integer qty =Integer.parseInt(itemdetail.getString("qty"));
            String unitPrice = itemdetail.getString("unitPrice");


            Statement stm = connection.createStatement();
            stm.executeUpdate(
                    "UPDATE item SET qtyOnHand =(qtyOnHand - qty WHERE itemCode =?)"
            );

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO orders VALUES (?,?,?)");
            pstm.setObject(1,id);
            pstm.setObject(2,date);
            pstm.setObject(3,customerId);
            PreparedStatement psst = connection.prepareStatement("INSERT INTO itemdetail VALUES (?,?,?,?)");
            psst.setObject(1,orderId);
            psst.setObject(2,itemCode);
            psst.setObject(3,qty);
            psst.setObject(4,unitPrice);

            boolean result = pstm.executeUpdate()>0;
            boolean resultnew = psst.executeUpdate()>0;



            if(result && resultnew){
                out.println("true");
            }else {
                out.println("false");
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
