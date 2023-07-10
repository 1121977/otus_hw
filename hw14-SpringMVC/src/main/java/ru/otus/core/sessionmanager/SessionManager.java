package ru.otus.core.sessionmanager;

import org.hibernate.Session;

public interface SessionManager {
    Session beginSession();

}
