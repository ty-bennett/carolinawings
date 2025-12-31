package com.carolinawings.webapp.security;

import com.carolinawings.webapp.enums.RoleName;
import com.carolinawings.webapp.factory.MenuItemFactory;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import com.carolinawings.webapp.security.jwt.AuthEntryPointJwt;
import com.carolinawings.webapp.security.jwt.AuthTokenFilter;
import com.carolinawings.webapp.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.time.temporal.ValueRange;
import java.util.*;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private MenuItemOptionRepository optionRepository;
    @Autowired
    private OptionGroupRepository groupRepository;

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
                .authorizeHttpRequests(auth -> auth
                        // typical auth endpoints
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/public/**").permitAll()
                        // Admin endpoints
                    .requestMatchers("/api/admin/**").hasAnyAuthority("ADMIN", "RESTAURANT_ADMIN")
                        // manager endpoints
                    .requestMatchers("/api/manager/**").hasAnyAuthority("MANAGER", "RESTAURANT_ADMIN", "ADMIN")
                        // user cart/order endpoints
                    .requestMatchers("/api/**").authenticated()
                       // any api request should be auth
                    .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin
        ));

        return http.build();
    }

    //tells which requests to ignore
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> web.ignoring().requestMatchers("/h2-console/**");
    }

    ////            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
    ////                    .orElseGet(() -> {
    ////                        Role newUserRole = new Role(RoleName.ROLE_USER);
    ////                        return roleRepository.save(newUserRole);
    ////                    });
    ////
    ////            Role restaurantAdminRole = roleRepository.findByName(RoleName.ROLE_RESTAURANTADMIN)
    ////                    .orElseGet(() -> {
    ////                        Role newRestaurantAdminRole = new Role(RoleName.ROLE_RESTAURANTADMIN);
    ////                        return roleRepository.save(newRestaurantAdminRole);
    ////                    });
    ////
    ////            Role managerRole = roleRepository.findByName(RoleName.ROLE_MANAGER)
    ////                    .orElseGet(() -> {
    ////                        Role newManagerRole = new Role(RoleName.ROLE_MANAGER);
    ////                        return roleRepository.save(newManagerRole);
    ////                    });
    ////            User tytest = userRepository.findByUsername("test")
    ////                    .orElseGet(() -> {
    ////                        PasswordEncoder encoder = new BCryptPasswordEncoder();
    ////                        User user = new User("test@gmail.com", "password", encoder.encode("password"), "8035215139", true, "ty bennett");
    ////                        Set<Role> roles = Set.of(userRole, restaurantAdminRole, managerRole);
    ////                        user.setRoles(roles);
    ////                        return userRepository.save(user);
    ////                    });
    ////            Restaurant testRestaurant = restaurantRepository.findByName("Carolina Wings Redbank")
    ////                    .orElseGet(() -> {
    ////                        Set<User> restaurantAdmins = new HashSet<>();
    ////                        Set<Menu> menus = new HashSet<>();
    ////                        restaurantAdmins.add(tytest);
    ////
    ////                        Restaurant newRestaurant = new Restaurant(
    ////                                "Carolina Wings Redbank",
    ////                                "1767E S Lake Dr",
    ////                                restaurantAdmins,
    ////                                menus);
    ////                        return restaurantRepository.save(newRestaurant);
    ////                    });
    ////            testRestaurant = restaurantRepository.findById(testRestaurant.getId()).orElseThrow(() -> new RuntimeException("testRestaurant not found"));
    ////            Menu testMenu = new Menu(
    ////                    "RedBank Menu",
    ////                    "Menu for CWS Redbank",
    ////                    new ArrayList<>(),
    ////                    testRestaurant,
    ////                    true
    ////            );
    ////            menuRepository.save(testMenu);
    ////            Menu secondTestMenu = new Menu(
    ////                    "Test disabled",
    ////                    "test",
    ////                    new ArrayList<>(),
    ////                    testRestaurant,
    ////                    false
    ////            );
    ////            menuRepository.save(secondTestMenu);
    ////
    ////            List<MenuItem> menuItems = menuItemRepository.findAll();
    ////
    ////            for (MenuItem item : menuItems) {
    ////                item.setMenu(testMenu);
    ////            }
    ////
    ////            testMenu.setMenuItemsList(menuItems);
    ////            testMenu.setIsPrimary(true);
    ////            menuRepository.save(testMenu);
    ////            menuItemRepository.saveAll(menuItems);
    ////
    ////            Set<Restaurant> restaurants = Set.of(testRestaurant);
    ////
    ////            tytest.setRestaurants(restaurants);
    ////            userRepository.save(tytest);
    ////
    ////            Set<Menu> menuSet = testRestaurant.getMenus();
    ////            if (menuSet == null) {
    ////                menuSet = new HashSet<>();
    ////            }
    ////            for (Menu menu : menuSet) {
    ////                menu.setRestaurant(testRestaurant);
    ////            }
    ////            menuSet.add(testMenu);
    ////            testRestaurant.setMenus(menuSet);
    ////            restaurantRepository.save(testRestaurant);
    ////
    ////            Set<Role> userRoles = Set.of(userRole);
    ////            Set<Role> sellerRoles = Set.of(restaurantAdminRole);
    ////            Set<Role> adminRoles = Set.of(userRole, restaurantAdminRole, managerRole);
    ////
    ////            Map<Integer, Integer> wingSauceQuantities = new HashMap<>();
    ////            wingSauceQuantities.put(27, 1);
    ////            wingSauceQuantities.put(28, 1);
    ////            wingSauceQuantities.put(29, 2);
    ////            wingSauceQuantities.put(30, 3);
    ////            wingSauceQuantities.put(31, 4);
    ////            wingSauceQuantities.put(32, 1);
    ////            wingSauceQuantities.put(33, 1);
    ////            wingSauceQuantities.put(34, 2);
    ////            wingSauceQuantities.put(35, 3);
    ////            wingSauceQuantities.put(36, 4);
    ////            wingSauceQuantities.put(37, 1);
    ////            wingSauceQuantities.put(38, 1);
    ////            wingSauceQuantities.put(39, 1);
    ////            wingSauceQuantities.put(40, 1);
    ////            wingSauceQuantities.put(41, 1);
    ////
    ////            OptionGroup saucesGroup = new OptionGroup();
    ////            saucesGroup.setName("WING_SAUCES");
    ////            OptionGroup ranchAndBleuCheeseSauceGroup = new OptionGroup();
    ////            ranchAndBleuCheeseSauceGroup.setName("RANCH_BLEU_CHEESE");
    ////            optionGroupRepository.save(saucesGroup);
    ////            optionGroupRepository.save(ranchAndBleuCheeseSauceGroup);
    ////
    ////            List<MenuItemOption> wingSauceList = getMenuItemOptions(saucesGroup);
    ////            menuItemOptionRepository.saveAll(wingSauceList);
    ////
    ////            saucesGroup.setOptions(wingSauceList);
    ////            optionGroupRepository.save(saucesGroup);
    ////
    ////            List<MenuItem> menuItemList = menuItemRepository.findAll();
    ////
    ////
    ////            MenuItemOptionGroup wingSauceGroup = new MenuItemOptionGroup();
    ////            for (MenuItem menuItem : menuItemList) {
    ////                if(menuItem.getId() != null && menuItem.getId() >= 26 && menuItem.getId() <= 41) {
    ////                    MenuItemOptionGroup wingSauceGroupTemp = new MenuItemOptionGroup();
    ////                    wingSauceGroupTemp.setMenuItem(menuItem);
    ////                    wingSauceGroupTemp.setOptionGroup(saucesGroup);
    ////                    wingSauceGroupTemp.setOptionType("sauce");
    ////                    int sauceCount = wingSauceQuantities.getOrDefault(menuItem.getId(), 1);
    ////                    wingSauceGroupTemp.setMaxChoices(wingSauceQuantities.getOrDefault(sauceCount, 3));
    ////                    menuItem.getOptionGroups().add(wingSauceGroupTemp);
    ////                    menuItemOptionGroupRepository.save(wingSauceGroupTemp);
    ////                    menuItemRepository.save(menuItem);
    ////                }
    ////            }
    ////            wingSauceGroup.setOptionGroup(saucesGroup);
    ////            wingSauceGroup.setOptionType("sauce");
    ////            menuItemOptionGroupRepository.save(wingSauceGroup);
    ////
    ////            MenuItemOptionGroup dressingGroup = new MenuItemOptionGroup();
    ////            OptionGroup saladGroup = new OptionGroup();
    ////            saladGroup.setName("salad dressings");
    ////            saladGroup.setOptions(saladDressingGroup(saladGroup));
    ////            List<MenuItem> saladMenuItemList = menuItemRepository.findByNameContaining("salad");
    ////            for(MenuItem menuItem : saladMenuItemList) {
    ////                dressingGroup.setMenuItem(menuItem);
    ////                dressingGroup.setOptionType("dressing");
    ////                dressingGroup.setOptionGroup(saladGroup);
    ////            }
    ////            System.out.println("All menu item options populated");
    ////        };
    //        };
    //    }
    //    private static List<MenuItemOption> getMenuItemOptions(OptionGroup saucesGroup) {
    //        MenuItemOption MILD = new MenuItemOption("MILD");
    //        MenuItemOption MILD_HONEY = new MenuItemOption("MILD HONEY");
    //        MenuItemOption BBQ = new MenuItemOption("BBQ");
    //        MenuItemOption TERIYAKI = new MenuItemOption("TERIYAKI");
    //        MenuItemOption TERI_BBQ = new MenuItemOption("TERI BBQ");
    //        MenuItemOption DOCS_BBQ = new MenuItemOption("DOCS BBQ");
    //        MenuItemOption HONEY_BBQ = new MenuItemOption("HONEY BBQ");
    //        MenuItemOption HOT_GARLIC = new MenuItemOption("HOT GARLIC");
    //        MenuItemOption HONEY_MUSTARD = new MenuItemOption("HONEY MUSTARD");
    //        MenuItemOption GARLIC_PARMESAN = new MenuItemOption("GARLIC PARMESAN");
    //        MenuItemOption MANGO_HABANERO = new MenuItemOption("MANGO HABANERO");
    //        MenuItemOption DOCS_SPECIAL = new MenuItemOption("DOCS SPECIAL");
    //        MenuItemOption MEDIUM = new MenuItemOption("MEDIUM");
    //        MenuItemOption HOT_HONEY = new MenuItemOption("HOT HONEY");
    //        MenuItemOption CAJUN_HONEY = new MenuItemOption("CAJUN HONEY");
    //        MenuItemOption TERI_HOT = new MenuItemOption("TERI HOT");
    //        MenuItemOption BUFFALO_CAJUN_RANCH = new MenuItemOption("BUFFALO CAJUN RANCH");
    //        MenuItemOption HOT_HONEY_MUSTARD = new MenuItemOption("HOT HONEY MUSTARD");
    //        MenuItemOption HOT = new MenuItemOption("HOT");
    //        MenuItemOption TERI_CAJUN = new MenuItemOption("TERI CAJUN");
    //        MenuItemOption CAJUN = new MenuItemOption("CAJUN");
    //        MenuItemOption FIRE_ISLAND = new MenuItemOption("FIRE ISLAND");
    //        MenuItemOption BLISTERING = new MenuItemOption("BLISTERING");
    //        MenuItemOption BEYOND_BLISTERING = new MenuItemOption("BEYOND BLISTERING");
    //        MenuItemOption CLASSIC = new MenuItemOption("CLASSIC");
    //        MenuItemOption GOLD = new MenuItemOption("GOLD");
    //        MenuItemOption PIG_SAUCE = new MenuItemOption("PIG SAUCE");
    //        MenuItemOption CAROLINA_RED = new MenuItemOption("CAROLINA RED");
    //        MenuItemOption CAROLINA_RUB = new MenuItemOption("CAROLINA RUB");
    //        MenuItemOption LEMON_PEPPER = new MenuItemOption("LEMON PEPPER");
    //        MenuItemOption PLAIN = new MenuItemOption("PLAIN");
    //
    //
    //        List<MenuItemOption> wingSauceList = List.of(
    //                MILD, MILD_HONEY, BBQ, TERIYAKI, TERI_BBQ, DOCS_BBQ, HONEY_BBQ, HOT_GARLIC, HONEY_MUSTARD,
    //                GARLIC_PARMESAN, MANGO_HABANERO, DOCS_SPECIAL, MEDIUM, HOT_HONEY, CAJUN, CAJUN_HONEY, TERI_HOT, BUFFALO_CAJUN_RANCH,
    //                HOT_HONEY_MUSTARD, HOT, TERI_CAJUN, FIRE_ISLAND, BLISTERING, BEYOND_BLISTERING, CLASSIC, GOLD,
    //                PIG_SAUCE, CAROLINA_RED, CAROLINA_RUB, LEMON_PEPPER, PLAIN);
    //
    //        wingSauceList.forEach(item -> item.setOptionGroup(saucesGroup));
    //        return wingSauceList;
    //    }
    //
    //    private static List<MenuItemOption> ranchAndBlueCheeseGroup(OptionGroup ranchAndBleuCheeseSauceGroup) {
    //        MenuItemOption RANCH = new MenuItemOption("RANCH");
    //        MenuItemOption BLUE_CHEESE = new MenuItemOption("BLEU CHEESE");
    //
    //        List<MenuItemOption> ranchAndBlueCheeseGroup = List.of(
    //                RANCH, BLUE_CHEESE
    //        );
    //        ranchAndBlueCheeseGroup.forEach(item -> item.setOptionGroup(ranchAndBleuCheeseSauceGroup));
    //        return ranchAndBlueCheeseGroup;
    //    }
    //
    //    private static List<MenuItemOption> fishDressingGroup (OptionGroup fishDressingSauceGroup) {
    //        MenuItemOption COCKTAIL = new MenuItemOption("COCKTAIL");
    //        MenuItemOption TARTAR = new MenuItemOption("TARTAR");
    //        List<MenuItemOption> fishDressingGroup = List.of(COCKTAIL, TARTAR);
    //        fishDressingGroup.forEach(item -> item.setOptionGroup(fishDressingSauceGroup));
    //        return fishDressingGroup;
    //    }
    //
    //    private static List<MenuItemOption> saladDressingGroup (OptionGroup saladDressingGroup) {
    //        MenuItemOption RANCH = new MenuItemOption("RANCH");
    //        MenuItemOption BLEU_CHEESE = new MenuItemOption("BLEU_CHEESE");
    //        MenuItemOption THOUSAND_ISLAND = new MenuItemOption("THOUSAND_ISLAND");
    //        MenuItemOption ITALIAN = new MenuItemOption("ITALIAN");
    //        MenuItemOption HONEY_MUSTARD = new MenuItemOption("HONEY_MUSTARD");
    //        MenuItemOption FRENCH = new MenuItemOption("FRENCH");
    //        MenuItemOption BALSAMIC_VINAIGRETTE = new MenuItemOption("BALSAMIC_VINAIGRETTE");
    //
    //        List<MenuItemOption> saladDressings = List.of(
    //                RANCH, BLEU_CHEESE, THOUSAND_ISLAND, ITALIAN, HONEY_MUSTARD, FRENCH, BALSAMIC_VINAIGRETTE
    //        );
    //        saladDressings.forEach(item -> item.setOptionGroup(saladDressingGroup));
    //        return saladDressings;
    //    }
    //
    //    private static List<MenuItemOption> quesadillaAndNachoGroup(OptionGroup salsaSourCreamGroup) {
    //        MenuItemOption SALSA = new MenuItemOption("SALSA");
    //        MenuItemOption SOUR_CREAM = new MenuItemOption("SOUR_CREAM");
    //
    //        List<MenuItemOption> salsaSourCream = List.of(
    //                SALSA, SOUR_CREAM
    //        );
    //        salsaSourCream.forEach(item -> item.setOptionGroup(salsaSourCreamGroup));
    //        return salsaSourCream;
    //    }
    //
    //    private static List<MenuItemOption> chiliToppingGroup(OptionGroup chiliToppingsGroup)
    //    {
    //        MenuItemOption CHEESE = new MenuItemOption("CHEESE");
    //        MenuItemOption ONIONS = new MenuItemOption("ONIONS");
    //        MenuItemOption JALAPENOS = new MenuItemOption("JALAPENOS");
    //        MenuItemOption SOUR_CREAM = new MenuItemOption("SOUR_CREAM");
    //
    //        List<MenuItemOption> chiliToppings = List.of(
    //                CHEESE, ONIONS, JALAPENOS, SOUR_CREAM
    //        );
    //        chiliToppings.forEach(item -> item.setOptionGroup(chiliToppingsGroup));
    //        return chiliToppings;
    //    }
    //
        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOrigins("http://localhost:5173") // your React app origin
                            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                            .allowedHeaders("*")
                            .allowCredentials(true);
                }
            };
        }
}
//
//    @Bean
//    public static CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, MenuRepository menuRepository, RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository, MenuItemOptionRepository menuItemOptionRepository, OptionGroupRepository optionGroupRepository, MenuItemOptionGroupRepository menuItemOptionGroupRepository) {
//        return args -> {
//            if (menuItemRepository.count() == 0) {
//                List<MenuItem> items = List.of(
//                        MenuItemFactory.createMenuItem("Buffalo Shrimp", "Our famous hand-battered shrimp tossed in our award-winning buffalo sauce. Served with bleu cheese or ranch dressing.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Catfish Fingers", "Fresh catfish filets sliced, hand-battered & fried. Served with a side of CW's Remoulade Sauce.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Texas Tumbleweeds", "Hand-rolled & fried tortillas stuffed with seasoned chicken, black beans, cheese & corn.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Quesadillas - Southwest Chicken", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Quesadillas - Brisket Tossed in Pig Sauce", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Quesadillas - Buffalo Chicken", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Chips & Salsa", "Medium spice salsa and freshly fried tortilla chips.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(4.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Ultimate Nachos - Rob's Chili", "Tortilla chips covered in Rob's Chili, shredded cheese, tomatoes & jalapeños. Served with sour cream & salsa.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Ultimate Nachos - Buffalo Chicken", "Tortilla chips covered in fried chicken with your choice of CW wing sauce, shredded cheese, tomatoes & jalapeños. Served with sour cream & salsa.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Ultimate Nachos - Pulled Pork", "Tortilla chips covered in pulled-pork BBQ with your choice of sauce, shredded cheese, tomatoes & jalapeños. Served with sour cream & salsa.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Mozzarella Sticks", "Crispy, golden brown mozzarella sticks served with a side of marinara", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(7.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Fried Pickles", "Hand-battered and fried dill pickle chips.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(7.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Fried Mushrooms", "Hand-battered and fried mushrooms", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(7.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Rib Bites", "4–5 Bones of our award-winning baby back ribs.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Loaded Fries/Tots - Cheese & Bacon", "French fries or wing chips covered in cheese & bacon.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Loaded Fries/Tots - Garlic Parmesan", "French fries or wing chips covered in garlic parmesan.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(7.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Loaded Fries/Tots - Cheese and Chili", "French fries or wing chips covered in cheese and chili.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Carolina Sampler", "Fried mushrooms, mozzarella sticks, hand-battered tenders & fried pickles on wing chips. No substitutions.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(13.99), "Shareables", true),
//                        MenuItemFactory.createMenuItem("Soup of the Day (Bowl)", "Ask your server for today's special.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(5.99), "Soup and Salads", true),
//                        MenuItemFactory.createMenuItem("Rob's Chili", "A hearty meat & tomato based chili with beans & just the right amount of heat!", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(6.99), "Soup and Salads", true),
//                        MenuItemFactory.createMenuItem("The Protein Salad", "Turkey, ham and bacon over fresh-cut greens with shredded cheese, carrots, cucumbers, tomatoes, onions, and red cabbage then topped with fresh Grilled Chicken.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(12.99), "Soup and Salads", true),
//                        MenuItemFactory.createMenuItem("The Carolina Salad", "Fresh-cut greens with shredded cheese, carrots, red cabbage, cucumbers, tomatoes, onion & croutons.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Soup and Salads", true),
//                        MenuItemFactory.createMenuItem("Grilled or Blackened Chicken (Salad Add-on)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.00), "Soup and Salads", true),
//                        MenuItemFactory.createMenuItem("Fried Chicken (Salad Add-on)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.00), "Soup and Salads", true),
//                        MenuItemFactory.createMenuItem("Buffalo Chicken (Salad Add-on)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.00), "Soup and Salads", true),
//                        MenuItemFactory.createMenuItem("CW Chef Salad", "Turkey, ham & bacon over fresh-cut greens with shredded cheese, carrots, red cabbage, cucumbers, tomatoes, onions & croutons.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Soup and Salads", true),
//                        MenuItemFactory.createMenuItem("Certified Jumbo Wings - 8 Wings", "Our award-winning chicken wings are deep-fried and pan sautéed in your favorite wing flavor.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Certified Jumbo Wings - 12 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(14.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Certified Jumbo Wings - 18 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(22.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Certified Jumbo Wings - 24 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(27.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Certified Jumbo Wings - 50 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(54.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Boneless Wings - 8 Wings", "Hand-battered boneless chicken bites fried to perfection & sautéed in your favorite wing sauce.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Boneless Wings - 12 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(12.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Boneless Wings - 18 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(16.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Boneless Wings - 24 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(22.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Boneless Wings - 50 Wings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(44.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Chicken Tenders - 5 Tenders", "Fresh chicken tenders battered & fried to a golden brown. Plain or tossed in one of your favorite wing sauces.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Chicken Tenders - 7 Tenders", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Chicken Tenders - Tender Plate", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(14.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Wing & Tenders Plate", "6 wings & 3 chicken tenders with a side of wing chips.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(14.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Wing Plate", "Your choice of 8 jumbo wings, 10 boneless wings, or 6 tenders with a side of wing chips.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(14.99), "Wings and Tenders", true),
//                        MenuItemFactory.createMenuItem("Baby Back Ribs - Small Rack", "Slow cooked for 12 hours & coated with your choice of BBQ sauce or our Carolina Rub.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(15.99), "Ribs, BBQ, and Seafood", true),
//                        MenuItemFactory.createMenuItem("Baby Back Ribs - Large Rack", "A full rack of slow cooked baby back ribs & coated with your choice of BBQ sauce or our Carolina Rub.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(19.99), "Ribs, BBQ, and Seafood", true),
//                        MenuItemFactory.createMenuItem("Pulled Pork BBQ Feast", "Pulled pork BBQ piled high & slow-cooked in your choice of BBQ sauce.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(13.99), "Ribs, BBQ, and Seafood", true),
//                        MenuItemFactory.createMenuItem("Fried Shrimp Plate", "1/2 lb. of our famous hand-battered shrimp fried to a crispy golden brown. Sauce things up by adding your favorite CW flavor for $0.59", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(13.99), "Ribs, BBQ, and Seafood", true),
//                        MenuItemFactory.createMenuItem("CW Flavor Add-on (Shrimp)", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(0.59), "Ribs, BBQ, and Seafood", true),
//                        MenuItemFactory.createMenuItem("Fried Catfish Plate", "Fresh catfish filets, hand-battered & fried to a crispy golden brown.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(13.99), "Ribs, BBQ, and Seafood", true),
//                        MenuItemFactory.createMenuItem("Combo - Ribs & Wings", "Served with wing chips. Upgrade to a large rack for just $6.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(18.99), "Ribs, BBQ, and Seafood", true),
//                        MenuItemFactory.createMenuItem("Combo - Ribs & BBQ", "Served with wing chips. Upgrade to a large rack for just $6.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(19.99), "Ribs, BBQ, and Seafood", true),
//                        MenuItemFactory.createMenuItem("Ribhouse Sampler", "A quarter rack of baby back ribs, 6 jumbo wings, & 1/4 lb. of slow-cooked pulled pork BBQ served with a side of wing chips.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(21.99), "Ribs, BBQ, and Seafood", true),
//                        MenuItemFactory.createMenuItem("Smokehouse Burger", "CW's classic BBQ sauce, American & Monterey Jack cheese, bacon & topped with onion rings.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(12.99), "Burgers", true),
//                        MenuItemFactory.createMenuItem("Carolina Pig Sauce Burger", "House-pattied burger, slow-roasted BBQ, bacon, onion rings, Monterey Jack and American cheese & smothered in our award-winning Carolina Pig Sauce.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(13.59), "Burgers", true),
//                        MenuItemFactory.createMenuItem("Black and Bleu Burger", "Cajun blackened seasoning seared in & topped w/ bleu cheese crumbles & LTO.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(11.99), "Burgers", true),
//                        MenuItemFactory.createMenuItem("Buffalo Bleu Burger", "Grilled in CW's medium buffalo sauce and topped with bleu cheese crumbles & LTO.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(11.59), "Burgers", true),
//                        MenuItemFactory.createMenuItem("Mushroom Jack Burger", "Grilled mushrooms, Monterey Jack cheese & LTO.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(11.99), "Burgers", true),
//                        MenuItemFactory.createMenuItem("The Carolina Burger", "Freshly made and seasoned with our signature rub with lettuce, tomato & onion (LTO).", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Burgers", true),
//                        MenuItemFactory.createMenuItem("Monterey Jack or American Cheese", "Our seasoned burger topped with the choice of Monterey Jack or American cheese", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.59), "Burgers", true),
//                        MenuItemFactory.createMenuItem("Cheese & Bacon", "Topped with American cheese and bacon", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(11.99), "Burgers", true),
//                        MenuItemFactory.createMenuItem("Bacon Egg & Cheese Burger", "Topped with bacon, egg, Monterey Jack cheese & LTO.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(12.99), "Burgers", true),
//                        MenuItemFactory.createMenuItem("Atomic Burger", "Our seasoned burger topped with Monterey Jack cheese, jalapeños, our signature hot sauce & LTO.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(11.99), "Burgers", true),
//                        MenuItemFactory.createMenuItem("Mini Sliders", "(3) Mini Hamburger Sliders, w/ or w/o Cheese, and Pickles", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Burgers", true),
//                        MenuItemFactory.createMenuItem("CW's Famous BBQ Sandwich", "Cooked to perfection and piled high. Don't forget to add one of CW's BBQ sauces! Served with a side of coleslaw.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Carolina Chicken", "Fried chicken breast sautéed in your favorite CW's wing sauce. Topped with Monterey Jack cheese, lettuce & tomato.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Carolina Dip", "Chopped sirloin, melted Monterey Jack cheese & Au Jus. Make it Philly style by adding peppers & onions for 59¢.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Philly Style Add-on", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(0.59), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Carolina Club", "Served on toasted wheat berry bread with fresh cut lettuce and tomato, stacked with turkey, ham, and bacon.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Kickin' Chicken Phily", "Diced grilled chicken, onion & peppers smothered in Monterey Jack cheese on a hoagie roll.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Brisket Cheese Melt", "Slow cooked Brisket, melted American cheese served on Country White Bread. Served with Au-Jus.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Myra's Chicken Wrap", "Grilled chicken, Monterey Jack cheese & our Doc's Special sauce. That's It!", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.59), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Grilled Chicken Wrap", "Diced grilled chicken tossed with fresh cut lettuce, diced tomato, & ranch dressing.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.59), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Philly Wrap", "Grilled sirloin, Monterey Jack, onions & peppers. Served with a side of Au Jus.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(10.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Buffalo Wrap", "Your choice of our boneless chicken bites or golden fried shrimp tossed in medium wing sauce with fresh cut lettuce & tomatoes.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("BLT Wrap", "Crisp bacon, fresh cut lettuce, tomato & mayo.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(8.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Carolina Club Wrap", "Our Famous Carolina Club served in a wrap!", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Veggie Wrap", "Our grilled veggies with parmesan cheese in a tomato basil wrap. Served with marinara dip on the side.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(9.99), "Sandwiches and Wraps", true),
//                        MenuItemFactory.createMenuItem("Wing Chips", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.59), "Side Items", true),
//                        MenuItemFactory.createMenuItem("Wing Chips - Cheese Add-on", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(1.99), "Side Items", true),
//                        MenuItemFactory.createMenuItem("French Fries", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.59), "Side Items", true),
//                        MenuItemFactory.createMenuItem("French Fries - Cheese Add-on", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(1.99), "Side Items", true),
//                        MenuItemFactory.createMenuItem("Grilled Veggies", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(3.59), "Side Items", true),
//                        MenuItemFactory.createMenuItem("Coleslaw", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(4.59), "Side Items", true),
//                        MenuItemFactory.createMenuItem("Side Salad", "Your choice of our Carolina or Caesar salad.", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(4.59), "Side Items", true),
//                        MenuItemFactory.createMenuItem("Substitution - Switch Any Side Item", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(0.59), "Side Items", true),
//                        MenuItemFactory.createMenuItem("Substitution - Upgrade to Onion Rings", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(1.99), "Side Items", true),
//                        MenuItemFactory.createMenuItem("Substitution - Upgrade To Side Salad", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(1.29), "Side Items", true),
//                        MenuItemFactory.createMenuItem("Substitution - Add A Side Salad", "", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(1.99), "Side Items", true),
//                        MenuItemFactory.createMenuItem("Tater Tots", "Crispy, golden brown tater tots", "https://cdn.carolinawings.com/images/placeholder.jpg", BigDecimal.valueOf(4.59), "Side Items,", true)
//                );
//                Restaurant restaurant = new Restaurant();
//                Set<Restaurant> restaurants = new HashSet<>();
//                restaurants.add(restaurant);
//
//                menuItemRepository.saveAll(items);
//                System.out.println("✅ Menu items seeded.");
//
//            } else {
//                System.out.println("Menu items already seeded.");
//            }
//            // Retrieve or create roles

//}
