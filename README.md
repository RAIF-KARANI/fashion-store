# FashionStore

A Java servlet-based e-commerce web application for browsing and purchasing fashion products, built with Jakarta Servlets and MySQL, deployed on Apache Tomcat.

## Features

- Browse products by category (men, women, kids) with product listing and detail pages
- User registration, login, and session-based authentication (with an auth filter for protected routes)
- Shopping cart management
- Checkout flow with order confirmation
- Order history ("My Orders")
- User profile management

## Tech Stack

- **Language:** Java 21
- **Web layer:** Jakarta Servlet API 6.0
- **Database:** MySQL (via `mysql-connector-j`)
- **JSON:** Gson
- **Build tool:** Maven (packaged as a WAR)
- **Server:** Apache Tomcat 10, run locally via the Cargo Maven plugin

## Project Structure

```
src/main/java/com/fashionstore/
  controller/   Servlets (Home, Login, Register, Cart, Checkout, Orders, Profile, ...)
  dao/          Data access objects for products, users, carts, and orders
  filter/       AuthFilter for protecting authenticated routes
  model/        Domain models (Product, User, Cart, Order, ...)
  util/         Database connection helper

src/main/webapp/
  views/        HTML views (home, login, register, product list/detail, cart, profile)
  assets/       CSS, JS, and images
  WEB-INF/      web.xml
```

## Getting Started

### Prerequisites

- JDK 21
- Maven (or use the included `mvnw` wrapper)
- MySQL Server

### Database Setup

1. Create a MySQL database (the app expects `fashion_store` on `localhost:3307` by default).
2. Configure the connection via environment variables (see below), or rely on the local defaults in [`DBConnection.java`](src/main/java/com/fashionstore/util/DBConnection.java).

### Configuration

Database credentials are read from environment variables, falling back to local defaults if unset:

| Variable  | Default                                        | Description            |
|-----------|-------------------------------------------------|-------------------------|
| `DB_URL`  | `jdbc:mysql://localhost:3307/fashion_store`     | JDBC connection URL    |
| `DB_USER` | `root`                                          | Database username      |
| `DB_PASS` | `root`                                          | Database password      |

Copy [`.env.example`](.env.example) to `.env` and adjust the values, or export them directly:

```bash
export DB_URL="jdbc:mysql://localhost:3307/fashion_store"
export DB_USER="root"
export DB_PASS="your-password"
```

### Run Locally

Using the Maven wrapper, this builds the WAR and starts it on an embedded Tomcat 10 instance via the Cargo plugin:

```bash
./mvnw cargo:run
```

The app will be available at `http://localhost:8081/FashionStore`.

## License

This project is licensed under the [MIT License](LICENSE).
