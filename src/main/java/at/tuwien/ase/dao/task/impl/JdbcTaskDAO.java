package at.tuwien.ase.dao.task.impl;

import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.domain.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Created by DanielHofer on 09.11.2015.
 */
public class JdbcTaskDAO implements TaskDAO {

    private static final Logger logger = LogManager.getLogger(JdbcTaskDAO.class);
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertTask(Task task) {

        String sql = "INSERT INTO TASK " +
                "(ID, TITLE, DESCRIPTION) VALUES (?, ?, ?)";
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, task.getId());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e){
                    logger.error(e.getMessage());
                }
            }
        }

    }

    public Task findByTaskId(int taskId) {
        String sql = "SELECT * FROM TASK WHERE ID = ?";

        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, taskId);
            Task task = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                task = new Task(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION")
                );
            }
            rs.close();
            ps.close();
            return task;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
}
