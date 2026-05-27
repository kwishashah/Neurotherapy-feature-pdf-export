/*
 * Copyright (c) 2026. All rights reserved. contact kwisha.shah2004@gmail.com for more details.
 */
package com.neuro.repo.impl;

import com.neuro.exceptions.DatabaseException;
import com.neuro.repo.SessionRepository;
import com.neuro.repo.queries.SqlQueries;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Vector;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** JDBC-backed {@link SessionRepository}. */
public final class JdbcSessionRepository implements SessionRepository {

    private static final Logger logger = LogManager.getLogger(JdbcSessionRepository.class);

    private final Supplier<Connection> connectionSupplier;

    public JdbcSessionRepository(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = Objects.requireNonNull(connectionSupplier, "connectionSupplier");
    }

    @Override
    public Vector<Vector<Object>> getSessionsByPatient(int patientId) {
        logger.debug("Loading sessions for patientId={}", patientId);
        Vector<Vector<Object>> data = new Vector<>();
        Connection con = connectionSupplier.get();
        try (PreparedStatement ps = con.prepareStatement(SqlQueries.SESSION_SELECT_BY_PATIENT)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("session_id"));
                    row.add(rs.getInt("session_number"));
                    row.add(rs.getDate("session_date"));
                    row.add(rs.getString("treatment_given"));
                    row.add(rs.getString("pain_before"));
                    row.add(rs.getString("pain_after"));
                    row.add(rs.getString("session_summary"));
                    data.add(row);
                }
            }
            if (data.isEmpty()) {
                logger.warn("No sessions found for patientId={}", patientId);
            }
            logger.info("Loaded {} sessions for patientId={}", data.size(), patientId);
            return data;
        } catch (SQLException e) {
            logger.error("Failed loading sessions for patientId={}", patientId, e);
            throw new DatabaseException("Failed loading sessions for patientId=" + patientId, e);
        }
    }

    @Override
    public int getNextSessionNumber(int patientId) {
        logger.debug("Fetching next session number for patientId={}", patientId);
        Connection con = connectionSupplier.get();
        try (PreparedStatement ps = con.prepareStatement(SqlQueries.SESSION_NEXT_NUMBER)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int next = rs.getInt(1);
                    logger.info("Next session number={} for patientId={}", next, patientId);
                    return next;
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting next session number patientId={}", patientId, e);
            throw new DatabaseException("Error getting next session number patientId=" + patientId, e);
        }
        return 1;
    }

    @Override
    public void addSession(
            int patientId,
            int sessionNo,
            Date date,
            String treatment,
            String painBefore,
            String painAfter,
            String summary)
            throws DatabaseException {
        logger.info("Saving session patientId={} sessionNo={}", patientId, sessionNo);
        if (treatment == null || treatment.isBlank()) {
            logger.warn("Treatment blank for patientId={} sessionNo={}", patientId, sessionNo);
        }
        if (painBefore == null || painBefore.isBlank()) {
            logger.warn("Pain before empty patientId={} sessionNo={}", patientId, sessionNo);
        }
        if (painAfter == null || painAfter.isBlank()) {
            logger.warn("Pain after empty patientId={} sessionNo={}", patientId, sessionNo);
        }
        Connection con = connectionSupplier.get();
        try (PreparedStatement ps = con.prepareStatement(SqlQueries.SESSION_INSERT)) {
            ps.setInt(1, patientId);
            ps.setInt(2, sessionNo);
            ps.setDate(3, date);
            ps.setString(4, treatment);
            ps.setString(5, painBefore);
            ps.setString(6, painAfter);
            ps.setString(7, summary);
            int rows = ps.executeUpdate();
            logger.info("Session saved successfully rows={} patientId={}", rows, patientId);
        } catch (SQLException e) {
            logger.error("Session save failed patientId={} sessionNo={}", patientId, sessionNo, e);
            throw new DatabaseException(
                    "Session save failed patientId=" + patientId + " sessionNo=" + sessionNo, e);
        }
    }

    @Override
    public void updateSession(
            int sessionId,
            int sessionNo,
            Date date,
            String treatment,
            String painBefore,
            String painAfter,
            String summary)
            throws DatabaseException {
        logger.info("Updating sessionId={}", sessionId);
        Connection con = connectionSupplier.get();
        try (PreparedStatement ps = con.prepareStatement(SqlQueries.SESSION_UPDATE)) {
            ps.setInt(1, sessionNo);
            ps.setDate(2, date);
            ps.setString(3, treatment);
            ps.setString(4, painBefore);
            ps.setString(5, painAfter);
            ps.setString(6, summary);
            ps.setInt(7, sessionId);
            int rows = ps.executeUpdate();
            logger.info("Session updated successfully rows={} sessionId={}", rows, sessionId);
        } catch (SQLException e) {
            logger.error("Session update failed sessionId={}", sessionId, e);
            throw new DatabaseException("Session update failed sessionId=" + sessionId, e);
        }
    }
}
