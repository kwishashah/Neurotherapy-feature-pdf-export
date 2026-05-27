/*
 * Copyright (c) 2026. All rights reserved. contact kwisha.shah2004@gmail.com for more details.
 */
package com.neuro.repo;

import com.neuro.exceptions.DatabaseException;
import java.sql.Date;
import java.util.Vector;

/** Domain operations against the {@code NeurotherapySessions} table. */
public interface SessionRepository {

    /** Returns every session row for a given patient. */
    Vector<Vector<Object>> getSessionsByPatient(int patientId);

    /** Returns the next session number for a patient (max + 1, or 1 if none). */
    int getNextSessionNumber(int patientId);

    /** Inserts a new session. */
    void addSession(
            int patientId,
            int sessionNo,
            Date date,
            String treatment,
            String painBefore,
            String painAfter,
            String summary)
            throws DatabaseException;

    /** Updates an existing session by id. */
    void updateSession(
            int sessionId,
            int sessionNo,
            Date date,
            String treatment,
            String painBefore,
            String painAfter,
            String summary)
            throws DatabaseException;
}
