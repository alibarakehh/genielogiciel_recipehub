// Create Recipe Functionality

let recipeIngredients = [];
let ingredientCounter = 0;

// Show create recipe modal
function showCreateRecipeModal() {
    if (!authToken) {
        showToast('Please login to create recipes', 'error');
        showAuthModal();
        return;
    }

    const modal = document.getElementById('createRecipeModal');
    modal.classList.add('active');
    loadCategoriesForSelect();
}

// Load categories for select dropdown
async function loadCategoriesForSelect() {
    try {
        const response = await fetch(`${API_BASE_URL}/categories`);
        const categories = await response.json();

        const select = document.getElementById('recipeCategory');
        select.innerHTML = '<option value="">Select a category</option>' +
            categories.map(cat => `<option value="${cat.id}">${cat.name}</option>`).join('');
    } catch (error) {
        console.error('Error loading categories:', error);
    }
}

// Add ingredient field
function addIngredient() {
    const container = document.getElementById('ingredientsContainer');
    const ingredientId = ingredientCounter++;

    const ingredientDiv = document.createElement('div');
    ingredientDiv.className = 'ingredient-item';
    ingredientDiv.id = `ingredient-${ingredientId}`;
    ingredientDiv.innerHTML = `
        <div class="ingredient-row">
            <input type="number" step="0.1" placeholder="Qty" class="ingredient-quantity" 
                   data-id="${ingredientId}" style="width: 80px;" required>
            <input type="text" placeholder="Unit (cup, tbsp, etc.)" class="ingredient-unit" 
                   data-id="${ingredientId}" style="width: 120px;" required>
            <input type="text" placeholder="Ingredient name" class="ingredient-name" 
                   data-id="${ingredientId}" style="flex: 1;" required>
            <input type="text" placeholder="Notes (optional)" class="ingredient-notes" 
                   data-id="${ingredientId}" style="flex: 1;">
            <button type="button" class="btn-remove" onclick="removeIngredient(${ingredientId})">
                <i class="fas fa-trash"></i>
            </button>
        </div>
    `;

    container.appendChild(ingredientDiv);
}

// Remove ingredient
function removeIngredient(ingredientId) {
    const element = document.getElementById(`ingredient-${ingredientId}`);
    if (element) {
        element.remove();
    }
}

// Handle recipe form submission
async function handleCreateRecipe(event) {
    event.preventDefault();

    if (!authToken) {
        showToast('Please login to create recipes', 'error');
        return;
    }

    // Collect ingredients
    const ingredients = [];
    document.querySelectorAll('.ingredient-item').forEach((item, index) => {
        const id = item.querySelector('.ingredient-quantity').dataset.id;
        ingredients.push({
            ingredientName: item.querySelector('.ingredient-name').value,
            quantity: parseFloat(item.querySelector('.ingredient-quantity').value),
            unit: item.querySelector('.ingredient-unit').value,
            notes: item.querySelector('.ingredient-notes').value || null,
            displayOrder: index + 1
        });
    });

    if (ingredients.length === 0) {
        showToast('Please add at least one ingredient', 'error');
        return;
    }

    const recipeData = {
        title: document.getElementById('recipeTitle').value,
        description: document.getElementById('recipeDescription').value,
        instructions: document.getElementById('recipeInstructions').value,
        prepTime: parseInt(document.getElementById('recipePrepTime').value),
        cookTime: parseInt(document.getElementById('recipeCookTime').value),
        servings: parseInt(document.getElementById('recipeServings').value),
        difficulty: document.getElementById('recipeDifficulty').value,
        categoryId: parseInt(document.getElementById('recipeCategory').value),
        imageUrl: document.getElementById('recipeImageUrl').value || null,
        calories: parseInt(document.getElementById('recipeCalories').value) || null,
        protein: parseFloat(document.getElementById('recipeProtein').value) || null,
        carbohydrates: parseFloat(document.getElementById('recipeCarbs').value) || null,
        fat: parseFloat(document.getElementById('recipeFat').value) || null,
        fiber: parseFloat(document.getElementById('recipeFiber').value) || null,
        ingredients: ingredients,
        published: document.getElementById('recipePublished').checked
    };

    try {
        showLoading();
        const response = await fetch(`${API_BASE_URL}/recipes`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify(recipeData)
        });

        if (response.ok) {
            const recipe = await response.json();
            showToast('Recipe created successfully!', 'success');
            closeCreateRecipeModal();
            // Redirect to recipe detail
            setTimeout(() => showRecipeDetail(recipe.id), 1000);
        } else {
            const error = await response.json();
            showToast(error.message || 'Failed to create recipe', 'error');
        }
        hideLoading();
    } catch (error) {
        console.error('Error creating recipe:', error);
        showToast('Failed to create recipe', 'error');
        hideLoading();
    }
}

// Close create recipe modal
function closeCreateRecipeModal() {
    const modal = document.getElementById('createRecipeModal');
    modal.classList.remove('active');
    document.getElementById('createRecipeForm').reset();
    document.getElementById('ingredientsContainer').innerHTML = '';
    ingredientCounter = 0;
}

// Favorite a recipe
async function favoriteRecipe(recipeId) {
    if (!authToken) {
        showToast('Please login to save favorites', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/users/${currentUser.id}/favorites/${recipeId}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            showToast('Added to favorites!', 'success');
        } else {
            showToast('Failed to add to favorites', 'error');
        }
    } catch (error) {
        console.error('Error favoriting recipe:', error);
        showToast('Action failed', 'error');
    }
}

// Show review form
function showReviewForm(recipeId) {
    if (!authToken) {
        showToast('Please login to write reviews', 'error');
        return;
    }

    const modal = document.getElementById('reviewModal');
    const form = document.getElementById('reviewForm');

    form.onsubmit = (e) => {
        e.preventDefault();
        submitReview(recipeId);
    };

    modal.classList.add('active');
}

// Submit review
async function submitReview(recipeId) {
    const rating = document.querySelector('input[name="rating"]:checked') ? .value;
    const comment = document.getElementById('reviewComment').value;

    if (!rating) {
        showToast('Please select a rating', 'error');
        return;
    }

    try {
        showLoading();
        const response = await fetch(`${API_BASE_URL}/recipes/${recipeId}/reviews`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify({ rating: parseInt(rating), comment })
        });

        if (response.ok) {
            showToast('Review submitted successfully!', 'success');
            closeReviewModal();
            // Reload recipe to show new review
            setTimeout(() => showRecipeDetail(recipeId), 1000);
        } else {
            const error = await response.json();
            showToast(error.message || 'Failed to submit review', 'error');
        }
        hideLoading();
    } catch (error) {
        console.error('Error submitting review:', error);
        showToast('Failed to submit review', 'error');
        hideLoading();
    }
}

// Close review modal
function closeReviewModal() {
    const modal = document.getElementById('reviewModal');
    modal.classList.remove('active');
    document.getElementById('reviewForm').reset();
}