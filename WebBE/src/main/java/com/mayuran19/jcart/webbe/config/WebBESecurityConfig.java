package com.mayuran19.jcart.webbe.config;

import com.mayuran19.jcart.core.config.CoreConfig;
import com.mayuran19.jcart.webbe.security.WebBEAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.*;

/**
 * Created by mayuran on 16/7/16.
 * This class is used to configure all the web related security configurations.
 * The urls starts with /*
 */

@Configuration
@Import({CoreConfig.class})
@ComponentScan(basePackages = {"com.mayuran19.jcart.webbe.security"})
@EnableWebSecurity(debug = true)
@Order(1)
public class WebBESecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationProvider webBEAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager(){
        AuthenticationManager authenticationManager = new ProviderManager(Arrays.asList(webBEAuthenticationProvider));

        return authenticationManager;
    }

    @Bean
    public SecurityContextPersistenceFilter securityContextPersistenceFilter(){
        return new SecurityContextPersistenceFilter();
    }

    @Bean
    public ExceptionTranslationFilter exceptionTranslationFilter(){
        LoginUrlAuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/error");
        ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(authenticationEntryPoint);
        exceptionTranslationFilter.setAccessDeniedHandler(accessDeniedHandler);
        exceptionTranslationFilter.afterPropertiesSet();

        return exceptionTranslationFilter;
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception{
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        List<ConfigAttribute> configs = new ArrayList<ConfigAttribute>();
        configs.add(new org.springframework.security.access.SecurityConfig(
                "isAuthenticated()"));
        requestMap.put(new AntPathRequestMatcher("/**"), configs);
        FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource = new ExpressionBasedFilterInvocationSecurityMetadataSource(
                requestMap, new DefaultWebSecurityExpressionHandler());
        filterSecurityInterceptor
                .setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
        filterSecurityInterceptor.afterPropertiesSet();

        return filterSecurityInterceptor;
    }

    @Bean
    public LogoutFilter logoutFilter() throws Exception{
        List<LogoutHandler> logoutHandlers = new ArrayList<>();
        logoutHandlers.add(new CookieClearingLogoutHandler("JSESSIONID"));
        logoutHandlers.add(new SecurityContextLogoutHandler());
        LogoutFilter logoutFilter = new LogoutFilter("/login",
                logoutHandlers.toArray(new LogoutHandler[] {}));
        logoutFilter.afterPropertiesSet();
        return logoutFilter;
    }

    @Bean
    public WebBEAuthenticationFilter webBEAuthenticationFilter(){
        WebBEAuthenticationFilter authenticationFilter = new WebBEAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager());
        return authenticationFilter;
    }

    @Bean(name = "springSecurityFilterChain")
    public FilterChainProxy springSecurityFilterChain() throws Exception{
        List<SecurityFilterChain> securityFilterChains = new ArrayList<>();

        //login path filter
        AntPathRequestMatcher loginPath = new AntPathRequestMatcher("/login");
        DefaultSecurityFilterChain loginPathFilter = new DefaultSecurityFilterChain(loginPath);

        AntPathRequestMatcher resourcesPath = new AntPathRequestMatcher("/resources/**");
        DefaultSecurityFilterChain resourcesPathFilter = new DefaultSecurityFilterChain(resourcesPath);

        AntPathRequestMatcher otherPath = new AntPathRequestMatcher("/**");
        DefaultSecurityFilterChain otherPathFilter = new DefaultSecurityFilterChain(otherPath,
                securityContextPersistenceFilter(),
                logoutFilter(),
                webBEAuthenticationFilter(),
                exceptionTranslationFilter(),
                filterSecurityInterceptor());

        securityFilterChains.add(loginPathFilter);
        securityFilterChains.add(resourcesPathFilter);
        securityFilterChains.add(otherPathFilter);

        return new FilterChainProxy(securityFilterChains);
    }

    public AccessDecisionManager accessDecisionManager() throws Exception{
        List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();
        voters.add(new WebExpressionVoter());
        voters.add(new RoleVoter());
        AffirmativeBased affirmativeBased = new AffirmativeBased(voters);
        affirmativeBased.setAllowIfAllAbstainDecisions(false);
        affirmativeBased.afterPropertiesSet();

        return affirmativeBased;
    }

/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("Mayuran123");
        for(String s : getApplicationContext().getBeanDefinitionNames()){
            System.out.println(s);
        }

        auth.authenticationProvider(webBEAuthenticationProvider);
    }*/

/*    @Bean(name="authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/


    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        WebBEAuthenticationFilter authenticationFilter = new WebBEAuthenticationFilter();
        http.addFilter(authenticationFilter);
        http.authorizeRequests()
                //allow anonymous resource request
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/resources*//**").permitAll()

                //all other request need to be authenticated
                .antMatchers("/webbe*//**").authenticated()
                //.and().formLogin()
                .and().exceptionHandling().accessDeniedPage("/accessDenied");

    }*/


}
