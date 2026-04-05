# GR07_1BT2_622_26A

Aplicacion web de supermercado (solo alimentos) con JSP, Servlets y ORM (JPA/Hibernate).

Permite:

- crear productos
- editar nombre, cantidad y precio
- eliminar productos
- listar inventario

## Requisitos

- Java 17 o superior
- Maven 3.9 o superior

## Estructura

- `pom.xml`: dependencias y configuración de compilación
- `src/main/java/ec/edu/epn/ma/model/Product.java`: entidad ORM
- `src/main/java/ec/edu/epn/ma/web/ProductServlet.java`: controlador Servlet CRUD
- `src/main/webapp/WEB-INF/views/products.jsp`: vista JSP
- `src/main/resources/META-INF/persistence.xml`: configuración JPA/Hibernate
- `src/test/java`: pruebas básicas

## Ejecutar pruebas

En PowerShell, desde la raíz del proyecto:

```powershell
mvn test
```

## Levantar la app web

En PowerShell, desde la raíz del proyecto:

```powershell
mvn jetty:run
```

Luego abre:

- `http://localhost:8080/products`

## Base de datos

- Motor: H2
- Modo: archivo local `./data/supermercado`
- Esquema: auto-actualizable (`hibernate.hbm2ddl.auto=update`)
