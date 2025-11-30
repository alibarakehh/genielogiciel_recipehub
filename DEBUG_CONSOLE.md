# 🔍 Browser Console Debugging Guide

## Step 1: Open Browser Console
1. Open http://localhost:8080 in your browser
2. Press `F12` or `Ctrl+Shift+I` to open Developer Tools
3. Go to the **Console** tab

## Step 2: Run These Commands to Test API

### Test 1: Check API Base URL
```javascript
console.log('API Base URL:', API_BASE_URL);
```
**Expected:** `http://localhost:8080/api`

### Test 2: Test Categories API
```javascript
fetch('http://localhost:8080/api/categories')
  .then(res => res.json())
  .then(data => console.log('Categories:', data))
  .catch(err => console.error('Categories Error:', err));
```
**Expected:** Array of 10 categories

### Test 3: Test Recipes API
```javascript
fetch('http://localhost:8080/api/recipes')
  .then(res => res.json())
  .then(data => console.log('Recipes:', data))
  .catch(err => console.error('Recipes Error:', err));
```
**Expected:** Page object with recipes

### Test 4: Check DOM Elements
```javascript
console.log('Categories Grid:', document.getElementById('categoriesGrid'));
console.log('Recipes Grid:', document.getElementById('recipesGrid'));
console.log('Create Recipe Modal:', document.getElementById('createRecipeModal'));
```
**Expected:** All should return HTML elements (not null)

### Test 5: Check if Functions Exist
```javascript
console.log('loadCategories:', typeof loadCategories);
console.log('loadRecipes:', typeof loadRecipes);
console.log('showCreateRecipeModal:', typeof showCreateRecipeModal);
```
**Expected:** All should return `"function"`

### Test 6: Manually Load Categories
```javascript
loadCategories();
```
**Expected:** Categories should appear on page

### Test 7: Manually Load Recipes
```javascript
loadRecipes();
```
**Expected:** Recipes should appear on page

### Test 8: Check for Errors in Network Tab
1. Open **Network** tab in DevTools
2. Refresh page (F5)
3. Look for any red (failed) requests
4. Check if `/api/categories` and `/api/recipes` return 200 OK

## Common Issues & Solutions

### Issue: CORS Error
**Symptom:** Console shows "CORS policy" error
**Solution:** Application needs restart with new CORS config
```powershell
# Stop current app (Ctrl+C in terminal)
# Restart with:
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"; .\mvnw.cmd spring-boot:run
```

### Issue: Categories/Recipes Grid Empty
**Symptom:** Sections exist but no content
**Solution:** Run manual load commands from Test 6 & 7

### Issue: 404 Not Found
**Symptom:** API endpoints return 404
**Solution:** Check if backend is running on port 8080

### Issue: Blank Modals
**Symptom:** Modal opens but shows nothing
**Solution:** Check if modal HTML exists in index.html

## Quick Fix Commands

### Reload All Data
```javascript
// Run this to force reload everything
loadData().then(() => console.log('Data reloaded!'));
```

### Show Create Recipe Modal
```javascript
// Test if modal opens
showCreateRecipeModal();
```

### Check Auth Status
```javascript
console.log('Auth Token:', authToken);
console.log('Current User:', currentUser);
```

### Force Update UI
```javascript
updateAuthUI();
loadCategories();
loadRecipes();
loadStats();
```

## Expected Results After Fixes

✅ **Categories Section:**
- Should show 10 category cards with images
- Clicking a category filters recipes

✅ **Recipes Section:**
- Should show 3+ recipe cards
- Each card shows image, title, author, time
- Clicking opens detailed modal

✅ **Create Recipe:**
- Button visible when logged in
- Modal opens with full form
- Can add/remove ingredients
- Form submits successfully

✅ **Network Requests:**
- All API calls return 200 OK
- No CORS errors
- Data loads within 1 second
