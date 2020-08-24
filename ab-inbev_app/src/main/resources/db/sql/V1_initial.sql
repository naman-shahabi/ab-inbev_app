CREATE TABLE IF NOT EXISTS product
(
    id uuid NOT NULL,
    name character varying(256) COLLATE pg_catalog."default" NOT NULL,
    description character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    brand character varying(256) COLLATE pg_catalog."default" NOT NULL,
    price numeric(9,2) NOT NULL,
    created_date timestamp with time zone NOT NULL,
    modified_date timestamp with time zone,
    created_by character varying(512),
    modified_by character varying(512) COLLATE pg_catalog."default",
    CONSTRAINT product_pkey PRIMARY KEY (id)
);