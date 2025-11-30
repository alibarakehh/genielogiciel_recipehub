package com.recipes.config;

import com.recipes.model.Category;
import com.recipes.model.Ingredient;
import com.recipes.model.Recipe;
import com.recipes.model.RecipeIngredient;
import com.recipes.model.User;
import com.recipes.repository.CategoryRepository;
import com.recipes.repository.IngredientRepository;
import com.recipes.repository.RecipeIngredientRepository;
import com.recipes.repository.RecipeRepository;
import com.recipes.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Data initialization configuration for demo purposes
 * Creates sample users, categories, ingredients, and recipes
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            IngredientRepository ingredientRepository,
            RecipeRepository recipeRepository,
            RecipeIngredientRepository recipeIngredientRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            logger.info("🌱 Initializing sample data...");

            // Create sample users
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@recipes.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Admin User");
            admin.setBio("System Administrator");
            admin.setProfileImageUrl("https://ui-avatars.com/api/?name=Admin+User&size=200&background=FF6B6B&color=fff");
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            admin = userRepository.save(admin);

            User chef = new User();
            chef.setUsername("chef_mario");
            chef.setEmail("mario@recipes.com");
            chef.setPassword(passwordEncoder.encode("password123"));
            chef.setFullName("Mario Rossi");
            chef.setBio("Professional chef specializing in Italian cuisine with 15+ years of experience");
            chef.setProfileImageUrl("https://ui-avatars.com/api/?name=Mario+Rossi&size=200&background=4ECDC4&color=fff");
            chef.setRole(User.Role.USER);
            chef.setEnabled(true);
            chef.setCreatedAt(LocalDateTime.now());
            chef.setUpdatedAt(LocalDateTime.now());
            chef = userRepository.save(chef);

            User foodie = new User();
            foodie.setUsername("foodie_jane");
            foodie.setEmail("jane@recipes.com");
            foodie.setPassword(passwordEncoder.encode("password123"));
            foodie.setFullName("Jane Smith");
            foodie.setBio("Food enthusiast and home cook passionate about healthy living");
            foodie.setProfileImageUrl("https://ui-avatars.com/api/?name=Jane+Smith&size=200&background=FFE66D&color=333");
            foodie.setRole(User.Role.USER);
            foodie.setEnabled(true);
            foodie.setCreatedAt(LocalDateTime.now());
            foodie.setUpdatedAt(LocalDateTime.now());
            foodie = userRepository.save(foodie);

            logger.info("✓ Created {} users", userRepository.count());

            // Create categories
            List<Category> categories = Arrays.asList(
                    createCategory("Italian", "Traditional Italian cuisine", "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=400&h=300&fit=crop"),
                    createCategory("Mexican", "Spicy and flavorful Mexican dishes", "https://images.unsplash.com/photo-1565299507177-b0ac66763828?w=400&h=300&fit=crop"),
                    createCategory("Asian", "Diverse flavors from Asia", "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=400&h=300&fit=crop"),
                    createCategory("Desserts", "Sweet treats and desserts", "https://images.unsplash.com/photo-1563729784474-d77dbb933a9e?w=400&h=300&fit=crop"),
                    createCategory("Vegetarian", "Meat-free delicious recipes", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400&h=300&fit=crop"),
                    createCategory("Seafood", "Fresh seafood dishes", "https://images.unsplash.com/photo-1615141982883-c7ad0e69fd62?w=400&h=300&fit=crop"),
                    createCategory("Breakfast", "Start your day right", "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666?w=400&h=300&fit=crop"),
                    createCategory("Salads", "Fresh and healthy salads", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400&h=300&fit=crop"),
                    createCategory("Soups", "Warm and comforting soups", "https://images.unsplash.com/photo-1547592166-23ac45744acd?w=400&h=300&fit=crop"),
                    createCategory("BBQ & Grilling", "Outdoor cooking favorites", "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=400&h=300&fit=crop")
            );
            categoryRepository.saveAll(categories);
            logger.info("✓ Created {} categories", categories.size());

            // Create ingredients
            List<Ingredient> ingredients = Arrays.asList(
                    createIngredient("Spaghetti", "Long, thin pasta"),
                    createIngredient("Eggs", "Fresh chicken eggs"),
                    createIngredient("Parmesan Cheese", "Aged Italian cheese"),
                    createIngredient("Bacon", "Smoked pork belly"),
                    createIngredient("Black Pepper", "Freshly ground black pepper"),
                    createIngredient("Tomatoes", "Fresh ripe tomatoes"),
                    createIngredient("Garlic", "Fresh garlic cloves"),
                    createIngredient("Olive Oil", "Extra virgin olive oil"),
                    createIngredient("Basil", "Fresh basil leaves"),
                    createIngredient("Mozzarella", "Fresh mozzarella cheese"),
                    createIngredient("Chicken Breast", "Boneless chicken breast"),
                    createIngredient("Bell Peppers", "Mixed color bell peppers"),
                    createIngredient("Onion", "Yellow onion"),
                    createIngredient("Salt", "Sea salt"),
                    createIngredient("Flour", "All-purpose flour")
            );
            ingredientRepository.saveAll(ingredients);
            logger.info("✓ Created {} ingredients", ingredients.size());

            // Create sample recipe: Spaghetti Carbonara
            Recipe carbonara = new Recipe();
            carbonara.setTitle("Classic Spaghetti Carbonara");
            carbonara.setDescription("A traditional Italian pasta dish made with eggs, cheese, bacon, and black pepper. Simple, quick, and absolutely delicious!");
            carbonara.setInstructions("1. Bring a large pot of salted water to boil and cook spaghetti according to package directions.\n\n" +
                    "2. While pasta cooks, dice the bacon and cook in a large skillet until crispy. Remove from heat.\n\n" +
                    "3. In a bowl, whisk together eggs, grated Parmesan cheese, and a generous amount of black pepper.\n\n" +
                    "4. When pasta is done, reserve 1 cup of pasta water, then drain.\n\n" +
                    "5. Add hot pasta to the skillet with bacon. Remove from heat.\n\n" +
                    "6. Quickly stir in the egg mixture, tossing continuously. Add pasta water a little at a time until creamy.\n\n" +
                    "7. Serve immediately with extra Parmesan and black pepper.");
            carbonara.setPrepTime(10);
            carbonara.setCookTime(15);
            carbonara.setServings(4);
            carbonara.setDifficulty(Recipe.Difficulty.MEDIUM);
            carbonara.setCalories(450);
            carbonara.setProtein(20.0);
            carbonara.setCarbohydrates(55.0);
            carbonara.setFat(18.0);
            carbonara.setFiber(3.0);
            carbonara.setPublished(true);
            carbonara.setAuthor(chef);
            carbonara.setCategory(categories.get(0)); // Italian
            carbonara.setImageUrl("https://images.unsplash.com/photo-1612874742237-6526221588e3?w=800&h=600&fit=crop");
            carbonara.setCreatedAt(LocalDateTime.now());
            carbonara.setUpdatedAt(LocalDateTime.now());
            carbonara = recipeRepository.save(carbonara);

            // Add ingredients to carbonara
            addRecipeIngredient(recipeIngredientRepository, carbonara, ingredients.get(0), 400, "g", 1); // Spaghetti
            addRecipeIngredient(recipeIngredientRepository, carbonara, ingredients.get(1), 4, "pieces", 2); // Eggs
            addRecipeIngredient(recipeIngredientRepository, carbonara, ingredients.get(2), 100, "g", 3); // Parmesan
            addRecipeIngredient(recipeIngredientRepository, carbonara, ingredients.get(3), 200, "g", 4); // Bacon
            addRecipeIngredient(recipeIngredientRepository, carbonara, ingredients.get(4), 1, "tsp", 5); // Black Pepper

            // Create sample recipe: Margherita Pizza
            Recipe pizza = new Recipe();
            pizza.setTitle("Margherita Pizza");
            pizza.setDescription("A classic Neapolitan pizza with tomatoes, mozzarella, basil, and olive oil. Simple ingredients, incredible taste!");
            pizza.setInstructions("1. Preheat oven to 475°F (245°C) with pizza stone inside.\n\n" +
                    "2. Roll out pizza dough on floured surface to desired thickness.\n\n" +
                    "3. Crush tomatoes and spread evenly on dough, leaving a border for crust.\n\n" +
                    "4. Tear fresh mozzarella and distribute over tomato sauce.\n\n" +
                    "5. Drizzle with olive oil and season with salt.\n\n" +
                    "6. Transfer to preheated pizza stone and bake for 10-12 minutes until crust is golden.\n\n" +
                    "7. Remove from oven, top with fresh basil leaves and a drizzle of olive oil. Slice and serve!");
            pizza.setPrepTime(20);
            pizza.setCookTime(12);
            pizza.setServings(2);
            pizza.setDifficulty(Recipe.Difficulty.MEDIUM);
            pizza.setCalories(280);
            pizza.setProtein(12.0);
            pizza.setCarbohydrates(35.0);
            pizza.setFat(10.0);
            pizza.setFiber(2.0);
            pizza.setPublished(true);
            pizza.setAuthor(chef);
            pizza.setCategory(categories.get(0)); // Italian
            pizza.setImageUrl("https://images.unsplash.com/photo-1604068549290-dea0e4a305ca?w=800&h=600&fit=crop");
            pizza.setCreatedAt(LocalDateTime.now().minusDays(1));
            pizza.setUpdatedAt(LocalDateTime.now().minusDays(1));
            pizza = recipeRepository.save(pizza);

            // Add ingredients to pizza
            addRecipeIngredient(recipeIngredientRepository, pizza, ingredients.get(14), 300, "g", 1); // Flour
            addRecipeIngredient(recipeIngredientRepository, pizza, ingredients.get(5), 200, "g", 2); // Tomatoes
            addRecipeIngredient(recipeIngredientRepository, pizza, ingredients.get(9), 200, "g", 3); // Mozzarella
            addRecipeIngredient(recipeIngredientRepository, pizza, ingredients.get(8), 10, "leaves", 4); // Basil
            addRecipeIngredient(recipeIngredientRepository, pizza, ingredients.get(7), 2, "tbsp", 5); // Olive Oil

            // Create sample recipe: Grilled Chicken Salad
            Recipe salad = new Recipe();
            salad.setTitle("Mediterranean Grilled Chicken Salad");
            salad.setDescription("A healthy and delicious salad with grilled chicken, fresh vegetables, and a light vinaigrette.");
            salad.setInstructions("1. Season chicken breast with salt, pepper, and olive oil.\n\n" +
                    "2. Grill chicken for 6-7 minutes per side until cooked through. Let rest 5 minutes, then slice.\n\n" +
                    "3. Chop tomatoes, bell peppers, and onion.\n\n" +
                    "4. In a large bowl, combine vegetables with olive oil, salt, and pepper.\n\n" +
                    "5. Top with sliced grilled chicken and fresh basil.\n\n" +
                    "6. Drizzle with additional olive oil and serve immediately.");
            salad.setPrepTime(15);
            salad.setCookTime(15);
            salad.setServings(2);
            salad.setDifficulty(Recipe.Difficulty.EASY);
            salad.setCalories(320);
            salad.setProtein(35.0);
            salad.setCarbohydrates(12.0);
            salad.setFat(15.0);
            salad.setFiber(4.0);
            salad.setPublished(true);
            salad.setAuthor(foodie);
            salad.setCategory(categories.get(7)); // Salads
            salad.setImageUrl("https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&h=600&fit=crop");
            salad.setCreatedAt(LocalDateTime.now().minusDays(2));
            salad.setUpdatedAt(LocalDateTime.now().minusDays(2));
            salad = recipeRepository.save(salad);

            // Add ingredients to salad
            addRecipeIngredient(recipeIngredientRepository, salad, ingredients.get(10), 300, "g", 1); // Chicken
            addRecipeIngredient(recipeIngredientRepository, salad, ingredients.get(5), 2, "pieces", 2); // Tomatoes
            addRecipeIngredient(recipeIngredientRepository, salad, ingredients.get(11), 2, "pieces", 3); // Bell Peppers
            addRecipeIngredient(recipeIngredientRepository, salad, ingredients.get(12), 1, "piece", 4); // Onion
            addRecipeIngredient(recipeIngredientRepository, salad, ingredients.get(7), 3, "tbsp", 5); // Olive Oil
            addRecipeIngredient(recipeIngredientRepository, salad, ingredients.get(8), 5, "leaves", 6); // Basil

            // ========== MORE RECIPES BY CHEF MARIO ==========
            
            // Recipe: Tiramisu (Dessert by Chef Mario)
            Recipe tiramisu = new Recipe();
            tiramisu.setTitle("Classic Italian Tiramisu");
            tiramisu.setDescription("An authentic Italian dessert with layers of coffee-soaked ladyfingers and mascarpone cream. A true masterpiece!");
            tiramisu.setInstructions("1. Brew strong espresso and let it cool.\n\n" +
                    "2. Separate eggs. Beat egg yolks with sugar until pale and creamy.\n\n" +
                    "3. Add mascarpone cheese to egg mixture and beat until smooth.\n\n" +
                    "4. In a separate bowl, beat egg whites until stiff peaks form.\n\n" +
                    "5. Gently fold egg whites into mascarpone mixture.\n\n" +
                    "6. Quickly dip ladyfingers in espresso and arrange in a layer in dish.\n\n" +
                    "7. Spread half of mascarpone mixture over ladyfingers.\n\n" +
                    "8. Repeat layers. Dust with cocoa powder.\n\n" +
                    "9. Refrigerate for at least 4 hours or overnight.\n\n" +
                    "10. Serve chilled and enjoy!");
            tiramisu.setPrepTime(30);
            tiramisu.setCookTime(0);
            tiramisu.setServings(8);
            tiramisu.setDifficulty(Recipe.Difficulty.MEDIUM);
            tiramisu.setCalories(450);
            tiramisu.setProtein(8.0);
            tiramisu.setCarbohydrates(45.0);
            tiramisu.setFat(28.0);
            tiramisu.setFiber(1.0);
            tiramisu.setPublished(true);
            tiramisu.setAuthor(chef);
            tiramisu.setCategory(categories.get(3)); // Desserts
            tiramisu.setImageUrl("https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=800&h=600&fit=crop");
            tiramisu = recipeRepository.save(tiramisu);
            
            addRecipeIngredient(recipeIngredientRepository, tiramisu, ingredients.get(1), 6, "pieces", 1); // Eggs
            addRecipeIngredient(recipeIngredientRepository, tiramisu, ingredients.get(6), 100, "g", 2); // Sugar
            addRecipeIngredient(recipeIngredientRepository, tiramisu, ingredients.get(13), 500, "g", 3); // Coffee

            // Recipe: Risotto (Italian by Chef Mario)
            Recipe risotto = new Recipe();
            risotto.setTitle("Creamy Mushroom Risotto");
            risotto.setDescription("A luxurious Italian rice dish cooked slowly with white wine, mushrooms, and parmesan. Comfort food at its finest!");
            risotto.setInstructions("1. Heat chicken broth in a separate pot and keep warm.\n\n" +
                    "2. In a large pan, sauté chopped onions in olive oil until translucent.\n\n" +
                    "3. Add rice and stir for 2 minutes until lightly toasted.\n\n" +
                    "4. Pour in white wine and stir until absorbed.\n\n" +
                    "5. Add warm broth one ladle at a time, stirring constantly.\n\n" +
                    "6. After 15 minutes, add sliced mushrooms.\n\n" +
                    "7. Continue adding broth and stirring for another 10 minutes.\n\n" +
                    "8. Remove from heat, stir in parmesan cheese and butter.\n\n" +
                    "9. Season with salt and pepper. Serve immediately.");
            risotto.setPrepTime(10);
            risotto.setCookTime(30);
            risotto.setServings(4);
            risotto.setDifficulty(Recipe.Difficulty.MEDIUM);
            risotto.setCalories(380);
            risotto.setProtein(12.0);
            risotto.setCarbohydrates(55.0);
            risotto.setFat(12.0);
            risotto.setFiber(2.0);
            risotto.setPublished(true);
            risotto.setAuthor(chef);
            risotto.setCategory(categories.get(0)); // Italian
            risotto.setImageUrl("https://images.unsplash.com/photo-1476124369491-c0df5c8e05d5?w=800&h=600&fit=crop");
            risotto = recipeRepository.save(risotto);
            
            addRecipeIngredient(recipeIngredientRepository, risotto, ingredients.get(12), 1, "piece", 1); // Onion
            addRecipeIngredient(recipeIngredientRepository, risotto, ingredients.get(7), 3, "tbsp", 2); // Olive Oil
            addRecipeIngredient(recipeIngredientRepository, risotto, ingredients.get(2), 100, "g", 3); // Parmesan

            // ========== MORE RECIPES BY FOODIE JANE ==========
            
            // Recipe: Smoothie Bowl (Breakfast by Jane)
            Recipe smoothie = new Recipe();
            smoothie.setTitle("Tropical Smoothie Bowl");
            smoothie.setDescription("A vibrant and nutritious breakfast bowl packed with tropical fruits, topped with granola and fresh berries.");
            smoothie.setInstructions("1. Freeze banana slices overnight.\n\n" +
                    "2. In a blender, combine frozen bananas, mango, and a splash of almond milk.\n\n" +
                    "3. Blend until smooth and thick.\n\n" +
                    "4. Pour into a bowl.\n\n" +
                    "5. Top with sliced fresh fruits, granola, coconut flakes, and chia seeds.\n\n" +
                    "6. Drizzle with honey if desired.\n\n" +
                    "7. Serve immediately and enjoy this refreshing breakfast!");
            smoothie.setPrepTime(10);
            smoothie.setCookTime(0);
            smoothie.setServings(2);
            smoothie.setDifficulty(Recipe.Difficulty.EASY);
            smoothie.setCalories(280);
            smoothie.setProtein(6.0);
            smoothie.setCarbohydrates(58.0);
            smoothie.setFat(4.0);
            smoothie.setFiber(8.0);
            smoothie.setPublished(true);
            smoothie.setAuthor(foodie);
            smoothie.setCategory(categories.get(6)); // Breakfast
            smoothie.setImageUrl("https://images.unsplash.com/photo-1590301157890-4810ed352733?w=800&h=600&fit=crop");
            smoothie = recipeRepository.save(smoothie);
            
            addRecipeIngredient(recipeIngredientRepository, smoothie, ingredients.get(6), 1, "tbsp", 1); // Sugar/Honey

            // Recipe: Vegetarian Tacos (Mexican by Jane)
            Recipe tacos = new Recipe();
            tacos.setTitle("Spicy Black Bean Tacos");
            tacos.setDescription("Delicious vegetarian tacos loaded with seasoned black beans, fresh veggies, and zesty toppings.");
            tacos.setInstructions("1. Heat oil in a pan and sauté diced onions until soft.\n\n" +
                    "2. Add bell peppers and cook for 3 minutes.\n\n" +
                    "3. Add black beans, cumin, chili powder, and salt.\n\n" +
                    "4. Cook for 5 minutes, mashing some beans for texture.\n\n" +
                    "5. Warm tortillas in a dry pan or microwave.\n\n" +
                    "6. Fill tortillas with bean mixture.\n\n" +
                    "7. Top with diced tomatoes, lettuce, and avocado.\n\n" +
                    "8. Drizzle with lime juice and serve!");
            tacos.setPrepTime(15);
            tacos.setCookTime(10);
            tacos.setServings(4);
            tacos.setDifficulty(Recipe.Difficulty.EASY);
            tacos.setCalories(320);
            tacos.setProtein(12.0);
            tacos.setCarbohydrates(45.0);
            tacos.setFat(10.0);
            tacos.setFiber(12.0);
            tacos.setPublished(true);
            tacos.setAuthor(foodie);
            tacos.setCategory(categories.get(1)); // Mexican
            tacos.setImageUrl("https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?w=800&h=600&fit=crop");
            tacos = recipeRepository.save(tacos);
            
            addRecipeIngredient(recipeIngredientRepository, tacos, ingredients.get(12), 1, "piece", 1); // Onion
            addRecipeIngredient(recipeIngredientRepository, tacos, ingredients.get(11), 2, "pieces", 2); // Bell Peppers
            addRecipeIngredient(recipeIngredientRepository, tacos, ingredients.get(5), 2, "pieces", 3); // Tomatoes

            // ========== ADMIN RECIPES ==========
            
            // Recipe: Grilled Steak (BBQ by Admin)
            Recipe steak = new Recipe();
            steak.setTitle("Perfect Grilled Ribeye Steak");
            steak.setDescription("A perfectly seasoned and grilled ribeye steak with a beautiful crust and juicy interior. Restaurant quality at home!");
            steak.setInstructions("1. Remove steak from refrigerator 30 minutes before cooking.\n\n" +
                    "2. Pat dry with paper towels.\n\n" +
                    "3. Season generously with salt and black pepper on both sides.\n\n" +
                    "4. Preheat grill to high heat (450-500°F).\n\n" +
                    "5. Brush grill grates with oil.\n\n" +
                    "6. Place steak on grill and cook for 4-5 minutes without moving.\n\n" +
                    "7. Flip and cook for another 4-5 minutes for medium-rare.\n\n" +
                    "8. Remove from grill and let rest for 5 minutes.\n\n" +
                    "9. Slice against the grain and serve with your favorite sides.");
            steak.setPrepTime(5);
            steak.setCookTime(10);
            steak.setServings(2);
            steak.setDifficulty(Recipe.Difficulty.EASY);
            steak.setCalories(550);
            steak.setProtein(52.0);
            steak.setCarbohydrates(0.0);
            steak.setFat(38.0);
            steak.setFiber(0.0);
            steak.setPublished(true);
            steak.setAuthor(admin);
            steak.setCategory(categories.get(9)); // BBQ & Grilling
            steak.setImageUrl("https://images.unsplash.com/photo-1558030006-450675393462?w=800&h=600&fit=crop");
            steak = recipeRepository.save(steak);
            
            addRecipeIngredient(recipeIngredientRepository, steak, ingredients.get(4), 1, "tsp", 1); // Black Pepper
            addRecipeIngredient(recipeIngredientRepository, steak, ingredients.get(7), 1, "tbsp", 2); // Olive Oil

            // Recipe: Asian Stir Fry (Asian by Admin)
            Recipe stirfry = new Recipe();
            stirfry.setTitle("Vegetable Stir Fry with Ginger");
            stirfry.setDescription("A quick and healthy Asian-inspired stir fry with colorful vegetables and a savory ginger sauce.");
            stirfry.setInstructions("1. Cut all vegetables into similar-sized pieces.\n\n" +
                    "2. Heat wok or large pan over high heat.\n\n" +
                    "3. Add oil and swirl to coat.\n\n" +
                    "4. Add ginger and garlic, stir fry for 30 seconds.\n\n" +
                    "5. Add harder vegetables first (carrots, bell peppers) and stir fry 2 minutes.\n\n" +
                    "6. Add remaining vegetables and stir fry 2-3 minutes.\n\n" +
                    "7. Add soy sauce and a splash of water.\n\n" +
                    "8. Toss everything together until vegetables are crisp-tender.\n\n" +
                    "9. Serve immediately over rice.");
            stirfry.setPrepTime(15);
            stirfry.setCookTime(8);
            stirfry.setServings(4);
            stirfry.setDifficulty(Recipe.Difficulty.EASY);
            stirfry.setCalories(180);
            stirfry.setProtein(5.0);
            stirfry.setCarbohydrates(25.0);
            stirfry.setFat(8.0);
            stirfry.setFiber(6.0);
            stirfry.setPublished(true);
            stirfry.setAuthor(admin);
            stirfry.setCategory(categories.get(2)); // Asian
            stirfry.setImageUrl("https://images.unsplash.com/photo-1512058564366-18510be2db19?w=800&h=600&fit=crop");
            stirfry = recipeRepository.save(stirfry);
            
            addRecipeIngredient(recipeIngredientRepository, stirfry, ingredients.get(11), 3, "pieces", 1); // Bell Peppers
            addRecipeIngredient(recipeIngredientRepository, stirfry, ingredients.get(12), 1, "piece", 2); // Onion
            addRecipeIngredient(recipeIngredientRepository, stirfry, ingredients.get(7), 2, "tbsp", 3); // Olive Oil

            logger.info("✓ Created {} recipes", recipeRepository.count());
            
            logger.info("✅ Sample data initialization complete!");
            logger.info("📊 Summary:");
            logger.info("   - Users: {}", userRepository.count());
            logger.info("   - Categories: {}", categoryRepository.count());
            logger.info("   - Ingredients: {}", ingredientRepository.count());
            logger.info("   - Recipes: {}", recipeRepository.count());
            logger.info("");
            logger.info("👤 Test Users:");
            logger.info("   - Username: admin, Password: admin123 (Admin)");
            logger.info("   - Username: chef_mario, Password: password123");
            logger.info("   - Username: foodie_jane, Password: password123");
        };
    }

    private Category createCategory(String name, String description, String iconUrl) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setIconUrl(iconUrl);
        return category;
    }

    private Ingredient createIngredient(String name, String description) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setDescription(description);
        return ingredient;
    }

    private void addRecipeIngredient(RecipeIngredientRepository repository, Recipe recipe, 
                                    Ingredient ingredient, double quantity, String unit, int order) {
        RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipe(recipe);
        ri.setIngredient(ingredient);
        ri.setQuantity(quantity);
        ri.setUnit(unit);
        ri.setDisplayOrder(order);
        repository.save(ri);
    }
}
