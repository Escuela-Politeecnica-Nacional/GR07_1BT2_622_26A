package ec.edu.epn.ma.web;

import ec.edu.epn.ma.model.Product;
import ec.edu.epn.ma.persistence.JpaUtil;
import ec.edu.epn.ma.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "productServlet", urlPatterns = "/products")
public class ProductServlet extends HttpServlet {

    private final ProductRepository productRepository = new ProductRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            List<Product> products = productRepository.findAll(entityManager);
            request.setAttribute("products", products);
            request.setAttribute("message", request.getParameter("message"));
            request.setAttribute("error", request.getParameter("error"));
            request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
        } finally {
            entityManager.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = valueOrEmpty(request.getParameter("action"));

        try {
            switch (action) {
                case "create" -> createProduct(request);
                case "update" -> updateProduct(request);
                case "delete" -> deleteProduct(request);
                default -> throw new IllegalArgumentException("Accion no valida.");
            }
            redirectWithMessage(response, request.getContextPath(), "Operacion realizada correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectWithError(response, request.getContextPath(), ex.getMessage());
        }
    }

    private void createProduct(HttpServletRequest request) {
        String name = parseName(request.getParameter("name"));
        int quantity = parseQuantity(request.getParameter("quantity"));
        BigDecimal price = parsePrice(request.getParameter("price"));

        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            productRepository.save(entityManager, new Product(name, quantity, price));
            entityManager.getTransaction().commit();
        } catch (RuntimeException ex) {
            rollback(entityManager);
            throw ex;
        } finally {
            entityManager.close();
        }
    }

    private void updateProduct(HttpServletRequest request) {
        long id = parseId(request.getParameter("id"));
        String name = parseName(request.getParameter("name"));
        int quantity = parseQuantity(request.getParameter("quantity"));
        BigDecimal price = parsePrice(request.getParameter("price"));

        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Optional<Product> optionalProduct = productRepository.findById(entityManager, id);
            Product product = optionalProduct.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado."));
            product.setName(name);
            product.setQuantity(quantity);
            product.setPrice(price);
            productRepository.update(entityManager, product);

            entityManager.getTransaction().commit();
        } catch (RuntimeException ex) {
            rollback(entityManager);
            throw ex;
        } finally {
            entityManager.close();
        }
    }

    private void deleteProduct(HttpServletRequest request) {
        long id = parseId(request.getParameter("id"));

        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Optional<Product> optionalProduct = productRepository.findById(entityManager, id);
            Product product = optionalProduct.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado."));
            productRepository.delete(entityManager, product);

            entityManager.getTransaction().commit();
        } catch (RuntimeException ex) {
            rollback(entityManager);
            throw ex;
        } finally {
            entityManager.close();
        }
    }

    private static void rollback(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    private static String parseName(String raw) {
        String name = valueOrEmpty(raw).trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        return name;
    }

    private static int parseQuantity(String raw) {
        try {
            int quantity = Integer.parseInt(valueOrEmpty(raw));
            if (quantity < 0) {
                throw new IllegalArgumentException("La cantidad no puede ser negativa.");
            }
            return quantity;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La cantidad debe ser un numero entero.");
        }
    }

    private static BigDecimal parsePrice(String raw) {
        try {
            BigDecimal price = new BigDecimal(valueOrEmpty(raw));
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo.");
            }
            return price.setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El precio debe ser numerico.");
        }
    }

    private static long parseId(String raw) {
        try {
            return Long.parseLong(valueOrEmpty(raw));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Identificador de producto invalido.");
        }
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private static void redirectWithMessage(HttpServletResponse response, String contextPath, String message) throws IOException {
        response.sendRedirect(contextPath + "/products?message=" + encode(message));
    }

    private static void redirectWithError(HttpServletResponse response, String contextPath, String error) throws IOException {
        response.sendRedirect(contextPath + "/products?error=" + encode(error));
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}


