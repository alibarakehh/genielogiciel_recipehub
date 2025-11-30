// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';
let authToken = localStorage.getItem('authToken');
let currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null');

// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    initializeApp();
    setupEventListeners();
    loadData();
    updateAuthUI();
});

// Initialize application
function initializeApp() {
    // Navbar scroll effect
    window.addEventListener('scroll', () => {
        const navbar = document.getElementById('navbar');
        if (window.scrollY > 100) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    });

    // Smooth scrolling for navigation links
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const targetId = link.getAttribute('href');
            const targetSection = document.querySelector(targetId);
            if (targetSection) {
                targetSection.scrollIntoView({ behavior: 'smooth' });
            }
            // Update active link
            document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
            link.classList.add('active');
        });
    });
}

// Setup event listeners
function setupEventListeners() {
    // Login form
    document.getElementById('loginForm').addEventListener('submit', async(e) => {
        e.preventDefault();
        await handleLogin();
    });

    // Signup form
    document.getElementById('signupForm').addEventListener('submit', async(e) => {
        e.preventDefault();
        await handleSignup();
    });

    // Recipe filters
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            const filter = btn.dataset.filter;
            filterRecipes(filter);
        });
    });
}

// Load initial data
async function loadData() {
    try {
        await Promise.all([
            loadCategories(),
            loadRecipes(),
            loadStats()
        ]);
    } catch (error) {
        console.error('Error loading data:', error);
        showToast('Failed to load data. Please refresh the page.', 'error');
    }
}

// Load categories
async function loadCategories() {
    try {
        const response = await fetch(`${API_BASE_URL}/categories`);
        const categories = await response.json();

        const grid = document.getElementById('categoriesGrid');
        grid.innerHTML = categories.map(category => `
            <div class="category-card" onclick="filterByCategory(${category.id})">
                <img src="${category.iconUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400&h=300&fit=crop'}" 
                     alt="${category.name}" class="category-image">
                <div class="category-content">
                    <h3 class="category-name">${category.name}</h3>
                    <p class="category-description">${category.description || ''}</p>
                </div>
            </div>
        `).join('');
    } catch (error) {
        console.error('Error loading categories:', error);
    }
}

// Load recipes
async function loadRecipes() {
    try {
        const response = await fetch(`${API_BASE_URL}/recipes?size=100`);
        const data = await response.json();

        // Handle Spring Boot Page response
        const recipes = data.content || data;
        console.log('Loaded recipes:', recipes);

        displayRecipes(recipes);
    } catch (error) {
        console.error('Error loading recipes:', error);
        showToast('Failed to load recipes', 'error');
    }
}

