-- Table: public.ftp_types

-- DROP TABLE public.ftp_types;

CREATE TABLE public.ftp_types
(
    type character varying(5) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT ftp_types_pkey PRIMARY KEY (type)
)

    TABLESPACE pg_default;

ALTER TABLE public.ftp_types
    OWNER to postgres;



INSERT INTO ftp_types (type) VALUES ('FTP');
INSERT INTO ftp_types (type) VALUES ('SFTP');
INSERT INTO ftp_types (type) VALUES ('FTPS');




-- SEQUENCE: public."Setting_setting_id_seq"

-- DROP SEQUENCE public."Setting_setting_id_seq";

CREATE SEQUENCE public."Setting_setting_id_seq"
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999999999
    CACHE 1;

ALTER SEQUENCE public."Setting_setting_id_seq"
    OWNER TO postgres;


-- Table: public.users

-- DROP TABLE public.users;

CREATE TABLE public.users
(
    user_id uuid NOT NULL,
    username character varying(25) COLLATE pg_catalog."default" NOT NULL,
    email character varying(256) COLLATE pg_catalog."default" NOT NULL,
    password character varying(512) COLLATE pg_catalog."default" NOT NULL,
    verified boolean NOT NULL,
    CONSTRAINT "User_pkey" PRIMARY KEY (user_id),
    CONSTRAINT u_email UNIQUE (email),
    CONSTRAINT u_username UNIQUE (username)
)

TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to postgres;




-- Table: public.settings

-- DROP TABLE public.settings;

CREATE TABLE public.settings
(
    user_id uuid NOT NULL,
    setting_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    value character varying COLLATE pg_catalog."default",
    CONSTRAINT settings_pkey PRIMARY KEY (user_id, setting_id),
    CONSTRAINT settings_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE public.settings
    OWNER to postgres;




-- Table: public.servers

-- DROP TABLE public.servers;

CREATE TABLE public.servers
(
    server_id uuid NOT NULL,
    ip character varying(64) COLLATE pg_catalog."default" NOT NULL,
    ftp_port integer,
    ssh_port integer,
    last_connection timestamp without time zone,
    name character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    ftp_type character varying(5) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT servers_pkey PRIMARY KEY (server_id),
    CONSTRAINT servers_ftp_type_fkey FOREIGN KEY (ftp_type)
        REFERENCES public.ftp_types (type) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

    TABLESPACE pg_default;

ALTER TABLE public.servers
    OWNER to postgres;




-- Table: public.server_groups

-- DROP TABLE public.server_groups;

CREATE TABLE public.server_groups
(
    group_id uuid NOT NULL,
    name character varying(256) COLLATE pg_catalog."default",
    user_id uuid NOT NULL,
    parent_group_id uuid,
    parent_user_id uuid,
    CONSTRAINT server_groups_pkey PRIMARY KEY (group_id, user_id),
    CONSTRAINT fk_parent FOREIGN KEY (parent_group_id, parent_user_id)
        REFERENCES public.server_groups (group_id, user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT user_id FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE public.server_groups
    OWNER to postgres;



-- Table: public.server_group_servers

-- DROP TABLE public.server_group_servers;

CREATE TABLE public.server_group_servers
(
    server_id uuid NOT NULL,
    group_id uuid NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT server_group_servers_pkey PRIMARY KEY (server_id, group_id, user_id),
    CONSTRAINT f_server_group FOREIGN KEY (group_id, user_id)
        REFERENCES public.server_groups (group_id, user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT f_server_id FOREIGN KEY (server_id)
        REFERENCES public.servers (server_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE public.server_group_servers
    OWNER to postgres;




-- Table: public.history_items

-- DROP TABLE public.history_items;

CREATE TABLE public.history_items
(
    user_id uuid NOT NULL,
    "when" timestamp without time zone NOT NULL,
    device character varying(256) COLLATE pg_catalog."default" NOT NULL,
    ip character varying(64) COLLATE pg_catalog."default" NOT NULL,
    deleted boolean NOT NULL,
    ssh_port integer,
    ftp_port integer,
    username character varying(255) COLLATE pg_catalog."default",
    ftp_type character varying(5) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT history_items_pkey PRIMARY KEY (user_id, "when"),
    CONSTRAINT f_user_id FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT history_items_ftp_type_fkey FOREIGN KEY (ftp_type)
        REFERENCES public.ftp_types (type) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

    TABLESPACE pg_default;

ALTER TABLE public.history_items
    OWNER to postgres;


-- Table: public.access_tokens

-- DROP TABLE public.access_tokens;

CREATE TABLE public.access_tokens
(
    token character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    user_id uuid NOT NULL,
    valid_until date NOT NULL,
    pc_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT access_tokens_pkey PRIMARY KEY (token, user_id),
    CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE public.access_tokens
    OWNER to postgres;




-- Table: public.confirmation_tokens

-- DROP TABLE public.confirmation_tokens;

CREATE TABLE public.confirmation_tokens
(
    token character varying(256) COLLATE pg_catalog."default" NOT NULL,
    user_id uuid NOT NULL,
    valid_until date,
    CONSTRAINT confirmation_tokens_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE public.confirmation_tokens
    OWNER to postgres;


