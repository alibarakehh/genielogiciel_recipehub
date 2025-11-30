# 🚀 QUICK START - Recipe Website

## ⚡ INSTANT RUN (Copy & Paste)

```powershell
# Set Java Home and Run
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"; .\mvnw.cmd spring-boot:run
```

**Then open:** http://localhost:8080

---

## 🎯 LOGIN & TEST

### Test Account
```
Username: chef_mario
Password: password123
```

### Quick Test Flow
1. Open http://localhost:8080
2. Click "Login"
3. Enter credentials above
4. Click "Create Recipe" button
5. Fill form and submit
6. See your recipe appear!

---

## 🔍 API TESTING

### Swagger UI
```
URL: http://localhost:8080/swagger-ui.html
```

### Quick API Test
```bash
# 1. Login (get token)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"chef_mario","password":"password123"}'

# 2. Get all recipes
curl http://localhost:8080/api/recipes

# 3. Get categories
curl http://localhost:8080/api/categories
```

---

## 💾 DATABASE ACCESS

### H2 Console
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:recipedb
Username: sa
Password: (leave empty)
```

### Sample Queries
```sql
-- View all users
SELECT * FROM users;

-- View all recipes with ratings
SELECT r.title, r.average_rating, r.review_count
FROM recipes r
ORDER BY r.average_rating DESC;

-- View categories
SELECT * FROM categories;
```

---

## 📱 FEATURES TO TEST

### ✅ User Features
- [ ] Register new account
- [ ] Login with existing account
- [ ] View user profile
- [ ] Edit profile (bio, image)
- [ ] Follow other users
- [ ] Favorite recipes

### ✅ Recipe Features
- [ ] Browse all recipes
- [ ] Filter by category
- [ ] Filter by difficulty
- [ ] View recipe details
- [ ] Create new recipe
- [ ] Edit own recipe
- [ ] Delete own recipe

### ✅ Review Features
- [ ] View recipe reviews
- [ ] Write review with rating
- [ ] See average rating
- [ ] View review count

### ✅ Social Features
- [ ] Follow/unfollow users
- [ ] View user recipes
- [ ] Add to favorites
- [ ] Browse by author

---

## 🛠️ USEFUL COMMANDS

### Start Application
```powershell
.\mvnw.cmd spring-boot:run
```

### Stop Application
```powershell
# Press Ctrl+C in terminal
# OR
# Find and kill process:
netstat -ano | findstr :8080
taskkill /PID <process_id> /F
```

### Build JAR
```powershell
.\mvnw.cmd clean package -DskipTests
```

### Run Tests
```powershell
.\mvnw.cmd test
```

### Clean Build
```powershell
.\mvnw.cmd clean install
```

---

## 🐛 COMMON ISSUES

### Issue: "Port 8080 already in use"
```powershell
# Solution: Kill existing process
netstat -ano | findstr :8080
taskkill /PID <pid> /F
```

### Issue: "JAVA_HOME not set"
```powershell
# Solution: Set it temporarily
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
```

### Issue: IDE shows 655 errors
```
✅ This is NORMAL!
- These are false positives from Lombok
- Application compiles and runs fine
- Just IGNORE the IDE errors
```

---

## 📊 SAMPLE DATA

### Users
| Username | Password | Role | Recipes |
|----------|----------|------|---------|
| admin | admin123 | ADMIN | 3 |
| chef_mario | password123 | USER | 3 |
| foodie_jane | password123 | USER | 3 |

### Categories (10 total)
- Italian 🇮🇹
- Mexican 🇲🇽
- Asian 🥢
- Desserts 🍰
- Vegetarian 🥗
- Seafood 🦐
- Breakfast 🥞
- Salads 🥗
- Soups 🍜
- BBQ & Grilling 🍖

### Sample Recipes (9 total)
Each user has 3 recipes with:
- Title, description, instructions
- Ingredients list
- Nutrition information
- Professional images
- Category assignment

---

## 🎨 FRONTEND URLS

| Page | URL | Description |
|------|-----|-------------|
| **Homepage** | / | Hero, categories, recipes |
| **Profile** | /profile.html?user=chef_mario | User profile page |

### Modal Features
- Login modal (click "Login")
- Signup modal (click "Sign Up")
- Create Recipe modal (click "Create Recipe" when logged in)
- Review modal (click stars on recipe)
- Edit Profile modal (click "Edit Profile" on own profile)

---

## 🔐 SECURITY

### API Security
- JWT-based authentication
- Token expiration: 24 hours
- BCrypt password hashing
- CORS enabled for localhost
- SQL injection prevention (JPA)
- XSS protection headers

### Authorization
- Public: Homepage, recipes, categories
- Authenticated: Create recipe, review, favorite
- Author Only: Edit/delete own recipes
- Admin Only: Manage categories

---

## 📦 TECHNOLOGIES

### Backend
- Spring Boot 3.2.0
- Java 17
- H2 Database (dev)
- MySQL ready (prod)
- JWT Security
- Hibernate JPA
- Lombok

### Frontend
- HTML5
- CSS3 (modern gradients)
- JavaScript ES6+
- Font Awesome icons
- Google Fonts
- Responsive design

---

## 🎯 SUCCESS CRITERIA

### ✅ Application is Ready When:
- [x] URL http://localhost:8080 opens
- [x] Homepage displays with hero section
- [x] 10 categories visible
- [x] 3 recipes visible
- [x] Login works with test account
- [x] Can create new recipe
- [x] Can add review with stars
- [x] Swagger UI accessible

### ALL GREEN = READY TO GO! 🚀

---

## 💡 PRO TIPS

1. **Always use Maven wrapper** (`.\mvnw.cmd`) not global Maven
2. **Ignore IDE errors** - They're Lombok false positives
3. **Use Swagger UI** for easy API testing
4. **Check H2 console** to see database contents
5. **Test with different users** to see multi-user features
6. **Try all modals** on frontend for full experience
7. **Follow users** to see social features
8. **Add favorites** to see personalized collections

---

## 📞 NEED HELP?

### Check These First:
1. **ERROR_RESOLUTION_REPORT.md** - Detailed troubleshooting
2. **FEATURES_COMPLETED.md** - Full feature list
3. **README.md** - Complete documentation
4. **Swagger UI** - http://localhost:8080/swagger-ui.html

### Application Logs:
- Watch terminal for Spring Boot logs
- Shows all HTTP requests
- Displays database queries
- Reports any errors

---

## 🎉 YOU'RE ALL SET!

The application is **100% ready** to use. Just run the command at the top and start testing!

**Happy Coding! 🚀👨‍🍳**
