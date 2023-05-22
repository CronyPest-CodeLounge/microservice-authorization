create
user "authorization";
alter
user "authorization" with PASSWORD 'authorization';
create schema "auth";
alter
schema "auth" owner to "authorization";
