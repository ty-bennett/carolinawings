package com.carolinawings.webapp.config;

import com.carolinawings.webapp.enums.RoleName;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Configuration
@Profile("dev")
public class DataSeeder {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemOptionRepository menuItemOptionRepository;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public DataSeeder(CartRepository cartRepository,
                      UserRepository userRepository,
                      MenuItemRepository menuItemRepository,
                      MenuItemOptionRepository menuItemOptionRepository,
                      RestaurantRepository restaurantRepository,
                      PasswordEncoder passwordEncoder,
                      RoleRepository roleRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuItemOptionRepository = menuItemOptionRepository;
        this.restaurantRepository = restaurantRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Bean
    public CommandLineRunner seedSampleCart() {
        return args -> {
            // Don’t reseed if there’s already a cart
            if (cartRepository.count() > 0) {
                return;
            }

            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName(RoleName.ADMIN);
                        return roleRepository.save(r);
                    });

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            // 1) Create a simple test user
            User user = new User();
            user.setName("Test User");
            user.setUsername("test@example.com");      // your "email" field
            user.setPassword(passwordEncoder.encode("password123"));           // dev only, meets @Size(min=8)
            user.setPhoneNumber("8035551234");
            user.setNewsletterMember(false);
            user.setDateJoined(LocalDate.now());
            user.setOrderHistoryList(new java.util.ArrayList<>());
            user.setRoles(roles);

            // 2) Create a restaurant
            Restaurant restaurant = new Restaurant();
            restaurant.setName("Carolina Wings Test");
            restaurant.setAddress("123 Main Street");
            restaurant.setRestaurantAdmin(new HashSet<>()); // optional
            restaurant.setMenus(new HashSet<>());           // optional
            restaurant = restaurantRepository.save(restaurant);

            Set<Restaurant> restaurants = new HashSet<>();
            restaurants.add(restaurant);
            user.setRestaurants(restaurants);
            user = userRepository.save(user);

            // 3) Create two menu items (wings + fries)
            MenuItem wings = new MenuItem(
                    "12pc Wings",
                    "12 wings with 2 sauces",
                    "https://example.com/wings.jpg",
                    new BigDecimal("14.99"),
                    "WINGS",
                    true
            );
            wings.setMenu(null); // menu is nullable, we can skip for now

            MenuItem fries = new MenuItem(
                    "Large Fries",
                    "Crispy seasoned fries",
                    "https://example.com/fries.jpg",
                    new BigDecimal("4.00"),
                    "SIDES",
                    true
            );
            fries.setMenu(null);

            wings = menuItemRepository.save(wings);
            fries = menuItemRepository.save(fries);

            // 4) Create two menu item options (ranch + mild)
            MenuItemOption ranch = new MenuItemOption();
            ranch.setName("Ranch");
            ranch.setPrice(BigDecimal.ZERO);
            ranch.setOptionGroup(null); // optional for now
            ranch = menuItemOptionRepository.save(ranch);

            MenuItemOption mild = new MenuItemOption();
            mild.setName("Mild Sauce");
            mild.setPrice(BigDecimal.ZERO);
            mild.setOptionGroup(null);
            mild = menuItemOptionRepository.save(mild);

            // 5) Build the Cart
            Cart cart = new Cart();
            cart.setUser(user);

            Set<CartItem> cartItems = new HashSet<>();

            // CartItem #1: 12pc Wings, 1x, ranch + mild sauce
            CartItem wingsItem = new CartItem();
            wingsItem.setCart(cart);
            wingsItem.setMenuItem(wings);
            wingsItem.setQuantity(1);
            wingsItem.setMemos("Extra crispy, all flats");
            wingsItem.setPrice(wings.getPrice());

            Set<CartItemChoice> wingsChoices = new HashSet<>();

            CartItemChoice ranchChoice = new CartItemChoice();
            ranchChoice.setCartItem(wingsItem);
            ranchChoice.setMenuItemOption(ranch);
            ranchChoice.setChoiceType("SAUCE");

            CartItemChoice mildChoice = new CartItemChoice();
            mildChoice.setCartItem(wingsItem);
            mildChoice.setMenuItemOption(mild);
            mildChoice.setChoiceType("SAUCE");

            wingsChoices.add(ranchChoice);
            wingsChoices.add(mildChoice);
            wingsItem.setChoices(wingsChoices);

            cartItems.add(wingsItem);

            // CartItem #2: Large Fries, 2x
            CartItem friesItem = new CartItem();
            friesItem.setCart(cart);
            friesItem.setMenuItem(fries);
            friesItem.setQuantity(2);
            friesItem.setMemos("Well done");
            friesItem.setPrice(fries.getPrice().multiply(BigDecimal.valueOf(2)));
            friesItem.setChoices(new HashSet<>());

            cartItems.add(friesItem);

            cart.setCartItems(cartItems);

            // 6) Compute totals
            BigDecimal subtotal = BigDecimal.ZERO;
            for (CartItem ci : cartItems) {
                subtotal = subtotal.add(ci.getPrice());
            }

            BigDecimal taxRate = new BigDecimal("0.08");
            BigDecimal totalTax = subtotal.multiply(taxRate)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalPrice = subtotal.add(totalTax);

            cart.setSubtotal(subtotal);
            cart.setTotalTax(totalTax);
            cart.setTotalPrice(totalPrice);

            // 7) Save cart
            Cart saved = cartRepository.save(cart);
            System.out.println(">>> Seeded sample cart with id = " + saved.getId());
            System.out.println(">>> Use restaurantId = " + restaurant.getId() + " in OrderCreateRequest");
        };
    }
}