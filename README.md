# Recipe Website - Professional Spring Boot Application

A comprehensive, production-ready recipe management system built with Spring Boot 3.x, featuring user authentication, recipe CRUD operations, reviews, ratings, social features, and more.

## 🚀 Features

### Core Features
- **User Management**
  - User registration and authentication with JWT
  - User profiles with bio and profile images
  - Follow/unfollow users
  - View followers and following lists

- **Recipe Management**
  - Create, read, update, and delete recipes
  - Rich recipe details (ingredients, instructions, nutritional info)
  - Recipe categories and difficulty levels
  - Image upload support
  - Draft and published states

- **Search & Discovery**
  - Search recipes by keywords
  - Filter by category, difficulty, and preparation time
  - Browse top-rated, latest, and most viewed recipes
  - Recipe recommendations

- **Reviews & Ratings**
  - Rate recipes (1-5 stars)
  - Write detailed reviews
  - View average ratings and review counts

- **Social Features**
  - Favorite recipes
  - Create recipe collections for meal planning
  - Follow favorite recipe authors
  - Share recipes

### Technical Features
- **Security**: JWT-based authentication with Spring Security
- **Database**: JPA/Hibernate with MySQL (H2 for development)
- **API Documentation**: Interactive Swagger/OpenAPI documentation
- **Validation**: Bean Validation for input validation
- **Exception Handling**: Global exception handler with custom exceptions
- **Pagination**: Paginated API responses
- **Docker**: Full Docker support with docker-compose
- **Testing**: Unit and integration tests

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (optional, uses H2 for development)
- Docker and Docker Compose (optional)

## 🛠️ Installation

### Option 1: Run with Maven (Development)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd GL
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Option 2: Run with Docker

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

This will start both MySQL and the application.

### Option 3: Run with Production Database

1. **Update `application-prod.properties`** with your MySQL credentials

2. **Run with production profile**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api-docs

### Authentication

Most endpoints require authentication. To use protected endpoints:

1. **Register a new user** via `POST /api/auth/signup`
2. **Login** via `POST /api/auth/login` to get JWT token
3. **Use the token** in the `Authorization` header as `Bearer <token>`

## 🔑 API Endpoints

### Authentication
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/profile` - Get current user profile
- `PUT /api/users/profile` - Update profile
- `POST /api/users/{id}/follow` - Follow user
- `DELETE /api/users/{id}/follow` - Unfollow user
- `GET /api/users/{id}/followers` - Get followers
- `GET /api/users/{id}/following` - Get following

### Recipes
- `GET /api/recipes` - Get all published recipes (paginated)
- `GET /api/recipes/{id}` - Get recipe by ID
- `GET /api/recipes/search?keyword=` - Search recipes
- `GET /api/recipes/category/{categoryId}` - Get recipes by category
- `GET /api/recipes/difficulty/{difficulty}` - Get recipes by difficulty
- `GET /api/recipes/top-rated` - Get top-rated recipes
- `GET /api/recipes/latest` - Get latest recipes
- `GET /api/recipes/author/{authorId}` - Get recipes by author
- `POST /api/recipes` - Create new recipe (authenticated)
- `PUT /api/recipes/{id}` - Update recipe (authenticated)
- `DELETE /api/recipes/{id}` - Delete recipe (authenticated)
- `POST /api/recipes/{id}/favorite` - Add to favorites (authenticated)
- `DELETE /api/recipes/{id}/favorite` - Remove from favorites (authenticated)
- `GET /api/recipes/favorites` - Get favorite recipes (authenticated)

### Reviews
- `POST /api/reviews/recipe/{recipeId}` - Create review (authenticated)
- `PUT /api/reviews/{reviewId}` - Update review (authenticated)
- `DELETE /api/reviews/{reviewId}` - Delete review (authenticated)
- `GET /api/reviews/recipe/{recipeId}` - Get reviews for recipe
- `GET /api/reviews/user/{userId}` - Get reviews by user

### Categories
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create category (Admin only)
- `PUT /api/categories/{id}` - Update category (Admin only)
- `DELETE /api/categories/{id}` - Delete category (Admin only)

## 🗄️ Database

### Development (H2)
The application uses H2 in-memory database for development.

- **H2 Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:recipedb`
- **Username**: `sa`
- **Password**: (empty)

### Production (MySQL)
For production, configure MySQL in `application-prod.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/recipedb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## 🧪 Testing

Run tests with:
```bash
mvn test
```

## 📦 Build for Production

Create a production-ready JAR:
```bash
mvn clean package -DskipTests
```

The JAR file will be in `target/recipe-website-1.0.0.jar`

Run it:
```bash
java -jar target/recipe-website-1.0.0.jar --spring.profiles.active=prod
```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── recipes/
│   │           ├── config/          # Configuration classes
│   │           ├── controller/      # REST controllers
│   │           ├── dto/             # Data Transfer Objects
│   │           ├── exception/       # Custom exceptions & handlers
│   │           ├── model/           # JPA entities
│   │           ├── repository/      # Spring Data repositories
│   │           ├── security/        # Security & JWT classes
│   │           ├── service/         # Business logic
│   │           └── RecipeWebsiteApplication.java
│   └── resources/
│       ├── application.properties
│       └── application-prod.properties
└── test/
    └── java/
```

## 🛡️ Security

- **Password Encryption**: BCrypt with Spring Security
- **JWT Authentication**: Secure token-based authentication
- **CORS**: Configured for cross-origin requests
- **Role-Based Access**: USER and ADMIN roles
- **Input Validation**: Bean Validation on all DTOs

## 🌟 Example Usage

### Register & Login
```bash
# Register
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"password123","fullName":"John Doe"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"password123"}'
```

### Create Recipe
```bash
curl -X POST http://localhost:8080/api/recipes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "title":"Chocolate Cake",
    "description":"Delicious chocolate cake",
    "instructions":"Mix ingredients and bake",
    "prepTime":20,
    "cookTime":40,
    "servings":8,
    "difficulty":"MEDIUM",
    "published":true,
    "ingredients":[
      {"ingredientName":"flour","quantity":2,"unit":"cups"},
      {"ingredientName":"sugar","quantity":1.5,"unit":"cups"}
    ]
  }'
```

## 📝 Configuration

Key configuration properties in `application.properties`:

- `server.port` - Application port (default: 8080)
- `spring.datasource.*` - Database configuration
- `jwt.secret` - JWT signing key (change in production!)
- `jwt.expiration` - Token expiration time (milliseconds)
- `spring.servlet.multipart.max-file-size` - Max upload size

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the Apache License 2.0.

## 📧 Contact

For questions or support, contact: contact@recipewebsite.com

## 🎯 Future Enhancements

- [ ] Recipe image upload to cloud storage
- [ ] Advanced search with filters
- [ ] Recipe sharing on social media
- [ ] Email notifications
- [ ] Recipe import from URLs
- [ ] Meal planner calendar
- [ ] Shopping list generation
- [ ] Nutrition calculator
- [ ] Multi-language support
- [ ] Mobile application

---

**Built with ❤️ using Spring Boot 3.x**
