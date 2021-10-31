INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES ('willian', '$2a$10$UGECZ4ewVYKAzX5z4GuJgOP4wdrieh2xfj62tTHz2T61KUMNz6Tse', 1, 'Wilian', 'Morales', 'willian.4@gmail.com');
INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES ('admin', '$2a$10$JV9ErAXD2uo6oYmI4FKKwOZE67m7zC1t1nUsZ3drnUXx.e8GW42.K', 1, 'Admin', 'Prueba', 'admin@gmail.com'); 

INSERT INTO `roles` (nombre) VALUES ('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (1, 1);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2, 2);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2, 1);