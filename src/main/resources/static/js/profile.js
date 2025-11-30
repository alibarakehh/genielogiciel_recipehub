// Profile Page Functionality

// Load user profile
async function loadUserProfile(username) {
    try {
        showLoading();
        const response = await fetch(`${API_BASE_URL}/users/profile/${username}`, {
            headers: authToken ? { 'Authorization': `Bearer ${authToken}` } : {}
        });

        if (!response.ok) throw new Error('Failed to load profile');

        const user = await response.json();
        displayUserProfile(user);
        await loadUserRecipes(user.id);
        await loadUserFavorites(user.id);

        hideLoading();
    } catch (error) {
        console.error('Error loading profile:', error);
        showToast('Failed to load profile', 'error');
        hideLoading();
    }
}

// Display user profile
function displayUserProfile(user) {
    const profileContainer = document.getElementById('profileContainer');

    const isOwnProfile = currentUser && currentUser.id === user.id;
    const isFollowing = user.isFollowing || false;

    profileContainer.innerHTML = `
        <div class="profile-header">
            <div class="profile-avatar">
                <img src="${user.profileImageUrl || 'https://ui-avatars.com/api/?name=' + encodeURIComponent(user.fullName || user.username) + '&size=200&background=FF6B6B&color=fff'}" 
                     alt="${user.username}">
            </div>
            <div class="profile-info">
                <h1 class="profile-name">${user.fullName || user.username}</h1>
                <p class="profile-username">@${user.username}</p>
                <p class="profile-bio">${user.bio || 'No bio yet'}</p>
                
                <div class="profile-stats">
                    <div class="stat">
                        <span class="stat-number">${user.recipeCount || 0}</span>
                        <span class="stat-label">Recipes</span>
                    </div>
                    <div class="stat">
                        <span class="stat-number">${user.followerCount || 0}</span>
                        <span class="stat-label">Followers</span>
                    </div>
                    <div class="stat">
                        <span class="stat-number">${user.followingCount || 0}</span>
                        <span class="stat-label">Following</span>
                    </div>
                </div>
                
                <div class="profile-actions">
                    ${isOwnProfile ? `
                        <button class="btn btn-primary" onclick="showEditProfileModal()">
                            <i class="fas fa-edit"></i> Edit Profile
                        </button>
                        <button class="btn btn-secondary" onclick="showCreateRecipeModal()">
                            <i class="fas fa-plus"></i> Create Recipe
                        </button>
                    ` : `
                        <button class="btn ${isFollowing ? 'btn-secondary' : 'btn-primary'}" 
                                onclick="toggleFollow(${user.id}, ${isFollowing})">
                            <i class="fas fa-${isFollowing ? 'user-check' : 'user-plus'}"></i> 
                            ${isFollowing ? 'Following' : 'Follow'}
                        </button>
                    `}
                </div>
            </div>
        </div>
        
        <div class="profile-tabs">
            <button class="profile-tab active" onclick="switchProfileTab('recipes')">
                <i class="fas fa-book"></i> Recipes
            </button>
            <button class="profile-tab" onclick="switchProfileTab('favorites')">
                <i class="fas fa-heart"></i> Favorites
            </button>
        </div>
        
        <div id="profileContent">
            <div id="recipesTab" class="tab-content active"></div>
            <div id="favoritesTab" class="tab-content"></div>
        </div>
    `;
}

// Load user recipes
async function loadUserRecipes(userId) {
    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}/recipes`);
        const recipes = await response.json();
        
        const container = document.getElementById('recipesTab');
        if (recipes.length === 0) {
            container.innerHTML = '<p class="empty-message">No recipes yet</p>';
            return;
        }
        
        container.innerHTML = `
            <div class="recipes-grid">
                ${recipes.map(recipe => createRecipeCard(recipe)).join('')}
            </div>
        `;
    } catch (error) {
        console.error('Error loading user recipes:', error);
    }
}

// Load user favorites
async function loadUserFavorites(userId) {
    if (!authToken || currentUser.id !== userId) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}/favorites`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        const recipes = await response.json();
        
        const container = document.getElementById('favoritesTab');
        if (recipes.length === 0) {
            container.innerHTML = '<p class="empty-message">No favorites yet</p>';
            return;
        }
        
        container.innerHTML = `
            <div class="recipes-grid">
                ${recipes.map(recipe => createRecipeCard(recipe)).join('')}
            </div>
        `;
    } catch (error) {
        console.error('Error loading favorites:', error);
    }
}

// Create recipe card HTML
function createRecipeCard(recipe) {
    return `
        <div class="recipe-card" onclick="showRecipeDetail(${recipe.id})">
            <div style="position: relative;">
                <img src="${recipe.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400&h=300&fit=crop'}" 
                     alt="${recipe.title}" class="recipe-image">
                <span class="recipe-badge">${recipe.difficulty || 'MEDIUM'}</span>
            </div>
            <div class="recipe-content">
                <h3 class="recipe-title">${recipe.title}</h3>
                <div class="recipe-author">
                    <i class="fas fa-user-circle"></i>
                    ${recipe.authorName || 'Anonymous'}
                </div>
                <p class="recipe-description">${(recipe.description || '').substring(0, 100)}${recipe.description && recipe.description.length > 100 ? '...' : ''}</p>
                <div class="recipe-meta">
                    <div class="recipe-meta-item">
                        <i class="fas fa-clock"></i>
                        ${recipe.prepTime + recipe.cookTime} min
                    </div>
                    <div class="recipe-meta-item">
                        <i class="fas fa-users"></i>
                        ${recipe.servings} servings
                    </div>
                    <div class="recipe-rating">
                        <i class="fas fa-star"></i>
                        ${recipe.averageRating?.toFixed(1) || 0}
                    </div>
                </div>
            </div>
        </div>
    `;
}

// Toggle follow
async function toggleFollow(userId, isFollowing) {
    if (!authToken) {
        showToast('Please login to follow users', 'error');
        return;
    }
    
    try {
        const endpoint = isFollowing ? 'unfollow' : 'follow';
        const response = await fetch(`${API_BASE_URL}/users/${userId}/${endpoint}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        
        if (response.ok) {
            showToast(isFollowing ? 'Unfollowed successfully' : 'Following now!', 'success');
            loadUserProfile(currentProfileUsername);
        }
    } catch (error) {
        console.error('Error toggling follow:', error);
        showToast('Action failed', 'error');
    }
}

// Switch profile tabs
function switchProfileTab(tab) {
    document.querySelectorAll('.profile-tab').forEach(t => t.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
    
    event.target.closest('.profile-tab').classList.add('active');
    document.getElementById(`${tab}Tab`).classList.add('active');
}