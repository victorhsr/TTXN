CREATE SEQUENCE IF NOT EXISTS public.seq_t_person_id;
CREATE TABLE IF NOT EXISTS public.t_person(id BIGINT, full_name VARCHAR(255));
--
CREATE SCHEMA IF NOT EXISTS middle_school;
CREATE SEQUENCE IF NOT EXISTS middle_school.seq_t_person_id;
CREATE TABLE IF NOT EXISTS middle_school.t_person(id BIGINT, full_name VARCHAR(255));
--
CREATE SCHEMA IF NOT EXISTS high_school;
CREATE SEQUENCE IF NOT EXISTS high_school.seq_t_person_id;
CREATE TABLE IF NOT EXISTS high_school.t_person(id BIGINT, full_name VARCHAR(255));