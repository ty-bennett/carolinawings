package com.carolinawings.webapp.config;

import com.carolinawings.webapp.enums.CartStatus;
import com.carolinawings.webapp.enums.RoleName;
import com.carolinawings.webapp.enums.RestaurantStatus;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Configuration
@Profile("dev")
public class DataSeeder {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;
    private final MenuItemOptionRepository menuItemOptionRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final MenuItemOptionGroupRepository menuItemOptionGroupRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantHoursRepository restaurantHoursRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public DataSeeder(CartRepository cartRepository,
                      UserRepository userRepository,
                      MenuItemRepository menuItemRepository,
                      MenuRepository menuRepository,
                      MenuItemOptionRepository menuItemOptionRepository,
                      OptionGroupRepository optionGroupRepository,
                      MenuItemOptionGroupRepository menuItemOptionGroupRepository,
                      RestaurantRepository restaurantRepository,
                      RestaurantHoursRepository restaurantHoursRepository,
                      PasswordEncoder passwordEncoder,
                      RoleRepository roleRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
        this.menuItemOptionRepository = menuItemOptionRepository;
        this.optionGroupRepository = optionGroupRepository;
        this.menuItemOptionGroupRepository = menuItemOptionGroupRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantHoursRepository = restaurantHoursRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            if (userRepository.count() > 0) {
                System.out.println(">>> Data already seeded, skipping...");
                return;
            }

            // ============ ROLES ============
            Role adminRole = createRole(RoleName.ADMIN);
            Role restaurantAdminRole = createRole(RoleName.RESTAURANT_ADMIN);
            Role managerRole = createRole(RoleName.MANAGER);
            Role userRole = createRole(RoleName.USER);

            // ============ RESTAURANTS ============
            Restaurant downtown = createRestaurant(
                    "Carolina Wings Downtown",
                    "123 Main Street, Columbia, SC 29201",
                    "803-555-1234",
                    "downtown@carolinawings.com"
            );

            Restaurant uptown = createRestaurant(
                    "Carolina Wings Uptown",
                    "456 Oak Avenue, Columbia, SC 29203",
                    "803-555-5678",
                    "uptown@carolinawings.com"
            );

            // ============ RESTAURANT HOURS ============
            createRestaurantHours(downtown);
            createRestaurantHours(uptown);

            // ============ USERS ============
            // Admin user - manages everything
            User admin = createUser(
                    "Admin",
                    "User",
                    "admin@carolinawings.com",
                    "admin123",
                    "803-555-0000",
                    Set.of(adminRole),
                    Set.of(downtown, uptown)
            );

            // Restaurant Admin - manages downtown
            User restaurantAdmin = createUser(
                    "Restaurant",
                    "Admin",
                    "restaurantadmin@carolinawings.com",
                    "admin123",
                    "803-555-1111",
                    Set.of(restaurantAdminRole),
                    Set.of(downtown)
            );

            // Manager - manages uptown
            User manager = createUser(
                    "Manager",
                    " User",
                    "manager@carolinawings.com",
                    "manager123",
                    "803-555-2222",
                    Set.of(managerRole),
                    Set.of(uptown)
            );

            // Regular customers
            User customer1 = createUser(
                    "John",
                    "Customer",
                    "john@example.com",
                    "customer123",
                    "803-555-3333",
                    Set.of(userRole),
                    Set.of()
            );

            User customer2 = createUser(
                    "Jane",
                    "Customer",
                    "jane@example.com",
                    "customer123",
                    "803-555-4444",
                    Set.of(userRole),
                    Set.of()
            );

            // ============ OPTION GROUPS ============
            OptionGroup sauceGroup = createOptionGroup("Wing Sauces");
            OptionGroup dipGroup = createOptionGroup("Dipping Sauces");
            OptionGroup sideGroup = createOptionGroup("Sides");
            OptionGroup drinkGroup = createOptionGroup("Drinks");

            // ============ OPTIONS ============
            // Wing sauces
            MenuItemOption mild = createOption("Mild", BigDecimal.ZERO, sauceGroup);
            MenuItemOption medium = createOption("Medium", BigDecimal.ZERO, sauceGroup);
            MenuItemOption hot = createOption("Hot", BigDecimal.ZERO, sauceGroup);
            MenuItemOption bbq = createOption("BBQ", BigDecimal.ZERO, sauceGroup);
            MenuItemOption lemonPepper = createOption("Lemon Pepper", BigDecimal.ZERO, sauceGroup);
            MenuItemOption garlicParm = createOption("Garlic Parmesan", BigDecimal.ZERO, sauceGroup);

            // Dipping sauces
            MenuItemOption ranch = createOption("Ranch", BigDecimal.ZERO, dipGroup);
            MenuItemOption bleuCheese = createOption("Bleu Cheese", BigDecimal.ZERO, dipGroup);
            MenuItemOption honeyMustard = createOption("Honey Mustard", BigDecimal.ZERO, dipGroup);

            // Sides
            MenuItemOption fries = createOption("Fries", new BigDecimal("2.99"), sideGroup);
            MenuItemOption coleslaw = createOption("Coleslaw", new BigDecimal("1.99"), sideGroup);
            MenuItemOption celery = createOption("Celery & Carrots", new BigDecimal("1.49"), sideGroup);

            // ============ MENUS ============
            Menu downtownMain = createMenu("Main Menu", downtown, true);
            Menu downtownLunch = createMenu("Lunch Specials", downtown, false);
            Menu uptownMain = createMenu("Main Menu", uptown, true);

            // ============ MENU ITEMS ============
            // Wings
            MenuItem wings6 = createMenuItem("6pc Wings", "6 crispy wings with your choice of sauce",
                    "https://example.com/wings6.jpg", new BigDecimal("8.99"), "WINGS", downtownMain);
            MenuItem wings12 = createMenuItem("12pc Wings", "12 crispy wings with your choice of 2 sauces",
                    "https://example.com/wings12.jpg", new BigDecimal("14.99"), "WINGS", downtownMain);
            MenuItem wings24 = createMenuItem("24pc Wings", "24 crispy wings with your choice of 3 sauces",
                    "https://example.com/wings24.jpg", new BigDecimal("26.99"), "WINGS", downtownMain);

            // Tenders
            MenuItem tenders4 = createMenuItem("4pc Tenders", "4 hand-breaded chicken tenders",
                    "https://example.com/tenders4.jpg", new BigDecimal("7.99"), "TENDERS", downtownMain);
            MenuItem tenders8 = createMenuItem("8pc Tenders", "8 hand-breaded chicken tenders",
                    "https://example.com/tenders8.jpg", new BigDecimal("13.99"), "TENDERS", downtownMain);

            // Sides
            MenuItem largeFries = createMenuItem("Large Fries", "Crispy seasoned fries",
                    "https://example.com/fries.jpg", new BigDecimal("4.99"), "SIDES", downtownMain);
            MenuItem onionRings = createMenuItem("Onion Rings", "Beer-battered onion rings",
                    "https://example.com/onion-rings.jpg", new BigDecimal("5.99"), "SIDES", downtownMain);

            // Drinks
            MenuItem soda = createMenuItem("Fountain Drink", "Your choice of soda",
                    "https://example.com/soda.jpg", new BigDecimal("2.49"), "DRINKS", downtownMain);

            // Lunch specials
            MenuItem lunchCombo = createMenuItem("Lunch Combo", "6pc wings + fries + drink",
                    "https://example.com/lunch.jpg", new BigDecimal("10.99"), "COMBOS", downtownLunch);

            // Uptown menu items (copy some)
            MenuItem uptownWings12 = createMenuItem("12pc Wings", "12 crispy wings with your choice of 2 sauces",
                    "https://example.com/wings12.jpg", new BigDecimal("14.99"), "WINGS", uptownMain);
            MenuItem uptownFries = createMenuItem("Large Fries", "Crispy seasoned fries",
                    "https://example.com/fries.jpg", new BigDecimal("4.99"), "SIDES", uptownMain);

            // ============ MENU ITEM OPTION GROUPS ============
            // Link option groups to menu items
            // Wings need sauce selection (required, pick 1-2)
            createMenuItemOptionGroup(wings6, sauceGroup, "SAUCE", true, 1, 1);
            createMenuItemOptionGroup(wings12, sauceGroup, "SAUCE", true, 1, 2);
            createMenuItemOptionGroup(wings24, sauceGroup, "SAUCE", true, 1, 3);
            createMenuItemOptionGroup(uptownWings12, sauceGroup, "SAUCE", true, 1, 2);

            // Wings can have dipping sauce (optional)
            createMenuItemOptionGroup(wings6, dipGroup, "DIP", false, 0, 2);
            createMenuItemOptionGroup(wings12, dipGroup, "DIP", false, 0, 2);
            createMenuItemOptionGroup(wings24, dipGroup, "DIP", false, 0, 3);
            createMenuItemOptionGroup(uptownWings12, dipGroup, "DIP", false, 0, 2);

            // Tenders can have dipping sauce
            createMenuItemOptionGroup(tenders4, dipGroup, "DIP", false, 0, 2);
            createMenuItemOptionGroup(tenders8, dipGroup, "DIP", false, 0, 3);

            // ============ CART FOR CUSTOMER ============
            createCartForUser(customer1, wings12, List.of(mild, ranch), downtown);

            System.out.println(">>> Data seeding complete!");
            System.out.println(">>> Test accounts:");
            System.out.println("    Admin: admin@carolinawings.com / admin123");
            System.out.println("    Restaurant Admin: restaurantadmin@carolinawings.com / admin123");
            System.out.println("    Manager: manager@carolinawings.com / manager123");
            System.out.println("    Customer: john@example.com / customer123");
            System.out.println("    Customer: jane@example.com / customer123");
            System.out.println(">>> Restaurants: Downtown (ID: " + downtown.getId() + "), Uptown (ID: " + uptown.getId() + ")");
        };
    }

    // ============ HELPER METHODS ============

    private Role createRole(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }

    private Restaurant createRestaurant(String name, String address, String phone, String email) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setPhone(phone);
        restaurant.setEmail(email);
        restaurant.setStatus(RestaurantStatus.OPEN);
        restaurant.setAcceptingOrders(true);
        restaurant.setEstimatedPickupMinutes(15);
        restaurant.setRestaurantAdmin(new HashSet<>());
        restaurant.setMenus(new HashSet<>());
        return restaurantRepository.save(restaurant);
    }

    private void createRestaurantHours(Restaurant restaurant) {
        for (DayOfWeek day : DayOfWeek.values()) {
            RestaurantHours hours = RestaurantHours.builder()
                    .restaurant(restaurant)
                    .dayOfWeek(day)
                    .openTime(LocalTime.of(11, 0))
                    .closeTime(LocalTime.of(22, 0))
                    .closed(false)
                    .build();
            restaurantHoursRepository.save(hours);
        }
    }

    private User createUser(String firstName, String lastName, String email, String password, String phone,
                            Set<Role> roles, Set<Restaurant> restaurants) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(phone);
        user.setNewsletterMember(false);
        user.setDateJoined(LocalDate.now());
        user.setOrderHistoryList(new ArrayList<>());
        user.setRoles(roles);
        user.setRestaurants(restaurants);
        return userRepository.save(user);
    }

    private OptionGroup createOptionGroup(String name) {
        OptionGroup group = new OptionGroup();
        group.setName(name);
        return optionGroupRepository.save(group);
    }

    private MenuItemOption createOption(String name, BigDecimal price, OptionGroup group) {
        MenuItemOption option = new MenuItemOption();
        option.setName(name);
        option.setPrice(price);
        option.setOptionGroup(group);
        option.setDefaultSelected(false);
        return menuItemOptionRepository.save(option);
    }

    private Menu createMenu(String name, Restaurant restaurant, boolean primary) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setRestaurant(restaurant);
        menu.setIsPrimary(primary);
        return menuRepository.save(menu);
    }

    private MenuItem createMenuItem(String name, String description, String imageUrl,
                                    BigDecimal price, String category, Menu menu) {
        MenuItem item = new MenuItem(name, description, imageUrl, price, category, true);
        item.setMenu(menu);
        return menuItemRepository.save(item);
    }

    private void createMenuItemOptionGroup(MenuItem menuItem, OptionGroup optionGroup,
                                           String optionType, boolean required, int minChoices, int maxChoices) {
        MenuItemOptionGroup miog = new MenuItemOptionGroup();
        miog.setMenuItem(menuItem);
        miog.setOptionGroup(optionGroup);
        miog.setOptionType(optionType);
        miog.setRequired(required);
        miog.setMinChoices(minChoices);
        miog.setMaxChoices(maxChoices);
        menuItemOptionGroupRepository.save(miog);
    }

    private void createCartForUser(User user, MenuItem item, List<MenuItemOption> options, Restaurant restaurant) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartStatus(CartStatus.ACTIVE);
        cart.setSubtotal(item.getPrice());
        cart.setTotalTax(item.getPrice().multiply(new BigDecimal("0.07")));
        cart.setTotalPrice(cart.getSubtotal().add(cart.getTotalTax()));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setMenuItem(item);
        cartItem.setQuantity(1);
        cartItem.setPrice(item.getPrice());
        cartItem.setMemos("");

        Set<CartItemOption> choices = new HashSet<>();
        for (MenuItemOption option : options) {
            CartItemOption choice = new CartItemOption();
            choice.setCartItem(cartItem);
            choice.setMenuItemOption(option);
            choice.setChoiceType(option.getOptionGroup().getName());
            choices.add(choice);
        }
        cartItem.setChoices(choices);

        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);

        cartRepository.save(cart);
    }
}