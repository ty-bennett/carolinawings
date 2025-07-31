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

import java.util.*;

import static java.util.Arrays.stream;

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
    @Autowired
    private MenuItemOptionGroupRepository ruleRepository;

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
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, MenuRepository menuRepository, RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository, MenuItemOptionRepository menuItemOptionRepository, OptionGroupRepository optionGroupRepository) {
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

            Map<Integer, Integer> wingSauceQuantities = Map.of(
                    8, 1,
                    12, 1,
                    18, 2,
                    24, 3,
                    50, 4,
                    100, 5);

            MenuItemOption mild = new MenuItemOption("MILD", "sauce");
            MenuItemOption MILD_HONEY = new MenuItemOption("MILD_HONEY", "sauce");
            MenuItemOption BBQ = new MenuItemOption("BBQ", "sauce");
            MenuItemOption TERIYAKI = new MenuItemOption("TERIYAKI","sauce");
            MenuItemOption TERI_BBQ = new MenuItemOption("TERI_BBQ", "sauce");
            MenuItemOption DOCS_BBQ = new MenuItemOption("DOCS_BBQ", "sauce");
            MenuItemOption HONEY_BBQ = new MenuItemOption("HONEY_BBQ", "sauce");
            MenuItemOption HOT_GARLIC = new MenuItemOption("HOT_GARLIC", "sauce");
            MenuItemOption HONEY_MUSTARD = new MenuItemOption("HONEY_MUSTARD", "sauce");
            MenuItemOption GARLIC_PARMESAN = new MenuItemOption("GARLIC_PARMESAN", "sauce");
            MenuItemOption MANGO_HABANERO = new MenuItemOption("MANGO_HABANERO", "sauce");
            MenuItemOption DOCS_SPECIAL = new MenuItemOption("DOCS_SPECIAL", "sauce");
            MenuItemOption MEDIUM = new MenuItemOption("MEDIUM", "sauce");
            MenuItemOption HOT_HONEY = new MenuItemOption("HOT_HONEY", "sauce");
            MenuItemOption CAJUN_HONEY = new MenuItemOption("CAJUN_HONEY", "sauce");
            MenuItemOption TERI_HOT = new MenuItemOption("TERI_HOT", "sauce");
            MenuItemOption BUFFALO_CAJUN_RANCH = new MenuItemOption("BUFFALO_CAJUN_RANCH", "sauce");
            MenuItemOption HOT_HONEY_MUSTARD = new MenuItemOption("HOT_HONEY_MUSTARD", "sauce");
            MenuItemOption HOT = new MenuItemOption("HOT", "sauce");
            MenuItemOption TERI_CAJUN = new MenuItemOption("TERI_CAJUN", "sauce");
            MenuItemOption CAJUN = new MenuItemOption("CAJUN", "sauce");
            MenuItemOption FIRE_ISLAND = new MenuItemOption("FIRE_ISLAND", "sauce");
            MenuItemOption BLISTERING = new MenuItemOption("BLISTERING", "sauce");
            MenuItemOption BEYOND_BLISTERING = new MenuItemOption("BEYOND_BLISTERING", "sauce");
            MenuItemOption CLASSIC = new MenuItemOption("CLASSIC", "sauce");
            MenuItemOption GOLD = new MenuItemOption("GOLD", "sauce");
            MenuItemOption PIG_SAUCE = new MenuItemOption("PIG_SAUCE", "sauce");
            MenuItemOption CAROLINA_RED = new MenuItemOption("CAROLINA_RED", "sauce");
            MenuItemOption CAROLINA_RUB = new MenuItemOption("CAROLINA_RUB", "sauce");
            MenuItemOption lemonPepper = new MenuItemOption("Lemon Pepper", "sauce");
            MenuItemOption none = new MenuItemOption("none", "sauce");

            List<MenuItemOption> wingSauceList = List.of(
                    mild, MILD_HONEY, BBQ, TERIYAKI, TERI_BBQ, DOCS_BBQ, HONEY_BBQ, HOT_GARLIC, HONEY_MUSTARD,
                    GARLIC_PARMESAN, MANGO_HABANERO, DOCS_SPECIAL, MEDIUM, HOT_HONEY, CAJUN, CAJUN_HONEY, TERI_HOT, BUFFALO_CAJUN_RANCH,
                    HOT_HONEY_MUSTARD, HOT, TERI_CAJUN, FIRE_ISLAND, BLISTERING, BEYOND_BLISTERING, CLASSIC, GOLD,
                    PIG_SAUCE, CAROLINA_RED, CAROLINA_RUB, lemonPepper, none);
            menuItemOptionRepository.saveAll(wingSauceList);
            OptionGroup saucesGroup = new OptionGroup();
            saucesGroup.setName("wing sauces");
            saucesGroup.setOptions(wingSauceList);
            menuItemOptionRepository.saveAll(wingSauceList);
            optionGroupRepository.save(saucesGroup);
            MenuItemOptionGroup wingSauceGroup = new MenuItemOptionGroup();
            wingSauceGroup.setOptionGroup(saucesGroup);
            wingSauceGroup.setOptionType("sauce");
            wingSauceList.forEach(item -> item.setGroup(saucesGroup));
            menuItemOptionRepository.saveAll(wingSauceList);


            // === DRESSINGS ===
//            MenuItemOption ranch = new MenuItemOption("Ranch", "dressing");
//            MenuItemOption blueCheese = new MenuItemOption("Blue Cheese", "dressing");
//            MenuItemOption italian = new MenuItemOption("Italian", "dressing");
//
//            MenuItemOptionGroup dressingGroup = new MenuItemOptionGroup();
//            dressingGroup.setName("Salad Dressings");
//            dressingGroup.setOptionType("dressing");
//            dressingGroup.setOptions(List.of(ranch, blueCheese, italian));
//
//            ranch.setGroup(dressingGroup);
//            blueCheese.setGroup(dressingGroup);
//            italian.setGroup(dressingGroup);

            // === SAVE GROUPS (cascades options) ===
            ruleRepository.save(wingSauceGroup);
        };
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
