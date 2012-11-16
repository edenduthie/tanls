 alter table profileitem add column areaofpractise_id integer;
 alter table profileitem add column customer_id integer;
 alter table profileitem add CONSTRAINT fk6dfc243c94c70c9f FOREIGN KEY (areaofpractise_id)
      REFERENCES areaofpractise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
alter table profileitem add CONSTRAINT fk6dfc243c980095bf FOREIGN KEY (customer_id)
      REFERENCES customer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
alter table question add column probono boolean;
alter table question add column quoterequired boolean;
update question set probono = false;
update question set quoterequired = false;
CREATE TABLE quote
(
  id integer NOT NULL,
  disbursments numeric(19,2),
  legalfees numeric(19,2),
  text text,
  "time" bigint,
  lawyer_id integer,
  question_id integer,
  CONSTRAINT quote_pkey PRIMARY KEY (id ),
  CONSTRAINT fk4ac4e5c29b7893f FOREIGN KEY (lawyer_id)
      REFERENCES lawyer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk4ac4e5cd17ab8bf FOREIGN KEY (question_id)
      REFERENCES question (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
alter table answer add column quote_id integer;
alter table answer add CONSTRAINT fk752f2bde8035b595 FOREIGN KEY (quote_id)
      REFERENCES quote (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
drop table paymentreceipt;
CREATE TABLE payment
(
  id integer NOT NULL,
  amount numeric(19,2),
  "time" bigint,
  transactionid character varying(255),
  file_id integer,
  CONSTRAINT payment_pkey PRIMARY KEY (id )
);
CREATE TABLE file
(
  id integer NOT NULL,
  status character varying(255),
  "time" bigint,
  customer_id integer,
  lawyer_id integer,
  payment_id integer,
  quote_id integer,
  completiontime bigint,
  feedback_id integer,
  holdpayment boolean,
  CONSTRAINT file_pkey PRIMARY KEY (id ),
  CONSTRAINT fk21699c29b7893f FOREIGN KEY (lawyer_id)
      REFERENCES lawyer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk21699c6d7a695 FOREIGN KEY (payment_id)
      REFERENCES payment (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk21699c8035b595 FOREIGN KEY (quote_id)
      REFERENCES quote (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk21699c980095bf FOREIGN KEY (customer_id)
      REFERENCES customer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk21699c4c228d8c FOREIGN KEY (feedback_id)
      REFERENCES profileitem (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
alter table payment add   CONSTRAINT fk3454c9e68885737f FOREIGN KEY (file_id)
      REFERENCES file (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
alter table customer add column creditcardtoken character varying(255);
alter table customer add column creditcardtype character varying(255);
alter table customer add column customerid character varying(255);
alter table customer add column maskednumber character varying(255);
alter table profileitem add column rating integer;
alter table lawyer add column abn character varying(255);
alter table lawyer add column accountnumber character varying(255);
alter table lawyer add column bsb character varying(255);
alter table lawyer add column gst boolean;