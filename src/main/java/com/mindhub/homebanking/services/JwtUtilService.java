package com.mindhub.homebanking.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtUtilService {

    //Crea la secret key, de tipo Jwt, con el algoritmo HS256
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();


    //Indica el tiempo de validez del token (60 minutos)
    private static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60;

    //Extrae todos los claims del token que recibe
    public Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    //Extrae el claim que nosotros digamos del token que reciba utilizando la función extractAllClaims()
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Extrae el username del token que digamos
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Extrae la expiración del token que indiquemos
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    //Indica si el token está expirado
    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    //Valida que el nombre de usuario del token sea igual que el del userDetails y que el token no este expirado
    public Boolean validateToken (String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    //Crea el token con los claims y el sujeto que se indique, el tiempo desde que se creo, el de expiración y lo firma con la SECRET_KEY
    public String createToken(Map<String, Object> claims, String subject){
        return Jwts
                .builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SECRET_KEY)
                .compact();
    }


    //Genera el token con el token creado anteriormente y le agrega el rol
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        var role = userDetails.getAuthorities().stream().toList().get(0);

        claims.put("role", role);
        return createToken(claims, userDetails.getUsername());
    }


}
