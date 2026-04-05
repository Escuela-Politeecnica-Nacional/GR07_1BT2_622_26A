<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Supermercado - Gestion de Alimentos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; }
        h1, h2 { margin-bottom: 8px; }
        table { border-collapse: collapse; width: 100%; margin-top: 12px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f3f3f3; }
        .row-form { display: flex; gap: 8px; align-items: center; }
        .row-form input { width: 100%; box-sizing: border-box; }
        .message { color: #0a7d27; font-weight: bold; }
        .error { color: #b30b0b; font-weight: bold; }
        .new-form { display: grid; grid-template-columns: 2fr 1fr 1fr auto; gap: 8px; max-width: 900px; }
        button { cursor: pointer; }
    </style>
</head>
<body>
<h1>Supermercado - Alimentos</h1>

<c:if test="${not empty message}">
    <p class="message">${message}</p>
</c:if>
<c:if test="${not empty error}">
    <p class="error">${error}</p>
</c:if>

<h2>Nuevo producto</h2>
<form method="post" action="${pageContext.request.contextPath}/products" class="new-form">
    <input type="hidden" name="action" value="create" />
    <input type="text" name="name" placeholder="Nombre" required />
    <input type="number" name="quantity" min="0" placeholder="Cantidad" required />
    <input type="number" name="price" min="0" step="0.01" placeholder="Precio" required />
    <button type="submit">Crear</button>
</form>

<h2>Inventario</h2>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Cantidad</th>
        <th>Precio</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>${product.id}</td>
            <td colspan="4">
                <form method="post" action="${pageContext.request.contextPath}/products" class="row-form">
                    <input type="hidden" name="id" value="${product.id}" />
                    <input type="text" name="name" value="${product.name}" required />
                    <input type="number" name="quantity" min="0" value="${product.quantity}" required />
                    <input type="number" name="price" min="0" step="0.01" value="${product.price}" required />
                    <button type="submit" name="action" value="update">Actualizar</button>
                    <button type="submit" name="action" value="delete" onclick="return confirm('Deseas eliminar este producto?');">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>

