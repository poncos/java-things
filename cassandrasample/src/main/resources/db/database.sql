
CREATE KEYSPACE IF NOT EXISTS metrics
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};

-- WITH replication = {'class': 'NetworkTopologyStrategy', 'DC1' : 1, 'DC2' : 3}
--  AND durable_writes = false;


CREATE TABLE IF NOT EXISTS metrics.temperature_by_day (
  device_id UUID,
  event_date DATE,
  event_time TIMESTAMP,
  reading text,
  PRIMARY KEY ((device_id,event_date),event_time)
);

CREATE TABLE IF NOT EXISTS metrics.use

CREATE TABLE IF NOT EXISTS metrics.device_by_user (
  id UUID,
  user_id UUID,
  device_type VARCHAR,
  registered_date TIMESTAMP,
  PRIMARY KEY (user_id,id)
);