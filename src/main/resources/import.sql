INSERT INTO regiones (nombre) VALUES('Sudamérica');
INSERT INTO regiones (nombre) VALUES('Centroamérica');
INSERT INTO regiones (nombre) VALUES('Norteamérica');
INSERT INTO regiones (nombre) VALUES('Europa');
INSERT INTO regiones (nombre) VALUES('Asia');
INSERT INTO regiones (nombre) VALUES('Africa');
INSERT INTO regiones (nombre) VALUES('Oaceanía');
INSERT INTO regiones (nombre) VALUES('Antártida');


INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(1, 'Andres', 'Guzman', 'profesor@bolsadeideas.com', '2017-08-01');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(2, 'John', 'Doe', 'john.doe@gmail.com', '2017-08-02');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(4, 'Linus', 'Torvalds', 'linus.torvalds@gmail.com', '2017-08-03');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(4, 'Jane', 'Doe', 'jane.doe@gmail.com', '2017-08-04');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(4, 'Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '2017-08-05');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'Erich', 'Gamma', 'erich.gamma@gmail.com', '2017-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'Richard', 'Helm', 'richard.helm@gmail.com', '2017-08-07');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'Ralph', 'Johnson', 'ralph.johnson@gmail.com', '2017-08-08');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'John', 'Vlissides', 'john.vlissides@gmail.com', '2017-08-09');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(5, 'James', 'Gosling', 'james.gosling@gmail.com', '2017-08-010');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(6, 'Bruce', 'Lee', 'bruce.lee@gmail.com', '2017-08-11');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(7, 'Johnny', 'Doe', 'johnny.doe@gmail.com', '2017-08-12');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(8, 'John', 'Roe', 'john.roe@gmail.com', '2017-08-13');

/* Creamos algunos usuarios con sus roles */
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES ('andres', '$2a$10$hiqpONv2Iw2JKiJIYw3k8.20F.yBufT6mbg6Gy8aMRKMHp1MeUaz.', 1, 'Andres', 'Guzman','profesor@bolsadeideas.com');
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES ('admin', '$2a$10$DkXG/biXyAo4eXl/P9dHyeQY8NFiCER7NT7KWx9zv4oGofUkv9OSW', 1, 'John', 'Doe', 'jhon.doe@bolsadeideas.com');

/*Por defecto se debe agregar el prefijo "ROL_" */
INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (1, 1);
INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2, 2);
INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2, 1);
/* Creamos algunos productos */
INSERT INTO productos (nombre, precio, create_at) VALUES ("Panasonic Pantalla LCD", 259990, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ("Sony Camara Digital DSC-W320B", 123490, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ("Apple iPod Shuffle", 1499990, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ("Sony Notebook Z110", 377990, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ("Hewlett Packard Multifuncional F2280", 69990, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ("Bianchi Bicicleta Aro 26", 69990, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ("Mica Comoda 5 cajones", 299990, now());

/* Creamos factura y sus items */
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ("Factura de equipos de oficina", null, 1, now());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 7);

/* Creamos factura y sus items */
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ("Factura Bicicleta", "Alguna nota importante!", 1, now());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (3, 2, 6);



