# Professional Recipe Website - Spring Boot

A comprehensive, enterprise-grade Recipe Management Platform built with Spring Boot 3.x, featuring user authentication, recipe management, reviews, and social features.

## 🌟 Features

### Core Features
- **User Management**
  - User registration and authentication with JWT
  - User profiles with bio and profile images
  - Follow/Unfollow other users
  - Role-based access control (USER, ADMIN)

- **Recipe Management**
  - Create, read, update, and delete recipes
  - Rich recipe details (ingredients, instructions, nutrition info)
  - Recipe categorization and tagging
  - Recipe difficulty levels (EASY, MEDIUM, HARD)
  - Recipe images and preparation/cook times
  - Recipe search and filtering
  - View tracking and popularity metrics

- **Reviews & Ratings**
  - Write reviews for recipes
  - 5-star rating system
  - Average rating calculation
  - Comment system

- **Social Features**
  - Favorite/bookmark recipes
  - Follow other users
  - User collections/cookbooks
  - Activity feeds

- **Categories & Ingredients**
  - Predefined recipe categories
  - Ingredient database with descriptions
  - Recipe-specific ingredient quantities and units

### Technical Features
- **Security**
  - JWT-based authentication
  - Password encryption with BCrypt
  - Role-based authorization
  - CORS configuration

- **API Documentation**
  - Interactive Swagger UI
  - OpenAPI 3.0 specification
  - Comprehensive endpoint documentation

- **Database**
  - H2 in-memory database (development)
  - MySQL support (production)
  - Automatic schema management
  - Database migration support

- **Deployment**
  - Docker support
  - Docker Compose configuration
  - Production-ready setup

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+ (or use included Maven wrapper)
- Docker (optional, for containerized deployment)

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd GL
```

2. **Build the project**
```bash
# Windows
.\mvnw.cmd clean package

# Linux/Mac
./mvnw clean package
```

3. **Run the application**
```bash
# Windows
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
java -jar target/recipe-website-1.0.0.jar

# Linux/Mac
export JAVA_HOME=/path/to/jdk-17
java -jar target/recipe-website-1.0.0.jar
```

4. **Access the application**
- API Base URL: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:recipedb`
  - Username: `sa`
  - Password: (leave empty)

## 🐳 Docker Deployment

### Using Docker Compose
```bash
docker-compose up -d
```

This will start:
- Recipe Website API on port 8080
- MySQL database on port 3306

### Using Docker Only
```bash
# Build the image
docker build -t recipe-website .

# Run the container
docker run -p 8080:8080 recipe-website
```

## 📚 API Documentation

### Authentication Endpoints

#### Register a new user
```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "securePassword123",
  "fullName": "John Doe"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "johndoe",
  "password": "securePassword123"
}
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer"
}
```

### Recipe Endpoints

#### Create a new recipe
```http
POST /api/recipes
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Classic Spaghetti Carbonara",
  "description": "A traditional Italian pasta dish",
  "instructions": "1. Cook pasta\n2. Prepare sauce\n3. Mix together",
  "prepTime": 10,
  "cookTime": 15,
  "servings": 4,
  "difficulty": "MEDIUM",
  "categoryId": 1,
  "ingredients": [
    {
      "ingredientName": "Spaghetti",
      "quantity": 400,
      "unit": "g"
    },
    {
      "ingredientName": "Eggs",
      "quantity": 4,
      "unit": "pieces"
    }
  ],
  "published": true
}
```

#### Get all recipes
```http
GET /api/recipes?page=0&size=10&sort=createdAt,desc
```

#### Get recipe by ID
```http
GET /api/recipes/{id}
```

#### Update a recipe
```http
PUT /api/recipes/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Updated Recipe Title",
  ...
}
```

#### Delete a recipe
```http
DELETE /api/recipes/{id}
Authorization: Bearer <token>
```

#### Search recipes
```http
GET /api/recipes/search?query=pasta&category=Italian&difficulty=EASY
```

#### Get popular recipes
```http
GET /api/recipes/popular?limit=10
```

### Review Endpoints

#### Add a review
```http
POST /api/reviews
Authorization: Bearer <token>
Content-Type: application/json

{
  "recipeId": 1,
  "rating": 5,
  "comment": "Absolutely delicious!"
}
```

#### Get recipe reviews
```http
GET /api/reviews/recipe/{recipeId}
```

#### Update a review
```http
PUT /api/reviews/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "rating": 4,
  "comment": "Updated review"
}
```

### User Endpoints

#### Get user profile
```http
GET /api/users/{username}
```

#### Update user profile
```http
PUT /api/users/{username}
Authorization: Bearer <token>
Content-Type: application/json

{
  "fullName": "John Smith",
  "bio": "Food enthusiast and home chef"
}
```

#### Favorite a recipe
```http
POST /api/users/{username}/favorites/{recipeId}
Authorization: Bearer <token>
```

#### Get user's favorite recipes
```http
GET /api/users/{username}/favorites
```

#### Follow a user
```http
POST /api/users/{username}/follow/{targetUsername}
Authorization: Bearer <token>
```

#### Get user's followers
```http
GET /api/users/{username}/followers
```

#### Get user's following
```http
GET /api/users/{username}/following
```

### Category Endpoints

#### Get all categories
```http
GET /api/categories
```

