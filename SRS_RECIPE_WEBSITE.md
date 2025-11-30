# SOFTWARE REQUIREMENTS SPECIFICATION (SRS)
## Recipe Website - Professional Recipe Management Platform

**Version:** 1.0  
**Date:** October 20, 2025  
**Status:** Approved

---

## DOCUMENT CONTROL

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | Oct 20, 2025 | Development Team | Initial release |

---

## TABLE OF CONTENTS

1. [Introduction](#1-introduction)
2. [Overall Description](#2-overall-description)
3. [System Features and Requirements](#3-system-features-and-requirements)
4. [External Interface Requirements](#4-external-interface-requirements)
5. [Non-Functional Requirements](#5-non-functional-requirements)
6. [Other Requirements](#6-other-requirements)

---

## 1. INTRODUCTION

### 1.1 Purpose

This Software Requirements Specification (SRS) describes the functional and non-functional requirements for the Recipe Website application. This document is intended for:
- Development team
- Project stakeholders
- Quality assurance team
- Future maintainers

### 1.2 Document Conventions

**Priority Levels:**
- **P0 (Critical):** Must be implemented for system to function
- **P1 (High):** Essential for primary use cases
- **P2 (Medium):** Important but not essential for launch
- **P3 (Low):** Nice to have, can be deferred

**Requirement Notation:**
- **FR:** Functional Requirement
- **NFR:** Non-Functional Requirement
- **UR:** User Requirement
- **SR:** System Requirement

### 1.3 Intended Audience

This document is intended for:
- Software developers implementing the system
- Testers validating requirements
- Project managers tracking progress
- Client stakeholders approving features

### 1.4 Project Scope

The Recipe Website is a comprehensive web-based platform for recipe management, sharing, and social interaction. The system enables users to:
- Create and manage personal recipes
- Search and discover recipes from other users
- Rate and review recipes
- Follow other cooking enthusiasts
- Organize favorite recipes
- Interact with a community of food lovers

**In Scope:**
- User authentication and authorization
- Recipe CRUD operations
- Review and rating system
- Category management
- Search and filtering
- Social features (follow, favorite)
- RESTful API
- API documentation

**Out of Scope:**
- Mobile applications (API-ready for future development)
- Image upload/storage (to be added later)
- Email notifications (future enhancement)
- Payment processing (future premium features)
- Recipe video support (future enhancement)

### 1.5 References

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- OpenAPI Specification: https://swagger.io/specification/
- JWT RFC 7519: https://tools.ietf.org/html/rfc7519
- REST API Best Practices: https://restfulapi.net/
- OWASP Security Guidelines: https://owasp.org/

---

## 2. OVERALL DESCRIPTION

### 2.1 Product Perspective

The Recipe Website is a standalone web application that provides a complete backend API for recipe management. The system is self-contained but designed to support future integrations:

**System Context:**
```
┌─────────────────────────────────────────────────┐
│            Future Client Applications            │
│  (Web Frontend, Mobile Apps, Third-party)       │
└───────────────────┬─────────────────────────────┘
                    │ HTTP/REST
┌───────────────────▼─────────────────────────────┐
│          Recipe Website API (Backend)            │
│  ┌─────────────────────────────────────────┐   │
│  │      Spring Boot Application           │   │
│  │  - Authentication (JWT)                 │   │
│  │  - Business Logic                       │   │
│  │  - Data Validation                      │   │
│  └────────────────┬────────────────────────┘   │
└──────────────────┬─────────────────────────────┘
                   │ JDBC
┌──────────────────▼──────────────────────────────┐
│              Database Layer                      │
│     (H2 for dev, MySQL for production)          │
└─────────────────────────────────────────────────┘
```

### 2.2 Product Functions

The system provides the following high-level functions:

1. **User Management**
   - User registration and authentication
   - Profile management
   - Social networking (follow/followers)

2. **Recipe Management**
   - Create, read, update, delete recipes
   - Recipe categorization
   - Ingredient management
   - Nutritional information tracking

3. **Discovery**
   - Search recipes
   - Filter by category/difficulty
   - View popular recipes
   - Browse user recipes

4. **Engagement**
   - Rate recipes (1-5 stars)
   - Write reviews
   - Favorite recipes
   - View recipe statistics

5. **Administration**
   - Category management
   - User role management
   - System monitoring

### 2.3 User Classes and Characteristics

| User Class | Description | Technical Expertise | Key Needs |
|------------|-------------|-------------------|-----------|
| **Anonymous User** | Visitor browsing recipes | Low | View recipes, search, browse categories |
| **Registered User** | Authenticated member | Low-Medium | Create recipes, write reviews, save favorites |
| **Recipe Author** | User who creates recipes | Medium | Manage own recipes, track engagement |
| **Administrator** | System manager | High | Manage categories, moderate content |
| **API Consumer** | External application | High | Access data programmatically |

### 2.4 Operating Environment

**Server Environment:**
- **OS:** Windows, Linux, macOS
- **Runtime:** Java 17 or higher
- **Application Server:** Embedded Tomcat (Spring Boot)
- **Database:** H2 (development), MySQL 8.0+ (production)

**Client Environment:**
- **Browser:** Any modern web browser with JavaScript support
- **API Clients:** Postman, cURL, custom applications
- **Network:** Internet connection required

**Development Environment:**
- **IDE:** Visual Studio Code, IntelliJ IDEA, Eclipse
- **Build Tool:** Maven 3.6+
- **Version Control:** Git

### 2.5 Design and Implementation Constraints

**Technical Constraints:**
- Must use Java 17+ (for long-term support)
- Must use Spring Boot 3.x framework
- Must use relational database (JPA/Hibernate)
- Must follow RESTful API principles
- Must use JWT for authentication
- Must be stateless (no server-side sessions)

**Security Constraints:**
- Passwords must be encrypted (BCrypt)
- API endpoints must be secured
- Must implement role-based access control
- Must prevent SQL injection
- Must validate all user inputs

**Performance Constraints:**
- API response time < 200ms (95th percentile)
- Support minimum 100 concurrent users
- Database queries must be optimized

**Compatibility Constraints:**
- Must be platform-independent (Java)
- Must use standard HTTP/REST protocols
- Must follow OpenAPI 3.0 specification

### 2.6 Assumptions and Dependencies

**Assumptions:**
1. Users have stable internet connection
2. Users understand basic recipe concepts
3. System will have single database instance initially
4. Text-only recipes (no images in v1.0)

**Dependencies:**
1. **Spring Boot Framework:** Core application framework
2. **Spring Security:** Authentication and authorization
3. **Spring Data JPA:** Data access layer
4. **JWT Library:** Token generation and validation
5. **H2/MySQL:** Database systems
6. **Swagger/SpringDoc:** API documentation
7. **Lombok:** Code generation
8. **Jakarta Validation:** Input validation

---

## 3. SYSTEM FEATURES AND REQUIREMENTS

### 3.1 User Authentication and Authorization

**Priority:** P0 (Critical)

**Description:**  
System must provide secure user authentication using JWT tokens and enforce authorization based on user roles.

#### 3.1.1 User Registration

**FR-AUTH-001:** User Registration  
**Priority:** P0  
**Description:** System shall allow new users to register with unique credentials.

**Requirements:**
- **FR-AUTH-001.1:** System shall accept username, email, password, and full name during registration
- **FR-AUTH-001.2:** System shall validate username is unique (3-50 characters)
- **FR-AUTH-001.3:** System shall validate email format and uniqueness
- **FR-AUTH-001.4:** System shall enforce password minimum length of 6 characters
- **FR-AUTH-001.5:** System shall encrypt password using BCrypt before storage
- **FR-AUTH-001.6:** System shall assign default role USER to new registrations
- **FR-AUTH-001.7:** System shall return appropriate error messages for validation failures

**Input:**
```json
{
  "username": "string (3-50 chars, unique)",
  "email": "string (valid email, unique)",
  "password": "string (min 6 chars)",
  "fullName": "string (max 100 chars)"
}
```

**Output:**
```json
{
  "message": "User registered successfully",
  "status": 201
}
```

**Error Conditions:**
- Username already exists (HTTP 400)
- Email already exists (HTTP 400)
- Invalid email format (HTTP 400)
- Password too short (HTTP 400)

#### 3.1.2 User Login

**FR-AUTH-002:** User Login  
**Priority:** P0  
**Description:** System shall authenticate users and issue JWT tokens.

**Requirements:**
- **FR-AUTH-002.1:** System shall accept username or email with password
- **FR-AUTH-002.2:** System shall verify credentials against stored encrypted passwords
- **FR-AUTH-002.3:** System shall generate JWT token upon successful authentication
- **FR-AUTH-002.4:** JWT token shall include user ID, username, and roles
- **FR-AUTH-002.5:** JWT token shall expire after 24 hours
- **FR-AUTH-002.6:** System shall return user information with token
- **FR-AUTH-002.7:** System shall return 401 for invalid credentials

**Input:**
```json
{
  "usernameOrEmail": "string",
  "password": "string"
}
```

**Output:**
```json
{
  "accessToken": "JWT token string",
  "tokenType": "Bearer",
  "id": 1,
  "username": "string",
  "email": "string",
  "roles": ["USER"]
}
```

#### 3.1.3 Authorization

**FR-AUTH-003:** Role-Based Access Control  
**Priority:** P0  
**Description:** System shall enforce role-based access to endpoints.

**Requirements:**
- **FR-AUTH-003.1:** System shall support USER and ADMIN roles
- **FR-AUTH-003.2:** System shall require authentication for protected endpoints
- **FR-AUTH-003.3:** System shall allow only ADMINs to manage categories
- **FR-AUTH-003.4:** System shall allow users to modify only their own resources
- **FR-AUTH-003.5:** System shall return 401 for unauthenticated requests
- **FR-AUTH-003.6:** System shall return 403 for unauthorized access attempts

---

### 3.2 User Profile Management

**Priority:** P1 (High)

**Description:**  
System must allow users to view and manage their profiles and interact with other users.

#### 3.2.1 Profile Viewing

**FR-USER-001:** View User Profile  
**Priority:** P1  
**Description:** System shall display user profile information.

**Requirements:**
- **FR-USER-001.1:** System shall display username, full name, bio, and profile image URL
- **FR-USER-001.2:** System shall display user's recipe count
- **FR-USER-001.3:** System shall display follower and following counts
- **FR-USER-001.4:** System shall hide email address from public view
- **FR-USER-001.5:** System shall show account creation date
- **FR-USER-001.6:** System shall return 404 for non-existent users

**Endpoint:** `GET /api/users/{username}`

#### 3.2.2 Profile Editing

**FR-USER-002:** Update User Profile  
**Priority:** P1  
**Description:** System shall allow users to update their own profiles.

**Requirements:**
- **FR-USER-002.1:** System shall allow updating full name, bio, and profile image URL
- **FR-USER-002.2:** System shall restrict edit access to profile owner
- **FR-USER-002.3:** System shall validate bio length (max 500 characters)
- **FR-USER-002.4:** System shall validate profile image URL format
- **FR-USER-002.5:** System shall prevent username and email changes
- **FR-USER-002.6:** System shall return updated profile on success

**Endpoint:** `PUT /api/users/{username}`

#### 3.2.3 Follow System

**FR-USER-003:** Follow/Unfollow Users  
**Priority:** P2 (Medium)  
**Description:** System shall allow users to follow and unfollow other users.

**Requirements:**
- **FR-USER-003.1:** System shall allow authenticated users to follow other users
- **FR-USER-003.2:** System shall prevent users from following themselves
- **FR-USER-003.3:** System shall allow users to unfollow followed users
- **FR-USER-003.4:** System shall maintain bidirectional follower relationships
- **FR-USER-003.5:** System shall return 400 for duplicate follow attempts
- **FR-USER-003.6:** System shall update follower/following counts

**Endpoints:**
- `POST /api/users/{username}/follow/{targetUsername}`
- `DELETE /api/users/{username}/follow/{targetUsername}`

---

### 3.3 Recipe Management

**Priority:** P0 (Critical)

**Description:**  
Core functionality for creating, viewing, updating, and deleting recipes.

#### 3.3.1 Create Recipe

**FR-RECIPE-001:** Create New Recipe  
**Priority:** P0  
**Description:** System shall allow authenticated users to create recipes.

**Requirements:**
- **FR-RECIPE-001.1:** System shall require authentication
- **FR-RECIPE-001.2:** System shall accept title, description, instructions, prep time, cook time, servings
- **FR-RECIPE-001.3:** System shall validate title (min 3, max 200 characters)
- **FR-RECIPE-001.4:** System shall validate description (max 1000 characters)
- **FR-RECIPE-001.5:** System shall require at least one ingredient
- **FR-RECIPE-001.6:** System shall support difficulty levels: EASY, MEDIUM, HARD
- **FR-RECIPE-001.7:** System shall auto-set author to authenticated user
- **FR-RECIPE-001.8:** System shall set creation and update timestamps
- **FR-RECIPE-001.9:** System shall support optional nutritional information
- **FR-RECIPE-001.10:** System shall support optional category assignment
- **FR-RECIPE-001.11:** System shall support published/draft status

**Input:**
```json
{
  "title": "string (3-200 chars, required)",
  "description": "string (max 1000 chars, required)",
  "instructions": "string (required)",
  "prepTime": "integer (minutes, required)",
  "cookTime": "integer (minutes, required)",
  "servings": "integer (required)",
  "difficulty": "EASY|MEDIUM|HARD",
  "categoryId": "long (optional)",
  "calories": "integer (optional)",
  "protein": "double (optional)",
  "carbohydrates": "double (optional)",
  "fat": "double (optional)",
  "fiber": "double (optional)",
  "ingredients": [
    {
      "ingredientName": "string",
      "quantity": "double",
      "unit": "string",
      "notes": "string (optional)"
    }
  ],
  "published": "boolean"
}
```

**Output:**
```json
{
  "id": 1,
  "title": "string",
  "description": "string",
  ...
  "author": {
    "id": 1,
    "username": "string",
    "fullName": "string"
  },
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

#### 3.3.2 View Recipe

**FR-RECIPE-002:** View Recipe Details  
**Priority:** P0  
**Description:** System shall display complete recipe information.

**Requirements:**
- **FR-RECIPE-002.1:** System shall display all recipe fields
- **FR-RECIPE-002.2:** System shall display author information
- **FR-RECIPE-002.3:** System shall display category if assigned
- **FR-RECIPE-002.4:** System shall display list of ingredients with quantities
- **FR-RECIPE-002.5:** System shall display nutritional information if available
- **FR-RECIPE-002.6:** System shall display average rating and review count
- **FR-RECIPE-002.7:** System shall display view count
- **FR-RECIPE-002.8:** System shall increment view count on each access
- **FR-RECIPE-002.9:** System shall return 404 for non-existent recipes
- **FR-RECIPE-002.10:** System shall show only published recipes to non-authors

**Endpoint:** `GET /api/recipes/{id}`

#### 3.3.3 Update Recipe

**FR-RECIPE-003:** Update Recipe  
**Priority:** P0  
**Description:** System shall allow recipe authors to update their recipes.

**Requirements:**
- **FR-RECIPE-003.1:** System shall require authentication
- **FR-RECIPE-003.2:** System shall restrict updates to recipe author
- **FR-RECIPE-003.3:** System shall allow updating all editable fields
- **FR-RECIPE-003.4:** System shall update timestamp on modification
- **FR-RECIPE-003.5:** System shall validate updated data
- **FR-RECIPE-003.6:** System shall preserve read-only fields (id, author, createdAt)
- **FR-RECIPE-003.7:** System shall return 403 for non-author update attempts

**Endpoint:** `PUT /api/recipes/{id}`

#### 3.3.4 Delete Recipe

**FR-RECIPE-004:** Delete Recipe  
**Priority:** P1  
**Description:** System shall allow recipe authors or admins to delete recipes.

**Requirements:**
- **FR-RECIPE-004.1:** System shall require authentication
- **FR-RECIPE-004.2:** System shall allow deletion by recipe author or admin
- **FR-RECIPE-004.3:** System shall cascade delete associated reviews
- **FR-RECIPE-004.4:** System shall cascade delete ingredient associations
- **FR-RECIPE-004.5:** System shall remove from user favorites
- **FR-RECIPE-004.6:** System shall return 204 on successful deletion
- **FR-RECIPE-004.7:** System shall return 403 for unauthorized deletion attempts

**Endpoint:** `DELETE /api/recipes/{id}`

#### 3.3.5 List Recipes

**FR-RECIPE-005:** List All Recipes  
**Priority:** P0  
**Description:** System shall display paginated list of recipes.

**Requirements:**
- **FR-RECIPE-005.1:** System shall support pagination (page, size parameters)
- **FR-RECIPE-005.2:** System shall return only published recipes to non-authors
- **FR-RECIPE-005.3:** System shall include basic recipe information in list
- **FR-RECIPE-005.4:** System shall include author information
- **FR-RECIPE-005.5:** System shall support sorting (by date, rating, views)
- **FR-RECIPE-005.6:** Default page size shall be 10
- **FR-RECIPE-005.7:** Maximum page size shall be 100

**Endpoint:** `GET /api/recipes?page=0&size=10&sort=createdAt,desc`

#### 3.3.6 Search Recipes

**FR-RECIPE-006:** Search Recipes  
**Priority:** P1  
**Description:** System shall allow searching recipes by various criteria.

**Requirements:**
- **FR-RECIPE-006.1:** System shall support search by title (case-insensitive, partial match)
- **FR-RECIPE-006.2:** System shall support filtering by category
- **FR-RECIPE-006.3:** System shall support filtering by difficulty
- **FR-RECIPE-006.4:** System shall support filtering by author username
- **FR-RECIPE-006.5:** System shall combine multiple filters with AND logic
- **FR-RECIPE-006.6:** System shall return paginated results
- **FR-RECIPE-006.7:** System shall return empty list if no matches found

**Endpoint:** `GET /api/recipes/search?title=pasta&category=1&difficulty=EASY&page=0&size=10`

---

### 3.4 Review and Rating System

**Priority:** P1 (High)

**Description:**  
System must allow users to rate and review recipes.

#### 3.4.1 Create Review

**FR-REVIEW-001:** Create Recipe Review  
**Priority:** P1  
**Description:** System shall allow users to review recipes.

**Requirements:**
- **FR-REVIEW-001.1:** System shall require authentication
- **FR-REVIEW-001.2:** System shall require rating (1-5 stars, integer)
- **FR-REVIEW-001.3:** System shall accept optional comment (max 2000 characters)
- **FR-REVIEW-001.4:** System shall enforce one review per user per recipe
- **FR-REVIEW-001.5:** System shall prevent users from reviewing own recipes
- **FR-REVIEW-001.6:** System shall automatically update recipe average rating
- **FR-REVIEW-001.7:** System shall automatically update recipe review count
- **FR-REVIEW-001.8:** System shall set review timestamp

**Input:**
```json
{
  "recipeId": 1,
  "rating": 5,
  "comment": "string (optional, max 2000 chars)"
}
```

**Endpoint:** `POST /api/reviews`

#### 3.4.2 Update Review

**FR-REVIEW-002:** Update Review  
**Priority:** P2  
**Description:** System shall allow users to update their own reviews.

**Requirements:**
- **FR-REVIEW-002.1:** System shall require authentication
- **FR-REVIEW-002.2:** System shall restrict updates to review author
- **FR-REVIEW-002.3:** System shall allow updating rating and comment
- **FR-REVIEW-002.4:** System shall recalculate recipe average rating
- **FR-REVIEW-002.5:** System shall return 403 for unauthorized updates

**Endpoint:** `PUT /api/reviews/{id}`

#### 3.4.3 Delete Review

**FR-REVIEW-003:** Delete Review  
**Priority:** P2  
**Description:** System shall allow users to delete their reviews.

**Requirements:**
- **FR-REVIEW-003.1:** System shall require authentication
- **FR-REVIEW-003.2:** System shall allow deletion by review author or admin
- **FR-REVIEW-003.3:** System shall recalculate recipe average rating
- **FR-REVIEW-003.4:** System shall decrement recipe review count

**Endpoint:** `DELETE /api/reviews/{id}`

#### 3.4.4 List Recipe Reviews

**FR-REVIEW-004:** Get Recipe Reviews  
**Priority:** P1  
**Description:** System shall display all reviews for a recipe.

**Requirements:**
- **FR-REVIEW-004.1:** System shall display all reviews for specified recipe
- **FR-REVIEW-004.2:** System shall include reviewer information
- **FR-REVIEW-004.3:** System shall order by date (newest first)
- **FR-REVIEW-004.4:** System shall include rating and comment
- **FR-REVIEW-004.5:** System shall show review timestamps

**Endpoint:** `GET /api/reviews/recipe/{recipeId}`

---

### 3.5 Category Management

**Priority:** P1 (High)

**Description:**  
System must support recipe categorization.

#### 3.5.1 List Categories

**FR-CAT-001:** List All Categories  
**Priority:** P1  
**Description:** System shall display all available categories.

**Requirements:**
- **FR-CAT-001.1:** System shall return all categories
- **FR-CAT-001.2:** System shall include category name, description, icon URL
- **FR-CAT-001.3:** System shall order categories alphabetically
- **FR-CAT-001.4:** Public endpoint (no authentication required)

**Endpoint:** `GET /api/categories`

#### 3.5.2 Create Category

**FR-CAT-002:** Create Category  
**Priority:** P2  
**Description:** System shall allow admins to create categories.

**Requirements:**
- **FR-CAT-002.1:** System shall require ADMIN role
- **FR-CAT-002.2:** System shall require unique category name
- **FR-CAT-002.3:** System shall validate name (3-100 characters)
- **FR-CAT-002.4:** System shall accept optional description (max 500 chars)
- **FR-CAT-002.5:** System shall accept optional icon URL
- **FR-CAT-002.6:** System shall return 403 for non-admin users

**Endpoint:** `POST /api/categories` (Admin only)

#### 3.5.3 Update Category

**FR-CAT-003:** Update Category  
**Priority:** P2  
**Description:** System shall allow admins to update categories.

**Requirements:**
- **FR-CAT-003.1:** System shall require ADMIN role
- **FR-CAT-003.2:** System shall validate updated data
- **FR-CAT-003.3:** System shall prevent duplicate names

**Endpoint:** `PUT /api/categories/{id}` (Admin only)

#### 3.5.4 Delete Category

**FR-CAT-004:** Delete Category  
**Priority:** P2  
**Description:** System shall allow admins to delete unused categories.

**Requirements:**
- **FR-CAT-004.1:** System shall require ADMIN role
- **FR-CAT-004.2:** System shall prevent deletion if category has recipes
- **FR-CAT-004.3:** System shall return appropriate error for in-use categories

**Endpoint:** `DELETE /api/categories/{id}` (Admin only)

---

### 3.6 Favorite System

**Priority:** P2 (Medium)

**Description:**  
System must allow users to favorite recipes.

#### 3.6.1 Add Favorite

**FR-FAV-001:** Favorite Recipe  
**Priority:** P2  
**Description:** System shall allow users to favorite recipes.

**Requirements:**
- **FR-FAV-001.1:** System shall require authentication
- **FR-FAV-001.2:** System shall add recipe to user's favorites
- **FR-FAV-001.3:** System shall prevent duplicate favorites
- **FR-FAV-001.4:** System shall return 400 for duplicate attempts

**Endpoint:** `POST /api/users/{username}/favorites/{recipeId}`

#### 3.6.2 Remove Favorite

**FR-FAV-002:** Unfavorite Recipe  
**Priority:** P2  
**Description:** System shall allow users to remove favorites.

**Requirements:**
- **FR-FAV-002.1:** System shall require authentication
- **FR-FAV-002.2:** System shall remove recipe from favorites
- **FR-FAV-002.3:** System shall return 404 if not favorited

**Endpoint:** `DELETE /api/users/{username}/favorites/{recipeId}`

#### 3.6.3 List Favorites

**FR-FAV-003:** Get User Favorites  
**Priority:** P2  
**Description:** System shall display user's favorite recipes.

**Requirements:**
- **FR-FAV-003.1:** System shall return all favorited recipes for user
- **FR-FAV-003.2:** System shall include complete recipe information
- **FR-FAV-003.3:** System shall order by date added (newest first)

**Endpoint:** `GET /api/users/{username}/favorites`

---

## 4. EXTERNAL INTERFACE REQUIREMENTS

### 4.1 User Interfaces

**Not applicable** - This is a backend API system. User interfaces will be developed separately as clients consuming the API.

**API Documentation Interface:**
- Swagger UI available at `/swagger-ui.html`
- Interactive API testing interface
- Endpoint documentation
- Request/response schemas

### 4.2 Hardware Interfaces

**Not applicable** - Standard web server hardware requirements only.

### 4.3 Software Interfaces

#### 4.3.1 Database Interface

**Database System:** H2 (dev) / MySQL (production)

**Interface Type:** JDBC via Spring Data JPA/Hibernate

**Operations:**
- CRUD operations on all entities
- Complex queries via JPA Query Methods
- Transaction management
- Connection pooling via HikariCP

**Data Format:** Relational database with foreign key constraints

#### 4.3.2 Authentication Interface

**JWT Token Format:**
```
Header: {
  "alg": "HS512",
  "typ": "JWT"
}
Payload: {
  "sub": "username",
  "id": 123,
  "roles": ["USER"],
  "iat": 1234567890,
  "exp": 1234654290
}
```

**Token Transmission:** HTTP Authorization header: `Bearer <token>`

### 4.4 Communication Interfaces

#### 4.4.1 HTTP/REST Protocol

**Protocol:** HTTP/1.1  
**Data Format:** JSON  
**Character Encoding:** UTF-8  
**Port:** 8080 (configurable)

**HTTP Methods Used:**
- GET: Retrieve resources
- POST: Create resources
- PUT: Update resources
- DELETE: Remove resources

**Status Codes:**
- 200 OK: Successful GET/PUT
- 201 Created: Successful POST
- 204 No Content: Successful DELETE
- 400 Bad Request: Invalid input
- 401 Unauthorized: Authentication required
- 403 Forbidden: Insufficient permissions
- 404 Not Found: Resource doesn't exist
- 409 Conflict: Resource conflict
- 500 Internal Server Error: Server error

#### 4.4.2 API Request/Response Format

**Request Headers:**
```
Content-Type: application/json
Authorization: Bearer <JWT-token>
Accept: application/json
```

**Success Response Example:**
```json
{
  "id": 1,
  "data": { ... },
  "timestamp": "2025-10-20T12:00:00Z"
}
```

**Error Response Format:**
```json
{
  "timestamp": "2025-10-20T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/recipes"
}
```

---

## 5. NON-FUNCTIONAL REQUIREMENTS

### 5.1 Performance Requirements

**NFR-PERF-001:** Response Time  
**Requirement:** 95% of API requests shall respond in less than 200ms under normal load.

**NFR-PERF-002:** Throughput  
**Requirement:** System shall support minimum 100 concurrent users.

**NFR-PERF-003:** Database Performance  
**Requirement:** Database queries shall be optimized with proper indexing on frequently queried fields.

**NFR-PERF-004:** Memory Usage  
**Requirement:** Application shall operate efficiently within 512MB heap space.

### 5.2 Safety Requirements

**NFR-SAFE-001:** Data Backup  
**Requirement:** System shall support database backup and restore procedures.

**NFR-SAFE-002:** Data Validation  
**Requirement:** All user input shall be validated before processing.

**NFR-SAFE-003:** Error Handling  
**Requirement:** System shall handle all errors gracefully without exposing sensitive information.

### 5.3 Security Requirements

**NFR-SEC-001:** Authentication  
**Requirement:** System shall use JWT tokens for stateless authentication.

**NFR-SEC-002:** Password Encryption  
**Requirement:** Passwords shall be encrypted using BCrypt with minimum strength of 12.

**NFR-SEC-003:** SQL Injection Prevention  
**Requirement:** System shall use parameterized queries (via JPA) to prevent SQL injection.

**NFR-SEC-004:** Input Validation  
**Requirement:** All API inputs shall be validated using Jakarta Validation annotations.

**NFR-SEC-005:** HTTPS Support  
**Requirement:** System shall support HTTPS in production environment.

**NFR-SEC-006:** Token Expiration  
**Requirement:** JWT tokens shall expire after 24 hours.

**NFR-SEC-007:** Authorization  
**Requirement:** System shall enforce role-based access control on protected endpoints.

### 5.4 Software Quality Attributes

#### 5.4.1 Reliability

**NFR-REL-001:** Availability  
**Requirement:** System shall maintain 99% uptime during business hours.

**NFR-REL-002:** Error Recovery  
**Requirement:** System shall gracefully handle and log all exceptions.

**NFR-REL-003:** Data Integrity  
**Requirement:** Database constraints shall enforce referential integrity.

#### 5.4.2 Maintainability

**NFR-MAIN-001:** Code Organization  
**Requirement:** System shall follow layered architecture (Controller-Service-Repository).

**NFR-MAIN-002:** Code Documentation  
**Requirement:** All public APIs shall have Javadoc documentation.

**NFR-MAIN-003:** Logging  
**Requirement:** System shall log all important operations and errors.

**NFR-MAIN-004:** Configuration  
**Requirement:** System configuration shall be externalized in properties files.

#### 5.4.3 Portability

**NFR-PORT-001:** Platform Independence  
**Requirement:** System shall run on Windows, Linux, and macOS without modification.

**NFR-PORT-002:** Containerization  
**Requirement:** System shall be deployable as Docker container.

**NFR-PORT-003:** Database Portability  
**Requirement:** System shall support both H2 and MySQL without code changes.

#### 5.4.4 Usability

**NFR-USE-001:** API Consistency  
**Requirement:** API shall follow consistent naming and structure conventions.

**NFR-USE-002:** Error Messages  
**Requirement:** Error messages shall be clear and actionable.

**NFR-USE-003:** API Documentation  
**Requirement:** Complete API documentation shall be available via Swagger UI.

#### 5.4.5 Scalability

**NFR-SCALE-001:** Stateless Design  
**Requirement:** Application shall be stateless to support horizontal scaling.

**NFR-SCALE-002:** Connection Pooling  
**Requirement:** System shall use connection pooling for database access.

**NFR-SCALE-003:** Pagination  
**Requirement:** List endpoints shall support pagination to handle large datasets.

---

## 6. OTHER REQUIREMENTS

### 6.1 Database Requirements

**Database Schema:**
- Users table with unique constraints on username and email
- Recipes table with foreign key to users
- Categories table with unique constraint on name
- Ingredients table with unique constraint on name
- RecipeIngredients junction table
- Reviews table with unique constraint on (user_id, recipe_id)
- Collections table for recipe organization
- Join tables for many-to-many relationships

**Indexes Required:**
- Primary keys on all tables
- Unique indexes on username, email, category name
- Foreign key indexes for performance
- Index on recipe title for search

### 6.2 Legal Requirements

**Data Protection:**
- System shall allow users to delete their accounts
- System shall remove user data upon account deletion
- System shall not share user data with third parties

**Copyright:**
- Recipe content copyright belongs to recipe authors
- System provides platform only, not responsible for content
- DMCA-compliant takedown process

### 6.3 Internationalization Requirements

**NFR-I18N-001:** Character Encoding  
**Requirement:** System shall support UTF-8 encoding for international characters.

**NFR-I18N-002:** Future i18n Support  
**Requirement:** System architecture shall support future internationalization.

### 6.4 Environmental Requirements

**Development Environment:**
- Java 17 JDK
- Maven 3.6+
- 4GB RAM minimum
- 1GB disk space

**Production Environment:**
- Java 17 JRE
- 2GB RAM minimum
- 10GB disk space
- MySQL 8.0+ server

---

## APPENDIX A: GLOSSARY

| Term | Definition |
|------|------------|
| **API** | Application Programming Interface |
| **CRUD** | Create, Read, Update, Delete |
| **DTO** | Data Transfer Object |
| **JPA** | Java Persistence API |
| **JWT** | JSON Web Token |
| **ORM** | Object-Relational Mapping |
| **REST** | Representational State Transfer |
| **RBAC** | Role-Based Access Control |
| **BCrypt** | Blowfish-based password hashing function |

---

## APPENDIX B: ANALYSIS MODELS

### Data Model

**Core Entities:**
1. User
2. Recipe
3. Review
4. Category
5. Ingredient
6. RecipeIngredient (junction)
7. Collection

**Relationships:**
- User (1) → (N) Recipe (one-to-many)
- Recipe (N) → (1) Category (many-to-one, optional)
- Recipe (1) → (N) Review (one-to-many)
- User (1) → (N) Review (one-to-many)
- Recipe (N) ← (N) Ingredient (many-to-many via RecipeIngredient)
- User (N) ← (N) Recipe (many-to-many for favorites)
- User (N) ← (N) User (many-to-many for followers)

---

## APPENDIX C: TO BE DETERMINED (TBD) LIST

Items for future consideration:
1. Image upload and storage mechanism
2. Email notification system
3. Recipe video support
4. Meal planning features
5. Shopping list generation
6. Recipe import from URLs
7. Social sharing integrations
8. Premium features and monetization
9. Mobile push notifications
10. Advanced search with Elasticsearch

---

**Document Status:** Approved  
**Implementation Status:** Complete  
**Last Updated:** October 20, 2025

---

*This SRS follows IEEE 830-1998 standard and CS 5150 requirements.*
