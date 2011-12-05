CREATE TABLE feeling (
  id                    INTEGER PRIMARY KEY,
  name                  TEXT NOT NULL,
  description           TEXT,
  needIsMet             INTEGER,
  groupd_id             INTEGER
);


--CREATE TABLE similar_feeling (
--  feeling1_id           INTEGER,
--  feeling2_id           INTEGER,
--  FOREIGN KEY (feeling1_id) REFERENCES feeling(id),
--  FOREIGN KEY (feeling2_id) REFERENCES feeling(id),
--  UNIQUE (feeling1_id, feeling2_id)
--);

CREATE TABLE non_feeling (
  id                    INTEGER PRIMARY KEY,
  name                  TEXT,
  description           TEXT
);

CREATE TABLE non_feeling__feeling (
  feeling1_id           INTEGER,
  feeling2_id           INTEGER,
  FOREIGN KEY (feeling1_id) REFERENCES feeling(id),
  FOREIGN KEY (feeling2_id) REFERENCES feeling(id),
  UNIQUE (feeling1_id, feeling2_id)
);

--CREATE TABLE opposite_feeling (
--  feeling1_id           INTEGER,
--  feeling2_id           INTEGER,
--  FOREIGN KEY (feeling1_id) REFERENCES feeling(id),
--  FOREIGN KEY (feeling2_id) REFERENCES feeling(id),
--  UNIQUE (feeling1_id, feeling2_id)
--);

CREATE TABLE need (
  id                    INTEGER PRIMARY KEY,
  name                  TEXT,
  description           TEXT,
  keywords              TEXT
);

CREATE TABLE strategy (
  id                    INTEGER PRIMARY KEY,
  label                 TEXT,
  description           TEXT
);

CREATE TABLE need__strategy (
  need_id               INTEGER,
  strategy_id           INTEGER,
  FOREIGN KEY (need_id)     REFERENCES need(id),
  FOREIGN KEY (strategy_id) REFERENCES strategy(id),
  UNIQUE (need_id, strategy_id)
);

-- needs
INSERT INTO need (id, name) VALUES (0, 'Anerkennung');
INSERT INTO need (id, name) VALUES (1, 'Sicherheit');
INSERT INTO need (id, name) VALUES (2, 'Liebe');
INSERT INTO need (id, name) VALUES (3, 'Gemeinschaft');
INSERT INTO need (id, name) VALUES (4, 'Ruhe');

-- feelings
INSERT INTO feeling (name, needIsMet) VALUES ('freudig', 1);
INSERT INTO feeling (name, needIsMet) VALUES ('glücklich', 1);
INSERT INTO feeling (name, needIsMet) VALUES ('traurig', 0);
INSERT INTO feeling (name, needIsMet) VALUES ('wuetend', 0);
INSERT INTO feeling (name, needIsMet) VALUES ('erregt', NULL);
INSERT INTO feeling (name, needIsMet) VALUES ('entspannt', 1);
-- examples for similar feelings
INSERT INTO feeling (name, needIsMet) VALUES ('genervt', 1);
INSERT INTO feeling (name, needIsMet) VALUES ('ruhelos', 1);
INSERT INTO feeling (name, needIsMet) VALUES ('rastlos', 1);

-- strategies
INSERT INTO strategy (id, label) VALUES (0, 'Spazieren gehen');
INSERT INTO strategy (id, label) VALUES (1, 'Buch lesen');
INSERT INTO strategy (id, label) VALUES (2, 'Etwas mit Partner unternehmen');
INSERT INTO strategy (id, label) VALUES (3, 'Freundeskreis erweitern');
INSERT INTO strategy (id, label) VALUES (4, 'Rückmeldung einholen');

-- need - strategy

INSERT INTO need__strategy (need_id, strategy_id) VALUES ((select _id from need where name = 'Ruhe'), (select _id from strategy where label = 'Spazieren gehen')); -- Spazieren --> Ruhe
INSERT INTO need__strategy (need_id, strategy_id) VALUES (4, 1); -- Buch lesen --> Ruhe
INSERT INTO need__strategy (need_id, strategy_id) VALUES (2, 2); -- Partner unternehmen --> Liebe
INSERT INTO need__strategy (need_id, strategy_id) VALUES (3, 2); -- Partner unternehmen --> Gemeinschaft
INSERT INTO need__strategy (need_id, strategy_id) VALUES (3, 3); -- Freundeskreis erweitern --> Gemeinschaft
INSERT INTO need__strategy (need_id, strategy_id) VALUES (1, 3); -- Freundeskreis erweitern --> Sicherheit (falls eine Freundschaft kaputt geht)
INSERT INTO need__strategy (need_id, strategy_id) VALUES (0, 4); -- Rückmeldung einholen --> Anerkennung