#### Create a category (Admin only)
```http
POST /api/categories
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "name": "Italian",
  "description": "Traditional Italian cuisine"
}
```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/recipes/
│   │   ├── RecipeWebsiteApplication.java
│   │   ├── config/           # Configuration classes
│   │   │   ├── OpenApiConfig.java
│   │   │   └── SecurityConfig.java
│   │   ├── controller/       # REST controllers
│   │   │   ├── AuthController.java
│   │   │   ├── CategoryController.java
│   │   │   ├── RecipeController.java
│   │   │   ├── ReviewController.java
│   │   │   └── UserController.java
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── exception/        # Custom exceptions and handlers
│   │   ├── model/            # JPA entities
│   │   │   ├── Category.java
│   │   │   ├── Collection.java
│   │   │   ├── Ingredient.java
│   │   │   ├── Recipe.java
│   │   │   ├── RecipeIngredient.java
│   │   │   ├── Review.java
│   │   │   └── User.java
│   │   ├── repository/       # Spring Data JPA repositories
│   │   ├── security/         # Security components (JWT, filters)
│   │   └── service/          # Business logic services
│   └── resources/
│       ├── application.properties
│       └── application-prod.properties
└── test/
    └── java/com/recipes/     # Unit and integration tests
```

## 🔧 Configuration

### Application Properties

#### Development (application.properties)
```properties
# Server Configuration
server.port=8080

# Database Configuration (H2 in-memory)
spring.datasource.url=jdbc:h2:mem:recipedb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JWT Configuration
jwt.secret=your-secret-key
jwt.expiration=86400000

# API Documentation
springdoc.swagger-ui.enabled=true
```

#### Production (application-prod.properties)
```properties
# Server Configuration
server.port=8080

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/recipedb
spring.datasource.username=root
spring.datasource.password=your-mysql-password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Production Security
jwt.secret=${JWT_SECRET:your-production-secret-key}
```

### Environment Variables

For production deployment, set these environment variables:

```bash
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=your-super-secret-jwt-key-at-least-256-bits
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/recipedb
SPRING_DATASOURCE_USERNAME=recipeuser
SPRING_DATASOURCE_PASSWORD=secure-password
```

## 🧪 Testing

### Run all tests
```bash
# Windows
.\mvnw.cmd test

# Linux/Mac
./mvnw test
```

### Run with coverage
```bash
# Windows
.\mvnw.cmd clean test jacoco:report

# Linux/Mac
./mvnw clean test jacoco:report
```

## 📦 Building for Production

### Create executable JAR
```bash
.\mvnw.cmd clean package -Pprod
```

### Run production build
```bash
java -jar -Dspring.profiles.active=prod target/recipe-website-1.0.0.jar
```

## 🛠️ Development

### Prerequisites for Development
- Java 17 JDK
- IDE (IntelliJ IDEA, Eclipse, or VS Code with Java extensions)
- Postman or similar tool for API testing
- Git

### Setup Development Environment

1. **Install Java Extensions for VS Code**
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Lombok Annotations Support

2. **Configure IDE**
   - Enable annotation processing for Lombok
   - Set Java 17 as project SDK
   - Configure Maven settings

3. **Start Development Server**
```bash
# Using Maven wrapper
.\mvnw.cmd spring-boot:run

# Or using VS Code task: "Spring Boot: Run"
```

### Code Style
- Follow Java naming conventions
- Use Lombok annotations to reduce boilerplate
- Write comprehensive JavaDoc for public APIs
- Keep methods small and focused
- Use meaningful variable names

### Database Migrations
For production, use Flyway or Liquibase for database version control:
1. Create migration scripts in `src/main/resources/db/migration/`
2. Follow naming convention: `V1__Initial_schema.sql`
3. Run migrations automatically on startup

## 🔐 Security Considerations

### JWT Token
- Tokens expire after 24 hours (configurable)
- Use strong secret keys (256+ bits) in production
- Rotate secrets periodically
- Store tokens securely on client side

### Password Security
- Passwords are encrypted using BCrypt
- Minimum password requirements can be added via validation
- Consider implementing password strength meter

### API Security
- All endpoints except `/api/auth/**` require authentication
- Admin endpoints require ADMIN role
- CORS is configured for specific origins
- Rate limiting should be implemented for production

## 🚀 Deployment Options

### Option 1: Traditional Deployment
1. Build the JAR file
2. Copy to server
3. Run with `java -jar`
4. Use systemd/supervisor for process management

### Option 2: Docker Deployment
1. Build Docker image
2. Push to registry
3. Deploy to container orchestration platform
4. Configure environment variables

### Option 3: Cloud Platforms
- **Heroku**: Use Procfile
- **AWS Elastic Beanstalk**: Deploy JAR directly
- **Google Cloud Run**: Use Docker container
- **Azure App Service**: Deploy JAR or Docker

## 📈 Performance Optimization

### Database Optimization
- Add indexes on frequently queried columns
- Use pagination for large result sets
- Implement caching with Redis
- Optimize N+1 query problems with `@EntityGraph`

### API Optimization
- Enable HTTP/2
- Implement response compression
- Use CDN for static assets
- Implement API rate limiting

### Monitoring
- Configure Spring Boot Actuator
- Integrate with monitoring tools (Prometheus, Grafana)
- Set up logging (ELK stack)
- Track application metrics

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- **Senior Software Engineering Team** - *Initial work*

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- All contributors who help improve this project
- Open source community for inspiration and support

## 📞 Support

For support and questions:
- Create an issue in the GitHub repository
- Email: support@recipewebsite.com
- Documentation: http://localhost:8080/swagger-ui.html

## 🗺️ Roadmap

### Version 1.1
- [ ] Recipe import from URL
- [ ] Meal planning feature
- [ ] Shopping list generation
- [ ] Email notifications

### Version 1.2
- [ ] Mobile app (React Native)
- [ ] Recipe recommendations based on preferences
- [ ] Nutritional analysis
- [ ] Recipe video support

### Version 2.0
- [ ] Multi-language support
- [ ] Advanced search with Elasticsearch
- [ ] Social sharing features
- [ ] Recipe collections marketplace

---

**Built with ❤️ using Spring Boot and Java**
