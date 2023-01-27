package com.bookstore.config

import com.bookstore.enums.Role
import com.bookstore.repository.CustomerRepository
import com.bookstore.security.AuthenticationFilter
import com.bookstore.security.AuthorizationFilter
import com.bookstore.security.CustomAuthenticationEntryPoint
import com.bookstore.service.JwtUtil
import com.bookstore.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val configuration: AuthenticationConfiguration,
    private val userDetails: UserDetailsCustomService,
    private val customEntryPoint: CustomAuthenticationEntryPoint,
    private val jwtUtil: JwtUtil
) {

    private val PUBLIC_MATCHERS = arrayOf<String>()

    private val PUBLIC_POST_MATCHERS = arrayOf(
        "/customers"
    )

    private val ADMIN_MATCHERS = arrayOf(
        "/admin/**"
    )

    private val PUBLIC_GET_MATCHERS = arrayOf(
        "/books"
    )

    fun configure (auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder())
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors().and().csrf().disable()
        http.authorizeHttpRequests()
            .requestMatchers(*PUBLIC_MATCHERS).permitAll()
            .requestMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHERS).permitAll()
            .requestMatchers(*ADMIN_MATCHERS).hasAuthority(Role.ADMIN.description)
            .requestMatchers(HttpMethod.GET, *PUBLIC_GET_MATCHERS).permitAll()
            .anyRequest().authenticated()
        http.addFilter(AuthenticationFilter(configuration.authenticationManager, customerRepository, jwtUtil))
        http.addFilter(AuthorizationFilter(configuration.authenticationManager, userDetails, jwtUtil))
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.exceptionHandling().authenticationEntryPoint(customEntryPoint)

        return http.build()
    }

//    fun configure(web: WebSecurity) {
//        web.ignoring().requestMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**")
//    }

    @Bean
    fun corsConfig(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}