// Display recipes
function displayRecipes(recipes) {
    const grid = document.getElementById('recipesGrid');

    if (recipes.length === 0) {
        grid.innerHTML = '<p style="grid-column: 1/-1; text-align: center; color: var(--gray);">No recipes found.</p>';
        return;
    }

    grid.innerHTML = recipes.map(recipe => `
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
                        ${recipe.averageRating || 0}
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

// Filter recipes
function filterRecipes(difficulty) {
    showLoading();

    let url = `${API_BASE_URL}/recipes`;
    if (difficulty !== 'all') {
        url = `${API_BASE_URL}/recipes/difficulty/${difficulty.toUpperCase()}`;
    }
    url += '?size=100';

    fetch(url)
        .then(response => response.json())
        .then(data => {
            // Handle Spring Boot Page response
            const recipes = data.content || data;
            console.log(`Filtered recipes (${difficulty}):`, recipes);
            displayRecipes(recipes);
            hideLoading();
        })
        .catch(error => {
            console.error('Error filtering recipes:', error);
            showToast('Failed to filter recipes', 'error');
            hideLoading();
        });
}

// Filter by category
function filterByCategory(categoryId) {
    showLoading();

    fetch(`${API_BASE_URL}/recipes/category/${categoryId}?size=100`)
        .then(response => response.json())
        .then(data => {
            // Handle Spring Boot Page response
            const recipes = data.content || data;
            displayRecipes(recipes);
            hideLoading();
            scrollToRecipes();
        })
        .catch(error => {
            console.error('Error filtering by category:', error);
            showToast('Failed to load category recipes', 'error');
            hideLoading();
        });
}

// Show recipe detail
async function showRecipeDetail(recipeId) {
    try {
        showLoading();
        const response = await fetch(`${API_BASE_URL}/recipes/${recipeId}`);
        const recipe = await response.json();

        const modal = document.getElementById('recipeModal');
        const detailContainer = document.getElementById('recipeDetail');

        detailContainer.innerHTML = `
            <div class="recipe-detail">
                <img src="${recipe.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&h=500&fit=crop'}" 
                     alt="${recipe.title}" style="width: 100%; height: 400px; object-fit: cover; border-radius: 15px; margin-bottom: 2rem;">
                
                <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 2rem;">
                    <div>
                        <h2 style="font-size: 2.5rem; margin-bottom: 1rem;">${recipe.title}</h2>
                        <div style="display: flex; gap: 1rem; color: var(--gray);">
                            <span><i class="fas fa-user"></i> ${recipe.authorName || 'Anonymous'}</span>
                            <span><i class="fas fa-clock"></i> ${recipe.prepTime + recipe.cookTime} min</span>
                            <span><i class="fas fa-users"></i> ${recipe.servings} servings</span>
                        </div>
                    </div>
                    <div style="text-align: center;">
                        <div class="recipe-rating" style="font-size: 2rem; color: var(--accent-color);">
                            <i class="fas fa-star"></i>
                            ${recipe.averageRating || 0}
                        </div>
                        <div style="color: var(--gray); font-size: 0.9rem;">${recipe.reviewCount || 0} reviews</div>
                    </div>
                </div>
                
                <p style="font-size: 1.1rem; line-height: 1.8; color: var(--gray); margin-bottom: 2rem;">
                    ${recipe.description || 'No description available.'}
                </p>
                
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 1rem; margin-bottom: 2rem;">
                    <div style="background: var(--light-color); padding: 1rem; border-radius: 10px; text-align: center;">
                        <div style="font-size: 1.5rem; font-weight: 600;">${recipe.calories || 'N/A'}</div>
                        <div style="color: var(--gray); font-size: 0.9rem;">Calories</div>
                    </div>
                    <div style="background: var(--light-color); padding: 1rem; border-radius: 10px; text-align: center;">
                        <div style="font-size: 1.5rem; font-weight: 600;">${recipe.protein || 'N/A'}g</div>
                        <div style="color: var(--gray); font-size: 0.9rem;">Protein</div>
                    </div>
                    <div style="background: var(--light-color); padding: 1rem; border-radius: 10px; text-align: center;">
                        <div style="font-size: 1.5rem; font-weight: 600;">${recipe.carbohydrates || 'N/A'}g</div>
                        <div style="color: var(--gray); font-size: 0.9rem;">Carbs</div>
                    </div>
                    <div style="background: var(--light-color); padding: 1rem; border-radius: 10px; text-align: center;">
                        <div style="font-size: 1.5rem; font-weight: 600;">${recipe.fat || 'N/A'}g</div>
                        <div style="color: var(--gray); font-size: 0.9rem;">Fat</div>
                    </div>
                </div>
                
                <h3 style="font-size: 1.8rem; margin-bottom: 1rem;">Ingredients</h3>
                <ul style="list-style: none; margin-bottom: 2rem;">
                    ${(recipe.ingredients || []).map(ing => `
                        <li style="padding: 0.8rem; background: var(--light-color); margin-bottom: 0.5rem; border-radius: 8px;">
                            <i class="fas fa-check" style="color: var(--success); margin-right: 10px;"></i>
                            ${ing.quantity} ${ing.unit} ${ing.ingredientName}
                            ${ing.notes ? `<span style="color: var(--gray); font-size: 0.9rem;"> (${ing.notes})</span>` : ''}
                        </li>
                    `).join('')}
                </ul>
                
                <h3 style="font-size: 1.8rem; margin-bottom: 1rem;">Instructions</h3>
                <div style="white-space: pre-line; line-height: 2; color: var(--gray);">
                    ${recipe.instructions || 'No instructions available.'}
                </div>
                
                ${authToken ? `
                    <div style="margin-top: 3rem; text-align: center;">
                        <button class="btn btn-primary" onclick="favoriteRecipe(${recipe.id})">
                            <i class="fas fa-heart"></i> Add to Favorites
                        </button>
                        <button class="btn btn-secondary" onclick="showReviewForm(${recipe.id})">
                            <i class="fas fa-comment"></i> Write a Review
                        </button>
                    </div>
                ` : `
                    <div style="margin-top: 3rem; text-align: center; padding: 2rem; background: var(--light-color); border-radius: 15px;">
                        <p style="margin-bottom: 1rem;">Login to save favorites and write reviews!</p>
                        <button class="btn btn-primary" onclick="closeRecipeModal(); showAuthModal();">
                            <i class="fas fa-sign-in-alt"></i> Login
                        </button>
                    </div>
                `}
            </div>
        `;
        
        modal.classList.add('active');
        hideLoading();
    } catch (error) {
        console.error('Error loading recipe detail:', error);
        hideLoading();
        showToast('Failed to load recipe details', 'error');
    }
}

// Load statistics
async function loadStats() {
    try {
        const [recipesRes, categoriesRes] = await Promise.all([
            fetch(`${API_BASE_URL}/recipes?size=1000`),
            fetch(`${API_BASE_URL}/categories`)
        ]);
        
        const recipesData = await recipesRes.json();
        const categories = await categoriesRes.json();
        
        // Handle Spring Boot Page response
        const recipes = recipesData.content || recipesData;
        const totalRecipes = recipesData.totalElements || recipes.length;
        
        // Animate counters
        animateCounter('recipesCount', totalRecipes);
        animateCounter('usersCount', 150); // Placeholder - can be updated with real data
        animateCounter('reviewsCount', recipes.reduce((sum, r) => sum + (r.reviewCount || 0), 0));
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

// Animate counter
function animateCounter(elementId, target) {
    const element = document.getElementById(elementId);
    const duration = 2000;
    const step = target / (duration / 16);
    let current = 0;
    
    const timer = setInterval(() => {
        current += step;
        if (current >= target) {
            element.textContent = target;
            clearInterval(timer);
        } else {
            element.textContent = Math.floor(current);
        }
    }, 16);
}

// Handle login
async function handleLogin() {
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;
    
    try {
        showLoading();
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });
        
        if (response.ok) {
            const data = await response.json();
            authToken = data.token;
            currentUser = data;
            
            localStorage.setItem('authToken', authToken);
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            
            closeAuthModal();
            updateAuthUI();
            showToast(`Welcome back, ${currentUser.username}!`, 'success');
            hideLoading();
        } else {
            const error = await response.json();
            showToast(error.message || 'Login failed', 'error');
            hideLoading();
        }
    } catch (error) {
        console.error('Login error:', error);
        showToast('Login failed. Please try again.', 'error');
        hideLoading();
    }
}

// Handle signup
async function handleSignup() {
    const username = document.getElementById('signupUsername').value;
    const email = document.getElementById('signupEmail').value;
    const fullName = document.getElementById('signupFullName').value;
    const password = document.getElementById('signupPassword').value;
    
    try {
        showLoading();
        const response = await fetch(`${API_BASE_URL}/auth/signup`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, email, fullName, password })
        });
        
        if (response.ok) {
            showToast('Account created successfully! Please login.', 'success');
            switchAuthTab('login');
            hideLoading();
        } else {
            const error = await response.json();
            showToast(error.message || 'Signup failed', 'error');
            hideLoading();
        }
    } catch (error) {
        console.error('Signup error:', error);
        showToast('Signup failed. Please try again.', 'error');
        hideLoading();
    }
}

// Update auth UI
function updateAuthUI() {
    const navMenu = document.querySelector('.nav-menu');
    const loginBtn = navMenu.querySelector('button') || navMenu.querySelector('#authBtn');
    
    if (currentUser && authToken) {
        loginBtn.innerHTML = `
            <div class="user-dropdown">
                <img src="${currentUser.profileImageUrl || 'https://ui-avatars.com/api/?name=' + encodeURIComponent(currentUser.username) + '&size=40&background=FF6B6B&color=fff'}" 
                     style="width: 35px; height: 35px; border-radius: 50%; margin-right: 8px; vertical-align: middle;">
                ${currentUser.username}
                <i class="fas fa-chevron-down" style="margin-left: 5px;"></i>
            </div>
        `;
        loginBtn.onclick = (e) => {
            e.stopPropagation();
            toggleUserMenu();
        };
        
        // Create dropdown menu if it doesn't exist
        let dropdown = document.getElementById('userDropdown');
        if (!dropdown) {
            dropdown = document.createElement('div');
            dropdown.id = 'userDropdown';
            dropdown.className = 'user-dropdown-menu';
            dropdown.innerHTML = `
                <a href="profile.html?user=${currentUser.username}">
                    <i class="fas fa-user"></i> My Profile
                </a>
                <a href="#" onclick="showCreateRecipeModal(); return false;">
                    <i class="fas fa-plus"></i> Create Recipe
                </a>
                <a href="profile.html?user=${currentUser.username}">
                    <i class="fas fa-heart"></i> My Favorites
                </a>
                ${currentUser.role === 'ADMIN' ? `
                    <a href="/swagger-ui.html" target="_blank">
                        <i class="fas fa-cog"></i> Admin Panel
                    </a>
                ` : ''}
                <hr style="border: none; border-top: 1px solid var(--light-color); margin: 0.5rem 0;">
                <a href="#" onclick="logout(); return false;">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            `;
            navMenu.appendChild(dropdown);
            
            // Close dropdown when clicking outside
            document.addEventListener('click', (e) => {
                if (!e.target.closest('.btn-primary') && !e.target.closest('#authBtn')) {
                    dropdown.classList.remove('active');
                }
            });
        }
    } else {
        loginBtn.innerHTML = `
            <i class="fas fa-sign-in-alt"></i> Login
        `;
        loginBtn.onclick = () => showAuthModal();
        
        // Remove dropdown if exists
        const dropdown = document.getElementById('userDropdown');
        if (dropdown) dropdown.remove();
    }
}

// Toggle user menu
function toggleUserMenu() {
    const dropdown = document.getElementById('userDropdown');
    if (dropdown) {
        dropdown.classList.toggle('active');
    }
}

// Logout
function logout() {
    authToken = null;
    currentUser = null;
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    updateAuthUI();
    showToast('Logged out successfully', 'success');
}

// Modal functions
function showAuthModal(tab = 'login') {
    document.getElementById('authModal').classList.add('active');
    switchAuthTab(tab);
}

function closeAuthModal() {
    document.getElementById('authModal').classList.remove('active');
}

function switchAuthTab(tab) {
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');
    const tabs = document.querySelectorAll('.auth-tab');
    
    tabs.forEach(t => t.classList.remove('active'));
    
    if (tab === 'login') {
        loginForm.style.display = 'block';
        signupForm.style.display = 'none';
        tabs[0].classList.add('active');
    } else {
        loginForm.style.display = 'none';
        signupForm.style.display = 'block';
        tabs[1].classList.add('active');
    }
}

function closeRecipeModal() {
    document.getElementById('recipeModal').classList.remove('active');
}

// Utility functions
function showLoading() {
    document.getElementById('loadingOverlay').classList.add('active');
}

function hideLoading() {
    document.getElementById('loadingOverlay').classList.remove('active');
}

function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} active`;
    
    setTimeout(() => {
        toast.classList.remove('active');
    }, 3000);
}

function scrollToRecipes() {
    document.getElementById('recipes').scrollIntoView({ behavior: 'smooth' });
}

function toggleMenu() {
    document.getElementById('navMenu').classList.toggle('active');
}

// Close modals when clicking outside
window.onclick = function(event) {
    const authModal = document.getElementById('authModal');
    const recipeModal = document.getElementById('recipeModal');
    
    if (event.target === authModal) {
        closeAuthModal();
    }
    if (event.target === recipeModal) {
        closeRecipeModal();
    }
}