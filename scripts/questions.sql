alter table tanlsuser drop column name;
alter table tanlsuser drop column homeaddress_id;
alter table tanlsuser drop column creditcardtoken;
alter table tanlsuser drop column braintreecustomerid;
alter table tanlsuser drop column maskednumber;
alter table tanlsuser drop column emailnotifications;
alter table tanlsuser drop column bsb;
alter table tanlsuser drop column accountnumber;
alter table tanlsuser drop column creditcardtype;
drop table address;
CREATE TABLE address
(
  id integer NOT NULL,
  name character varying(255),
  postcode character varying(255),
  state character varying(255),
  streetaddress character varying(255),
  suburb character varying(255),
  CONSTRAINT address_pkey PRIMARY KEY (id )
);
CREATE TABLE customer
(
  id integer NOT NULL,
  companyname character varying(255),
  name character varying(255),
  numberofquestionsasked integer,
  billingaddress_id integer,
  user_id integer,
  CONSTRAINT customer_pkey PRIMARY KEY (id ),
  CONSTRAINT fk27fbe3fe8332bc90 FOREIGN KEY (billingaddress_id)
      REFERENCES address (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk27fbe3fece9982bd FOREIGN KEY (user_id)
      REFERENCES tanlsuser (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE TABLE question
(
  id integer NOT NULL,
  businessquestion boolean,
  text text,
  "time" bigint,
  areaofpractise_id integer,
  customer_id integer,
  CONSTRAINT question_pkey PRIMARY KEY (id ),
  CONSTRAINT fkbe5ca00694c70c9f FOREIGN KEY (areaofpractise_id)
      REFERENCES areaofpractise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkbe5ca006980095bf FOREIGN KEY (customer_id)
      REFERENCES customer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE TABLE answer
(
  id integer NOT NULL,
  status character varying(255),
  text text,
  lawyer_id integer,
  question_id integer,
  CONSTRAINT answer_pkey PRIMARY KEY (id ),
  CONSTRAINT fk752f2bde29b7893f FOREIGN KEY (lawyer_id)
      REFERENCES lawyer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk752f2bded17ab8bf FOREIGN KEY (question_id)
      REFERENCES question (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
alter table lawyer add column questionnotifications boolean default true;
update lawyer set questionnotifications = true;
alter table answer add column "time" bigint;
alter table lawyer add column uuid character varying(255);
alter table customer add column emailnotifications boolean;
update customer set emailnotifications = true;