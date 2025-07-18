package com.carolinawings.webapp.security;

import com.carolinawings.webapp.exceptions.APIException;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

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
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, MenuRepository menuRepository, RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(RoleName.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role restaurantAdminRole = roleRepository.findByName(RoleName.ROLE_RESTAURANTADMIN)
                    .orElseGet(() -> {
                        Role newRestaurantAdminRole= new Role(RoleName.ROLE_RESTAURANTADMIN);
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
            Menu testMenu = new Menu(
                    "RedBank Menu",
                    "Menu for CWS Redbank",
                    new ArrayList<>(),
                    new HashSet<>(Set.of(testRestaurant)),
                    true
            );
            menuRepository.save(testMenu);
            Menu secondTestMenu = new Menu(
                    "Test disabled",
                    "test",
                    new ArrayList<>(),
                    new HashSet<>(Set.of(testRestaurant)),
                    false
            );
            menuRepository.save(secondTestMenu);
            List<MenuItem> menuItems = menuItemRepository.findAll();

            for(MenuItem item : menuItems) {
                item.setMenu(testMenu);
            }

            testMenu.setMenuItemsList(menuItems);
            menuRepository.save(testMenu);
            menuItemRepository.saveAll(menuItems);

            Set<Restaurant> restaurants = Set.of(testRestaurant);

            tytest.setRestaurants(restaurants);
            userRepository.save(tytest);

            testRestaurant.getMenus().add(testMenu);
            restaurantRepository.save(testRestaurant);

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(restaurantAdminRole);
            Set<Role> adminRoles = Set.of(userRole, restaurantAdminRole, managerRole);
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
