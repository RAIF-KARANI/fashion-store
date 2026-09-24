# FashionStore

A Java servlet-based e-commerce web application for browsing and purchasing fashion products.

## Features

- **Product catalog** — browse products by category (men, women, kids), with listing and detail pages
- **Product variants** — support for sizes and variants per product
- **Authentication** — user registration, login/logout, and session-based auth guarded by a servlet filter
- **Shopping cart** — add, update, and manage cart items
- **Checkout** — checkout flow with order confirmation
- **Order history** — users can view their past orders ("My Orders")
- **User profile** — view and manage account details

## Tech Stack

- **Language:** Java 21
- **Web layer:** Jakarta Servlet API 6.0
- **Database:** MySQL (`mysql-connector-j`)
- **JSON:** Gson
- **Build tool:** Maven (packaged as a WAR)
- **Server:** Apache Tomcat 10
- **Frontend:** HTML, CSS, vanilla JavaScript

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

## License

This project is licensed under the [MIT License](LICENSE).
