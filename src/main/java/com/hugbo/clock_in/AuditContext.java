package com.hugbo.clock_in;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Self-explanatory, sets the user that caused an insertion/update/deletion
// into the database, so that audit_log correctly logs the user that caused the change.
@Component
public class AuditContext {
    @Autowired
    private DataSource dataSource;

    public void setCurrentUserId(Long userId) {
        if (userId != null) {
            try (Connection connection = dataSource.getConnection();
                    Statement statement = connection.createStatement()) {
                statement.execute("SET app.user_id = '" + userId + "'");
            } catch (SQLException e) {
                Logger.getLogger(AuditContext.class.getName())
                    .warning("Failed to set audit user ID: " + e.getMessage());
            }
        }
    }

    public void clearCurrentUserId() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("RESET app.user_id");
        } catch (SQLException e) {
            Logger.getLogger(AuditContext.class.getName())
                .warning("Failed to reset audit user ID: " + e.getMessage());
        }
    }
}
