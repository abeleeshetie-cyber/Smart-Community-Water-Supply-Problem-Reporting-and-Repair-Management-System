-- Stores all users: residents, administrators and repair workers
-- role is RESIDENT, ADMIN or WORKER
-- trade and available are used only by workers, address only by residents
CREATE TABLE IF NOT EXISTS users (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    phone TEXT,
    password TEXT NOT NULL,
    role TEXT NOT NULL,
    trade TEXT,
    available INTEGER,
    address TEXT
);

-- Stores water problem reports
-- type and status are saved as text (enum names), reported_at as ISO date text
CREATE TABLE IF NOT EXISTS reports (
    id INTEGER PRIMARY KEY,
    resident_id TEXT NOT NULL,
    location TEXT NOT NULL,
    type TEXT NOT NULL,
    severity INTEGER NOT NULL,
    households INTEGER NOT NULL,
    reported_at TEXT NOT NULL,
    status TEXT NOT NULL,
    priority REAL
);

-- Stores which worker is assigned to which report (one assignment per report)
CREATE TABLE IF NOT EXISTS assignments (
    report_id INTEGER PRIMARY KEY,
    worker_id TEXT NOT NULL,
    assigned_at TEXT NOT NULL
);

-- Stores maintenance notes written during a repair (many records per report)
CREATE TABLE IF NOT EXISTS records (
    report_id INTEGER NOT NULL,
    note TEXT NOT NULL,
    date TEXT NOT NULL
);
