DROP TABLE IF EXISTS t_broadcast_detail;
DROP TABLE IF EXISTS t_anime;
DROP TABLE IF EXISTS t_watch_list;

CREATE TABLE t_anime (
    id SERIAL NOT NULL,
    title VARCHAR(100) NOT NULL,
    update_date TIMESTAMP,
    PRIMARY KEY(id)
);

CREATE TABLE t_broadcast_detail (
    broadcast_detail_id SERIAL NOT NULL,
    anime_id INT,
    tv_station VARCHAR(20) NOT NULL,
    broadcast_date VARCHAR(50),
    update_date TIMESTAMP,
    PRIMARY KEY(broadcast_detail_id),
    CONSTRAINT t_broadcast_detail_t_anime_id_fk FOREIGN KEY (anime_id) REFERENCES t_anime (id)
);

CREATE TABLE t_watch_list (
    watch_list_id SERIAL NOT NULL,
    title VARCHAR(100) NOT NULL,
    update_date TIMESTAMP,
    PRIMARY KEY(watch_list_id)
);