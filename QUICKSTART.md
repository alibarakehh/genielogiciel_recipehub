# 🚀 QUICK START GUIDE

## Run the Application (Choose One Method)

### 🎯 Method 1: Interactive Startup Script (RECOMMENDED)
```powershell
.\start.ps1
```
Then select option **1** (Build and Run)

### 🎯 Method 2: Direct Command
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
.\mvnw.cmd clean package -DskipTests
java -jar target\recipe-website-1.0.0.jar
```

### 🎯 Method 3: Docker
```bash
docker-compose up -d
```

---

## 🌐 Access the Application

| What | URL |
|------|-----|
| **API Docs (START HERE!)** | http://localhost:8080/swagger-ui.html |
| **API Base** | http://localhost:8080 |
| **Database Console** | http://localhost:8080/h2-console |

---

## 👤 Test Accounts

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |
| chef_mario | password123 | USER |
| foodie_jane | password123 | USER |

---

## 🧪 Quick API Test

### 1. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"chef_mario","password":"password123"}'
```

Copy the `accessToken` from response.

### 2. Get All Recipes
```bash
curl http://localhost:8080/api/recipes
```

### 3. Create a Recipe (Replace YOUR_TOKEN)
```bash
curl -X POST http://localhost:8080/api/recipes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "title": "My Amazing Recipe",
    "description": "Delicious and easy!",
    "instructions": "Step 1...\nStep 2...",
    "prepTime": 10,
    "cookTime": 20,
    "servings": 4,
    "difficulty": "EASY",
    "categoryId": 1,
    "ingredients": [{"ingredientName":"Flour","quantity":500,"unit":"g"}],
    "published": true
  }'
```

---

## 📱 Using Swagger UI (Easiest Way!)

1. Open: http://localhost:8080/swagger-ui.html
2. Click **"Authorize"** button (top right)
3. Login using POST `/api/auth/login`:
   - Username: `chef_mario`
   - Password: `password123`
4. Copy the `accessToken` from response
5. Paste token in "Authorize" dialog (add "Bearer " prefix)
6. Now you can test all endpoints with the green **"Try it out"** buttons!

---

## 📊 What's Included

✅ 3 sample users (1 admin, 2 regular)  
✅ 10 recipe categories  
✅ 15 common ingredients  
✅ 3 complete recipes with instructions  

---

## 🛑 Troubleshooting

### "JAVA_HOME not found"
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
```

### "Port 8080 already in use"
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

### "Build failed"
```powershell
.\mvnw.cmd clean install -U
```

---

## 📚 More Info

- Full Documentation: `DOCUMENTATION.md`
- Project Summary: `PROJECT_SUMMARY.md`
- README: `README.md`

---

**Ready to Code! 🎉**
