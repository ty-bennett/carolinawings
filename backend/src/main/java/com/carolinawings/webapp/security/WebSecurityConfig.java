package com.carolinawings.webapp.security;

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

import java.time.temporal.ValueRange;
import java.util.*;


@Configuration
@EnableWebSecurity
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
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/admin/**)").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/platform-admin/**").permitAll()
                                .requestMatchers("/public/**").permitAll()
                                .requestMatchers("/**").permitAll()
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
    public WebSecurityCustomizer webSecurityCustomizer()
    {
        return(WebSecurity::ignoring);
    }

    @Bean
    public static CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, MenuRepository menuRepository, RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository, MenuItemOptionRepository menuItemOptionRepository, OptionGroupRepository optionGroupRepository, MenuItemOptionGroupRepository menuItemOptionGroupRepository) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(RoleName.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role restaurantAdminRole = roleRepository.findByName(RoleName.ROLE_RESTAURANTADMIN)
                    .orElseGet(() -> {
                        Role newRestaurantAdminRole = new Role(RoleName.ROLE_RESTAURANTADMIN);
                        return roleRepository.save(newRestaurantAdminRole);
                    });

            Role managerRole = roleRepository.findByName(RoleName.ROLE_MANAGER)
                    .orElseGet(() -> {
                        Role newManagerRole = new Role(RoleName.ROLE_MANAGER);
                        return roleRepository.save(newManagerRole);
                    });
            User tytest = userRepository.findByUsername("test")
                    .orElseGet(() -> {
                        PasswordEncoder encoder = new BCryptPasswordEncoder();
                        User user = new User("test@gmail.com", "password", encoder.encode("password"), "8035215139", true, "ty bennett");
                        Set<Role> roles = Set.of(userRole, restaurantAdminRole, managerRole);
                        user.setRoles(roles);
                        return userRepository.save(user);
                    });
            Restaurant testRestaurant = restaurantRepository.findByName("Carolina Wings Redbank")
                    .orElseGet(() -> {
                        Set<User> restaurantAdmins = new HashSet<>();
                        Set<Menu> menus = new HashSet<>();
                        restaurantAdmins.add(tytest);

                        Restaurant newRestaurant = new Restaurant(
                                "Carolina Wings Redbank",
                                "1767E S Lake Dr",
                                restaurantAdmins,
                                menus);
                        return restaurantRepository.save(newRestaurant);
                    });
            testRestaurant = restaurantRepository.findById(testRestaurant.getId()).orElseThrow(() -> new RuntimeException("testRestaurant not found"));
            Menu testMenu = new Menu(
                    "RedBank Menu",
                    "Menu for CWS Redbank",
                    new ArrayList<>(),
                    testRestaurant,
                    true
            );
            menuRepository.save(testMenu);
            Menu secondTestMenu = new Menu(
                    "Test disabled",
                    "test",
                    new ArrayList<>(),
                    testRestaurant,
                    false
            );
            menuRepository.save(secondTestMenu);

            List<MenuItem> menuItems = menuItemRepository.findAll();

            for (MenuItem item : menuItems) {
                item.setMenu(testMenu);
            }

            testMenu.setMenuItemsList(menuItems);
            testMenu.setIsPrimary(true);
            menuRepository.save(testMenu);
            menuItemRepository.saveAll(menuItems);

            Set<Restaurant> restaurants = Set.of(testRestaurant);

            tytest.setRestaurants(restaurants);
            userRepository.save(tytest);

            Set<Menu> menuSet = testRestaurant.getMenus();
            if (menuSet == null) {
                menuSet = new HashSet<>();
            }
            for (Menu menu : menuSet) {
                menu.setRestaurant(testRestaurant);
            }
            menuSet.add(testMenu);
            testRestaurant.setMenus(menuSet);
            restaurantRepository.save(testRestaurant);

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(restaurantAdminRole);
            Set<Role> adminRoles = Set.of(userRole, restaurantAdminRole, managerRole);

            Map<Integer, Integer> wingSauceQuantities = new HashMap<>();
            wingSauceQuantities.put(27, 1);
            wingSauceQuantities.put(28, 1);
            wingSauceQuantities.put(29, 2);
            wingSauceQuantities.put(30, 3);
            wingSauceQuantities.put(31, 4);
            wingSauceQuantities.put(32, 1);
            wingSauceQuantities.put(33, 1);
            wingSauceQuantities.put(34, 2);
            wingSauceQuantities.put(35, 3);
            wingSauceQuantities.put(36, 4);
            wingSauceQuantities.put(37, 1);
            wingSauceQuantities.put(38, 1);
            wingSauceQuantities.put(39, 1);
            wingSauceQuantities.put(40, 1);
            wingSauceQuantities.put(41, 1);

            OptionGroup saucesGroup = new OptionGroup();
            saucesGroup.setName("WING_SAUCES");
            OptionGroup ranchAndBleuCheeseSauceGroup = new OptionGroup();
            ranchAndBleuCheeseSauceGroup.setName("ranch and bleu cheese");
            optionGroupRepository.save(saucesGroup);
            optionGroupRepository.save(ranchAndBleuCheeseSauceGroup);

            List<MenuItemOption> wingSauceList = getMenuItemOptions(saucesGroup);
            menuItemOptionRepository.saveAll(wingSauceList);

            saucesGroup.setOptions(wingSauceList);
            optionGroupRepository.save(saucesGroup);

            List<MenuItem> menuItemList = menuItemRepository.findAll();


            MenuItemOptionGroup wingSauceGroup = new MenuItemOptionGroup();
            for (MenuItem menuItem : menuItemList) {
                if(menuItem.getId() != null && menuItem.getId() >= 26 && menuItem.getId() <= 41) {
                    MenuItemOptionGroup wingSauceGroupTemp = new MenuItemOptionGroup();
                    wingSauceGroupTemp.setMenuItem(menuItem);
                    wingSauceGroupTemp.setOptionGroup(saucesGroup);
                    wingSauceGroupTemp.setOptionType("sauce");
                    int sauceCount = wingSauceQuantities.getOrDefault(menuItem.getId(), 1);
                    wingSauceGroupTemp.setMaxChoices(wingSauceQuantities.getOrDefault(sauceCount, 3));
                    menuItem.getOptionGroups().add(wingSauceGroupTemp);
                    menuItemOptionGroupRepository.save(wingSauceGroupTemp);
                    menuItemRepository.save(menuItem);
                }
            }
            wingSauceGroup.setOptionGroup(saucesGroup);
            wingSauceGroup.setOptionType("sauce");
            menuItemOptionGroupRepository.save(wingSauceGroup);

            MenuItemOptionGroup dressingGroup = new MenuItemOptionGroup();
            OptionGroup saladGroup = new OptionGroup();
            saladGroup.setName("salad dressings");
            saladGroup.setOptions(saladDressingGroup(saladGroup));
            List<MenuItem> saladMenuItemList = menuItemRepository.findByNameContaining("salad");
            for(MenuItem menuItem : saladMenuItemList) {
                dressingGroup.setMenuItem(menuItem);
                dressingGroup.setOptionType("dressing");
                dressingGroup.setOptionGroup(saladGroup);
            }



        };
    }

    private static List<MenuItemOption> getMenuItemOptions(OptionGroup saucesGroup) {
        MenuItemOption MILD = new MenuItemOption("MILD");
        MenuItemOption MILD_HONEY = new MenuItemOption("MILD HONEY");
        MenuItemOption BBQ = new MenuItemOption("BBQ");
        MenuItemOption TERIYAKI = new MenuItemOption("TERIYAKI");
        MenuItemOption TERI_BBQ = new MenuItemOption("TERI BBQ");
        MenuItemOption DOCS_BBQ = new MenuItemOption("DOCS BBQ");
        MenuItemOption HONEY_BBQ = new MenuItemOption("HONEY BBQ");
        MenuItemOption HOT_GARLIC = new MenuItemOption("HOT GARLIC");
        MenuItemOption HONEY_MUSTARD = new MenuItemOption("HONEY MUSTARD");
        MenuItemOption GARLIC_PARMESAN = new MenuItemOption("GARLIC PARMESAN");
        MenuItemOption MANGO_HABANERO = new MenuItemOption("MANGO HABANERO");
        MenuItemOption DOCS_SPECIAL = new MenuItemOption("DOCS SPECIAL");
        MenuItemOption MEDIUM = new MenuItemOption("MEDIUM");
        MenuItemOption HOT_HONEY = new MenuItemOption("HOT HONEY");
        MenuItemOption CAJUN_HONEY = new MenuItemOption("CAJUN HONEY");
        MenuItemOption TERI_HOT = new MenuItemOption("TERI HOT");
        MenuItemOption BUFFALO_CAJUN_RANCH = new MenuItemOption("BUFFALO CAJUN RANCH");
        MenuItemOption HOT_HONEY_MUSTARD = new MenuItemOption("HOT HONEY MUSTARD");
        MenuItemOption HOT = new MenuItemOption("HOT");
        MenuItemOption TERI_CAJUN = new MenuItemOption("TERI CAJUN");
        MenuItemOption CAJUN = new MenuItemOption("CAJUN");
        MenuItemOption FIRE_ISLAND = new MenuItemOption("FIRE ISLAND");
        MenuItemOption BLISTERING = new MenuItemOption("BLISTERING");
        MenuItemOption BEYOND_BLISTERING = new MenuItemOption("BEYOND BLISTERING");
        MenuItemOption CLASSIC = new MenuItemOption("CLASSIC");
        MenuItemOption GOLD = new MenuItemOption("GOLD");
        MenuItemOption PIG_SAUCE = new MenuItemOption("PIG SAUCE");
        MenuItemOption CAROLINA_RED = new MenuItemOption("CAROLINA RED");
        MenuItemOption CAROLINA_RUB = new MenuItemOption("CAROLINA RUB");
        MenuItemOption LEMON_PEPPER = new MenuItemOption("LEMON PEPPER");
        MenuItemOption PLAIN = new MenuItemOption("PLAIN");


        List<MenuItemOption> wingSauceList = List.of(
                MILD, MILD_HONEY, BBQ, TERIYAKI, TERI_BBQ, DOCS_BBQ, HONEY_BBQ, HOT_GARLIC, HONEY_MUSTARD,
                GARLIC_PARMESAN, MANGO_HABANERO, DOCS_SPECIAL, MEDIUM, HOT_HONEY, CAJUN, CAJUN_HONEY, TERI_HOT, BUFFALO_CAJUN_RANCH,
                HOT_HONEY_MUSTARD, HOT, TERI_CAJUN, FIRE_ISLAND, BLISTERING, BEYOND_BLISTERING, CLASSIC, GOLD,
                PIG_SAUCE, CAROLINA_RED, CAROLINA_RUB, LEMON_PEPPER, PLAIN);

        wingSauceList.forEach(item -> item.setOptionGroup(saucesGroup));
        return wingSauceList;
    }

    private static List<MenuItemOption> ranchAndBlueCheeseGroup(OptionGroup ranchAndBleuCheeseSauceGroup) {
        MenuItemOption RANCH = new MenuItemOption("RANCH");
        MenuItemOption BLUE_CHEESE = new MenuItemOption("BLEU CHEESE");

        List<MenuItemOption> ranchAndBlueCheeseGroup = List.of(
                RANCH, BLUE_CHEESE
        );
        ranchAndBlueCheeseGroup.forEach(item -> item.setOptionGroup(ranchAndBleuCheeseSauceGroup));
        return ranchAndBlueCheeseGroup;
    }

    private static List<MenuItemOption> fishDressingGroup (OptionGroup fishDressingSauceGroup) {
        MenuItemOption COCKTAIL = new MenuItemOption("COCKTAIL");
        MenuItemOption TARTAR = new MenuItemOption("TARTAR");
        List<MenuItemOption> fishDressingGroup = List.of(COCKTAIL, TARTAR);
        fishDressingGroup.forEach(item -> item.setOptionGroup(fishDressingSauceGroup));
        return fishDressingGroup;
    }

    private static List<MenuItemOption> saladDressingGroup (OptionGroup saladDressingGroup) {
        MenuItemOption RANCH = new MenuItemOption("RANCH");
        MenuItemOption BLEU_CHEESE = new MenuItemOption("BLEU_CHEESE");
        MenuItemOption THOUSAND_ISLAND = new MenuItemOption("THOUSAND_ISLAND");
        MenuItemOption ITALIAN = new MenuItemOption("ITALIAN");
        MenuItemOption HONEY_MUSTARD = new MenuItemOption("HONEY_MUSTARD");
        MenuItemOption FRENCH = new MenuItemOption("FRENCH");
        MenuItemOption BALSAMIC_VINAIGRETTE = new MenuItemOption("BALSAMIC_VINAIGRETTE");

        List<MenuItemOption> saladDressings = List.of(
                RANCH, BLEU_CHEESE, THOUSAND_ISLAND, ITALIAN, HONEY_MUSTARD, FRENCH, BALSAMIC_VINAIGRETTE
        );
        saladDressings.forEach(item -> item.setOptionGroup(saladDressingGroup));
        return saladDressings;
    }

    private static List<MenuItemOption> quesadillaAndNachoGroup(OptionGroup salsaSourCreamGroup) {
        MenuItemOption SALSA = new MenuItemOption("SALSA");
        MenuItemOption SOUR_CREAM = new MenuItemOption("SOUR_CREAM");

        List<MenuItemOption> salsaSourCream = List.of(
                SALSA, SOUR_CREAM
        );
        salsaSourCream.forEach(item -> item.setOptionGroup(salsaSourCreamGroup));
        return salsaSourCream;
    }

    private static List<MenuItemOption> chiliToppingGroup(OptionGroup chiliToppingsGroup)
    {
        MenuItemOption CHEESE = new MenuItemOption("CHEESE");
        MenuItemOption ONIONS = new MenuItemOption("ONIONS");
        MenuItemOption JALAPENOS = new MenuItemOption("JALAPENOS");
        MenuItemOption SOUR_CREAM = new MenuItemOption("SOUR_CREAM");

        List<MenuItemOption> chiliToppings = List.of(
                CHEESE, ONIONS, JALAPENOS, SOUR_CREAM
        );
        chiliToppings.forEach(item -> item.setOptionGroup(chiliToppingsGroup));
        return chiliToppings;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173") // your React app origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
