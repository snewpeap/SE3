insert into authors (id,au_name) values (1,'a'),(2,'b'),(3,'c');
insert into affiliations (id,country, af_name) values (1,'china','nju'),(2,'usa','google');
insert into conferences (id,c_name, ordno, hold_year) values (1,'ase',1,2000),(2,'icse',2,2032);
insert into terms (id,content) values (1,'data'),(2,'mining');
insert into papers (id,title,conference_id) values (1,'data data',1),(2,'mining',2),(3,'data mining',1);
insert into paper_aa (paper_id, affiliation_id, author_id) values (1,1,1),(2,2,2),(3,2,3),(3,1,1);
insert into papers_author_keywords (paper_id, author_keywords_id) values (1,1),(2,2),(3,1),(3,2);